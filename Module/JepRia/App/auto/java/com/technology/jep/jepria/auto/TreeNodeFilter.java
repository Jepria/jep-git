package com.technology.jep.jepria.auto;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.*;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Вспомогательный интерфейс (паттерн команда), используемый при обходе дерева.
 * Методы инфтерфейса применяются ко всем узлам дерева во время обхода.
 * @author RomanovAS
 */
public interface TreeNodeFilter {
  /**
   * Метод, проверяющий, нужно ли данный узел дерева помещать в результирующий список.
   * Например, если нужно получить только развернутые узлы в дереве:
   * <br><br><code><pre>
   * return "true".equals(treeNode.getAttribute("aria-expanded"));}
   * <pre/></code>
   * @param treeNode проверяемый узел дерева
   */
  boolean putToResult(WebElement treeNode);
  
  /**
   * Метод, проверяющий, нужно ли рекурсивно обходить детей данного узла, 
   * либо остановить обход вглубь на данном узле.
   * Например, если нужно получить только узлы 1-4 уровней в дереве:
   * <br><br><code><pre>
   * return "1|2|3".contains(treeNode.getAttribute("aria-level"));
   * <pre/></code><br>
   * @param treeNode проверяемый узел
   */
  boolean traverseDescendants(WebElement treeNode);
  
  /**
   * Реализация фильтра для получения всех видимых (развернутых) узлов. 
   */
  public static final TreeNodeFilter FILTER_VISIBLE = new TreeNodeFilter() {
    @Override
    public boolean putToResult(WebElement treeNode) {
      return true;
    }
    @Override
    public boolean traverseDescendants(WebElement treeNode) {
      return true;
    }
  };
  
  /**
   * Реализация фильтра для получения всех отмеченных узлов (листьев и папок). 
   */
  public static final TreeNodeFilter FILTER_CHECKED = new TreeNodeFilter() {
    @Override
    public boolean putToResult(WebElement treeNode) {
      try {
        WebElement span = treeNode.findElement(By.xpath(
            String.format(".//span[contains(@id, '%s')]", JEP_TREENODE_INFIX)));
        
        return JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED.equals(span.getAttribute(JEP_TREENODE_CHECKEDSTATE_HTML_ATTR));
        
      } catch (NoSuchElementException e) {
        return false;
      }
    }
    @Override
    public boolean traverseDescendants(WebElement treeNode) {
      return true;
    }
  };
  
  /**
   * Реализация фильтра для получения только листовых отмеченных узлов. 
   */
  public static final TreeNodeFilter FILTER_CHECKED_LEAVES = new TreeNodeFilter() {
    @Override
    public boolean putToResult(WebElement treeNode) {
      try {
        WebElement span = treeNode.findElement(By.xpath(
          String.format(".//span[contains(@id, '%s')]", JEP_TREENODE_INFIX)));
      
        return "true".equals(span.getAttribute(JEP_TREENODE_ISLEAF_HTML_ATTR)) &&
            JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED.equals(span.getAttribute(JEP_TREENODE_CHECKEDSTATE_HTML_ATTR));
      
      } catch (NoSuchElementException e) {
        return false;
      }
    }
    @Override
    public boolean traverseDescendants(WebElement treeNode) {
      return true;
    }
  };
  
  /**
   * Реализация фильтра для получения всех папок с отметкой 'partial'. 
   */
  public static final TreeNodeFilter FILTER_PARTIAL = new TreeNodeFilter() {
    @Override
    public boolean putToResult(WebElement treeNode) {
      try {
        WebElement span = treeNode.findElement(By.xpath(
            String.format(".//span[contains(@id, '%s')]", JEP_TREENODE_INFIX)));
        
        return JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL.equals(span.getAttribute(JEP_TREENODE_CHECKEDSTATE_HTML_ATTR));
      
      } catch (NoSuchElementException e) {
        return false;
      }
    }
    @Override
    public boolean traverseDescendants(WebElement treeNode) {
      return true;
    }
  };
}