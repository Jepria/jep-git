package com.technology.jep.jepria.client.widget.field.tree;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.*;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_ISLEAF_HTML_ATTR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.EventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.container.ElementSimplePanel;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.CheckChangeHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.HasCheckChangeHandlers;
import com.technology.jep.jepria.client.widget.field.tree.images.TreeFieldResources;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.log.JepLoggerImpl;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс, представляющий реализацию поля выбора в виде древовидной иерархии.
 */
public class TreeField<V extends JepOption> extends ScrollPanel implements HasCheckChangeHandlers<V> {
  
  /**
   * Check cascade enum.
   */
  public enum CheckCascade {
    /**
     * Checks cascade to all child nodes.
     */
    CHILDREN,
    /**
     * Checks to not cascade.
     */
    NONE,
    /**
     * Checks cascade to all parent nodes.
     */
    PARENTS;
  }

  /**
   * Check nodes enum.
   */
  public enum CheckNodes {
    /**
     * Check boxes for both leafs and parent nodes.
     */
    BOTH,
    /**
     * Check boxes for only leaf nodes.
     */
    LEAF,
    /**
     * Check boxes for only parent nodes.
     */
    PARENT;
  }

  /**
   * Виджет, позволяющий представить иерархию выбора в виде дерева.
   */
  private CellTree tree;
  
  /**
   * Загрузчик данных для данного компонента.
   */
  private DataLoader<V> loader;
  
  /**
   * Список частично выбранных узлов, позволяющий снизить 
   * нагрузку на клиенте за счет отсутствия необходимости получения 
   * списка детей родительского узла 
   */
  private List<V> partialSelectedNodes = new ArrayList<V>();
  
  /**
   * Раскрываемый узел
   */
  private V openingNode;
  
  /**
   * Наименование селектора (класса стилей) для данного компонента
   */
  private static final String JEP_RIA_TREE_FIELD_STYLE = "jepRia-TreeField-Input";
  
  /**
   * Карта соответствия узла дерева с его логическим представлением.
   */
  private Map<V, TreeNodeInfo<V>> nodeMapOfDisplay = new HashMap<V, TreeNodeInfo<V>>();
  
  /* Resources: texts and images */
  private TreeFieldMessages messages = new TreeFieldMessages();
  private TreeFieldResources images = GWT.create(TreeFieldResources.class);
  
  /**
   * Признак возможного выбора в древовидном справочнике (по умолчанию, выбор как родительских, так и листовых узлов). 
   */
  private CheckNodes checkNodes = CheckNodes.BOTH;
  
  /**
   * Стиль выбора узла и зависимых от него узлов (по умолчанию, выбор только самого себя).
   */
  private CheckCascade checkStyle = CheckCascade.NONE;
  
  /**
   * Модель множественного выбора узлов дерева.
   */
  private SetSelectionModel<V> selectionModel;
  
  /**
   * Возможность выбора узлов дерева (по умолчанию, допускается выделение узлов). 
   */
  private boolean checkable = true;
  
  /**
   * ID объемлющего Jep-поля как Web-элемента.
   */
  private final String fieldIdAsWebEl;
  
  /**
   * Создает экземпляры данного класса.
   */
  public TreeField(String fieldIdAsWebEl){
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    
    addStyleName(JEP_RIA_TREE_FIELD_STYLE);
  }
  
