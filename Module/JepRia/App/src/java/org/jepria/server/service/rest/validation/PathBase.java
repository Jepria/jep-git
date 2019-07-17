package org.jepria.server.service.rest.validation;

import java.util.Arrays;
import java.util.Iterator;

import javax.validation.ElementKind;
import javax.validation.Path;

public class PathBase implements Path {
  
  protected final Iterable<Path.Node> nodes;
  
  public PathBase(Iterable<Node> nodes) {
    this.nodes = nodes;
  }
  
  public PathBase(String node) {
    this(Arrays.asList(new NodeBase(node)));
  }

  @Override
  public Iterator<Node> iterator() {
    return nodes.iterator();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator<Node> iterator = iterator();
    boolean first = true;
    while (iterator.hasNext()) {
      if (!first) {
        sb.append('/');
      } else {
        first = false;
      }
      sb.append(iterator.next().toString());
    }
    return sb.toString();
  }
  
  public static class NodeBase implements Path.Node {

    protected final Integer index;
    protected final String name;
    
    public NodeBase(Integer index, String name) {
      this.index = index;
      this.name = name;
    }
    
    public NodeBase(String name) {
      this(null, name);
    }

    @Override
    public <T extends Node> T as(Class<T> arg0) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public Integer getIndex() {
      return index;
    }

    @Override
    public Object getKey() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public ElementKind getKind() {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public boolean isInIterable() {
      // TODO Auto-generated method stub
      return false;
    }
    
    @Override
    public String toString() {
      return getName();
    }
  }
}
