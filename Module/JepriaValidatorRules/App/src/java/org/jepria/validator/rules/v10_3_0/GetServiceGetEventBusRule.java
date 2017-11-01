package org.jepria.validator.rules.v10_3_0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
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
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;

/**
 * Удаляет инициализацию dataService, mainService и eventBus в конструкторе фабрик
 * Преобразует методы getService в createService, getMainService в createMainService, getEventBus в createEventBus в клиентских фабриках
 */
public class GetServiceGetEventBusRule extends ValidatorRule {
  
  @Override
  public Transformation forJavaResource(JavaResource resource) {
    
    if (resource.getName().contains("ClientFactory")) {

      final CompilationUnit unit = resource.asCompilationUnit();
      boolean modified = false;
      
      for (TypeDeclaration<?> typeDeclaration: unit.getTypes()) {
        
        boolean isPlainClientFactoryImpl = Util.isPlainClientFactoryImpl(typeDeclaration);
        boolean isMainClientFactoryImpl = Util.isMainClientFactoryImpl(typeDeclaration);
        
        if (isPlainClientFactoryImpl || isMainClientFactoryImpl) {
          modified |= getEventBusToCreateEventBus(typeDeclaration);
          modified |= initEventBusToCreateEventBus(typeDeclaration);
        }
        if (isPlainClientFactoryImpl) {
          modified |= getServieToCreateService(typeDeclaration);
          modified |= initDataServiceToCreateService(typeDeclaration);
        }
        if (isMainClientFactoryImpl) {
          modified |= getMainServieToCreateMainService(typeDeclaration);
          modified |= initMainServiceToCreateMainService(typeDeclaration);
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
  
  /**
   * @param typeDeclaration
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean getServieToCreateService(TypeDeclaration<?> typeDeclaration) {
    boolean modified = false;
    
    // преобразуем метод getService в createService
    MethodDeclaration method = Util.getMethodBySignature(typeDeclaration, "getService()");
    if (method != null) {
      
      boolean checkingOk = false;
      
      if (method.getBody().isPresent()) {
        BlockStmt body = method.getBody().get();
        
        /*
         * Проверяем что тело метода -- следующего вида:
         * if(dataService == null) {
         *   dataService = ...
         * }
         * return dataService;
         */
        List<Node> nodes = body.getChildNodes();
        if (nodes != null && nodes.size() == 2 && nodes.get(0) instanceof IfStmt && nodes.get(1) instanceof ReturnStmt) {
          IfStmt ifStmt = (IfStmt)nodes.get(0);
          ReturnStmt returnStmt = (ReturnStmt)nodes.get(1);

          if ("dataService == null".equals(ifStmt.getCondition().toString())) {
            Statement statement = ifStmt.getThenStmt();
            if (statement != null && 
                statement.getChildNodes() != null && 
                statement.getChildNodes().size() == 1) {
              
              final String assignExpr = statement.getChildNodes().get(0).toString();
              if (assignExpr.startsWith("dataService = ")) {
                
                if (returnStmt.getExpression().isPresent() && 
                    "dataService".equals(returnStmt.getExpression().get().toString())) {
                  
                  checkingOk = true;
                  
                  handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use createService() instead of getService()",
                      MarkSpan.of(method.getName().getBegin(), method.getName().getEnd())));

                  // модификация кода:
                  
                  // нет ли метода createService уже в этом классе?
                  boolean createMethodExists = Util.getMethodBySignature(typeDeclaration, "createService()") != null;
                  
                  String returnType = method.getType().asString();
                  
                  if (!createMethodExists) {
                    MethodDeclaration newMethod = typeDeclaration.addMethod("createService", Modifier.PUBLIC);
                    newMethod.setType(returnType);
                    newMethod.addMarkerAnnotation("Override");
                    
                    BlockStmt newBody = new BlockStmt();
                    newBody.addStatement("return " + assignExpr.substring("dataService = ".length()));
                    newMethod.setBody(newBody);
                    
                    typeDeclaration.remove(method);
                    
                    modified = true;
                  }
                }
              }
            }
          }
        }
      }
      
      if (!checkingOk) {
        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, "It may be possible to use createService() instead of getService()",
            MarkSpan.of(method.getName().getBegin(), method.getName().getEnd()))); 
      }
    }
    
    return modified;
    
  }
    
  /**
   * @param typeDeclaration
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean getMainServieToCreateMainService(TypeDeclaration<?> typeDeclaration) {  
    boolean modified = false;
    
    // преобразуем метод getMainService в createMainService
    MethodDeclaration method = Util.getMethodBySignature(typeDeclaration, "getMainService()");
    if (method != null) {
      
      boolean checkingOk = false;
      
      if (method.getBody().isPresent()) {
        BlockStmt body = method.getBody().get();
        
        /*
         * Проверяем что тело метода -- следующего вида:
         * if(mainService == null) {
         *   mainService = ...
         * }
         * return mainService;
         */
        List<Node> nodes = body.getChildNodes();
        if (nodes != null && nodes.size() == 2 && nodes.get(0) instanceof IfStmt && nodes.get(1) instanceof ReturnStmt) {
          IfStmt ifStmt = (IfStmt)nodes.get(0);
          ReturnStmt returnStmt = (ReturnStmt)nodes.get(1);

          if ("mainService == null".equals(ifStmt.getCondition().toString())) {
            Statement statement = ifStmt.getThenStmt();
            if (statement != null && 
                statement.getChildNodes() != null && 
                statement.getChildNodes().size() == 1) {
              
              final String assignExpr = statement.getChildNodes().get(0).toString();
              if (assignExpr.startsWith("mainService = ")) {
                
                if (returnStmt.getExpression().isPresent() && 
                    "mainService".equals(returnStmt.getExpression().get().toString())) {
                  
                  checkingOk = true;
                  
                  handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use createMainService() instead of getMainService()",
                      MarkSpan.of(method.getName().getBegin(), method.getName().getEnd())));

                  // модификация кода:
                  
                  // нет ли метода createMainService уже в этом классе?
                  boolean createMethodExists = Util.getMethodBySignature(typeDeclaration, "createMainService()") != null;
                  
                  String returnType = method.getType().asString();
                  
                  if (!createMethodExists) {
                    MethodDeclaration newMethod = typeDeclaration.addMethod("createMainService", Modifier.PUBLIC);
                    newMethod.setType(returnType);
                    newMethod.addMarkerAnnotation("Override");
                    
                    BlockStmt newBody = new BlockStmt();
                    newBody.addStatement("return " + assignExpr.substring("mainService = ".length()));
                    newMethod.setBody(newBody);
                    
                    typeDeclaration.remove(method);
                    
                    modified = true;
                  }
                }
              }
            }
          }
        }
      }
      
      if (!checkingOk) {
        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, "It may be possible to use createMainService() instead of getMainService()",
            MarkSpan.of(method.getName().getBegin(), method.getName().getEnd()))); 
      }
    }
    
    return modified;
  }
    
    
  /**
   * @param typeDeclaration
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean getEventBusToCreateEventBus(TypeDeclaration<?> typeDeclaration) {  
    boolean modified = false;  
    
    // преобразуем метод getEventBus в createEventBus
    MethodDeclaration method = Util.getMethodBySignature(typeDeclaration, "getEventBus()");
    if (method != null) {
      
      boolean checkingOk = false;
      
      if (method.getBody().isPresent()) {
        BlockStmt body = method.getBody().get();
        
        /*
         * Проверяем что тело метода -- следующего вида:
         * if(eventBus == null) {
         *   eventBus = ...
         * }
         * return eventBus;
         */
        List<Node> nodes = body.getChildNodes();
        if (nodes != null && nodes.size() == 2 && nodes.get(0) instanceof IfStmt && nodes.get(1) instanceof ReturnStmt) {
          IfStmt ifStmt = (IfStmt)nodes.get(0);
          ReturnStmt returnStmt = (ReturnStmt)nodes.get(1);

          if ("eventBus == null".equals(ifStmt.getCondition().toString())) {
            Statement statement = ifStmt.getThenStmt();
            if (statement != null && 
                statement.getChildNodes() != null && 
                statement.getChildNodes().size() == 1) {
              
              final String assignExpr = statement.getChildNodes().get(0).toString();
              if (assignExpr.startsWith("eventBus = ")) {
                
                if (returnStmt.getExpression().isPresent() && 
                    "eventBus".equals(returnStmt.getExpression().get().toString())) {
                  
                  checkingOk = true;
                  
                  handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use createEventBus() instead of getEventBus()",
                      MarkSpan.of(method.getName().getBegin(), method.getName().getEnd())));

                  // модификация кода:
                  
                  // нет ли метода createMainService уже в этом классе?
                  boolean createMethodExists = Util.getMethodBySignature(typeDeclaration, "createEventBus()") != null;
                  
                  String returnType = method.getType().asString();
                  
                  if (!createMethodExists) {
                    MethodDeclaration newMethod = typeDeclaration.addMethod("createEventBus", Modifier.PUBLIC);
                    newMethod.setType(returnType);
                    newMethod.addMarkerAnnotation("Override");
                    
                    BlockStmt newBody = new BlockStmt();
                    newBody.addStatement("return " + assignExpr.substring("eventBus = ".length()));
                    newMethod.setBody(newBody);
                    
                    typeDeclaration.remove(method);
                    
                    modified = true;
                  }
                }
              }
            }
          }
        }
      }
      
      if (!checkingOk) {
        handleMessage(new Message(MessageLevel.MANUAL_TRANSFORM, "It may be possible to use createEventBus() instead of getEventBus()",
            MarkSpan.of(method.getName().getBegin(), method.getName().getEnd()))); 
      }
    }
    
    return modified;
  }
  
  
  
  /**
   * Убирает инициализацию dataService из констуктора, перенося её в createService
   * 
   * @param typeDeclaration
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean initDataServiceToCreateService(TypeDeclaration<?> typeDeclaration) {
    
    boolean modified = false;
    
    List<ConstructorDeclaration> constructors = new ArrayList<>();
    for (BodyDeclaration<?> bodyDeclaration: typeDeclaration.getMembers()) {
      if (bodyDeclaration instanceof ConstructorDeclaration) {
        constructors.add((ConstructorDeclaration)bodyDeclaration);
      }
    }
    
    for (ConstructorDeclaration constructor: constructors) {
      BlockStmt blockStmt = constructor.getBody();
      
      List<Node> nodesToRemove = new ArrayList<>();
      
      for (Node node: blockStmt.getChildNodes()) {
        
        if (node instanceof ExpressionStmt) {
          final String assignExpr = node.toString();
          
          if (assignExpr.startsWith("dataService = ")) {
            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use createService() to create dataService instead of creating it in constructor",
                MarkSpan.of(node.getBegin(), node.getEnd())));
            
            // модификация кода:
            
            MethodDeclaration createMethod = Util.getMethodBySignature(typeDeclaration, "createService()");
            if (createMethod == null) {
              // Если поле определяется только в конструкторе (нет перегруженного метода), создаём этот перегруженный метод
              
              String serviceTypename = "S"; //TODO вычислить тип создаваемого метода! (из объявления фабрики и её типовых параметров)
              
              createMethod = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), JavaParser.parseClassOrInterfaceType(serviceTypename), "createService");
              createMethod.addMarkerAnnotation("Override");
              typeDeclaration.addMember(createMethod);
            }
            // Инициализация в конструкторе доминирует и над getService, и над createService,
            // поэтому заменяем тело createService на новое (даже если метод createService был переопределен)
            BlockStmt newBody = new BlockStmt();
            newBody.addStatement("return " + assignExpr.substring("dataService = ".length()));
            createMethod.setBody(newBody);
            
            nodesToRemove.add(node);
            
            modified = true;
          }
        }
      }
    }
    
    return modified;
  }
  
  /**
   * Убирает инициализацию mainService из констуктора, перенося её в createMainService
   * 
   * @param typeDeclaration
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean initMainServiceToCreateMainService(TypeDeclaration<?> typeDeclaration) {
    
    boolean modified = false;
    
    List<ConstructorDeclaration> constructors = new ArrayList<>();
    for (BodyDeclaration<?> bodyDeclaration: typeDeclaration.getMembers()) {
      if (bodyDeclaration instanceof ConstructorDeclaration) {
        constructors.add((ConstructorDeclaration)bodyDeclaration);
      }
    }
    
    for (ConstructorDeclaration constructor: constructors) {
      BlockStmt blockStmt = constructor.getBody();
      
      List<Node> nodesToRemove = new ArrayList<>();
      
      for (Node node: blockStmt.getChildNodes()) {
        
        if (node instanceof ExpressionStmt) {
          final String assignExpr = node.toString();
          
          if (assignExpr.startsWith("mainService = ")) {
            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use createMainService() to create mainService instead of creating it in constructor",
                MarkSpan.of(node.getBegin(), node.getEnd())));
            
            // модификация кода:
            
            MethodDeclaration createMethod = Util.getMethodBySignature(typeDeclaration, "createMainService()");
            if (createMethod == null) {
              // Если поле определяется только в конструкторе (нет перегруженного метода), создаём этот перегруженный метод
              
              String serviceTypename = "S"; //TODO вычислить тип создаваемого метода! (из объявления фабрики и её типовых параметров)
              
              createMethod = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), JavaParser.parseClassOrInterfaceType(serviceTypename), "createMainService");
              createMethod.addMarkerAnnotation("Override");
              typeDeclaration.addMember(createMethod);
            }
            // Инициализация в конструкторе доминирует и над getMainService, и над createMainService,
            // поэтому заменяем тело createMainService на новое (даже если метод createMainService был переопределен)
            BlockStmt newBody = new BlockStmt();
            newBody.addStatement("return " + assignExpr.substring("mainService = ".length()));
            createMethod.setBody(newBody);
            
            nodesToRemove.add(node);
            
            modified = true;
          }
        }
      }
    }
    
    return modified;
  }
  
  /**
   * Убирает инициализацию eventBus из констуктора, перенося её в createEventBus
   * 
   * @param typeDeclaration
   * @return true если исходный код был изменен (и нужна трансформация), false иначе
   */
  private boolean initEventBusToCreateEventBus(TypeDeclaration<?> typeDeclaration) {
    
    boolean modified = false;
    
    List<ConstructorDeclaration> constructors = new ArrayList<>();
    for (BodyDeclaration<?> bodyDeclaration: typeDeclaration.getMembers()) {
      if (bodyDeclaration instanceof ConstructorDeclaration) {
        constructors.add((ConstructorDeclaration)bodyDeclaration);
      }
    }
    
    for (ConstructorDeclaration constructor: constructors) {
      BlockStmt blockStmt = constructor.getBody();
      
      List<Node> nodesToRemove = new ArrayList<>();
      
      for (Node node: blockStmt.getChildNodes()) {
        
        if (node instanceof ExpressionStmt) {
          final String assignExpr = node.toString();
          
          if (assignExpr.startsWith("eventBus = ")) {
            handleMessage(new Message(MessageLevel.AUTO_TRANSFORM, "Use createEventBus() to create eventBus instead of creating it in constructor",
                MarkSpan.of(node.getBegin(), node.getEnd())));
            
            // модификация кода:
            
            MethodDeclaration createMethod = Util.getMethodBySignature(typeDeclaration, "createEventBus()");
            if (createMethod == null) {
              // Если поле определяется только в конструкторе (нет перегруженного метода), создаём этот перегруженный метод
              
              String eventBusTypename = "E"; //TODO вычислить тип создаваемого метода! (из объявления фабрики и её типовых параметров)
              
              createMethod = new MethodDeclaration(EnumSet.of(Modifier.PUBLIC), JavaParser.parseClassOrInterfaceType(eventBusTypename), "createEventBus");
              createMethod.addMarkerAnnotation("Override");
              typeDeclaration.addMember(createMethod);
            }
            // Инициализация в конструкторе доминирует и над getEventBus, и над createEventBus,
            // поэтому заменяем тело createEventBus на новое (даже если метод createEventBus был переопределен)
            BlockStmt newBody = new BlockStmt();
            newBody.addStatement("return " + assignExpr.substring("eventBus = ".length()));
            createMethod.setBody(newBody);
            
            nodesToRemove.add(node);
            
            modified = true;
          }
        }
      }
      
      for (Node node: nodesToRemove) {
        node.remove();
      }
    }
      
    return modified;
  }
}