  /**
   * Устанавливает значение текущего загрузчика данных.
   * 
   * @param dataLoader    инциализируемый загрузчик данных
   */
  public void setLoader(DataLoader<V> dataLoader){
    this.loader = dataLoader;
    
    selectionModel = new MultiSelectionModel<V>(){
      @Override
      public void setSelected(V item, boolean selected) {
        boolean isLeaf = isLeaf(item);
        switch(checkNodes){
          // допустимо выделение только листьев
          case LEAF : { 
            if(!isLeaf) return; 
            break; 
          } 
          // допустимо выделение только родительских узлов
          case PARENT : { 
            if(isLeaf) return; 
            break; 
          }
        }
        super.setSelected(item, selected);
        // если текущий узел был отмечен как частично выделенный
        if (partialSelectedNodes.contains(item)){
          partialSelectedNodes.remove(item);
        }
        
        refreshNode(item);
        
        switch (checkStyle){
          case PARENTS : {
            TreeNodeInfo<V> treeNodeInfo = nodeMapOfDisplay.get(item);
            if (JepRiaUtil.isEmpty(treeNodeInfo)) return;
            V parentValue = treeNodeInfo.getParent();
            while (!JepRiaUtil.isEmpty(parentValue)){
              // если родительский узел был отмечен как частично выделенный
              if (partialSelectedNodes.contains(parentValue)){
                partialSelectedNodes.remove(parentValue);
              }
              refreshNode(parentValue);
              parentValue = nodeMapOfDisplay.get(parentValue).getParent();
            }
            break;
          }
          case CHILDREN : {
            List<V> children = getChildrenNodes(item);
            if (!JepRiaUtil.isEmpty(children)){
              for (V child : children){
                if (selected ? !isSelected(child) : isSelected(child)){
                  setSelected(child, selected);
                }
              }
            }
            break;
          }
        }
      }
    };
    
    tree = new CellTree(new TreeModel(), null, images, messages, Integer.MAX_VALUE){
      @Override
      public void onBrowserEvent(Event event) {
        // prevent multi-opening
        if (openingNode == null){
          super.onBrowserEvent(event);
        }
      }
    };
    tree.addOpenHandler(new OpenHandler<TreeNode>() {
      @Override
      @SuppressWarnings("unchecked")
      public void onOpen(OpenEvent<TreeNode> event) {
        TreeNode node = event.getTarget();
        V currentNode = (V) node.getValue();
        TreeNodeInfo<V> info = nodeMapOfDisplay.get(currentNode);
        if (JepRiaUtil.isEmpty(info)) return;
        
        if (JepRiaUtil.isEmpty(info.getNode())){
          info.setNode(node);
        }
        
        if (checkStyle.equals(CheckCascade.CHILDREN)) {
          if (JepRiaUtil.isEmpty(node) || node.equals(tree.getRootTreeNode())) return;
          selectionModel.setSelected(currentNode, isSelected(currentNode));
        }
        else if (!JepRiaUtil.isEmpty(getNodeInfoByValue(currentNode))){
          // refresh expanded node
          refreshNode(currentNode);
        }
        ensureVisible(currentNode);
      }
    });
    
    tree.addCloseHandler(new CloseHandler<TreeNode>() {
      @Override
      @SuppressWarnings("unchecked")
      public void onClose(final CloseEvent<TreeNode> event) {
        refreshNode((V) event.getTarget().getValue());
      }
    });
  }

  /**
   * Получает список выбранных узлов в компоненте.
   * 
   * @return  список узлов дерева
   */
  public List<V> getCheckedSelection() {
    return new ArrayList<V>(selectionModel.getSelectedSet());
  }
  
  /**
   * Устанавливает список частично выбранных узлов дерева (без необходимости получения информации о его потомках).
   * 
   * @param list  список узлов дерева
   */
  public void setPartialSelected(List<V> list){
    if (!JepRiaUtil.isEmpty(list) && !list.isEmpty()){
      this.partialSelectedNodes = list;
      for (V node : list){
        refreshNode(node);
      }
    }
  }
  
  /**
   * Размещает указанный узел дерева в области видимости пользователя.
   * 
   * @param value    узел дерева
   */
  public void ensureVisible(V value){
    Element cellTreeElement = getWidget().getElement();
    NodeList<Element> spanNodes = cellTreeElement.getElementsByTagName("span");
    for (int i = 0; i < spanNodes.getLength(); i++) {
      Element spanElement = spanNodes.getItem(i);
      if (String.valueOf(value.getValue()).equalsIgnoreCase(spanElement.getId())) {
        super.ensureVisible(new ElementSimplePanel(spanElement));
      }
    }
  }
  
  /**
   * Сбрасывает текущее выбранные узлы, в том числе и частично выбранные.
   */
  public void clearSelection(){
    // очищаем логический список частично выделенных узлов
    partialSelectedNodes.clear();
    // также визуально убираем галочки на карте редактирования
    for (V option : getCheckedSelection()) {
      setChecked(option, false);
    }
  }

  /**
   * Привязывается обработчик выбора узла к компоненту. 
   * 
   * @param handler    обработчки выбора узла
   */
  public HandlerRegistration addCheckChangeHandler(CheckChangeHandler<V> handler) {
    return addHandler(handler, CheckChangeEvent.getType());
  }
  
