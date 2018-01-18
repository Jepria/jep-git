package org.jepria.validator.rules.v10_6_0;

import java.io.IOException;
import java.util.List;

import org.jepria.validator.core.base.MarkSpan;
import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Производит преобразования, связанные с переименованием RefreshEvent в RefreshListEvent:
 * <ol>
 *  <li>Переименовывает {@link #renameImplementsHandler(ClassOrInterfaceDeclaration) класс RefreshEvent.Handler в RefreshEventList.Handler в списке implements} и 
 *  {@link #renameAddHandlerInStartMethod(ClassOrInterfaceDeclaration) методе start}.</li>
 *  <li>Переименовывает {@link #renameOnRefreshMethod(TypeDeclaration) метод onRefresh в onRefreshList} и {@link #renameImport(CompilationUnit) соответствующий import события}.</li>
 *  <li>{@link #renameEventBusCall(ClassOrInterfaceDeclaration) Переименовывает eventBus.refresh() в eventBus.refreshList()}.</li>
 *  <li>{@link #renameToolBarCalls(ClassOrInterfaceDeclaration) В *ToolBarPresenter переименовывает метод refresh() в refreshList()}.</li>
 * </ol>
 */
public class RefreshListEventRule extends ValidatorRule {

  private final String REFRESH_EVENT_NAME = "RefreshEvent";
  
  private final String REFRESH_LIST_EVENT_NAME = "RefreshListEvent";
  
  private final String TOOLBAR_PRESENTER_REFRESH_METHOD_NAME = "refresh";
  
  private final String TOOLBAR_PRESENTER_REFRESH_LIST_METHOD_NAME = "refreshList";
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    if (Boolean.FALSE.equals(resource.getPathName().matches(".+/client/.+"))) return null;
    
    CompilationUnit unit = resource.asCompilationUnit();
    
    boolean modified = renameImport(unit);
    
    for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
      if (typeDeclaration instanceof ClassOrInterfaceDeclaration && 
          (typeDeclaration.getNameAsString() + ".java").equals(resource.getName()) && 
          !((ClassOrInterfaceDeclaration) typeDeclaration).isInterface()) { // Не интерфейс!
        modified |= renameOnRefreshMethod((ClassOrInterfaceDeclaration) typeDeclaration);
        modified |= renameImplementsHandler((ClassOrInterfaceDeclaration) typeDeclaration);
        modified |= renameAddHandlerInStartMethod((ClassOrInterfaceDeclaration) typeDeclaration);
        modified |= renameEventBusCalls((ClassOrInterfaceDeclaration) typeDeclaration);
        modified |= renameToolBarCalls((ClassOrInterfaceDeclaration) typeDeclaration);
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
  
  private boolean renameToolBarCalls(ClassOrInterfaceDeclaration classDeclaration) {
    // Только для презентеров инструментальной панели
    if (Boolean.FALSE.equals(classDeclaration.getNameAsString().contains("ToolBarPresenter"))) return false; 
    
    boolean[] modified = new boolean[] {false}; // effectively final
   
    classDeclaration.accept(new VoidVisitorAdapter<Void>() {
      @Override
      public void visit(MethodCallExpr methodCallExpr, Void arg) {
        String methodName = methodCallExpr.getNameAsString();
        if ("bindButton".equals(methodName) && // Находим вызов bindButton
            methodCallExpr.toString().contains(TOOLBAR_PRESENTER_REFRESH_METHOD_NAME + "()")) { // в котором есть вызов TOOLBAR_PRESENTER_REFRESH_METHOD_NAME
          
          methodCallExpr.getArgument(2).accept(new VoidVisitorAdapter<Void>() { // Делаем обход второго параметра
            @Override
            public void visit(MethodCallExpr bindMethodCallExpr, Void arg) {
              
              if ("refresh".equals(bindMethodCallExpr.getNameAsString())) {
                handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                    "Rename in ToolBarPresenter bindButton call: " + 
                        TOOLBAR_PRESENTER_REFRESH_METHOD_NAME + "() to " + TOOLBAR_PRESENTER_REFRESH_LIST_METHOD_NAME + "().",
                    MarkSpan.of(bindMethodCallExpr.getBegin(), bindMethodCallExpr.getEnd())));
                
                bindMethodCallExpr.setName(TOOLBAR_PRESENTER_REFRESH_LIST_METHOD_NAME);
                
                modified[0] = true;
              }
            }
          }, null);
        } else if (TOOLBAR_PRESENTER_REFRESH_METHOD_NAME.equals(methodName)) { // Находим вызов TOOLBAR_PRESENTER_REFRESH_METHOD_NAME
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Rename in ToolBarPresenter call: " + 
                  TOOLBAR_PRESENTER_REFRESH_METHOD_NAME + "() to " + TOOLBAR_PRESENTER_REFRESH_LIST_METHOD_NAME + "().",
              MarkSpan.of(methodCallExpr.getBegin(), methodCallExpr.getEnd())));
          
          methodCallExpr.setName(TOOLBAR_PRESENTER_REFRESH_LIST_METHOD_NAME);
          
          modified[0] = true;
        }
      }
    }, null);
    
    return modified[0];
  }
  
  private boolean renameEventBusCalls(ClassOrInterfaceDeclaration classDeclaration) {
    // Только для презентеров
    if (Boolean.FALSE.equals(classDeclaration.getNameAsString().contains("Presenter"))) return false; 
    
    boolean[] modified = new boolean[] {false}; // effectively final
    
    classDeclaration.accept(new VoidVisitorAdapter<Void>() {
      @Override
      public void visit(MethodCallExpr methodCallExpr, Void arg) {
        if ("eventBus.refresh()".equals(methodCallExpr.toString())) {
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Rename EventBus calls: eventBus.refresh() to eventBus.refreshList().",
              MarkSpan.of(methodCallExpr.getBegin(), methodCallExpr.getEnd())));
          
          methodCallExpr.setName("refreshList");
          
          modified[0] = true;
        }
      }
    }, null);
    
    return modified[0];
  }
  
  private boolean renameAddHandlerInStartMethod(ClassOrInterfaceDeclaration classDeclaration) {
    boolean modified = false;
    
    List<MethodDeclaration> startMethods = classDeclaration.getMethodsByName("start");
    for (MethodDeclaration startMethod: startMethods) {
      
      List<Parameter> parameters = startMethod.getParameters();
      if (startMethod.isPublic() && // Если public
          startMethod.getType() instanceof VoidType && // Возвращаемый тип void
          parameters.size() == 2 && // Два параметра
          "AcceptsOneWidget".equals(parameters.get(0).getType().toString()) && 
          "EventBus".equals(parameters.get(1).getType().toString())) { 
        
        String eventBusParameterName = parameters.get(1).getNameAsString();
        String stmtToReplace = eventBusParameterName + ".addHandler(" + REFRESH_EVENT_NAME + ".TYPE, this);";
        for (Node startMethodLine: startMethod.getBody().get().getChildNodes()) {
          if (startMethodLine instanceof ExpressionStmt &&
              stmtToReplace.equals(startMethodLine.toString())) {
            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Rename addHandler call in start method.",
                MarkSpan.of(startMethodLine.getBegin(), startMethodLine.getEnd())));
            
            ((ExpressionStmt) startMethodLine).setExpression(
                eventBusParameterName + ".addHandler(" + REFRESH_LIST_EVENT_NAME + ".TYPE, this);");
            
            modified = true;
            break;
          }
        }
      }
    }
    
    return modified;
  }
  
  private boolean renameImplementsHandler(ClassOrInterfaceDeclaration classDeclaration) {
    boolean modified = false;
    
    for (ClassOrInterfaceType classOrInterfaceType: classDeclaration.getImplementedTypes()) {
      if ((REFRESH_EVENT_NAME + ".Handler").equals(classOrInterfaceType.toString())) {
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Rename implementation handler.",
            MarkSpan.of(classOrInterfaceType.getBegin(), classOrInterfaceType.getEnd())));
        
        classOrInterfaceType.setScope(JavaParser.parseClassOrInterfaceType(REFRESH_LIST_EVENT_NAME));
        
        modified = true;
        break;
      }
    }
    
    return modified;
  }
  
  private boolean renameOnRefreshMethod(ClassOrInterfaceDeclaration classDeclaration) {
    boolean modified = false;
    
    List<MethodDeclaration> onRefreshMethods = classDeclaration.getMethodsByName("onRefresh");
    
    for (MethodDeclaration onRefreshMethod: onRefreshMethods) {
      
      List<Parameter> parameters = onRefreshMethod.getParameters();
      if (onRefreshMethod.isPublic() && // Если public
          onRefreshMethod.getType() instanceof VoidType && // Возвращаемый тип void
          parameters.size() == 1) { // Один параметра
        
        Parameter parameter = parameters.get(0); 
        if (REFRESH_EVENT_NAME.equals(parameter.getType().toString())) { // и этот параметр RefreshEvent
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Rename onRefresh to onRefreshList.",
              MarkSpan.of(onRefreshMethod.getBegin(), onRefreshMethod.getEnd())));
          
          parameter.setType(REFRESH_LIST_EVENT_NAME);
          onRefreshMethod.setName("onRefreshList");
          
          modified = true;
          break;
        }
      }
    }
    
    return modified;
  }
  
  private boolean renameImport(CompilationUnit unit) {
    boolean modified = false;
    
    for (ImportDeclaration importd: unit.getImports()) {
      if (importd.getNameAsString().equals("com.technology.jep.jepria.client.ui.eventbus.plain.event.RefreshEvent")) {
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Rename import.",
            MarkSpan.of(importd.getBegin(), importd.getEnd())));
        
        importd.setName("com.technology.jep.jepria.client.ui.eventbus.plain.event.RefreshListEvent");
        
        modified = true;
        break;
      }
    }
    
    return modified;
  }
}
