package com.technology.jep.jepria.client.widget.field.tree;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_CHECKED;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_PARTIAL;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKABLE;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_CHECKEDSTATE_VALUE_UNCHECKED;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_INFIX;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_TREENODE_ISLEAF_HTML_ATTR;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.EventTranslator;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.widget.container.ElementSimplePanel;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.CheckChangeHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.HasCheckChangeHandlers;
import com.technology.jep.jepria.client.widget.field.tree.images.TreeFieldResources;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс, представляющий реализацию поля выбора в виде древовидной иерархии.
 */
@SuppressWarnings("unchecked")
public class TreeField<V extends JepOption> extends Composite implements HasCheckChangeHandlers<V> {
  
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
   * Наименование селектора (класса стилей) для недоступных элементов.
   */
  private static final String DISABLED_FIELD_STYLE = "jepRia-Field-disabled";
  
  /**
   * Карта соответствия узла дерева с его логическим представлением.
   */
  private Map<V, TreeNodeInfo<V>> nodeMapOfDisplay = new HashMap<V, TreeNodeInfo<V>>();
  
  /**
   * Список доступных для выбора узлов, учитывающий возможность установки значения в модель выбора
   */
  private List<V> availableSelectedNodes = new ArrayList<V>();
  
  /* Resources: texts and images */
  private static final TreeFieldMessages messages = new TreeFieldMessages();
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
   * Флаг "Выбрать все".
   */
  protected CheckBox selectAllCheckBox;
  
  /**
   * Наименование класса стилей для флага "Выделить все".
   */
  private static final String SELECT_ALL_CHECK_BOX_STYLE = "jepRia-ListField-SelectAllCheckBox";
  
  /**
   * Панель для размещения дерева и флага "Выделить все".
   */
  protected VerticalPanel widgetPanel;
  
  /**
   * Панель для горизонтального размещения панели с виджетами и индикатора загрузки.
   */
  private HorizontalPanel panel;
  
  /**
   * Панель прокручивания для дерева.
   */
  protected ScrollPanel treePanel;
  
  interface Template extends SafeHtmlTemplates {
    
    /**
     * Html-шаблон для отрисовки всей структуры узла дерева.
     */
    @Template("<span id=\"{0}\" " + JEP_TREENODE_ISLEAF_HTML_ATTR +"=\"{1}\" " + JEP_TREENODE_CHECKEDSTATE_HTML_ATTR +"=\"{2}\">{3}{4}{5}</span>")
    SafeHtml nodeWrapper(String id, String jepTreeNodeIsLeafAttr, String jepTreeNodeCheckedStateAttr, SafeHtml checkedImage, SafeHtml folderImage, SafeHtml label);
    
    /**
     * Html-шаблон для отрисовки надписи узла дерева.
     */
    @Template("<label title=\"{0}\" style=\"{1}\">{2}{0}</label>")
    SafeHtml labelWrapper(String name, SafeStyles style, SafeHtml padding);
    
    /**
     * Html-шаблон для отрисовки пустой опции.
     */
    @Template("<span style=\"font-style:italic;\">{0}</span>")
    SafeHtml emtpyNodeWrapper(String text);
  } 
  
  /**
   * Интерфейс стилизации дерева.
   */
  private final static Template template = GWT.create(Template.class);
  
  /**
   * Пустой узел дерева.
   */
  private final V EMPTY_TREE_OPTION = (V) new JepOption(messages.emptyTree(), null);
  