  /**
   * Проверка развернутости указанного узла дерева
   * 
   * @param node    раскрываемый узел, среди потомков которого ищется узел дерева   
   * @param value    проверяемый узел дерева
   * @return    логический признак открытости
   */
  @SuppressWarnings("unchecked")
  public boolean isNodeOpened(TreeNode node, V value){
    for(int i = 0; i < node.getChildCount(); i++){
      V entityChild = (V) node.getChildValue(i);
      if(value.equals(entityChild)){
        return node.isChildOpen(i);
      } else if(node.isChildOpen(i)){
        TreeNode currentNode = node.setChildOpen(i, true, false);
        if(!JepRiaUtil.isEmpty(currentNode)){
          if (isNodeOpened(currentNode, value)) return true;
        }
      }
    }
    return false;
  }
  
  /**
   * Проверка развернутости указанного узла дерева.
   *    
   * @param value    проверяемый узел дерева
   * @return    логический признак открытости
   */
  public boolean isNodeOpened(V value){
    return !JepRiaUtil.isEmpty(getChildrenNodes(value)) && isNodeOpened(tree.getRootTreeNode(), value);
  }
  
  /**
   * Проверка узла дерева на наличие потомков.
   *    
   * @param value    проверяемый узел дерева
   * @return    логический признак открытости
   */
  public boolean isLeaf(Object value) {
    return JepRiaUtil.isEmpty(value) ? false : !(value instanceof JepParentOption);
  }
  
  /**
   * Показывает текущий компонент.
   */
  public void showTree(){
    if (JepRiaUtil.isEmpty(getWidget())) {
      setWidget(tree);
      getElement().getStyle().setProperty("background", "none");
    }
  }
  
  /**
   * Проверяет является ли указанный узел дерева выбранным.
   * 
   * @param value      узел дерева
   * @return    логический признак выбора узла
   */
  public boolean isSelected(V value){
    return selectionModel.isSelected(value);
  }
  
  /**
   * Устанавливает или снимает выбор для заданного узла дерева.
   * 
   * @param value      узел дерева  
   * @param checked    признак выбора
   */
  public void setChecked(V value, boolean checked){
    selectionModel.setSelected(value, checked);
  }
  
  /**
   * Search specified node in the tree. Expand or collapse it. And return founded node or null if it's leaf. 
   * 
   * @param node        relative node
   * @param value        specified value
   * @param expand      flag to expand or collapse this node
   * @return reference of current node 
   */
  @SuppressWarnings("unchecked")
  public TreeNode setExpanded(TreeNode node, V value, boolean expand){
    for(int i = 0; i < node.getChildCount(); i++){
      V entityChild = (V) node.getChildValue(i);
      if(value.equals(entityChild)){
        return node.setChildOpen(i, expand);
      } else if(node.isChildOpen(i)){
        TreeNode currentNode = node.setChildOpen(i, true);
        if(!JepRiaUtil.isEmpty(currentNode)){
          setExpanded(currentNode, value, expand);
        }
      }
    }
    return null;
  }
  
  /**
   * Search specified node in the tree beginning from root. Expand or collapse it. And return founded node or null if it's leaf. 
   * 
   * @param value        specified value
   * @param expand      flag to expand or collapse this node
   * @return reference of current node 
   */
  public TreeNode setExpanded(V value, boolean expand){
    TreeNode rootNode = tree.getRootTreeNode();
    // if current node - root one
    if (JepRiaUtil.isEmpty(value)) {
      // expand or collapse all nodes of root
      for (int index = 0; index < rootNode.getChildCount(); index++){
        rootNode.setChildOpen(index, expand);
      }
      // and return root node
      return rootNode;
    }
    return setExpanded(rootNode, value, expand);
  }
  
  /**
   * Сворачивает все узлы дерева.
   */
  public void collapseAll(){
    setExpanded(null, false);
  }
  
  /**
   * Refresh state of node with specified value
   * 
   * @param node    value of this node
   */
  @SuppressWarnings("unchecked")
  public void refreshNode(V node){
    ((TreeModel) tree.getTreeViewModel()).refreshNode(node);
  }
  
  /**
   * Устанавливает признак возможного выбора узлов в дереве. 
   * 
   * @param checkNodes    признак возможного выбора
   */
  public void setCheckNodes(CheckNodes checkNodes) {
    this.checkNodes = checkNodes;
  }

