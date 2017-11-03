package org.jepria.validator.rules.v09_x_x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jepria.validator.core.base.MarkSpan;
import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.resource.ResourceUtils;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.ResourceRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;
import org.jepria.validator.rules.MessageLevel;
import org.jepria.validator.rules.Util;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * Преобразует *ServiceImpl от ejb к dao виду
 * <br>
 * Удаляет из *ServerConstant объявления полей BEAN_JNDI_NAME
 * <br>
 * Преобразует *Bean от ejb к dao виду
 * <br>
 * Преобразует ejb/dao-интерфейс от ejb к dao виду
 */
public class EjbToDaoRule extends ValidatorRule {
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    if (resource.getName().endsWith("ServiceImpl.java")) {
      CompilationUnit unit = resource.asCompilationUnit();
      boolean modified = forGwtServiceImpl(unit);
      
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
    } else if (resource.getName().endsWith("ServerConstant.java")) {
      CompilationUnit unit = resource.asCompilationUnit();
      boolean modified = forServerConstant(unit);
      
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
    } else if (resource.getPathName().contains("/ejb/") 
        && resource.getName().endsWith("Bean.java")) {
      CompilationUnit unit = resource.asCompilationUnit();
      boolean modified = forBean(unit);
      
      final String newName = resource.getName().replaceAll("Bean", "Dao");
      
      handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
          "Rename the resource to " + newName, 
          null));
      
      
      // Изменим package
      modified |= ejbToDaoPackage(unit);
      
      // действие по переименованию Bean в Dao
      final Action<ResourceRefactorer> renameAction;
      final String pathName = resource.getPathName();
      int ind = pathName.lastIndexOf("/server/");
      if (ind != -1) {
        // переименовываем из Bean в Dao и перемещаем в пакет dao
        renameAction = new Action<ResourceRefactorer>() {
          @Override
          public void perform(ResourceRefactorer refactorer) {
            String newPathName = pathName.substring(0, ind + "/server/".length()) + "dao/" + newName;
            refactorer.renameThisResource(newPathName);
            handleMessage(new Message("Transformation succeeded"));
          }
        };
      } else {
        // только переименовываем из Bean в Dao
        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
            "The resource is not located within 'server' folder. Move it manually to 'server/dao' package", 
            null));
        renameAction = new Action<ResourceRefactorer>() {
          @Override
          public void perform(ResourceRefactorer refactorer) {
            String newPathName = pathName.substring(0, pathName.lastIndexOf(resource.getName())) + newName;
            refactorer.renameThisResource(newPathName);
            handleMessage(new Message("Transformation succeeded"));
          }
        };
      }
      
      if (modified) {
        return Transformation
            .of()
            .content(new Action<ContentRefactorer>() {
              @Override
              public void perform(ContentRefactorer refactorer) {
                try {
                  Util.prettyPrintJava(unit, refactorer.getRefactoringStream());
                  handleMessage(new Message("Transformation succeeded"));
                } catch (IOException e) {
                  handleThrowable(e);
                }
              }
            })
            .andOf()
            .resource(renameAction);
      }
    } else if (resource.getPathName().contains("/ejb/")
        && !resource.getName().endsWith("Bean.java")
        && !resource.getName().endsWith("Local.java")
        && !resource.getName().endsWith("Remote.java")) {
      
      // ВЕРОЯТНО это ejb/dao-интерфейс. Проверим сразу же:
      
      CompilationUnit unit = resource.asCompilationUnit();
      for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
        if (typeDeclaration instanceof ClassOrInterfaceDeclaration 
            && (typeDeclaration.getNameAsString() + ".java").equals(resource.getName())) {
          for (ClassOrInterfaceType extended: ((ClassOrInterfaceDeclaration)typeDeclaration).getExtendedTypes()) {
            if (extended.getNameAsString().equals("JepDataStandard")) {
              
              // действие по переименованию Bean в Dao
              final Action<ResourceRefactorer> renameAction;
              final String pathName = resource.getPathName();
              int ind = pathName.lastIndexOf("/server/");
              if (ind != -1) {
                // перемещаем в пакет dao
                renameAction = new Action<ResourceRefactorer>() {
                  @Override
                  public void perform(ResourceRefactorer refactorer) {
                    String newPathName = pathName.substring(0, ind + "/server/".length()) + "dao/" + resource.getName();
                    refactorer.renameThisResource(newPathName);
                    handleMessage(new Message("Transformation succeeded"));
                  }
                };
              } else {
                handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
                    "The resource is not located within 'server' folder. Move it manually to 'server/dao' package", 
                    null));
                renameAction = null;
              }

              // модификация кода:
              
              ejbToDaoPackage(unit);
              
              // переименуем импорт JepDataStandard
              for (ImportDeclaration importd: unit.getImports()) {
                if (importd.getNameAsString().equals("com.technology.jep.jepria.server.ejb.JepDataStandard")) {
                  // модификация кода:
                  importd.setName("com.technology.jep.jepria.server.dao.JepDataStandard");
                  break;
                }
              }
              
              return Transformation
                  .of()
                  .content(new Action<ContentRefactorer>() {
                    @Override
                    public void perform(ContentRefactorer refactorer) {
                      // сохраним изменения:
                      try {
                        Util.prettyPrintJava(unit, refactorer.getRefactoringStream());
                        handleMessage(new Message("Transformation succeeded"));
                      } catch (IOException e) {
                        handleThrowable(e);
                      }
                    }
                  })
                  .andOf()
                  .resource(renameAction);
            }
          }
        }
      }
    }
    
    
    return null;
  }
  
  /**
   * Преобразует package юнита из .ejb. к .dao.
   * @param unit
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean ejbToDaoPackage(CompilationUnit unit) {
    if (unit.getPackageDeclaration().isPresent()) {
      PackageDeclaration packaged = unit.getPackageDeclaration().get();
      String packageName = packaged.getNameAsString();
      if (packageName.contains(".server.") || packageName.endsWith(".server")) {
        String newPackageName = packageName.substring(0, packageName.lastIndexOf("server") + "server".length()) + ".dao"; 
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
            "Change package declaration to " + newPackageName, 
            MarkSpan.of(packaged.getName().getBegin(), packaged.getName().getEnd())));
        
        packaged.setName(newPackageName);
        return true;
      } else {
        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
            "The type is not located within 'server' package. Move it manually to 'server.dao' package", 
            MarkSpan.of(packaged.getName().getBegin(), packaged.getName().getEnd())));
      }
    }
    return false;
  }
  
  /**
   * Преобразует *ServiceImpl от ejb к dao виду
   * @param unit
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean forGwtServiceImpl(CompilationUnit unit) {
    
    boolean modified = false;
    
    for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
      if (Util.isGwtServiceImpl(typeDeclaration)) {

        // Найдем конструкторы
        for (BodyDeclaration<?> member: typeDeclaration.getMembers()) {
          
          if (member instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructor = (ConstructorDeclaration)member;
            
            BlockStmt blockStmt = constructor.getBody();
            
            for (Node node: blockStmt.getChildNodes()) {
              if (node instanceof ExplicitConstructorInvocationStmt) {
                ExplicitConstructorInvocationStmt eci = (ExplicitConstructorInvocationStmt)node;
                
                Node beanJndiNameNode = null;
                
                for (Expression expr: eci.getArguments()) {
                  if ("BEAN_JNDI_NAME".equals(expr.toString())) {
                    beanJndiNameNode = expr;
                    break;
                  }
                }
                
                
                
                // Вычислим название ejb/dao-интерфейса модуля
                String daoInterfaceName = null;
                
                NodeList<ClassOrInterfaceType> implementeds = ((ClassOrInterfaceDeclaration)typeDeclaration).getImplementedTypes();
                for (ClassOrInterfaceType implemented: implementeds) {
                  String name = implemented.getNameAsString();
                  if (name.endsWith("Service")) {
                    daoInterfaceName = name.substring(0, name.lastIndexOf("Service"));
                    break;
                  }
                }
                if (daoInterfaceName == null) {
                  // fallback
                  daoInterfaceName = typeDeclaration.getNameAsString().substring(0, typeDeclaration.getNameAsString().lastIndexOf("ServiceImpl"));
                }
                
                
                
                if (beanJndiNameNode != null) {
                  if (eci.getArguments().size() == 2 && eci.getArgument(1) == beanJndiNameNode) {
                    handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use ServerFactory instance in super constructor call, instead of BEAN_JNDI_NAME",
                        MarkSpan.of(beanJndiNameNode.getBegin(), beanJndiNameNode.getEnd())));

                    // модификация кода:
                    
                    eci.setArgument(1, JavaParser.parseExpression("new ServerFactory<" + daoInterfaceName + ">(new " + daoInterfaceName + "Dao(), DATA_SOURCE_JNDI_NAME)"));
                    
                    // Проверим, что dao интерфейс с вычисленным именем уже существует в проекте
                    String daoInterfacePackage = null;
                    List<PlainResource> daoInterfaces = getContextRead().listPlainResourcesByName(daoInterfaceName + ".java");
                    for (PlainResource r: daoInterfaces) {
                      if (r.getPathName().contains("/dao/")) {
                        CompilationUnit daoUnit = ResourceUtils.asJava(r.newInputStream());
                        if (daoUnit.getPackageDeclaration().isPresent()) {
                          daoInterfacePackage = daoUnit.getPackageDeclaration().get().getNameAsString();
                        }
                        break;
                      }
                    }
                    if (daoInterfacePackage == null) {
                      // dao интерфейс не существует, но будет создан
                      if (unit.getPackageDeclaration().isPresent()) {
                        String packaged = unit.getPackageDeclaration().get().getNameAsString();
                        int ind = packaged.lastIndexOf("server");
                        if (ind != -1) {
                          daoInterfacePackage = packaged.substring(0, ind + "server".length()) + ".dao"; 
                        } else {
                          daoInterfacePackage = "dao";
                        }
                      }
                    }
                    
                    // добавим импорты
                    unit.addImport("com.technology.jep.jepria.server.ServerFactory");
                    unit.addImport(daoInterfacePackage + "." + daoInterfaceName);
                    unit.addImport(daoInterfacePackage + "." + daoInterfaceName + "Dao");
                    // изменим импорт BEAN_JNDI_NAME на DATA_SOURCE_JNDI_NAME
                    for (ImportDeclaration importd: unit.getImports()) {
                      if (importd.getNameAsString().contains("BEAN_JNDI_NAME")) {
                        importd.setName(importd.getNameAsString().replaceAll("BEAN_JNDI_NAME", "DATA_SOURCE_JNDI_NAME"));
                      }
                    }
                    
                    modified = true;
                    
                  } else {
                    // сигнатура вызова супер конструктора неожиданна, но BEAN_JNDI_NAME все же содержится
                    handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, "Use ServerFactory instance in super constructor call, instead of BEAN_JNDI_NAME",
                        MarkSpan.of(beanJndiNameNode.getBegin(), beanJndiNameNode.getEnd())));
                  }
                  
                  // Добавим типовой параметр к JepDataServiceServlet
                  NodeList<ClassOrInterfaceType> extendedTypes = ((ClassOrInterfaceDeclaration)typeDeclaration).getExtendedTypes();
                  for (ClassOrInterfaceType extType: extendedTypes) {
                    if (extType.getNameAsString().equals("JepDataServiceServlet")) {
                      if (!extType.getTypeArguments().isPresent()) {
                        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Parametrize the type JepDataServiceServlet with " + daoInterfaceName,
                            MarkSpan.of(extType.getBegin(), extType.getEnd())));
                        
                        // модификация кода:
                        extType.setTypeArguments(JavaParser.parseType(daoInterfaceName));
                        
                        modified = true;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    
    return modified;
  }
  
  /**
   * Удаляет из *ServerConstant объявления полей BEAN_JNDI_NAME
   * @param unit
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean forServerConstant(CompilationUnit unit) {
    boolean modified = false;
    
    List<Node> nodesToRemove = new ArrayList<>();
    
    for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
      for (BodyDeclaration<?> member: typeDeclaration.getMembers()) {
        if (member instanceof FieldDeclaration) {
          FieldDeclaration field = (FieldDeclaration)member;
          if (field.getChildNodes().size() == 1 && field.getChildNodes().get(0) instanceof VariableDeclarator) {
            VariableDeclarator variable = (VariableDeclarator)field.getChildNodes().get(0);
            if ("BEAN_JNDI_NAME".equals(variable.getNameAsString())) {
              handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Remove the BEAN_JNDI_NAME field declaration",
                  MarkSpan.of(variable.getName().getBegin(), variable.getName().getEnd())));
              
              nodesToRemove.add(field);
              modified = true;
            }
          }
        }
      }
    }
    
    // модификация кода:
    if (modified) {
      for (Node node: nodesToRemove) {
        node.remove();
      }
    }
    return modified;
  }
  
  
  /**
   * Список импортов класса *Bean, которые нужно удалить
   */
  private static final Set<String> beanImportsToRemove = new HashSet<String>() {
    private static final long serialVersionUID = 1L;
  {
    add("javax.ejb.Local");
    add("javax.ejb.Remote");
    add("javax.ejb.Stateless");
    add("oracle.j2ee.ejb.StatelessDeployment");
    add("com.technology.jep.jepria.server.ejb.JepDataStandardBean");
  }};
  
  /**
   * Список аннотаций класса *Bean, которые нужно удалить
   */
  private static final Set<String> beanAnnotationsToRemove = new HashSet<String>() {
    private static final long serialVersionUID = 1L;
  {
    add("Local");
    add("Remote");
    add("Stateless");
    add("StatelessDeployment");
  }};
  
  /**
   * Преобразует *Bean от ejb к dao виду
   * @param unit
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean forBean(CompilationUnit unit) {
    boolean modified = false;
    
    // список всех элементов (любого типа), которые нудно будет удалить из юнита
    List<Node> nodesToRemove = new ArrayList<>();
    
    // реорганизуем импорты
    for (ImportDeclaration importd: unit.getImports()) {
      if (importd.getNameAsString().endsWith("DATA_SOURCE_JNDI_NAME")
          || importd.getNameAsString().endsWith("RESOURCE_BUNDLE_NAME")
          || beanImportsToRemove.contains(importd.getNameAsString())) {
        nodesToRemove.add(importd);
        modified = true;
      }
    }

    unit.addImport("com.technology.jep.jepria.server.dao.JepDaoStandard");
    
    for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
      // переименуем extends
      for (ClassOrInterfaceType extended: ((ClassOrInterfaceDeclaration)typeDeclaration).getExtendedTypes()) {
        if (extended.getNameAsString().equals("JepDataStandardBean")) {
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Set the type extends JepDaoStandard",
              MarkSpan.of(extended.getBegin(), extended.getEnd())));
          
          extended.setName("JepDaoStandard");
          modified = true;
        }
      }
      
      // найдем класс-интерфейс dao implements
      String daoInterfaceName = null;
      for (ClassOrInterfaceType implemented: ((ClassOrInterfaceDeclaration)typeDeclaration).getImplementedTypes()) {
        if ((implemented.getNameAsString() + "Bean").equals(typeDeclaration.getNameAsString())) {
          daoInterfaceName = implemented.getNameAsString();
          break;
        }
      }
      if (daoInterfaceName == null) {
        String typeName = typeDeclaration.getNameAsString();
        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
            "The class " + typeName + " does not implement the interface "
            + typeName.substring(0, typeName.lastIndexOf("Bean")) + ", resolve the import manually",
            MarkSpan.of(typeDeclaration.getName().getBegin(), typeDeclaration.getName().getEnd())));
      } else {
        // уберем импорт интерфейса (он должен быть в том же пакете)
        for (ImportDeclaration importd: unit.getImports()) {
          if (importd.getNameAsString().endsWith("." + daoInterfaceName)) {
            nodesToRemove.add(importd);
            modified = true;
            break;
          }
        }
      }
      
      // переименуем класс
      if (typeDeclaration.getNameAsString().endsWith("Bean")) {
        String newTypeName = typeDeclaration.getNameAsString().replaceAll("Bean", "Dao");
        
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
            "Rename the type to " + newTypeName,
            MarkSpan.of(typeDeclaration.getName().getBegin(), typeDeclaration.getName().getEnd())));
        
        typeDeclaration.setName(newTypeName);
        modified = true;
      }
      
      // уберем аннотации
      for (AnnotationExpr annotation: typeDeclaration.getAnnotations()) {
        if (beanAnnotationsToRemove.contains(annotation.getNameAsString())) {
          nodesToRemove.add(annotation);
          modified = true;
        }
      }
      
      // Уберем вызов супер-конструктора с аргументами DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME
      for (BodyDeclaration<?> member: typeDeclaration.getMembers()) {
        if (member instanceof ConstructorDeclaration) {
          ConstructorDeclaration constructor = (ConstructorDeclaration)member;
          
          BlockStmt blockStmt = constructor.getBody();
          
          for (Node node: blockStmt.getChildNodes()) {
            if (node instanceof ExplicitConstructorInvocationStmt) {
              ExplicitConstructorInvocationStmt eci = (ExplicitConstructorInvocationStmt)node;
              if (eci.getArguments() != null && eci.getArguments().size() == 2
                  && eci.getArgument(0).toString().equals("DATA_SOURCE_JNDI_NAME") 
                  && eci.getArgument(1).toString().equals("RESOURCE_BUNDLE_NAME")) {
                nodesToRemove.add(node);
                modified = true;
                break;
              }
            }
          }
          
        }
      }
    }
    
    for (Node node: nodesToRemove) {
      node.remove();
    }
    
    return modified;
  }
  
}
