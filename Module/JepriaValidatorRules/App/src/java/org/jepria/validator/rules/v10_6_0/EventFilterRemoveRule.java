package org.jepria.validator.rules.v10_6_0;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.jepria.validator.core.base.MarkSpan;
import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Производит преобразования, связанные с удалением механизма фильтрации событий:
 * <ol>
 *  <li>{@link #removeFlagFromAddProtection(ClassOrInterfaceDeclaration) Удаляет в *MainModulePresenter третий параметр (флаг типа проверки ролей) из addModuleProtection}.</li>
 *  <li>{@link #replaceCheckAndFireEventMethod(ClassOrInterfaceDeclaration) Заменяет} в *EventBus вызов checkAndFireEvent на fireEvent, 
 *  {@link #removeClienFactoryFromEventBusConstructor(ClassOrInterfaceDeclaration) удаляет} параметр clientFactory из констуктора.</li>
 *  <li>{@link #removeClienFactoryFromEventBusConstructor(ClassOrInterfaceDeclaration) Удаляет в *ClientFactoryImpl, в методе createEventBus, параметр this из конструктора шины}.</li>
 * </ol>
 */
public class EventFilterRemoveRule extends ValidatorRule {

  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    if (Boolean.FALSE.equals(resource.getPathName().matches(".+/client/.+"))) return null;
    
    CompilationUnit unit = resource.asCompilationUnit();
    
    boolean modified = false;
    
    for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
      if (typeDeclaration instanceof ClassOrInterfaceDeclaration && 
          (typeDeclaration.getNameAsString() + ".java").equals(resource.getName()) && 
          !((ClassOrInterfaceDeclaration) typeDeclaration).isInterface()) { // Не интерфейс!
        
        if (Util.isPlainClientFactoryImpl(typeDeclaration) || // Изменения для фабрики
            Util.isMainClientFactoryImpl(typeDeclaration)) { // Изменения для main-фабрики
          modified = removeClientFactoryFromCreateEventBus((ClassOrInterfaceDeclaration) typeDeclaration);
        } else if (typeDeclaration.getNameAsString().contains("EventBus")) { // Изменения для шины
          for (ImportDeclaration importd: unit.getImports()) {
            if (importd.getNameAsString().equals("com.technology.jep.jepria.client.ui.plain.PlainClientFactory")) {
              handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                  "Remove com.technology.jep.jepria.client.ui.plain.PlainClientFactory from import.",
                  MarkSpan.of(importd.getBegin(), importd.getEnd())));
              
              importd.remove();
              
              modified = true;
              break;
            }
          }
          modified |= alterEventBus((ClassOrInterfaceDeclaration) typeDeclaration);
        } else { // Изменения для наследника MainModulePresenter
          
          NodeList<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration) typeDeclaration).getExtendedTypes();
          if (!extendedTypes.isEmpty() && extendedTypes.get(0).getNameAsString().equals("MainModulePresenter")) {
            for (ImportDeclaration importd: unit.getImports()) {
              if (importd.getNameAsString().equals("com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_OR") || 
                  importd.getNameAsString().equals("com.technology.jep.jepria.client.security.ClientSecurity.CHECK_ROLES_BY_AND")) {
                handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                    "Remove protection flag (CHECK_ROLES_BY_OR, CHECK_ROLES_BY_AND) from import.",
                    MarkSpan.of(importd.getBegin(), importd.getEnd())));
                
                importd.remove();
                
                modified = true;
                break;
              }
            }
            modified |= removeFlagFromAddProtection((ClassOrInterfaceDeclaration) typeDeclaration);
          }
        }
      }
    }
    
    if (modified) {
      return Transformation.of().content(new Action<ContentRefactorer>() {
        @Override
        public void perform(ContentRefactorer refactorer) {
          try {
            // сохраним изменения: 
            Util.prettyPrintJava(unit, refactorer.getRefactoringStream());
            handleMessage(new Message("Transformation succeeded"));
          } catch (IOException e) {
            handleThrowable(e);
          }
        }
      });
    } else {
      return null;
    }
  }

  private boolean removeFlagFromAddProtection(ClassOrInterfaceDeclaration classDeclaration) {
    boolean[] modified = new boolean[] {false}; // effectively final
    
    classDeclaration.accept(new VoidVisitorAdapter<Void>() {
      @Override
      public void visit(MethodCallExpr methodCallExpr, Void arg) {
        if ("addModuleProtection".equals(methodCallExpr.getNameAsString()) && // имя функции addModuleProtection
            methodCallExpr.getArguments().size() == 3) { // три параметра
          //следовательно третий параметр флаг и его нужно удалить.
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Remove protection flag (CHECK_ROLES_BY_OR, CHECK_ROLES_BY_AND) in addModuleProtection call.",
              MarkSpan.of(methodCallExpr.getBegin(), methodCallExpr.getEnd())));
          
          methodCallExpr.getArgument(2).remove();
          
          modified[0] = true;
        }
      }
    }, null);
    
    return modified[0];
  }
  
  private boolean alterEventBus(ClassOrInterfaceDeclaration classDeclaration) {
    boolean modified = replaceCheckAndFireEventMethod(classDeclaration);
    return modified || removeClienFactoryFromEventBusConstructor(classDeclaration);
  }
  
  private boolean removeClienFactoryFromEventBusConstructor(ClassOrInterfaceDeclaration classDeclaration) {

    Optional<ConstructorDeclaration> contructorDeclarationOptional = classDeclaration.getConstructorByParameterTypes("PlainClientFactory<?, ?>");
    
    if (!contructorDeclarationOptional.isPresent() || // Если нет конструктора с стандартной сигнатурой 
        classDeclaration.getConstructors().size() != 1) { // Или такой конструктор есть, но в классе более одного конструтора 
      handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
          "If СlientFactory was used in not-standard way, may be needed to remove it manually.",
          MarkSpan.of(classDeclaration.getBegin(), classDeclaration.getEnd())));
    }
    
    if (!contructorDeclarationOptional.isPresent()) return false;
    
    ConstructorDeclaration contructorDeclaration = contructorDeclarationOptional.get();
    
    handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
        "Remove ClientFactoryImpl from EventBusConstructor parameters.",
        MarkSpan.of(contructorDeclaration.getBegin(), contructorDeclaration.getEnd())));
    
    contructorDeclaration.getParameter(0).remove();
    
    for (Node constructorLine: contructorDeclaration.getBody().getChildNodes()) {
      if (constructorLine instanceof ExplicitConstructorInvocationStmt &&
          constructorLine.toString().contains("super") && 
          ((ExplicitConstructorInvocationStmt) constructorLine).getArguments().size() == 1) {
        
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
            "Remove passing clientFactory to super contructor.",
            MarkSpan.of(contructorDeclaration.getBegin(), contructorDeclaration.getEnd())));
        
        ((ExplicitConstructorInvocationStmt) constructorLine).getArgument(0).remove();
      }
    }
    
    return true;
  }

  private boolean replaceCheckAndFireEventMethod(ClassOrInterfaceDeclaration classDeclaration) {
    boolean[] modified = new boolean[] {false};
    
    classDeclaration.accept(new VoidVisitorAdapter<Void>() {
      @Override
      public void visit(MethodCallExpr methodCallExpr, Void arg) {
        if ("checkAndFireEvent".equals(methodCallExpr.getNameAsString())) {

          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Replace checkAndFireEvent to fireEvent.",
              MarkSpan.of(methodCallExpr.getBegin(), methodCallExpr.getEnd())));
          
          methodCallExpr.setName("fireEvent");
          
          modified[0] = true;
        }
      }
    }, null);
    
    return modified[0];
  }

  private boolean removeClientFactoryFromCreateEventBus(ClassOrInterfaceDeclaration classDeclaration) {
    boolean[] modified = new boolean[] {false};
    
    for (MethodDeclaration createEventBusMethod: classDeclaration.getMethodsByName("createEventBus")) {
      createEventBusMethod.accept(new VoidVisitorAdapter<Void>() {
        @Override
        public void visit(ObjectCreationExpr objectCreationExpr, Void arg) {
          List<Expression> objectCreationExprArguments = objectCreationExpr.getArguments();
          if (objectCreationExpr.toString().contains("EventBus") && 
              objectCreationExprArguments.size() == 1 &&
              "this".equals(objectCreationExpr.getArgument(0).toString())) {
            
            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                "Remove 'this' parameter from EventBus constructor in createEventBus method.",
                MarkSpan.of(objectCreationExpr.getBegin(), objectCreationExpr.getEnd())));
            
            objectCreationExpr.getArgument(0).remove();
            
            modified[0] = true;
          }
          
          super.visit(objectCreationExpr, arg);
        }
      }, null);
    }
    
    return modified[0];
  }
}
