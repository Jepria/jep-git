package org.jepria.validator.rules.v10_3_0;

import java.io.IOException;

import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.Util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * Изменяется тип метода createMainModulePresenter
 */
public class CreateMainModulePresenterTypeRule extends ValidatorRule {
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    if (resource.getName().contains("ClientFactory")) {
      
      boolean modified = false;
      
      // поиск главной клиентской фабрики (а не модульной)
      CompilationUnit unit = resource.asCompilationUnit();
      for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
        if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
          NodeList<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration)typeDeclaration).getExtendedTypes();
          for (ClassOrInterfaceType coit: extendedTypes) {
            if (coit.getNameAsString().equals("MainClientFactoryImpl")) {
              
              // Проверим метод createMainModulePresenter
              MethodDeclaration createMainModulePresenterMethod = Util.getMethodBySignature(typeDeclaration, "createMainModulePresenter()");
              if (createMainModulePresenterMethod != null) {
                if ("Activity".equals(createMainModulePresenterMethod.getType().toString())) {
                  // модификация кода:
                  
                  // Изменим тип метода createMainModulePresenter
                  createMainModulePresenterMethod.setType(resource.getName().substring(0, resource.getName().indexOf("ClientFactory")) + "MainModulePresenter");
                  
                  // Уберем импорт Activity
                  for (ImportDeclaration importd: unit.getImports()) {
                    if ("com.google.gwt.activity.shared.Activity".equals(importd.getNameAsString())) {
                      importd.remove();
                      break;
                    }
                  }
                            
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
