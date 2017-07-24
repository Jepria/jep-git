package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.field.TreeCellNames.HAS_CHILDREN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.message.JepMessageBox;
import com.technology.jep.jepria.client.message.JepMessageBoxImpl;
import com.technology.jep.jepria.client.widget.list.JepGrid.DndMode;
import com.technology.jep.jepria.client.widget.list.PagingManager.DropType;
import com.technology.jep.jepria.client.widget.list.cell.ListTreeNode;
import com.technology.jep.jepria.client.widget.toolbar.PagingToolBar;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс, управляющий древовидной структурой на списочной форме 
 */
public class TreeGridManager<W extends AbstractCellTable<JepRecord>, P extends PagingToolBar, S extends SetSelectionModel<JepRecord>> extends
  GridManager<W, P, S> {
  
  /**
   * Колонка древовидного справочника
   */
  private JepColumn<JepRecord, ?> treeCellColumn;
  
  /**
   * Коллекция содержит узлы дерева (primaryKey связывается с узлом дерева) 
   */
  private Map<Object, ListTreeNode> nodes = new HashMap<Object, ListTreeNode>();
  
  /**
   * Наименование, по которому из record берется значение первичного ключа
   */
  private String primaryKeyName;

  /**
   * Загрузчик данных списка
   */
  private DataLoader<JepRecord> loader;
  
  /**
   * Список идентификаторов "fake"-узлов, добавляемых в списочную форму
   */
  private Map<Object, List<ListTreeNode>> fakeUidMap = new HashMap<Object, List<ListTreeNode>>();

  /**
   * Список узлов которые нужно развернуть
   */
  private List<JepRecord> expandNodes = null;
  
  /**
   * Устанавливает наименование для первичного ключа
   * @param primaryKeyName наименование для первичного ключа
   */
  public void setPrimaryKeyName(String primaryKeyName){
    this.primaryKeyName = primaryKeyName;
  }
  
  /**
   * Получает наименование для первичного ключа
   * @return наименование для первичного ключа
   */
  public String getPrimaryKeyName(){
    return primaryKeyName;
  }
  
  /**
   * Инициализация простого первичного ключа по record definition. <br>
   * Для установки произвольного значения первичного ключа следует использовать метод {@link TreeGridManager#setPrimaryKeyName(String)}
   * @param recordDefinition    ссылка на описание записи
   */
  public void initPrimaryKey(JepRecordDefinition recordDefinition){
    setPrimaryKeyName(recordDefinition.getPrimaryKey()[0]);
  }
  
  /**
   * Устанавливает загрузчик данных списка
   * @param loader загрузчик данных списка
   */
  public void setLoader(DataLoader<JepRecord> loader){
    this.loader = loader;
  }
  
  @Override
  /**
   * {@inheritDoc}
   */
  public void setDndEnabled(boolean dndEnabled) {
	super.setDndEnabled(dndEnabled);
    if (dndEnabled) {
      ((JepGrid<?>) widget).setDndMode(DndMode.BOTH);
    } else {
      ((JepGrid<?>) widget).setDndMode(DndMode.NONE);
    }
  }
  /**
   * Инициализация древовидной списочной формы. <br/> 
   * Осуществляется связывание {@link com.technology.jep.jepria.client.widget.list.cell.TreeCell} или {@link com.technology.jep.jepria.client.widget.list.cell.EditTreeCell} к {@link com.technology.jep.jepria.client.widget.list.TreeGridManager}
   */
  public void bindTree() {
    for (int i = 0; i < widget.getColumnCount(); i++) {
      Cell<?> cell = widget.getColumn(i).getCell();
      // осуществляется поиск колонки нужного типа TreeCell или EditTreeCell,
      // поскольку порядок следования столбцов может быть изменен пользователем
      if (cell instanceof HasTreeGridManager) {
        treeCellColumn = (JepColumn<JepRecord, ?>) widget.getColumn(i);
        ((HasTreeGridManager) cell).setTreeGridManager(this);
        break;
      }
    }
  }
  
  /**
   * Поиск узла, соответствующего записи
   * 
   * @param record    искомая запись
   * @return логический узел
   */
  public ListTreeNode findNode(JepRecord record){
    return record == null ? null : findByPrimaryKey(record.get(primaryKeyName));
  }
  
  /**
   * Получает узел из кеша
   * @param primaryKey первичный ключ
   * @return узел из кеша
   */
  public ListTreeNode findByPrimaryKey(Object primaryKey){
    return nodes.get(primaryKey);
  }
  
  /**
   * Добавляет поддерево в список по записи
   * @param record запись, после которой добавить поддерево
   * @param data поддерево
   */
  public void setChildrenInList(JepRecord record, List<JepRecord> data){
    Object primaryKey = record.get(primaryKeyName);
    List<ListTreeNode> fakeNodes = getFakeNodesByParentId(primaryKey);
    List<JepRecord> fakeRecordsForParent = null;
    if (!JepRiaUtil.isEmpty(fakeNodes)) {
      fakeRecordsForParent = new ArrayList<JepRecord>(fakeNodes.size());
      for (ListTreeNode fakeTreeNode : fakeNodes) {
        fakeRecordsForParent.add(fakeTreeNode.getRecord());
      }
    }
    List<JepRecord> existedList = dataProvider.getList();
    if (!JepRiaUtil.isEmpty(fakeRecordsForParent)){
      data.removeAll(fakeRecordsForParent);
      data.addAll(0, fakeRecordsForParent);
      existedList.removeAll(fakeRecordsForParent);
    }
    // добавляем поддерево
    dataProvider.getList().addAll(existedList.indexOf(record) + 1, data);
    widget.setVisibleRange(0, dataProvider.getList().size());
  }
  
  /**
   * Рекурсивная процедура. <br>
   * Удаляет список записей со всеми их поддеревьями из структуры списочной формы
   * @param list список записей
   */
  public void removeBranchFromList(List<JepRecord> list){
    if(list == null){
      return;
    }
    List<JepRecord> removeNodes = new ArrayList<JepRecord>();
    for(JepRecord item: list){
      removeNodes.add(item);
      if(Boolean.TRUE.equals(item.get(HAS_CHILDREN))){
        Object itemPrimaryKey = item.get(primaryKeyName);
        ListTreeNode node = findByPrimaryKey(itemPrimaryKey);
        node.close();
        removeBranchFromList(node.children);
      }
    }
    
    dataProvider.getList().removeAll(removeNodes);
  }

  /**
   * Обработка раскрытия/скрытия дерева. <br>
   * Также запросы кешируются, очистка кеша происходит вместе с очисткой списка
   * @param context контекст строки, по которой был осуществлен клик
   */
  public void toggleChildren(final Context context) {
    final JepRecord rowRecord = (JepRecord) context.getKey();    
    Boolean hasChildren = Boolean.TRUE.equals(rowRecord.get(HAS_CHILDREN));
    //Листовой узел
    if (!hasChildren) return;
    final Object primaryKey = rowRecord.get(primaryKeyName);    
    ListTreeNode currentNode = findByPrimaryKey(primaryKey);
    //Узла нет в кеше, то это первый клик по строке
    if (currentNode == null) {
      currentNode = new ListTreeNode(rowRecord);
      nodes.put(primaryKey, currentNode);
    }
    //Детей нет в кеше, то осуществляется поиск
    if(currentNode.children == null){
      final ListTreeNode parentNode = currentNode;
      mask(JepTexts.loadingPanel_dataLoading());
      loader.load(new PagingConfig(rowRecord), new AsyncCallback<List<JepRecord>>() {
        @SuppressWarnings("unchecked")
        @Override
        public void onSuccess(List<JepRecord> subList) {
          parentNode.children = subList;
          for (JepRecord record: subList) {
            nodes.put(record.get(primaryKeyName), new ListTreeNode(record, rowRecord, parentNode.getDepth() + 1));
          }
          setChildrenInList(rowRecord, subList);
          parentNode.open();
          if (!JepRiaUtil.isEmpty(expandNodes)) {
            ListTreeNode node = findByPrimaryKey(expandNodes.get(0));
            expandNodes.remove(0);
            toggleChildren(getContextByTreeNode(node));
          }
          unmask(); // Скроем индикатор "Загрузка данных...".
          widget.redraw();
        }
        
        public void onFailure(Throwable caught) {
          unmask(); // Скроем индикатор "Загрузка данных...".
        }
      });
    } else {
      if (currentNode.isOpen()) {
        //Удаляется поддерево у текущей записи
        removeBranchFromList(currentNode.children);
      } else {
        setChildrenInList(rowRecord, currentNode.children);
      }
      currentNode.toggleOpenStatus();
      widget.redraw();
    }  
  }
  
  /**
   * Очистка списка, также очищается кеш
   */
  @Override
  public void clear() {
    super.clear();
    
    nodes.clear();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void remove(JepRecord record) {
    dataProvider.getList().remove(record);
    Object primaryKey = record.get(primaryKeyName);
    ListTreeNode cacheNode = findByPrimaryKey(primaryKey);
    //Если поддерево открыто, то удаляет и его
    if (cacheNode != null && cacheNode.isOpen()) {
      removeBranchFromList(cacheNode.children);
    }
    if (cacheNode.getParentRecord() != null){
      ListTreeNode parentNode = findNode(cacheNode.getParentRecord());//Если у узла была единственная дочерняя запись,
      parentNode.children.remove(record);
      if (parentNode.children.size() == 0) {// то его нужно закрыть.
        setExpanded(cacheNode.getParentRecord(), false);
        parentNode.getRecord().set(HAS_CHILDREN, false);
        update(parentNode.getRecord());
        dataProvider.refresh();
        widget.redraw();
      }
    }
  }
    
  /**
   * Динамическое добавление узла на древовидный справочник
   * 
   * @param parentRecord    родительский узел
   * @param isLeaf      признак листового узла
   */
  public void addChildrenNode(JepRecord parentRecord, boolean isLeaf) {
  //TODO Данная функция выглядит бесполезной, возможно её следует удалить.
    if (!Boolean.TRUE.equals(parentRecord.get(HAS_CHILDREN))) return;
    Object primaryKey = parentRecord.get(primaryKeyName);
    ListTreeNode cacheNode = findByPrimaryKey(primaryKey);
    int depth = cacheNode == null ? 1 : cacheNode.getDepth();
    JepRecord record = new JepRecord();
    record.set(HAS_CHILDREN, !isLeaf);
    String id = DOM.createUniqueId();
    record.set(primaryKeyName, id);
    int fakeNodeCount = getFakeNodes().size();
    record.set(getTreeCellDbName(), (isLeaf ? JepTexts.tree_newLeaf() : JepTexts.tree_newNode()) + (fakeNodeCount < 1 ? "" : " " + (++fakeNodeCount)));
    ListTreeNode treeNode = new ListTreeNode(record, parentRecord, depth + 1);
    nodes.put(id, treeNode);
    if (fakeUidMap.containsKey(primaryKey)){
      fakeUidMap.get(primaryKey).add(treeNode);
    } else {
      fakeUidMap.put(primaryKey, new ArrayList<ListTreeNode>(Collections.singletonList(treeNode)));
    }
    // раскроем узел, если он не раскрыт
    if (cacheNode == null || !cacheNode.isOpen()){
      setExpanded(cacheNode, true);
    } else {
      cacheNode.children.add(record);
      setChildrenInList(parentRecord, new ArrayList<JepRecord>(Collections.singletonList(record)));
    }
  }
  
  /**
   * Динамическое добавление дочернего узла на древовидный справочник
   * 
   * @param parentRecord    родительский узел
   * @param childRecord    дочерний узел
   * @param isLeaf      признак листового узла
   */
  public void addChildNode(JepRecord parentRecord, JepRecord childRecord, boolean isLeaf) {
    if (!JepRiaUtil.isEmpty(parentRecord)){
      if (!Boolean.TRUE.equals(parentRecord.get(HAS_CHILDREN)) || childRecord == null) return;
      ListTreeNode cacheNode = findByPrimaryKey(parentRecord.get(primaryKeyName));
      int depth = cacheNode == null ? 1 : cacheNode.getDepth();
      childRecord.set(HAS_CHILDREN, isLeaf);
      ListTreeNode treeNode = new ListTreeNode(childRecord, parentRecord, depth + 1);
      nodes.put(childRecord.get(primaryKeyName), treeNode);
      if (fakeUidMap.containsKey(childRecord.get(primaryKeyName))){
        fakeUidMap.get(childRecord.get(primaryKeyName)).add(treeNode);
      } else {
        fakeUidMap.put(childRecord.get(primaryKeyName), new ArrayList<ListTreeNode>(Collections.singletonList(treeNode)));
      }
      if (cacheNode.children != null) {
        cacheNode.children.add(childRecord);
      } else {
        cacheNode.children = new ArrayList<JepRecord>();
        cacheNode.children.add(childRecord);
      }
      if(cacheNode.isOpen()){
          dataProvider.getList().add(dataProvider.getList().indexOf(parentRecord) + getBranchDepth(cacheNode), 
              childRecord);
          widget.setVisibleRange(0, dataProvider.getList().size());
      }
    } else if (!JepRiaUtil.isEmpty(childRecord)){
       ListTreeNode treeNode = new ListTreeNode(childRecord, null, 1);
       nodes.put(childRecord.get(primaryKeyName), treeNode);
       dataProvider.getList().add(childRecord);
    }
    dataProvider.refresh();
    widget.redraw();
  }
  
  /**
   * Список "fake"-узлов
   * 
   * @return список фиктивно добавленных узлов
   */
  public List<ListTreeNode> getFakeNodes(){
    Collection<List<ListTreeNode>> collection = fakeUidMap.values();
    List<ListTreeNode> resultList = new ArrayList<ListTreeNode>();
    for (List<ListTreeNode> list : collection){
      resultList.addAll(list);
    }
    return resultList;
  }
  
  /**
   * Получение списка "fake"-узлов дерева по значению ключа 
   * @param primaryKey  значение ключа
   * @return список "fake"-узлов дерева
   */
  public List<ListTreeNode> getFakeNodesByParentId(Object primaryKey){
    return fakeUidMap.get(primaryKey);
  }
  
  /**
   * Вычисление глубины текущего состояния ветки, начиная с заданного узла.
   * @param node Узел дерева
   * @return Глубина ветки
   */
  private int getBranchDepth(ListTreeNode node){
    if (!JepRiaUtil.isEmpty(node)) {
      if (node.isOpen()) {
        int result = 0;
        List<JepRecord> children = node.children;
        for(int i = 0; i < children.size(); i++){
          result += 1;
          result += getBranchDepth(findNode(children.get(i)));
        }
        return result;
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void beforeDrop(List<Object> rowList, JepRecord newPositionRecord, DropType dropType) {
    List<JepRecord> tableRows = dataProvider.getList();
    if (dropType == DropType.APPEND) {
      for (Object row : rowList) {
        if (isChild((JepRecord) row, newPositionRecord) || isChild(newPositionRecord, (JepRecord) row) || row.equals(newPositionRecord)) {
          JepMessageBox messageBox = JepMessageBoxImpl.instance;  //Проверка вырожденных случаев:
          messageBox.showError("Невозможно совершить действие!"); //Перемещение записи на своего текущего родителя, перемещение родителя на его дочернюю запись
          return;                                                 //Совпадение перемещаемой записи и целевого узла
        }                                                         //TODO добавить возможность Custom обработки этих случаев
      }
    } 
    for (int i = 0; i < rowList.size(); i++) { //Удаление старых строк
      if (rowList.get(i).equals(newPositionRecord)) {
        rowList.remove(rowList.get(i));
        i-=1;
        continue;
      } 
      tableRows.remove(rowList.get(i));
      ListTreeNode node = findNode((JepRecord) rowList.get(i));
      if (!JepRiaUtil.isEmpty(node)) {
        if (node.isOpen()) { 
            setExpanded(node, false);
        }
      }
    }
    switch (dropType) {
      case APPEND : appendNodes((List<JepRecord>) (Object) rowList, newPositionRecord);
                    break;
      case BEFORE : insertBefore((List<JepRecord>) (Object) rowList, newPositionRecord);
                    break;
      case AFTER : insertAfter((List<JepRecord>) (Object) rowList, newPositionRecord);
                   break;
      default : break;
    }
  }

  /**
   * Вставка строки перед заданным записью.
   * @param oldPositionRecord
   * @param newPositionRecord
   */
  private void insertBefore(List<JepRecord> oldPositionRecords, JepRecord newPositionRecord) {
    ListTreeNode newParentTreeNode = null;
    JepRecord newPositionParentRecord = findNode(newPositionRecord).getParentRecord();
    int newIndex = dataProvider.getList().indexOf(newPositionRecord);
    if (!JepRiaUtil.isEmpty(newPositionParentRecord)) {
      newParentTreeNode = findNode(newPositionParentRecord);
      int indexOfNewNode = newParentTreeNode.children.indexOf(newPositionRecord);
      newParentTreeNode.children.addAll(indexOfNewNode, oldPositionRecords);
    }
    for (JepRecord oldPositionRecord : oldPositionRecords) {
      ListTreeNode oldPositionTreeNode = findNode(oldPositionRecord);
      if (!JepRiaUtil.isEmpty(oldPositionTreeNode.getParentRecord())) {
        ListTreeNode oldParentNode = findNode(oldPositionTreeNode.getParentRecord());
        oldParentNode.children.remove(oldPositionTreeNode.getRecord());
        if (oldParentNode.children.isEmpty()) oldPositionTreeNode.getParentRecord().set(HAS_CHILDREN, false);
      }
      nodes.put(oldPositionRecord.get(primaryKeyName), changeNodePosition(oldPositionRecord, newPositionParentRecord,
          newParentTreeNode != null ? newParentTreeNode.getDepth() : 0));
    }
    List<JepRecord> rowList = dataProvider.getList();
    rowList.addAll(newIndex, oldPositionRecords);
    dataProvider.refresh();
    widget.redraw();
  }
  
  /**
   * Вставка строки после заданной записи.
   * @param oldPositionRecord
   * @param newPositionRecord
   */
  private void insertAfter(List<JepRecord> oldPositionRecords, JepRecord newPositionRecord) {
    ListTreeNode newParentTreeNode = null;
    JepRecord newPositionParentRecord = findNode(newPositionRecord).getParentRecord();
    ListTreeNode newPositionTreeNode = findNode(newPositionRecord);
    int newIndex = dataProvider.getList().indexOf(newPositionRecord);
    if (newPositionTreeNode.isOpen() && (Boolean)newPositionRecord.get(HAS_CHILDREN) && newIndex != 0) {//Если узел, после которого вставляется новый,
      newIndex += getBranchDepth(newPositionTreeNode);//не листовой и раскрыт, то нужно увеличить новый индекс 
    }                 
    if (!JepRiaUtil.isEmpty(newPositionParentRecord)) {
      newParentTreeNode = findNode(newPositionParentRecord);
      int indexOfNewNode = newParentTreeNode.children.indexOf(newPositionRecord);
      newParentTreeNode.children.addAll(indexOfNewNode + 1, oldPositionRecords);
    }
    for (JepRecord oldPositionRecord : oldPositionRecords) {
      ListTreeNode oldPositionTreeNode = findNode(oldPositionRecord);
      if (!JepRiaUtil.isEmpty(oldPositionTreeNode.getParentRecord())) {
        ListTreeNode oldParentNode = findNode(oldPositionTreeNode.getParentRecord());
        oldParentNode.children.remove(oldPositionTreeNode.getRecord());
        if (oldParentNode.children.isEmpty()) oldPositionTreeNode.getParentRecord().set(HAS_CHILDREN, false);
      }
      nodes.put(oldPositionRecord.get(primaryKeyName), changeNodePosition(oldPositionRecord, newPositionParentRecord,
        !JepRiaUtil.isEmpty(newParentTreeNode) ? newParentTreeNode.getDepth() : 0));
    }
    List<JepRecord> rowList = dataProvider.getList();
    rowList.addAll(newIndex + 1, oldPositionRecords);
    dataProvider.refresh();
    widget.redraw();
  }

  /**
   * Проверка является ли запись record2 дочерней в дереве для записи record1.
   * 
   * @param record1
   * @param record2
   * @return 
   */
  private boolean isChild(JepRecord record1, JepRecord record2){
    while (true) {
      ListTreeNode parentNode = findNode(findNode(record2).getParentRecord());
      if (!JepRiaUtil.isEmpty(parentNode)) {
        if (parentNode.getRecord().equals(record1)) {
          return true;
        } else {
          return isChild(record1, parentNode.getRecord());
        }
      } else {
        return false;
      }
    }
  }
  
  /**
   * Присоединение всех дочерних узлов перемещаемой записи.
   * 
   * @param newPositionParentRecord
   * @param oldPositionRecords
   */
  private void appendChildren(final JepRecord newPositionParentRecord, final List<JepRecord> oldPositionRecords){
    final ListTreeNode newParentTreeNode = findNode(newPositionParentRecord);
    if (JepRiaUtil.isEmpty(newParentTreeNode.children)) {//Если дочерние узлы, еще не были загружены из DB
      mask(JepTexts.loadingPanel_dataLoading());
      loader.load(new PagingConfig(newPositionParentRecord),
        new AsyncCallback<List<JepRecord>>() {
          @Override
          public void onSuccess(List<JepRecord> subList) {
            newParentTreeNode.children = subList;
            for (JepRecord record : subList) {
              nodes.put(record.get(primaryKeyName),new ListTreeNode(record,
                  newPositionParentRecord,newParentTreeNode.getDepth() + 1));
            }
            Iterator<JepRecord> oldPositionRecordsIterator = oldPositionRecords.iterator();
            int i = newParentTreeNode.children.size() - oldPositionRecords.size() < 0 ? 0 : newParentTreeNode.children.size() - oldPositionRecords.size();
            if (!subList.isEmpty() && !newParentTreeNode.children.get(i).get(primaryKeyName).equals(oldPositionRecords.get(0).get(primaryKeyName))) {
              while (oldPositionRecordsIterator.hasNext()) {
                JepRecord oldPositionRecord = oldPositionRecordsIterator.next();
                newParentTreeNode.children.add(oldPositionRecord);
                nodes.put(oldPositionRecord.get(primaryKeyName),
                    changeNodePosition(oldPositionRecord, newPositionParentRecord, newParentTreeNode.getDepth()));
              }//Добавлено для синхронизации с DB (Чтобы избежать создания дубликатов перемещаемых записей)
            }
            unmask(); // Скроем индикатор "Загрузка данных...".
          }
          @Override
          public void onFailure(Throwable caught) {
            unmask();
          }
        });
    } else {
      newParentTreeNode.children.addAll(oldPositionRecords);
      for(JepRecord oldPositionRecord : oldPositionRecords) {
        nodes.put(oldPositionRecord.get(primaryKeyName),
            changeNodePosition(oldPositionRecord, newPositionParentRecord, newParentTreeNode.getDepth()));
      }
    }
  }
  
  /**
   * Присоединение одного узла к другому в качестве дочернего.
   * 
   * @param oldPositionRecords
   * @param newPositionRecord
   */
  private void appendNodes(final List<JepRecord> oldPositionRecords, final JepRecord newPositionRecord) {
    final ListTreeNode newPositionTreeNode = findNode(newPositionRecord);
    int newIndex = dataProvider.getList().indexOf(newPositionRecord);
    if (newPositionTreeNode.isOpen() && newPositionRecord.<Boolean>get(HAS_CHILDREN)){
      newIndex += getBranchDepth(newPositionTreeNode);
    }
    if (newPositionRecord.<Boolean>get(HAS_CHILDREN)) {
      appendChildren(newPositionRecord, oldPositionRecords);
    } else {
      newPositionRecord.set(HAS_CHILDREN, true);
      newPositionTreeNode.children = new ArrayList<JepRecord>(oldPositionRecords);
      for (JepRecord oldPositionRecord : oldPositionRecords) {
        ListTreeNode oldPositionTreeNode = findNode(oldPositionRecord);
        if (!JepRiaUtil.isEmpty(oldPositionTreeNode.getParentRecord())) {
          ListTreeNode oldParentNode = findNode(oldPositionTreeNode.getParentRecord());
          oldParentNode.children.remove(oldPositionTreeNode.getRecord());
          if (oldParentNode.children.isEmpty()) oldPositionTreeNode.getParentRecord().set(HAS_CHILDREN, false);
        }
        nodes.put(oldPositionRecord.get(primaryKeyName),
          changeNodePosition(oldPositionRecord, newPositionRecord, newPositionTreeNode.getDepth()));
      }
    }
    List<JepRecord> rowList = dataProvider.getList();
    if (newPositionTreeNode.isOpen() && newPositionRecord.<Boolean>get(HAS_CHILDREN)) {
      rowList.addAll(newIndex+1, oldPositionRecords);
    }
    dataProvider.refresh();
    widget.redraw();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeRowPosition(List<Object> rowList, int newIndex, boolean isOver, boolean insertBefore, boolean insertAfter){
    if (newIndex == -1) {
      newIndex = 0;
      insertBefore = true;
    }
    List<JepRecord> tableRows = dataProvider.getList();
    JepRecord newPositionRecord = tableRows.get(newIndex);
    if (!isOver) {
      if (insertBefore && !insertAfter) {
        beforeDrop(rowList, newPositionRecord, DropType.BEFORE);
      } else if (!insertBefore && insertAfter) {
        beforeDrop(rowList, newPositionRecord, DropType.AFTER);
      }
    } else {
      beforeDrop(rowList, newPositionRecord, DropType.APPEND);
    }
  }
  
  /**
   * Рекурсивное перемещение ветки в дереве.
   * 
   * @param oldPositionRecord запись перемещаемого узла
   * @param newPositionParentRecord родительская запись целевого узла
   * @param newDepth глубина целевого узла
   * @return ListTreeNode root новой ветки
   */
  private ListTreeNode changeNodePosition(JepRecord oldPositionRecord, JepRecord newPositionParentRecord, int newDepth){
    ListTreeNode oldPositionTreeNode = findNode(oldPositionRecord);
    if (!JepRiaUtil.isEmpty(oldPositionTreeNode)) {
      if (!JepRiaUtil.isEmpty(oldPositionTreeNode.children)) {
        List<JepRecord> saveChildren = oldPositionTreeNode.children;
        oldPositionTreeNode = new ListTreeNode(oldPositionRecord, newPositionParentRecord, newDepth+1);
        for(int i=0; i<saveChildren.size();i++){
          JepRecord childRecord = saveChildren.get(i);
          nodes.put(childRecord.get(primaryKeyName),
              changeNodePosition(childRecord, oldPositionRecord, newDepth+1));
        }
        oldPositionTreeNode.children = saveChildren;
      } else {
        oldPositionTreeNode = new ListTreeNode(oldPositionRecord, newPositionParentRecord, newDepth+1);
      }
    }
    return oldPositionTreeNode;
  }
  
  /**
   * Получение контекста по логической ссылке на узел
   * @param treeNode  логическая ссылка на узел
   * @return получение контекста узла
   */
  private Context getContextByTreeNode(ListTreeNode treeNode){
    return new Context(treeNode.getDepth(), widget.getColumnIndex(treeCellColumn), treeNode.getRecord());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void set(PagingResult<JepRecord> pagingResult) {
    super.set(pagingResult);
    // add info about existed nodes
    List<JepRecord> data = pagingResult.getData();
    for (JepRecord record : data){
      nodes.put(record.get(primaryKeyName), new ListTreeNode(record));
    }
  }
  
  /**
   * Получение имени поля в БД, соответствующего колонке древовидного справочника
   * @return наименование поля БД
   */
  public String getTreeCellDbName(){
    return treeCellColumn.getDataStoreName();
  }
  
  /**
   * Раскрывает отрисованные узлы и удаляет их из списка узлов, которые необходимо раскрыть 
   * {@link com.technology.jep.jepria.client.widget.field.multistate.JepTreeField#expandedValues}.
   */
  public void setExpanded(List<JepRecord> nodes, boolean expanded) {
    expandNodes = nodes;
    if (!JepRiaUtil.isEmpty(expandNodes)) {
      ListTreeNode node = this.findNode(expandNodes.get(0));
      expandNodes.remove(0);
      if (node != null) {
        if (node.getRecord().<Boolean> get(HAS_CHILDREN)) {
          this.setExpanded(node, expanded);
        }
      }
    }
  }
  
  /**
   * Раскрытие/закрытие записи грида
   *  
   * @param record    запись
   * @param expanded    флаг открытия/закрытия
   */
  public void setExpanded(JepRecord record, boolean expanded){
    if (JepRiaUtil.isEmpty(expandNodes)) {
      setExpanded(findNode(record), expanded);
    } else {
      expandNodes.add(record);
    }
  }
  
  /**
   * Раскрытие/закрытие узла грида
   *  
   * @param treeNode    узел грида
   * @param expanded    флаг открытия/закрытия
   */
  public void setExpanded(ListTreeNode treeNode, boolean expanded){
    if (treeNode == null) return;
    if (expanded){
      if (!treeNode.isOpen()){
        toggleChildren(getContextByTreeNode(treeNode));
      }
    } else {
      if (treeNode.isOpen()){
        toggleChildren(getContextByTreeNode(treeNode));
      }
    }
  }
}
