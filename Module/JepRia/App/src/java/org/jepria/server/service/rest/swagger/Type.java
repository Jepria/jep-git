package org.jepria.server.service.rest.swagger;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

// abstract: not to instantiate
public abstract class Type {
  
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
    } else if (getClass() != obj.getClass()) {
      return false;
    } else {
      Type type = (Type) obj;
      return Objects.equals(literal, type.literal);
    }
  }
  
  // Object can be extended
  public static class Object extends Type {
    /**
     * Unmodifiable map
     */
    public final Map<String, Type> properties;
    
    /**
     * @param properties null-safe. The map is unmodifiable after creation 
     */
    public Object(Map<String, Type> properties) {
      super("object");
      // important to make map unmodifiable to avoid adding cyclic references: Type A {b: B}, Type B {a: A}
      this.properties = properties == null ? null : Collections.unmodifiableMap(properties); 
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
      } else if (getClass() != obj.getClass()) {
        return false;
      } else {
        Type.Object type = (Type.Object) obj;
        return Objects.equals(literal, type.literal) && Objects.equals(properties, type.properties);
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
      } else if (getClass() != obj.getClass()) {
        return false;
      } else {
        Type.Array type = (Type.Array) obj;
        return Objects.equals(literal, type.literal) && Objects.equals(items, type.items);
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
  
  public static Type object() {
    return new Type.Object(null);
  }
}
