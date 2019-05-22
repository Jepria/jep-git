package org.jepria.server.service.rest.swagger;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public interface TypeDeployer {
  /**
   * Deploys the type
   * @param type null-safe
   * @param target map to deploy the type into, if {@code null}, does nothing
   */
  void deploy(Type type, Map<String, Object> target);
  
  /**
   * 
   * @return swagger {@code definitions} block data for the deployed types
   */
  Map<String, Object> getDefinitions();
  
  /**
   * Non-recursive implementation
   */
  public static class NonRecursive implements TypeDeployer {
  
    /**
     * Maps Types to refs in swagger {@code definitions} block
     */
    private final Map<Type, String> externalizedTypes = new HashMap<>();
    
    /**
     * Swagger {@code definitions} block data
     */
    private final Map<String, Object> definitions = new HashMap<>();
  
    @Override
    public Map<String, Object> getDefinitions() {
      return definitions;
    }
    
    /**
     * Deploys the type
     * @param type null-safe
     * @param target map to deploy the type into, if {@code null}, does nothing
     */
    @Override
    public void deploy(Type type, Map<String, Object> target) {
      if (target != null) {
        Stack<TypeTreeNode> nodes = new Stack<>();
        
        // push to deploy the node
        TypeTreeNode node = new TypeTreeNode();
        node.type = type;
        node.target = target;
        nodes.push(node);
        
        while (nodes.size() > 0) {
          deployTypeTree(nodes);
        }
      }
    }
    
    protected class TypeTreeNode {
      public Type type;
      public Map<String, Object> target;
    }
    
    /**
     * Deploys the type tree non-recursively
     * @param nodes stack of tree nodes to deploy (to avoid recursive invocations)
     */
    protected void deployTypeTree(Stack<TypeTreeNode> nodes) {
      if (nodes != null && nodes.size() > 0) {
        TypeTreeNode node = nodes.pop(); 
        Type type = node.type;
        Map<String, Object> target = node.target;
        
        if (target != null) {
          if (type == null) {
            // treat null as an empty object
            
            // push to deploy the next node (equivalent for a recursive invocation)
            TypeTreeNode nextNode = new TypeTreeNode();
            nextNode.type = Type.object(null);
            nextNode.target = target;
            nodes.push(nextNode);
            
          } else if (type instanceof Type.Primitive) {
            target.put("type", type.literal);
            
          } else if (type instanceof Type.Array) {
            Type.Array typeArray = (Type.Array) type;
            target.put("type", typeArray.literal);
            Map<String, Object> itemsMap = new HashMap<>();
            target.put("items", itemsMap);
            
            // push to deploy the next node (equivalent for a recursive invocation)
            TypeTreeNode nextNode = new TypeTreeNode();
            nextNode.type = typeArray.items;
            nextNode.target = itemsMap;
            nodes.push(nextNode);
            
          } else if (type instanceof Type.Object) {
            Type.Object typeObject = (Type.Object) type;
            
            if (typeObject.properties == null || typeObject.properties.size() == 0) {
              // empty object
              target.put("type", typeObject.literal);
            } else {
              // externalize complex type
              String typeRef = externalizeType(typeObject, nodes);
              
              target.put("$ref", typeRef);
            }
          }
        }
      }
    }
    
    /**
     * Externalizes an object type into swagger {@code definitions} block and returns its {@code $ref}
     * @param type
     * @param nodes
     * @return
     */
    protected String externalizeType(Type.Object type, Stack<TypeTreeNode> nodes) {
      String definitionRef = externalizedTypes.get(type);
      if (definitionRef == null) {
        // the type has not been externalized yet
        
        String newExternalTypeRef;
        {// create new definition
          String typeRefName = "Type" + (definitions.size() + 1);
          newExternalTypeRef = "#/definitions/" + typeRefName;
          externalizedTypes.put(type, newExternalTypeRef);// put-before
          
          Map<String, Object> definition = new HashMap<>();
          definitions.put(typeRefName, definition); // put-before
          {
            definition.put("type", type.literal);
            
            Map<String, Type> properties = type.properties;
            if (properties != null) {
              Map<String, Object> propertiesMap = new HashMap<>();
              {
                for (String propertyName: properties.keySet()) {
                  Type propertyType = properties.get(propertyName);
                  Map<String, Object> propertyTypeMap = new HashMap<>();
                  propertiesMap.put(propertyName, propertyTypeMap); // put-before
                  
                  // push to deploy the next node (equivalent for a recursive invocation)
                  TypeTreeNode nextNode = new TypeTreeNode();
                  nextNode.type = propertyType;
                  nextNode.target = propertyTypeMap;
                  nodes.push(nextNode);
                }
              }
              definition.put("properties", propertiesMap);
            }
          }
          
        }
        definitionRef = newExternalTypeRef; 
      }
      return definitionRef;
    }
  }
  
  /**
   * Recursive implementation
   */
  public static class Recursive implements TypeDeployer {
  
    /**
     * Maps Types to refs in swagger {@code definitions} block
     */
    private final Map<Type, String> externalizedTypes = new HashMap<>();
    
    /**
     * Swagger {@code definitions} block data
     */
    private final Map<String, Object> definitions = new HashMap<>();
  
    @Override
    public Map<String, Object> getDefinitions() {
      return definitions;
    }
    
    /**
     * Deploys the type
     * @param type null-safe
     * @param target map to deploy the type into, if {@code null}, does nothing
     */
    @Override
    public void deploy(Type type, Map<String, Object> target) {
      if (target != null) {
        if (type == null) {
          // treat null as an empty object
          
          deploy(Type.object(null), target);
          
        } else if (type instanceof Type.Primitive) {
          target.put("type", type.literal);
          
        } else if (type instanceof Type.Array) {
          Type.Array typeArray = (Type.Array) type;
          target.put("type", typeArray.literal);
          Map<String, Object> itemsMap = new HashMap<>();
          target.put("items", itemsMap);
          
          deploy(typeArray.items, itemsMap);
          
        } else if (type instanceof Type.Object) {
          Type.Object typeObject = (Type.Object) type;
          
          if (typeObject.properties == null || typeObject.properties.size() == 0) {
            // empty object
            target.put("type", typeObject.literal);
          } else {
            // externalize complex type
            String typeRef = externalizeType(typeObject);
            
            target.put("$ref", typeRef);
          }
        }
      }
    }
    
    /**
     * Externalizes an object type into swagger {@code definitions} block and returns its {@code $ref}
     * @param type
     * @param nodes
     * @return
     */
    protected String externalizeType(Type.Object type) {
      String definitionRef = externalizedTypes.get(type);
      if (definitionRef == null) {
        // the type has not been externalized yet
        
        String newExternalTypeRef;
        {// create new definition
          String typeRefName = "Type" + (definitions.size() + 1);
          newExternalTypeRef = "#/definitions/" + typeRefName;
          externalizedTypes.put(type, newExternalTypeRef);// put-before
          
          Map<String, Object> definition = new HashMap<>();
          definitions.put(typeRefName, definition); // put-before
          {
            definition.put("type", type.literal);
            
            Map<String, Type> properties = type.properties;
            if (properties != null) {
              Map<String, Object> propertiesMap = new HashMap<>();
              {
                for (String propertyName: properties.keySet()) {
                  Type propertyType = properties.get(propertyName);
                  Map<String, Object> propertyTypeMap = new HashMap<>();
                  propertiesMap.put(propertyName, propertyTypeMap); // put-before
                  
                  deploy(propertyType, propertyTypeMap);
                }
              }
              definition.put("properties", propertiesMap);
            }
          }
          
        }
        definitionRef = newExternalTypeRef; 
      }
      return definitionRef;
    }
  }
}
