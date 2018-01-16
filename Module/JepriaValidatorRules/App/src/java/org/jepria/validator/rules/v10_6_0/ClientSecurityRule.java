package org.jepria.validator.rules.v10_6_0;

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

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Заменяет статические вызовы методов isUserHaveAllRoles, isUserHaveRoles класса ClientSecurity
 * на вызовы через статическое поле instance (экземпляр singleton).
 */
public class ClientSecurityRule extends ValidatorRule {
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    if (Boolean.FALSE.equals(resource.getPathName().matches(".+/client/.+"))) return null;
    
    CompilationUnit unit = resource.asCompilationUnit();
    boolean[] modified = new boolean[] {false}; //effectively final
    
    unit.accept(new VoidVisitorAdapter<Void>() {
      @Override
      public void visit(MethodCallExpr methodCallExpr, Void arg) {
        if ("isUserHaveAllRoles".equals(methodCallExpr.getNameAsString()) || 
            "isUserHaveRoles".equals(methodCallExpr.getNameAsString())) {
          
          Node firstChild = methodCallExpr.getChildNodes().get(0);
          if (firstChild instanceof NameExpr && 
              "ClientSecurity".equals(((NameExpr) firstChild).getNameAsString())) {

            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                "Replace ClientSecurity static method call to instance method call.",
                MarkSpan.of(methodCallExpr.getBegin(), methodCallExpr.getEnd())));
            
            methodCallExpr.setScope(JavaParser.parseExpression("ClientSecurity.instance"));
            modified[0] = true;
          }
        }
      }
    }, null);

    if (modified[0]) {
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
}
