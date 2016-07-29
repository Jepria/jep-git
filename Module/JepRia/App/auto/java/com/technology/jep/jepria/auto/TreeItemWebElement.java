package com.technology.jep.jepria.auto;

import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKED;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_INFIX;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.exceptions.AutomationException;

/**
 * Декоратор для тех экземпляров класса {@link org.openqa.selenium.WebElement}, которые являются
 * узлами GWT-дерева (иденификация по атрибуту <code>role='treeitem'</code>), либо корнем дерева
 * (корнем может быть любой элемент, содержащий внутри себя узлы GWT-дерева).
 * 
 * Декоратор предоставляет упрощенное получение свойств (атрибутов) узла дерева как из самого <code>treeitem</code>-элемента
 * (например, <code>aria-expanded</code> &mdash; признак развернутости узла, <code>aria-level</code> &mdash; уровень вложенности),
 * так и из содержащегося в нем JEP-попрожденного элемента <code>span</code>, описывающего структуру ячейки дерева
 * (например, имя узла или его checked-состояние).
 * 
 * Класс является одновременно фабрикой, производящей объекты по результатам парсинга веб-элементов. 
 * @author RomanovAS
 */
public final class TreeItemWebElement {
  /**
   * Этот енум определяется JEP(а не GWT)-свойством отмеченности узла дерева, то есть атрибутом
   * <code>jep-treenode-checkedstate</code> элемента <code>span</code>, а не атрибутом <code>aria-selected</code>
   * элемента <code>div[role='treeitem']</code>. Элементы енума тождественны автоматизационным константам-значениям
   * атрибута <code>jep-treenode-checkedstate</code>, однако не стоит их смешивать, равно как и использовать этот енум на клиенте
   * (см. {@link com.technology.jep.jepria.client.widget.field.tree.TreeField.TreeModel#getNodeInfo getNodeInfo::new CompositeCell<V>#render}).  
   * @author RomanovAS
   */
  public enum CheckedState {CHECKED, UNCHECKED, PARTIAL, UNCHECKABLE};
  
  /**
   * Веб-элемент, на основе которого фабрикой был создан данный экземпляр.
   * Используется для получения любых свойств данного узла (даже не предусмотренных интерфейсом декоратора).
   */
  private final WebElement webElement;
  
  /**
   * Jep-имя узла дерева (имя <code>JepOption</code>, лежащей в основе узла).
   */
  private final String itemName;
  
  /**
   * Признак корня дерева (в любом дереве может быть только один корень, не являющийся GWT-узлом,
   * то есть не имеющий <code>role='treeitem'</code>, на уровне вложенности <code>0</code>).
   */
  private final boolean isRootOfTree;
  
  /**
   * Признаки разворачиваемости и развернутости узла (<code>aria-expanded == null</code>, <code>aria-expanded == true</code>).
   */
  private final boolean isLeaf, isExpanded;
  
  /**
   * Уровень вложенности узла в дереве (<code>aria-level</code>).
   */
  private final int level;
  
  /**
   * Jep-состояние отмеченности узла
   * (<code>checked</code>/<code>unchecked</code>/<code>partial</code>).
   */
  private final CheckedState checkedState;
  
  
  private TreeItemWebElement(WebElement webElement, String itemName, boolean isRootOfTree,
      boolean isLeaf, boolean isExpanded, int level, CheckedState checkedState) {
    this.webElement = webElement;
    this.itemName = itemName;
    this.isRootOfTree = isRootOfTree;
    this.isLeaf = isLeaf;
    this.isExpanded = isExpanded;
    this.level = level;
    this.checkedState = checkedState;
  }

  
  public WebElement asWebElement() {
    return webElement;
  }
  
  public String getItemName() {
    return itemName;
  }
  public boolean isRootOfTree() {
    return isRootOfTree;
  }
  public boolean isLeaf() {
    return isLeaf;
  }
  public boolean isExpanded() {
    return isExpanded;
  }
  public int getLevel() {
    return level;
  }
  public CheckedState getCheckedState() {
    return checkedState;
  }

  
  /**
   * Создает экземпляр класса из произвольного Веб-элемента, используя парсинг внутренней структуры.
   */
  public static final TreeItemWebElement fromWebElement(WebElement webElement) {
    
    if ("treeitem".equals(webElement.getAttribute("role"))) {
      // parse the inner span
      try {
        WebElement span = webElement.findElement(By.xpath(
            String.format(".//span[contains(@id, '%s')]", JEP_TREENODE_INFIX))); 
        
        String id = span.getAttribute("id");
        final String itemName = id.substring(id.indexOf(JEP_TREENODE_INFIX) + JEP_TREENODE_INFIX.length()).replaceAll("/", "\\\\/");
        
        final boolean isLeaf = webElement.getAttribute("aria-expanded") == null;
        final boolean isExpanded = "true".equals(webElement.getAttribute("aria-expanded"));
        
        int level_;
        try {
          level_ = Integer.parseInt(webElement.getAttribute("aria-level"));
        } catch (Exception e) {
          level_ = 0;
        }
        final int level = level_;
        
        final CheckedState checkedState;
        String jepCheckedStateVal = span.getAttribute(JEP_TREENODE_CHECKEDSTATE_HTML_ATTR);
        if (JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED.equals(jepCheckedStateVal)) {
          checkedState = CheckedState.CHECKED;
        } else if (JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKED.equals(jepCheckedStateVal)) {
          checkedState = CheckedState.UNCHECKED;
        } else if (JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL.equals(jepCheckedStateVal)) {
          checkedState = CheckedState.PARTIAL;
        } else {
          checkedState = CheckedState.UNCHECKABLE;
        }
        
        return new TreeItemWebElement(webElement, itemName, false, isLeaf, isExpanded, level, checkedState);
      } catch (NoSuchElementException e) {
        
        throw new AutomationException("The tree node does not contain inside a JepRia-provided span with id matching '*" + JEP_TREENODE_INFIX + "*'");
      }
    } else {
      // create the root of tree
      return new TreeItemWebElement(webElement, null, true, false, true, 0, CheckedState.UNCHECKABLE);
    }
  }
  
  @Override
  public String toString() {
    return getClass().getSimpleName() + ":["
        + "name:" + itemName + "; "
        + "rootOfTree? " + isRootOfTree + "; "
        + "leaf? " + isLeaf + "; "
        + "expanded? " + isExpanded + "; "
        + "level: " + level + "; "
        + "checkedState: " + checkedState + "; "
        + "source WebElement:[" + webElement.toString() + "]]";
  }
}