  /**
   * Устанавливает стиль выбора узлов в дереве. 
   * 
   * @param checkStyle    признак возможного выбора
   */
  public void setCheckStyle(CheckCascade checkStyle) {
    this.checkStyle = checkStyle;
  }
  
  /**
   * Устанавливает или блокирует выбор узлов дерева. 
   * 
   * @param checkable    признак возможного выбора
   */
  public void setCheckable(boolean checkable){
    this.checkable = checkable;
  }
  
  /**
   * Устанавливает или снимает границы компонента.
   * 
   * @param borders    признак наличия границ компонента
   */
  public void setBorders(boolean borders){
    getElement().getStyle().setProperty("border", borders ? "1px solid #ccc" : "none");
  }
  
  /**
   * Получает список узлов-потомков указанного узла дерева.
   * 
   * @param node    узел дерева
   * @return список узлов-потомков
   */
  public List<V> getChildrenNodes(V node){
    TreeNodeInfo<V> info = getNodeInfoByValue(node);
    return JepRiaUtil.isEmpty(info) ? null : info.getData();
  }
  
  /**
   * Получает логическое описание узла дерева.
   * 
   * @param node    узел дерева
   * @return логическое описание дерева
   */
  public TreeNodeInfo<V> getNodeInfoByValue(V node){
    // check: is node a leaf
    if (isLeaf(node)) return null;
    
    for (Entry<V, TreeNodeInfo<V>> entry : nodeMapOfDisplay.entrySet()){
      TreeNodeInfo<V> nodeInfo = entry.getValue();
      if (JepRiaUtil.equalWithNull(node, nodeInfo.getParent())){
        return nodeInfo;
      }
    }
    return null;
  }
  
  /**
   * Модель представления данных в компоненте.
   */
  class TreeModel implements TreeViewModel {
    
    /**
     * Провайдер данных.
     */
    private TreeDataProvider provider;
    
    /**
     * Менеджер для управления выбором узлов дерева.
     */
    private final DefaultSelectionEventManager<V> selectionManager =
      DefaultSelectionEventManager.createCustomManager(new EventTranslator<V>(){
        @Override
        public boolean clearCurrentSelection(CellPreviewEvent<V> event) {
          return false;
        }
        
        @Override
        public SelectAction translateSelectionEvent(CellPreviewEvent<V> event) {
          // Handle the event.
          NativeEvent nativeEvent = event.getNativeEvent();
          if (CLICK.equals(nativeEvent.getType())) {
            V currentNode = event.getValue();
            if ((checkNodes.equals(CheckNodes.LEAF) && !isLeaf(currentNode)) || 
                (checkNodes.equals(CheckNodes.PARENT) && isLeaf(currentNode)) || !checkable) return SelectAction.IGNORE;
            // fire event
            TreeField.this.fireEvent(new CheckChangeEvent<V>(currentNode, !isSelected(currentNode)));
            return SelectAction.TOGGLE;
          }
          // For keyboard events, do the default action.
          return SelectAction.DEFAULT;
        }
      });
    