  /**
   * Создает экземпляры данного класса.
   */
  public TreeField(String fieldIdAsWebEl){
    this.fieldIdAsWebEl = fieldIdAsWebEl;
    
    widgetPanel = new VerticalPanel();
    
    treePanel = new ScrollPanel(); 
    
    selectAllCheckBox = new CheckBox(JepTexts.listField_selectAll());
    selectAllCheckBox.addStyleName(SELECT_ALL_CHECK_BOX_STYLE);
    selectAllCheckBox.addStyleName(MAIN_FONT_STYLE);
    
    setSelectAllCheckBoxVisible(false);
    
    selectAllCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {      
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        onSelectAll(event != null && event.getValue() != null && event.getValue());
      }
    });
    
    populateWidgetPanel();
    
    panel = new HorizontalPanel();
    panel.add(widgetPanel);
    
    initWidget(panel);
    
    treePanel.addStyleName(JEP_RIA_TREE_FIELD_STYLE);
  }
  
  public void setImageResources(TreeFieldResources resources){
    this.images = resources;
  }
  /**
   * Устанавливает значение текущего загрузчика данных.
   * 
   * @param dataLoader    инциализируемый загрузчик данных
   */
  public void setLoader(DataLoader<V> dataLoader){
    this.loader = dataLoader;
    
    selectionModel = getSelectionModel();
    tree = getTree();
  }

  /**
   * Получает модель выбора в древовидном справочнике.
   */
  protected SetSelectionModel<V> getSelectionModel() {
    SetSelectionModel<V> result = new MultiSelectionModel<V>(){
      @Override
      public void setSelected(V item, boolean selected) {
        
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
    
    result.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        boolean isSameList = availableSelectedNodes.containsAll(getCheckedSelection()) && 
            getCheckedSelection().containsAll(availableSelectedNodes);
        selectAllCheckBox.setValue(isSameList);
      }
    });
    
    return result;
  }

  /**
   * Формирует GWT-виджет древовидного справочника.
   */
  protected CellTree getTree() {
    CellTree result = new CellTree(new TreeModel(), null, images, messages, Integer.MAX_VALUE){
      @Override
      public void onBrowserEvent(Event event) {
        // prevent multi-opening
        if (openingNode == null){
          super.onBrowserEvent(event);
        }
      }
    };
    result.addOpenHandler(new OpenHandler<TreeNode>() {
      @Override
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
    
    result.addCloseHandler(new CloseHandler<TreeNode>() {
      @Override
      public void onClose(final CloseEvent<TreeNode> event) {
        refreshNode((V) event.getTarget().getValue());
      }
    });
    result.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
    
    return result;
  }

  /**
   * Получает список выбранных узлов в компоненте.
   * 
   * @return  список узлов дерева
   */
  public List<V> getCheckedSelection() {
    List<V> selected = new ArrayList<V>(selectionModel.getSelectedSet());
    if (this.checkStyle != CheckCascade.NONE) {
      return selected.stream().filter(item -> {
        return !partialSelectedNodes.contains(item);
      }).collect(Collectors.toList());
    } else {
      return selected;
    }
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
    Element cellTreeElement = treePanel.getWidget().getElement();
    NodeList<Element> spanNodes = cellTreeElement.getElementsByTagName("span");
    for (int i = 0; i < spanNodes.getLength(); i++) {
      Element spanElement = spanNodes.getItem(i);
      if (String.valueOf(value.getValue()).equalsIgnoreCase(spanElement.getId())) {
        treePanel.ensureVisible(new ElementSimplePanel(spanElement));
      }
    }
  }
  
  /**
   * Прокручивает дерево к первоначальному состоянию.
   */
  public void scrollToTop(){
    treePanel.scrollToTop();
  }
  
  /**
   * Установка цвета фона для панели дерева.
   * 
   * @param backgroundColor   цвет фона
   */
  public void setBackgroundColor(String backgroundColor){
    treePanel.getElement().getStyle().setBackgroundColor(backgroundColor);
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
    if (JepRiaUtil.isEmpty(treePanel.getWidget())) {
      treePanel.setWidget(tree);
      treePanel.getElement().getStyle().setProperty("background", "none");
    }
  }
  
  /**
   * Заполнение панели деревом и флагом "Выделить все".<br>
   * При необходимости изменить стандартный порядок следования виджетов,
   * метод следует переопределить в классе-наследнике.
   */
  protected void populateWidgetPanel() {
    widgetPanel.add(treePanel);
    widgetPanel.add(selectAllCheckBox);
  }
  
  /**
   * Установка видимости флага "Выделить все".<br>
   * По умолчанию флаг невидим.
   * @param visible если true, то показать, в противном случае - скрыть
   */
  public void setSelectAllCheckBoxVisible(boolean visible) {
    selectAllCheckBox.setVisible(visible);
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
   * Обработчик события щелчка по флагу "Выделить все".<br>
   * Если флаг проставлен, выделяются все видимые узлы дерева. В противном случае всё сбрасывается.
   * После этого вызывается событие 
   * {@link com.technology.jep.jepria.client.widget.event.JepEventType#CHANGE_SELECTION_EVENT}.
   */
  protected void onSelectAll(boolean selectAll) {
    for (V node : nodeMapOfDisplay.keySet()) {
        setChecked(node, selectAll);
    }
  }
  
  /**
   * Search specified node in the tree. Expand or collapse it. And return founded node or null if it's leaf. 
   * 
   * @param node        relative node
   * @param value        specified value
   * @param expand      flag to expand or collapse this node
   * @return reference of current node 
   */
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
  public void refreshNode(V node){
    ((TreeModel) tree.getTreeViewModel()).refreshNode(node);
  }
  
  /**
   * Refresh state of tree
   */
  public void refresh(){
    ((TreeModel) tree.getTreeViewModel()).refresh();
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
    this.selectAllCheckBox.setEnabled(checkable);
  }
  
  /**
   * Установка признака доступности/недоступности элемента. 
   * 
   * @param enabled  true - поле становится доступным, в противном случае - блокируется.
   */
  public void setEnabled(boolean enabled){
  }
  
  /**
   * Устанавливает или снимает границы компонента.
   * 
   * @param borders    признак наличия границ компонента
   */
  public void setBorders(boolean borders){
    treePanel.getElement().getStyle().setProperty("border", borders ? "1px solid #ccc" : "none");
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
      if (Objects.equals(node, nodeInfo.getParent())){
        return nodeInfo;
      }
    }
    return null;
  }
  
  /**
   * Получение DOM-элемента по значению узла.
   * 
   * @param node    значение узла
   * @return получение DOM-элемента
   */
  public Element getTreeNode(V node){
    Element treeNode = DOM.getElementById(node.hashCode() + "");
    while (!Roles.getTreeitemRole().getName().equalsIgnoreCase(treeNode.getAttribute("role"))) {
      treeNode = treeNode.getParentElement();
    }
    return treeNode;
  }
  
  /**
   * Установка высоты.<br>
   * Устанавливает высоту виджета. Если флаг "Выделить все" показан,
   * то его высота не учитывается.
   * @param height значение высоты.
   */
  @Override
  public void setHeight(String height) {
    treePanel.setHeight(height);
  }
  
  /**
   * Установка ширины.<br>
   * Устанавливает ширину виджета.
   * @param width значение ширины.
   */
  @Override
  public void setWidth(String width) {
    treePanel.setWidth(width);
  }
  
  /**
   * Помечаем узел как доступный для выбора, если это возможно.
   * 
   * @param value   узел дерева
   */
  public void markNodeAsSelectedIfAvailable(V value) {
    // fill up the list of available of checking nodes
    boolean isLeaf = isLeaf(value);
    switch(checkNodes){
      // допустимо выделение только листьев
      case LEAF : { 
        if(isLeaf) {
          availableSelectedNodes.add(value);
        }
        break; 
      } 
      // допустимо выделение только родительских узлов
      case PARENT : { 
        if(!isLeaf) {
          availableSelectedNodes.add(value);
        }
        break; 
      }
      case BOTH :
      default : { // допускается выделять любые типы узлов дерева
        availableSelectedNodes.add(value);
      } 
    }
  }
  
  /**
   * Определяем состояние узла дерева. В случае необходимости - можно перегрузить в наследниках.
   * 
   * @return  представление узла дерева
   */
  protected Cell<V> getTreeCell() {
    return new TreeCell();
  }

  /**
   * Определяем поведение выбора узла дерева.
   * 
   * @param event   событие, определяющее выбор узла дерева
   * @return действие перед выбором узла дерева
   */
  protected SelectAction translateSelectionEvent(CellPreviewEvent<V> event) {
    // Handle the event.
    NativeEvent nativeEvent = event.getNativeEvent();
    if (CLICK.equals(nativeEvent.getType())) {
      V currentNode = event.getValue();
      if ((checkNodes.equals(CheckNodes.LEAF) && !isLeaf(currentNode)) || 
          (checkNodes.equals(CheckNodes.PARENT) && isLeaf(currentNode)) || !checkable) return SelectAction.IGNORE;
      CheckChangeEvent<?> checkChangeEvent = new CheckChangeEvent<V>(currentNode, !isSelected(currentNode));
      // fire event
      TreeField.this.fireEvent(checkChangeEvent);
      return checkChangeEvent.isCancelled() ? SelectAction.IGNORE : SelectAction.TOGGLE;
      
    }
    // For keyboard events, do the default action.
    return SelectAction.DEFAULT;
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
          return TreeField.this.translateSelectionEvent(event);
        }
      });
    
    /**
     * Получает информацию о текущем узле.
     * 
     * @param value   узел дерева
     */
    public <T> NodeInfo<?> getNodeInfo(T value) {
      provider = new TreeDataProvider((V) value);
      return new DefaultNodeInfo<V>(provider, getTreeCell(), selectionModel, selectionManager, null);
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
    
    /**
     * Обновляет информацию о структуре дерева.
     */
    public void refresh(){
      nodeMapOfDisplay.clear();
      treePanel.setWidget(tree = getTree());
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
            if (result.isEmpty()) {
              // add empty option and render it in special way
              // to avoid endless loop
              result.add((V) new JepOption(EMPTY_TREE_OPTION));
            }
            for (V value : result){
              // store info (current nodes and correspondent display) about tree level
              nodeMapOfDisplay.put(value, info);
              markNodeAsSelectedIfAvailable(value);
            }
            openingNode = null;
            // If data retrieve without delays, we should initialize tree firstly
            info.setFromCache(true);
            refreshDisplay(display, info.getData());
            showTree();
            if (expandNode != null) {
              TreeNode openingNode = nodeMapOfDisplay.get(expandNode).getNode();
              if (openingNode != null) { // it node has no child
                OpenEvent.fire(tree, openingNode);
              }
            }
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
          TreeNode openingNode = nodeMapOfDisplay.get(expandNode).getNode();
          if (openingNode != null) { // it node has no child
            OpenEvent.fire(tree, openingNode);
          }
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
  
  /**
   * Установка ID внутренних компонентов TreeField: кнопки "выделить все"
   * @param fieldIdAsWebEl ID JepTreeField'а, который берется за основу ID внутренних компонентов
   */
  public void setCompositeWebIds(String fieldIdAsWebEl) {
    selectAllCheckBox.getElement().setId(fieldIdAsWebEl + JepRiaAutomationConstant.JEP_TREE_FIELD_CHECKALL_POSTFIX);
  }
  
  public class TreeCell extends AbstractCell<V> {

    protected Integer checkedState;
    
    protected String nodeCheckedState;
    
    private static final String PADDING = "   ";
    
    /**
     * Пустой узел дерева.
     */
    private final V EMPTY_TREE_OPTION = (V) new JepOption(messages.emptyTree(), null);
    
    public TreeCell(){
      super();
    }
    
    public TreeCell(String... consumedEvents) {
      super(consumedEvents);
    }
    
    @Override
    public boolean isEditing(Context context, Element parent, V value) {
      return false;
    }
    
    @Override
    public boolean resetFocus(Context context, Element parent, V value) {
      return false;
    }
    
    @Override
    public void render(Context context, V value, SafeHtmlBuilder sb) {
      if (EMPTY_TREE_OPTION.equals(value)){
        sb.append(template.emtpyNodeWrapper(value.getName()));
        return;
      }
      
      final boolean isLeaf = isLeaf(value);
      
      // null for non-checkable node (no checkbox is shown), 0 for unchecked, 1 for checked, 2 for partial
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
          partialSelectedNodes.add(value);
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
      
      sb.append(getCellHtml(value));
    }
    
    protected int hasPartlySelectedChildren(V value){
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
    
    protected SafeHtml getCellHtml(V value){
      final boolean isLeaf = isLeaf(value);

      final String nodeId;
      if (fieldIdAsWebEl != null) {
        nodeId = fieldIdAsWebEl + JEP_TREENODE_INFIX +  value.getName();
      } else {
        nodeId = "";
      }
      
      // firstly call this method to initialize nodeCheckedState field
      SafeHtml checkBoxHtml = getCheckBoxHtml(value);
      
      return template.nodeWrapper(nodeId, 
          Boolean.toString(isLeaf), nodeCheckedState,
              checkBoxHtml, getFolderHtml(value), getLabelHtml(value));
    }
    
    protected SafeHtml getCheckBoxHtml(V value){
      // 1st part of Composite cell - Show a checkbox image and select it "selected" property is true
      final SafeHtml checkImageHtml;
      
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
      
      return checkImageHtml;
    }
    
    
    protected SafeHtml getFolderHtml(V value){
      // 2nd part of Composite cell - Show a folder image for parent nodes
      final SafeHtml folderImageHtml;
      
      if (isLeaf(value)) {
        folderImageHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;
      } else {
        ImageResource folderImg = isNodeOpened(value) ? images.folderOpened() : images.folderClosed();
        folderImageHtml = AbstractImagePrototype.create(folderImg).getSafeHtml();
      }
      return folderImageHtml;
    }
    
    protected SafeHtml getLabelHtml(V value){
      // Build the css.
      SafeStylesBuilder cssBuilder = new SafeStylesBuilder();
      if ( checkedState != null) {
        cssBuilder.appendTrustedString("cursor: pointer;");
      }
      // 3nd part of Composite cell - Show a label for tree node
      return template.labelWrapper(
          value.getName(),
            cssBuilder.toSafeStyles(),
                SafeHtmlUtils.fromString(PADDING));
    }
  }
  
}

