package com.technology.jep.jepria.client.widget.field.tree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.HasData;

public class TreeNodeInfo<V> {
  
  private V parent;
  private HasData<V> display;
  private List<V> data;
  private List<V> children;
  private Set<V> selectedChildren = new HashSet<V>();
  private TreeNode node;
  private boolean isFromCache;
  private boolean isDestroyed = false;
  
  public TreeNodeInfo(HasData<V> display, List<V> data, List<V> children, V parent) {
    this.display = display;
    this.data = data;
    this.children = children;
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
  public List<V> getChildren() {
    return children;
  }
  public void setChildren(List<V> children) {
    this.children = children;
    clearSelectedChildren();
  }
  public Set<V> getSelectedChildren() {
    return selectedChildren;
  }
  public void clearSelectedChildren(){
    selectedChildren.clear();
  }
  public void addSelectedChild(V item) {
    selectedChildren.add(item);
  }
  public void removeSelectedChild(V item) {
    selectedChildren.remove(item);
  }
  public boolean isDestroyed() {
    return isDestroyed;
  }
  public void setDestroyed(boolean isDestroyed) {
    this.isDestroyed = isDestroyed;
  }
}
