package org.jepria.validator.rules.v09_x_x;

import java.io.IOException;

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
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;

/**
 *  В *ListFormViewImpl вызов grid.setDndEnabled в конструкторе исправлен на list.setDndEnabled
 */
public class SetDndEnabledRule extends ValidatorRule {
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    
    
    if (resource.getName().endsWith("ListFormViewImpl.java")) {
      CompilationUnit unit = resource.asCompilationUnit();
      
      boolean modified = false;
      
      for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
  
        // Найдем конструкторы
        for (BodyDeclaration<?> member: typeDeclaration.getMembers()) {

          if (member instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructor = (ConstructorDeclaration)member;

            BlockStmt blockStmt = constructor.getBody();

            for (Node node: blockStmt.getChildNodes()) {
              if (node instanceof ExpressionStmt) {
                ExpressionStmt expNode = ((ExpressionStmt)node); 
                Expression exp = expNode.getExpression();
                if (exp.toString().startsWith("grid.setDndEnabled(")) {
                  
                  handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                      "Invoke setDndEnabled on list instead of grid",
                      MarkSpan.of(exp.getBegin(), exp.getEnd())));
                  
                  expNode.setExpression(exp.toString().replace("grid", "list"));
                  modified = true;
                }
              }
            }
          }
        }
      }
      
      if (modified) {
        return Transformation.of().content(new Action<ContentRefactorer>() {
          @Override
          public void perform(ContentRefactorer refactorer) {
            try {
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
