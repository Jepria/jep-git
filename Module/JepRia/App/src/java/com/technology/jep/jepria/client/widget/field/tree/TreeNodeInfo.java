package com.technology.jep.jepria.client.widget.field.tree;

import java.util.List;

import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.HasData;

public class TreeNodeInfo<V> {
  
  private V parent;
  private HasData<V> display;
  private List<V> data;
  private TreeNode node;
  private boolean isFromCache;
  
  public TreeNodeInfo(HasData<V> display, List<V> data, V parent) {
    this.display = display;
    this.data = data;
    this.parent = parent;
    this.isFromCache = false;
  }
  public HasData<V> getDisplay() {
    return display;
  }
  public void setDisplay(HasData<V> display) {
    this.display = display;
  }
  public List<V> getData() {
    return data;
  }
  public void setData(List<V> data) {
    this.data = data;
  }
  public V getParent() {
    return parent;
  }
  public void setParent(V parent) {
    this.parent = parent;
  }
  public boolean isFromCache() {
    return isFromCache;
  }
  public void setFromCache(boolean isFromCache) {
    this.isFromCache = isFromCache;
  }
  public TreeNode getNode() {
    return node;
  }
  public void setNode(TreeNode node) {
    this.node = node;
  }
}