    /**
     * Получает информацию о текущем узле.
     * 
     * @param value   узел дерева
     */
    @SuppressWarnings("unchecked")
    public <T> NodeInfo<?> getNodeInfo(T value) {
      provider = new TreeDataProvider((V) value);
      
      // FIXME TODO Ниже нужно избежать использования CompositeCell, так как после рефакторинга
      // элемент списка состоит из одного, а не двух элементов и поэтому больше не является композитным.
      // Проблема в том, что Column не создается от HasCell (зато создается от CompositeCell(HasCell)),
      // а использование HasCell нужно для задания в нем FieldUpdater.
      
      // Create a list of cell. These cells will make up the composite cell
      // Here I am constructing a composite cell with 2 parts that includes a checkbox.
      final List<HasCell<V, ?>> cellComponents = new ArrayList<HasCell<V, ?>>();
      
      // FIXME TODO This HasCell is a STUB! Probably replace with some ~default~ Cell to not override methods
      cellComponents.add(new HasCell<V, String>() {

        private final Cell<String> cell = new TextCell();
        
        @Override
        public Cell<String> getCell() {
          return cell;
        }

        @Override
        public FieldUpdater<V, String> getFieldUpdater() {
          return null;
        }

        @Override
        public String getValue(V object) {
          return !JepRiaUtil.isEmpty(object) ? object.getName() : null;
        }
        
      });
      
      // Create a composite cell and pass the definition of
      // individual cells that the composite cell should render.
      CompositeCell<V> compositeCell = new CompositeCell<V>(cellComponents){
        private static final String PADDING = "   ";
        
        @Override
        public boolean isEditing(Context context, Element parent, V value) {
          return false;
        }
        
        @Override
        public boolean resetFocus(Context context, Element parent, V value) {
          return false;
        }
        
        @Override
        protected <X> void render(Context context, V value, SafeHtmlBuilder sb, HasCell<V, X> hasCell) {
          
          // 1st part of Composite cell - Show a checkbox image and select it "selected" property is true
          final SafeHtml checkImageHtml;
          
          final boolean isLeaf = isLeaf(value);
          // null for non-checkable node (no checkbox is shown), 0 for unchecked, 1 for checked, 2 for partial
          final Integer checkedState;
          if (checkNodes == CheckNodes.LEAF && !isLeaf) {
            // допустимо выделение только листьев, не отображаем соответствующую картинку
            checkedState = null;
          } else if (checkNodes == CheckNodes.PARENT && isLeaf) {
            // допустимо выделение только родительских узлов, не отображаем соответствующую картинку
            checkedState = null;
          } else if (partialSelectedNodes.contains(value)){
            checkedState = 2;
          } else if (isNodeOpened(value) && checkStyle.equals(CheckCascade.PARENTS)) {
            int in = hasPartlySelectedChildren(value);
            if (in == 1) {
              checkedState = 1;
            } else if (in == 0) {
              checkedState = 2;
            } else {
              checkedState = 0;
            }
          } else {
            if (isSelected(value)) {
              checkedState = 1;
            } else {
              checkedState = 0;
            }
          }
          
          final String nodeCheckedState;
          
          if (checkedState == null) {
            checkImageHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;
            nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKABLE;
          } else {
            final ImageResource checkImg;
            switch (checkedState) {
              case 0:
                checkImg = images.unchecked();
                nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKED;
                break;
              case 1:
                checkImg = images.checked();
                nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED;
                break;
              case 2: default:
                checkImg = images.partialChecked();
                nodeCheckedState = JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL;
                break;
            }
            checkImageHtml = AbstractImagePrototype.create(checkImg).getSafeHtml();
          }
          
          
          // 2nd part of Composite cell - Show a folder image for parent nodes
          final SafeHtml folderImageHtml;
          
          if (isLeaf(value)) {
            folderImageHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;
          } else {
            ImageResource folderImg = isNodeOpened(value) ? images.folderOpened() : images.folderClosed();
            folderImageHtml = AbstractImagePrototype.create(folderImg).getSafeHtml();
          }
          
          
          // 3rd part of Composite cell - Show Text for the Cell
          SafeHtml labelHtml = SafeHtmlUtils.fromTrustedString(
              JepClientUtil.substitute("<label title='{2}' {0}>{1}{2}</label>",
                  checkedState == null ? "" : "style=\"cursor: pointer;\"",
                  SafeHtmlUtils.fromString(PADDING).asString(),
                  value.getName()));
          
          
          final String nodeId;
          if (fieldIdAsWebEl != null) {
            nodeId = "id='" + fieldIdAsWebEl + JEP_TREENODE_INFIX +  value.getName() + "'";
          } else {
            nodeId = "";
          }
          
          
          sb.appendHtmlConstant(
              JepClientUtil.substitute("<span {0} {1} {2}>",
                  nodeId,
                  JEP_TREENODE_ISLEAF_HTML_ATTR + (isLeaf ? "='true'" : "='false'"),
                  JEP_TREENODE_CHECKEDSTATE_HTML_ATTR + "='" + nodeCheckedState + "'"));
          sb.append(checkImageHtml);
          sb.append(folderImageHtml);
          sb.append(labelHtml);
          sb.appendHtmlConstant("</span>");
        }
        
        private int hasPartlySelectedChildren(V value){
          List<V> children = isNodeOpened(value) ? getChildrenNodes(value) : null;
          if (JepRiaUtil.isEmpty(children)) return -1;
          
          int childrenCount = children.size(), selectedCount = 0;
          
          for (int i = 0; i < childrenCount; i++){
            V childrenValue = children.get(i);
            int selected = hasPartlySelectedChildren(childrenValue);
            if (selected == 0) {
              if (!isSelected(value)) {
                selectionModel.setSelected(value, true);
              }
              return 0;
            }
            if (isSelected(childrenValue)) selectedCount++;
          }
          
          if (selectedCount == 0){ // no one node is selected
            if (isSelected(value)) {
              selectionModel.setSelected(value, false);
            }
            return -1;
          } else if (selectedCount == childrenCount){ // all nodes are selected
            if (!isSelected(value)) {
              selectionModel.setSelected(value, true);
            }
            return 1;
          } else { // some of nodes are selected
            if (!isSelected(value)) {
              selectionModel.setSelected(value, true);
            }
            return 0;
          }
        }
      };
      return new DefaultNodeInfo<V>(provider, compositeCell, selectionModel, selectionManager, null);
    }

