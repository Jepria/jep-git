package org.jepria.validator.rules.v09_x_x;

import java.io.IOException;
import java.util.ArrayList;
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
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;

/**
 * В конструкторе главной клиентской фабрики пары (ID модуля, текстовая константа) заменяются на ModuleItem
 */
public class ModuleItemsMainClientFactoryRule extends ValidatorRule {
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    if (resource.getName().contains("ClientFactory")) {
      
      boolean modified = false;
      
      // поиск главной клиентской фабрики (а не модульной)
      CompilationUnit unit = resource.asCompilationUnit();
      for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
        if (Util.isMainClientFactoryImpl(typeDeclaration)) {
          
          // Список ID модулей, которые будут использоваться в конструкторе с ModuleItem
          List<String> moduleIds = new ArrayList<>();
          // Список строк-наименований модулей, которые будут использоваться в конструкторе с ModuleItem
          List<String> moduleTitles = new ArrayList<>();
          
          for (BodyDeclaration<?> member: typeDeclaration.getMembers()) {
            
            if (member instanceof ConstructorDeclaration) {
              ConstructorDeclaration constructor = (ConstructorDeclaration)member;
              
              BlockStmt blockStmt = constructor.getBody();
              
              List<Node> nodesToRemove = new ArrayList<>();
              
              // Флаг успешного прохождения всех условий правила
              boolean everythingMatched = false;
              
              for (Node node: blockStmt.getChildNodes()) {
                if (node instanceof ExplicitConstructorInvocationStmt) {
                  ExplicitConstructorInvocationStmt eci = (ExplicitConstructorInvocationStmt)node;
                  
                  if (eci.getArguments() != null && eci.getArguments().size() == 2) {
                  
                    if (eci.getArgument(0) instanceof ArrayCreationExpr && eci.getArgument(1) instanceof ArrayCreationExpr) {
                      ArrayCreationExpr arg0 = (ArrayCreationExpr)eci.getArgument(0);
                      ArrayCreationExpr arg1 = (ArrayCreationExpr)eci.getArgument(1);
                      
                      if ("String".equals(arg0.getElementType().asString())
                          && arg0.getInitializer().isPresent() 
                          && "String".equals(arg1.getElementType().asString())
                          && arg1.getInitializer().isPresent()) {
                        
                        for (Node moduleIdNode: arg0.getInitializer().get().getChildNodes()) {
                          moduleIds.add(moduleIdNode.toString());
                        }
                        
                        for (Node moduleTitleNode: arg1.getInitializer().get().getChildNodes()) {
                          moduleTitles.add(moduleTitleNode.toString());
                        }
                        
                        if (moduleIds.size() > 0 && moduleIds.size() == moduleTitles.size()) {
                          everythingMatched = true;
                        }
                      }
                      
                      if (everythingMatched) {
                        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use ModuleItems in super constructor call, instead of two parallel arrays",
                            MarkSpan.of(eci.getBegin(), eci.getEnd())));
                        
                        // убираем старый вызов super конструктора
                        nodesToRemove.add(node);
                      } else {
                        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
                            "It may be possible to use ModuleItems in super constructor call, instead of two parallel arrays",
                            MarkSpan.of(eci.getBegin(), eci.getEnd())));
                      }
                    }
                  }
                }
              }
              
              if (everythingMatched) {
                
                // модификация кода:
                
                for (Node node: nodesToRemove) {
                  node.remove();
                }
                
                // формуируем строку вызова super конструктора
                StringBuilder eciSb = new StringBuilder();
                eciSb.append("super(");
                for (int i = 0; i < moduleIds.size(); i++) {
                  if (i > 0) {
                    eciSb.append(',');
                  }
                  eciSb.append("new ModuleItem(")
                    .append(moduleIds.get(i))
                    .append(", ")
                    .append(moduleTitles.get(i))
                    .append(")");
                }
                eciSb.append(");");
                
                blockStmt.addStatement(0, JavaParser.parseExplicitConstructorInvocationStmt(eciSb.toString()));
                
                // Добавим импорт
                unit.addImport("com.technology.jep.jepria.client.ModuleItem");
                
                modified = true;
              }
            }
          }
        }
      }
      
      if (modified) {
        return Transformation
            .of()
            .content(new Action<ContentRefactorer>() {
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
      }
    }
    
    return null;
  }
}
