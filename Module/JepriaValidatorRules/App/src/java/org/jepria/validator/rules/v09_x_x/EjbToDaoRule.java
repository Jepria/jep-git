package org.jepria.validator.rules.v09_x_x;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jepria.validator.core.base.MarkSpan;
import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.ContextRefactorer;
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
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Преобразует *ServiceImpl от ejb к dao виду
 * <br>
 * Создает класс *ServerFactory
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
      boolean modified = forServiceImpl(unit);
      
      
      
      // действие по созданию класса *ServerFactory
      Action<ContextRefactorer> createServerFactoryAction = null;
      
      final String appNamePrefix = resource.getName().substring(0, resource.getName().lastIndexOf("ServiceImpl.java"));
      String serverFactoryPathName = resource.getPathName().substring(0, resource.getPathName().lastIndexOf("/server/") 
          + "/server/".length()) + appNamePrefix + "ServerFactory.java";
      
      if (!getContextRead().plainResourceExists(serverFactoryPathName)) {
        
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
            "Create a class extending com.technology.jep.jepria.server.ServerFactory in file " + serverFactoryPathName,
            null));
        
        // Вычислим название server-пакета
        String serverPackageName = null;
        if (unit.getPackageDeclaration().isPresent()) {
          String packaged = unit.getPackageDeclaration().get().getNameAsString();
          int ind = packaged.lastIndexOf("server");
          if (ind != -1) {
            serverPackageName = packaged.substring(0, ind + "server".length()); 
          }
        }
        if (serverPackageName == null) {
          // fallback
          handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
              "The resource is not located within 'server' folder. Move the new resource " 
              + serverFactoryPathName + " into 'server' package manually", 
              null));
          serverPackageName = "server";
        }
        final String serverPackageNameFinal = serverPackageName;
        
        createServerFactoryAction = new Action<ContextRefactorer>() {
          @Override
          public void perform(ContextRefactorer refactorer) {
            OutputStream os = refactorer.writeNewResource(serverFactoryPathName);
            final String serverFactoryClassname = appNamePrefix + "ServerFactory";
            
            try {
              try (PrintStream ps = new PrintStream(os, true, "UTF-8")) {
                ps.println("package " + serverPackageNameFinal + ";");
                ps.println();
                ps.println("import static " + serverPackageNameFinal + "." + appNamePrefix + "ServerConstant.DATA_SOURCE_JNDI_NAME;");
                ps.println();
                ps.println("import com.technology.jep.jepria.server.ServerFactory;");
                ps.println("import " + serverPackageNameFinal + ".dao." + appNamePrefix + ";");
                ps.println("import " + serverPackageNameFinal + ".dao." + appNamePrefix + "Dao;");
                ps.println();
                ps.println("public class " + serverFactoryClassname + " extends ServerFactory<" + appNamePrefix + "> {");
                ps.println();
                ps.println("  private " + serverFactoryClassname + "() {");
                ps.println("    super(new " + appNamePrefix + "Dao(), DATA_SOURCE_JNDI_NAME);");
                ps.println("  }");
                ps.println();
                ps.println("  public static final " + serverFactoryClassname + " instance = new " + serverFactoryClassname + "();");
                ps.println();
                ps.println("}");
              }
              
              handleMessage(new Message("Transformation succeeded"));
            } catch (IOException e) {
              handleThrowable(e);
            }
          }
        };
        
        modified = true;
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
            .context(createServerFactoryAction);
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
  private boolean forServiceImpl(CompilationUnit unit) {
    
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
                
                
                if (beanJndiNameNode != null) {
                  
                  
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
                  
                  
                  
                  if (eci.getArguments().size() == 2 && eci.getArgument(1) == beanJndiNameNode) {
                    handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use ServerFactory instance in super constructor call, instead of BEAN_JNDI_NAME",
                        MarkSpan.of(beanJndiNameNode.getBegin(), beanJndiNameNode.getEnd())));

                    // модификация кода:
                    
                    String serverFactoryClassname = typeDeclaration.getNameAsString().substring(0, typeDeclaration.getNameAsString().lastIndexOf("ServiceImpl"))
                        + "ServerFactory"; 
                    
                    eci.setArgument(1, JavaParser.parseExpression(serverFactoryClassname + ".instance"));
                    
                    // Вычислим название server-пакета
                    String serverPackageName = null;
                    if (unit.getPackageDeclaration().isPresent()) {
                      String packaged = unit.getPackageDeclaration().get().getNameAsString();
                      int ind = packaged.lastIndexOf("server");
                      if (ind != -1) {
                        serverPackageName = packaged.substring(0, ind + "server".length()); 
                      }
                    }
                    if (serverPackageName == null) {
                      // fallback
                      handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, 
                          "The resource is not located within 'server' folder. The imports in this type must be resolved manually", 
                          null));
                      serverPackageName = "server";
                    }
                    
                    
                    
                    // добавим импорты
                    unit.addImport(serverPackageName + "." + serverFactoryClassname);
                    unit.addImport(serverPackageName + ".dao." + daoInterfaceName);
                    
                    // удалим импорт BEAN_JNDI_NAME и ejb-импорты
                    List<Node> nodesToRemove = new ArrayList<>();
                    for (ImportDeclaration importd: unit.getImports()) {
                      String name = importd.getNameAsString();
                      if (name.contains("BEAN_JNDI_NAME") 
                          || name.contains(".ejb.")) {
                        nodesToRemove.add(importd);
                      }
                    }
                    for (Node node2: nodesToRemove) {
                      node2.remove();
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
        
        
        // Заменим ejbLookup и вызов ejb обращениями к dao
        
        final boolean[] modified1 = new boolean[]{false};
        List<Node> nodesToRemove = new ArrayList<>();

        VoidVisitorAdapter<Void> ejbLookupVisitor = new VoidVisitorAdapter<Void>() {
          @Override
          public void visit(MethodCallExpr node, Void arg) {
            super.visit(node, arg);
            if ("ejbLookup".equals(node.getNameAsString())) {
              boolean everythingMatched = false;
              Node parent = node;
              if (parent.getParentNode().isPresent()) {
                parent = parent.getParentNode().get();
                if (parent instanceof CastExpr && parent.getParentNode().isPresent()) {
                  parent = parent.getParentNode().get();
                  if (parent instanceof VariableDeclarator && parent.getParentNode().isPresent()) {
                    // Сохраним название ejb-переменной
                    final String varName = ((VariableDeclarator)parent).getNameAsString();
                    
                    parent = parent.getParentNode().get();
                    if (parent instanceof VariableDeclarationExpr && parent.getParentNode().isPresent()) {
                      parent = parent.getParentNode().get();
                      if (parent instanceof ExpressionStmt && parent.getParentNode().isPresent()) {
                        Node parent2 = parent.getParentNode().get();
                        if (parent2 instanceof BlockStmt) {
                          everythingMatched = true;
                          
                          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Remove the usage of ejbLookup method",
                              MarkSpan.of(node.getName().getBegin(), node.getName().getEnd())));
                          
                          nodesToRemove.add(parent);
                          
                          // Заменим в блоке-родителе все использования ejb-переменной на dao
                          parent2.accept(new EjbVarNameVisitor(varName), null);
                          
                          modified1[0] = true;
                        }
                      }
                    }
                  }
                }
              }
              if (!everythingMatched) {
                handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, "Remove the usage of the ejbLookup method",
                    MarkSpan.of(node.getName().getBegin(), node.getName().getEnd())));
              }
            }
          }
        };
        
        unit.accept(ejbLookupVisitor, null);
        modified |= modified1[0];
        
        // модификация кода
        for (Node node: nodesToRemove) {
          node.remove();
        }
      }
    }
    
    return modified;
  }
  
  /**
   * Посетитель, заменяющий имена переменных ejb (по заданному имени) на dao 
   */
  private class EjbVarNameVisitor extends VoidVisitorAdapter<Void> {
    private final String ejbVarName;
    public EjbVarNameVisitor(String ejbVarName) {
      this.ejbVarName = ejbVarName;
    }
    @Override
    public void visit(SimpleName node, Void arg) {
      super.visit(node, arg);
      if (ejbVarName.equals(node.asString())) {
        // модификация кода:
        
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use the field 'dao' instead of variable '" + ejbVarName + "'",
            MarkSpan.of(node.getBegin(), node.getEnd())));
        
        node.setIdentifier("dao");
      }
    }
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
    add("com.technology.jep.jepria.server.ejb.JepDataBean");
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

    for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
      // переименуем extends (из JepDataStandardBean в JepDaoStandard, из JepDataBean в JepDao)
      for (ClassOrInterfaceType extended: ((ClassOrInterfaceDeclaration)typeDeclaration).getExtendedTypes()) {
        if (extended.getNameAsString().equals("JepDataStandardBean")) {
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Set the type extends JepDaoStandard",
              MarkSpan.of(extended.getBegin(), extended.getEnd())));
          
          extended.setName("JepDaoStandard");
          unit.addImport("com.technology.jep.jepria.server.dao.JepDaoStandard");
          
          modified = true;
        } else if (extended.getNameAsString().equals("JepDataBean")) {
          handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
              "Set the type extends JepDao",
              MarkSpan.of(extended.getBegin(), extended.getEnd())));
          
          extended.setName("JepDao");
          unit.addImport("com.technology.jep.jepria.server.dao.JepDao");
          
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
      
      
      if (typeDeclaration.getNameAsString().endsWith("Bean")) {
        // переименуем класс
        String newTypeName = typeDeclaration.getNameAsString().replaceAll("Bean", "Dao");
        
        handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, 
            "Rename the type to " + newTypeName,
            MarkSpan.of(typeDeclaration.getName().getBegin(), typeDeclaration.getName().getEnd())));
        
        typeDeclaration.setName(newTypeName);
        modified = true;
        
        
        // переименуем конструкторы
        for (Node child: typeDeclaration.getChildNodes()) {
          if (child instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructor = (ConstructorDeclaration)child;
            constructor.setName(newTypeName);
            modified = true;
          }
        }
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
