package com.technology.jep.jepria.auto;

import com.technology.jep.jepria.auto.TreeItemWebElement.CheckedState;

/**
 * Вспомогательный интерфейс (паттерн команда), используемый при обходе дерева.
 * Методы инфтерфейса применяются ко всем узлам дерева во время обхода.
 * @author RomanovAS
 */
public interface TreeItemFilter {
  /**
   * Метод, проверяющий, нужно ли данный узел дерева помещать в результирующий список.
   * Например, если нужно получить только отмеченные развернутые папки в дереве:
   * <br><br><code><pre>
   * return treeItem.isExpanded() && treeItem.getCheckedState() == CheckedState.CHECKED;
   * <pre/></code>
   * @param treeItem проверяемый узел дерева
   */
  boolean putToResult(TreeItemWebElement treeItem);
  
  /**
   * Метод, проверяющий, нужно ли рекурсивно обходить детей данного узла, 
   * либо остановить обход вглубь на данном узле.
   * Например, если не нужно обходить папки 3-го и больших уровней вложенности в дереве:
   * <br><br><code><pre>
   * return treeItem.getLevel() < 3;
   * <pre/></code><br>
   * Метод вызывается только для развернутых папок и, если имеется, корневого элемента.
   * @param treeNode проверяемый узел
   */
  boolean traverseDescendants(TreeItemWebElement treeItem);
  
  /**
   * Реализация фильтра для получения всех видимых (развернутых) узлов. 
   */
  public static final TreeItemFilter FILTER_VISIBLE = new TreeItemFilter() {
    @Override
    public boolean putToResult(TreeItemWebElement treeItem) {
      return true;
    }
    @Override
    public boolean traverseDescendants(TreeItemWebElement treeItem) {
      return true;
    }
  };
  
  /**
   * Реализация фильтра для получения всех отмеченных узлов (листьев и папок). 
   */
  public static final TreeItemFilter FILTER_CHECKED = new TreeItemFilter() {
    @Override
    public boolean putToResult(TreeItemWebElement treeItem) {
      return treeItem.getCheckedState() == CheckedState.CHECKED;
    }
    @Override
    public boolean traverseDescendants(TreeItemWebElement treeItem) {
      return true;
    }
  };
  
  /**
   * Реализация фильтра для получения только листовых отмеченных узлов. 
   */
  public static final TreeItemFilter FILTER_CHECKED_LEAVES = new TreeItemFilter() {
    @Override
    public boolean putToResult(TreeItemWebElement treeItem) {
      return treeItem.isLeaf() && treeItem.getCheckedState() == CheckedState.CHECKED;
    }
    @Override
    public boolean traverseDescendants(TreeItemWebElement treeItem) {
      return true;
    }
  };
  
  /**
   * Реализация фильтра для получения всех папок с отметкой 'partial'. 
   */
  public static final TreeItemFilter FILTER_PARTIAL = new TreeItemFilter() {
    @Override
    public boolean putToResult(TreeItemWebElement treeItem) {
      return treeItem.getCheckedState() == CheckedState.PARTIAL;
    }
    @Override
    public boolean traverseDescendants(TreeItemWebElement treeItem) {
      return true;
    }
  };
}