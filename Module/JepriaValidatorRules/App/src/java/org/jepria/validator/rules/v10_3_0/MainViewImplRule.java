package org.jepria.validator.rules.v10_3_0;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jepria.validator.core.base.MarkSpan;
import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.exception.TransformationException;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.ContextRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * В конструкторе главной клиентской фабрики ModuleItem замененяются на ID модулей
 * <br>
 * Создается MainViewImpl, если он не создан
 * <br>
 * Переопределяется метод getMainView, если не переопределен
 */
public class MainViewImplRule extends ValidatorRule {
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    // Список пар 'MODULE_ID, module_title()', которые используются в конструкторе с ModuleItem
    // (используются при возможном создании MainViewImpl)
    List<String[]> moduleIdAndModuleTitles = new ArrayList<>();
    
    // Контейтер для строки с контекстным именем прикладного файла *MainViewImpl
    final String[] mainViewImplPathName = new String[1];
    
    if (resource.getName().contains("ClientFactory")) {
      
      boolean modified = false;
      
      // поиск главной клиентской фабрики (а не модульной)
      CompilationUnit unit = resource.asCompilationUnit();
      for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
        if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
          NodeList<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration)typeDeclaration).getExtendedTypes();
          for (ClassOrInterfaceType coit: extendedTypes) {
            if (coit.getNameAsString().equals("MainClientFactoryImpl")) {
              
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
                      
                      for (Expression expr: eci.getArguments()) {
                        if (expr instanceof ObjectCreationExpr) {
                          ObjectCreationExpr oce = (ObjectCreationExpr)expr;
                          if ("ModuleItem".equals(oce.getType().asString())) {
                            NodeList<Expression> args = oce.getArguments();
                            
                            if (args != null && args.size() == 2) {
                              everythingMatched = true;// одного достаточно
                              moduleIdAndModuleTitles.add(new String[]{args.get(0).toString(), args.get(1).toString()});
                            }
                          }
                        }
                      }
                      
                      if (everythingMatched) {
                        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use MODULE_IDs only in super constructor call, instead of ModuleItems",
                            MarkSpan.of(eci.getBegin(), eci.getEnd())));
                        
                        // убираем старый вызов super конструктора
                        nodesToRemove.add(node);
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
                    for (int i = 0; i < moduleIdAndModuleTitles.size(); i++) {
                      if (i > 0) {
                        eciSb.append(',');
                      }
                      eciSb.append(moduleIdAndModuleTitles.get(i)[0]);
                    }
                    eciSb.append(");");
                    
                    blockStmt.addStatement(0, JavaParser.parseExplicitConstructorInvocationStmt(eciSb.toString()));
                    
                    // Найдем имена текстовых констант, из которых брались заголовки модулей
                    // в общем случае их может быть несколько
                    Set<String> textConstantFields = new HashSet<>(); 
                    for (String[] moduleIdAndModuleTitle: moduleIdAndModuleTitles) {
                      int dotIndex = moduleIdAndModuleTitle[1].indexOf('.');
                      if (dotIndex != -1) {
                        String textConstantField = moduleIdAndModuleTitle[1].substring(0, dotIndex);
                        textConstantFields.add(textConstantField);
                      }
                    }
                    
                    // Найдём только те имена текстовых констант, которые используются где-либо ещё (не только в конструкторе)
                    Set<String> textConstantFieldsUsed = new HashSet<>();
                    unit.accept(new VoidVisitorAdapter<Void>() {
                      @Override
                      public void visit(SimpleName n, Void arg) {
                        super.visit(n, arg);
                        
                        if (textConstantFields.contains(n.asString())) {
                          textConstantFieldsUsed.add(n.asString());
                        }
                      }
                    }, null);
                    
                    // Удалим ненужные импорты: ModuleItem и неиспользуемые textConstantFields
                    nodesToRemove.clear();
                    for (ImportDeclaration importd: unit.getImports()) {
                      if (importd.getNameAsString().equals("com.technology.jep.jepria.client.ModuleItem")) {
                        nodesToRemove.add(importd);
                      }
                      String importLastName = importd.getNameAsString().substring(importd.getNameAsString().lastIndexOf('.') + 1);
                      if (textConstantFields.contains(importLastName) && !textConstantFieldsUsed.contains(importLastName)) {
                        nodesToRemove.add(importd);
                      }
                    }
                    
                    for (Node node: nodesToRemove) {
                      node.remove();
                    }
                    
                    modified = true;
                  }
                }
              }
              
              // Проверим наличие MainViewImpl
              final String mainViewImplClassname = resource.getName().substring(0, resource.getName().indexOf("ClientFactory")) + "MainViewImpl";
              mainViewImplPathName[0] = resource.getPathName().substring(0, resource.getPathName().lastIndexOf('/'))
                  + "/ui/main/" + mainViewImplClassname + ".java";
              boolean mainViewImplExists = getContextRead().plainResourceExists(mainViewImplPathName[0]);
              
              if (!mainViewImplExists) {
                handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                    "Create a class extending com.technology.jep.jepria.client.ui.main.MainViewImpl in file " + mainViewImplPathName[0],
                    null));
                
                // создание MainViewImpl происходит в трансформации
              }
              
              
              // проверим метод getMainView
              MethodDeclaration method = Util.getMethodBySignature(typeDeclaration, "getMainView()");
              if (method == null) {
                
                handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
                    "Override getMainView() method and return a static reference to a MainViewImpl instance", 
                    null));
                
                // модификация кода:
                
                // Создадим статическое поле класса mainView
                FieldDeclaration field = new FieldDeclaration(
                    EnumSet.of(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL),
                    new VariableDeclarator(
                        JavaParser.parseType("IsWidget"),
                        "mainView",
                        JavaParser.parseExpression("new " + mainViewImplClassname + "()")));
                
                typeDeclaration.addMember(field);
                
                // Вставим импорт прикладной реализации MainViewImpl (сама реализация будет создана в трансформации)
                final String mainViewImplQualified; 
                if (unit.getPackageDeclaration().isPresent()) {
                  mainViewImplQualified = unit.getPackageDeclaration().get().getNameAsString() + ".ui.main." + mainViewImplClassname;
                } else {
                  mainViewImplQualified = "ui.main." + mainViewImplClassname;
                }
                unit.addImport(mainViewImplQualified);
                
                // Создадим метод getMainView
                MethodDeclaration newMethod = typeDeclaration.addMethod(
                    "getMainView", Modifier.PUBLIC);
                newMethod.setType("IsWidget");
                newMethod.addMarkerAnnotation("Override");
                
                unit.addImport("com.google.gwt.user.client.ui.IsWidget");
                
                BlockStmt newBody = new BlockStmt();
                newBody.addStatement("return mainView;");
                newMethod.setBody(newBody);
              } else {
                //TODO а если метод getMainView существует, но имеет другую начинку?
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
              // сохраним изменения:
              
              Util.prettyPrintJava(unit, refactorer.getRefactoringStream());
              handleMessage(new Message("Transformation succeeded"));
            } catch (IOException e) {
              handleThrowable(e);
            }
          }
        })
            .andOf().context(new Action<ContextRefactorer>() {
          @Override
          public void perform(ContextRefactorer refactorer) {
            boolean mainViewImplExists = getContextRead().plainResourceExists(mainViewImplPathName[0]);
            
            if (!mainViewImplExists) {
              // Создадим файл MainViewImpl
              if (mainViewImplPathName[0] != null) {
                OutputStream os = refactorer.writeNewResource(mainViewImplPathName[0]);
                try {
                  String clientPackage = unit.getPackageDeclaration().get().getNameAsString();
                  String appNamePrefix = resource.getName().substring(0, resource.getName().indexOf("ClientFactory"));
                  
                  try (PrintStream ps = new PrintStream(os, true, "UTF-8")) {
                  
                    ps.println("package " + clientPackage + ".ui.main;");
                    ps.println();
                    ps.println("import static " + clientPackage + "." + appNamePrefix + "ClientConstant.*;");
                    ps.println();
                    ps.println("import java.util.ArrayList;");
                    ps.println("import java.util.List;");
                    ps.println();
                    ps.println("import com.technology.jep.jepria.client.ui.main.MainViewImpl;");
                    ps.println("import com.technology.jep.jepria.client.ui.main.ModuleConfiguration;");
                    ps.println();
                    ps.println("public class " + appNamePrefix + "MainViewImpl extends MainViewImpl {");
                    ps.println();
                    ps.println("  @Override");
                    ps.println("  protected List<ModuleConfiguration> getModuleConfigurations() {");
                    ps.println("    List<ModuleConfiguration> ret = new ArrayList<>();");
                    for (String[] moduleIdAndTitle: moduleIdAndModuleTitles) {
                      ps.println("    ret.add(new ModuleConfiguration(" + moduleIdAndTitle[0] + ", " + moduleIdAndTitle[1] + "));");
                    }
                    ps.println("    return ret;");
                    ps.println("  }");
                    ps.println("}");
                  }
                  
                  handleMessage(new Message("Transformation succeeded"));
                  
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              } else {
                throw new TransformationException("mainViewImplPathName has not been set during validation. Cannot perform transformation");
              }
            }
          }
        });
      }
    }
    
    
    
    return null;
  }

}
