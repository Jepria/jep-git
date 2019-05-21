package org.jepria.server.service.rest.swagger;

import java.util.Map;
import java.util.Objects;

public class Type {
  
  public final String literal;
  
  // private: creation is limited
  private Type(String literal) {
    this.literal = literal;
  }
  
  // final: Primitive cannot be extended
  public static final class Primitive extends Type {
    // private: creation is limited
    private Primitive(String literal) {
      super(literal);
    }
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(literal);
  }
  
  @Override
  public boolean equals(java.lang.Object obj) {
    if (obj == null) {
      return false;
    } else if (obj == this) {
      return true;
    } else if (!(obj instanceof Type)) {
      return false;
    } else {
      Type objType = (Type) obj;
      return objType.hashCode() == this.hashCode();
    }
  }
  
  // Object can be extended
  public static class Object extends Type {
    public final Map<String, Type> properties;
    
    /**
     * @param properties null-safe
     */
    public Object(Map<String, Type> properties) {
      super("object");
      this.properties = properties; 
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(literal, properties);
    }
    
    @Override
    public boolean equals(java.lang.Object obj) {
      if (obj == null) {
        return false;
      } else if (obj == this) {
        return true;
      } else if (!(obj instanceof Object)) {
        return false;
      } else {
        Object objObject = (Object) obj;
        return objObject.hashCode() == this.hashCode();
      }
    }
  }
  
  // Array can be extended
  public static class Array extends Type {
    public final Type items;
    
    public Array(Type items) {
      super("array");
      this.items = items;
    }
    
    @Override
    public int hashCode() {
      return Objects.hash(literal, items);
    }
    
    @Override
    public boolean equals(java.lang.Object obj) {
      if (obj == null) {
        return false;
      } else if (obj == this) {
        return true;
      } else if (!(obj instanceof Array)) {
        return false;
      } else {
        Array objArray = (Array) obj;
        return objArray.hashCode() == this.hashCode();
      }
    }
  }

  
  public static Type string() {
    return new Type.Primitive("string");
  }
  
  public static Type number() {
    return new Type.Primitive("number");
  }
  
  public static Type integer() {
    return new Type.Primitive("integer");
  }
  
  public static Type booleam() {
    return new Type.Primitive("boolean");
  }
  
  public static Type array(Type items) {
    return new Type.Array(items);
  }
  
  public static Type object(Map<String, Type> properties) {
    return new Type.Object(properties);
  }
}