    /**
     * Проверка, что указанный узел дерева является листовым.
     * 
     * @param value   узел дерева
     */
    public boolean isLeaf(Object value) {
      return TreeField.this.isLeaf(value);
    }
    
    /**
     * Обновляет информацию об узле дерева.
     * 
     * @param value    узел дерева
     */
    public void refreshNode(V value){
      provider.refreshNode(value);
    }
  }
  
  /**
   * A custom {@link AsyncDataProvider}.
   */
  class TreeDataProvider extends AsyncDataProvider<V> {
    
    /**
     * Раскрываемый узел
     */
    private V expandNode;
    
    /**
     * Создает провайдер данных для раскрываемого узла.
     * 
     * @param node    узел дерева
     */
    public TreeDataProvider(V node){
      this.expandNode = node;
    }
    
    /**
     * {@link #onRangeChanged(HasData)} is called when the table requests a
     * new range of data. You can push data back to the displays using
     * {@link #updateRowData(int, List)}.
     */
    @Override
    protected void onRangeChanged(final HasData<V> display) {
      TreeNodeInfo<V> nodeInfo = getNodeInfoByValue(expandNode);
      // if cache doesn't have info about node's children
      if (nodeInfo == null){
        openingNode = expandNode;
        // Query the data asynchronously making an RPC call to DB.
        loader.load(expandNode, new JepAsyncCallback<List<V>>() {
          @Override
          public void onSuccess(List<V> result) {
            TreeNodeInfo<V> info = new TreeNodeInfo<V>(display, result, expandNode);
            for (V value : result){
              // store info (current nodes and correspondent display) about tree level
              nodeMapOfDisplay.put(value, info);            
            }
            openingNode = null;
            onRangeChanged(display);
          }
        });
      }
      // node have been already saved with its children -
      // fetch children's info from cache
      else {
        nodeInfo.setDisplay(display);
        boolean isFromCache = nodeInfo.isFromCache();
        if (!isFromCache){
          nodeInfo.setFromCache(true);
        }
        
        refreshDisplay(display, nodeInfo.getData());
        
        if (JepRiaUtil.isEmpty(expandNode)){
          showTree();
        } 
        else if (!isFromCache) { // expand node again
          OpenEvent.fire(tree, nodeMapOfDisplay.get(expandNode).getNode());
        }
      }
    }    
    
    /**
     * Обновляет текущее отображение списка указанных узлов дерева.
     * 
     * @param display    отображение узла дерева
     * @param data      список узлов дерева
     */
    public void refreshDisplay(HasData<V> display, List<V> data){
      display.setRowData(display.getVisibleRange().getStart(), data);
      display.setRowCount(data.size(), true);
    }
    
    /**
     * Обновляет отображение указанного узла дерева.
     * 
     * @param value      узел дерева
     */
    public void refreshNode(V value){
      // Если узел листовой или не получена информация о его детях
      if (!partialSelectedNodes.contains(value) && JepRiaUtil.isEmpty(getChildrenNodes(value))) return; 
        
      TreeNodeInfo<V> nodeInfo = nodeMapOfDisplay.get(value);
      if (!JepRiaUtil.isEmpty(nodeInfo)) {
        refreshDisplay(nodeInfo.getDisplay(), nodeInfo.getData());
      }      
    }
  }
}
