package org.jepria.validator.rules.v10_3_0;

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

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

/**
 * Убирает вызовы initActivityMappers из конструкторов всех клиентских фабрик
 */
public class InitActivityMappersRule extends ValidatorRule {
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    if (resource.getName().contains("ClientFactory")) {
      
      final CompilationUnit unit = resource.asCompilationUnit();
      boolean modified = false;
      
      List<TypeDeclaration<?>> typeDeclarations = unit.getTypes();
      for (TypeDeclaration<?> typeDeclaration: typeDeclarations) {
        for (BodyDeclaration<?> member: typeDeclaration.getMembers()) {
          
          // Убирает вызовы initActivityMappers из конструкторов всех клиентских фабрик
          if (member instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructor = (ConstructorDeclaration)member;
            BlockStmt blockStmt = constructor.getBody();
            
            List<Node> nodesToRemove = new ArrayList<>();
            
            for (Node node: blockStmt.getChildNodes()) {
              if (node instanceof ExpressionStmt) {
                if ("initActivityMappers(this);".equals(node.toString())) {
                  handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Remove initActivityMappers invocation from constructor",
                      MarkSpan.of(node.getBegin(), node.getEnd())));
                  
                  // модификация кода:
                  nodesToRemove.add(node);
                  modified = true;
                }
              }
            }
            
            for (Node node: nodesToRemove) {
              node.remove();
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
    
    return null;
  }
}
