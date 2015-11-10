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
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.async.DataLoader;
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
	 * Инициализируем названия ключей для работы с деревом по recordDefinition. <br>
	 * Для нестандартных случаев, есть set-методы
	 * @param recordDefinition
	 */
	public void initKeysFromRecordDefinition(JepRecordDefinition recordDefinition){
		// Обрабатываются только "простые" ключи
		primaryKeyName = recordDefinition.getPrimaryKey()[0];
	}
	
	/**
	 * Устанавливает загрузчик данных списка
	 * @param loader загрузчик данных списка
	 */
	public void setLoader(DataLoader<JepRecord> loader){
		this.loader = loader;
	}
		
	/**
	 * Инициализация. <br/> 
	 * Выставляет привязку в {@link com.technology.jep.jepria.client.widget.list.cell.TreeCell} к {@link com.technology.jep.jepria.client.widget.list.TreeGridManager}
	 */
	public void bindTree() {
		for (int i = 0; i < widget.getColumnCount(); i++){
			// пробегаем по всем колонкам древовидного справочника
			Cell<?> cell = widget.getColumn(i).getCell();
			if (cell instanceof HasTreeGridManager){
				treeCellColumn = (JepColumn<JepRecord, ?>) widget.getColumn(i);
				((HasTreeGridManager) cell).setTreeGridManager(this);
				break;
			}
		}
	}
	
	/**
	 * Поиск узла, соответствующего записи
	 * 
	 * @param record		искомая запись
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
		if (!JepRiaUtil.isEmpty(fakeNodes)){
			fakeRecordsForParent = new ArrayList<JepRecord>(fakeNodes.size());
			for (ListTreeNode fakeTreeNode : fakeNodes){
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
		if(!hasChildren) return;

		final Object primaryKey = rowRecord.get(primaryKeyName);		
		ListTreeNode currentNode = findByPrimaryKey(primaryKey);
		
		//Узла нет в кеше, то это первый клик по строке
		if(currentNode == null){
			currentNode = new ListTreeNode(rowRecord);
			nodes.put(primaryKey, currentNode);
		}

		//Детей нет в кеше, то осуществляется поиск
		if(currentNode.children == null){
			
			final ListTreeNode parentNode = currentNode;
			mask(JepTexts.loadingPanel_dataLoading());
			loader.load(new PagingConfig(rowRecord), new AsyncCallback<List<JepRecord>>() {
				
				@Override
				public void onSuccess(List<JepRecord> subList) {
					
					parentNode.children = subList;
					
					for(JepRecord record: subList){
						nodes.put(record.get(primaryKeyName), new ListTreeNode(record, rowRecord, parentNode.getDepth() + 1));
					}
					
					setChildrenInList(rowRecord, subList);

					parentNode.open();
					unmask(); // Скроем индикатор "Загрузка данных...".
					
					widget.redraw();
				}
				
				public void onFailure(Throwable caught) {
					unmask(); // Скроем индикатор "Загрузка данных...".
				}
			});

		}else{
			
			if(currentNode.isOpen()){
				
				//Удаляется поддерево у текущей записи
				removeBranchFromList(currentNode.children);
			}else{
				
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
		if(cacheNode != null && cacheNode.isOpen()){
			removeBranchFromList(cacheNode.children);
		}
	}
		
	/**
	 * Динамическое добавление узла на древовидный справочник
	 * 
	 * @param parentRecord		родительский узел
	 * @param isLeaf			признак листового узла
	 */
	public void addChildrenNode(JepRecord parentRecord, boolean isLeaf) {
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
		}
		else {
			fakeUidMap.put(primaryKey, new ArrayList<ListTreeNode>(Collections.singletonList(treeNode)));
		}
		// раскроем узел, если он не раскрыт
		if (cacheNode == null || !cacheNode.isOpen()){
			setExpanded(cacheNode, true);
		}
		else {
			cacheNode.children.add(record);
			setChildrenInList(parentRecord, new ArrayList<JepRecord>(Collections.singletonList(record)));
		}
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
	 * @param primaryKey	значение ключа
	 * @return список "fake"-узлов дерева
	 */
	public List<ListTreeNode> getFakeNodesByParentId(Object primaryKey){
		return fakeUidMap.get(primaryKey);
	}
	
	/**
	 * {@inheritDoc}
	 * Особенности реализации:<br/>
	 * Перед заменой строк древовидного справочника перетаскиваемые узлы схлопываются, если они ранее были раскрыты.
	 */
	@Override
	public void changeRows(int oldIndex, int newIndex, boolean isAbove) {
		JepRecord oldRecord = get(oldIndex);
		JepRecord newRecord = get(newIndex);
		
		ListTreeNode oldTreeNode = findNode(oldRecord), newTreeNode = findNode(newRecord);
		if (oldTreeNode.isOpen()){
			toggleChildren(getContextByTreeNode(oldTreeNode));
		}
		if (newTreeNode.isOpen()){
			toggleChildren(getContextByTreeNode(newTreeNode));
		}
		oldIndex = dataProvider.getList().indexOf(oldRecord);
		newIndex = dataProvider.getList().indexOf(newRecord);
		
		int newDepth = newTreeNode.getDepth();
		
		JepRecord oldParentRecord = oldTreeNode.getParentRecord();
		JepRecord newParentRecord = newTreeNode.getParentRecord();
		
		ListTreeNode oldParentTreeNode = null, newParentTreeNode = null;
		if (oldParentRecord != null) {
			oldParentTreeNode = findNode(oldParentRecord);
			oldParentTreeNode.children.remove(oldRecord);
		}
		if (newParentRecord != null) {
			newParentTreeNode = findNode(newParentRecord);
			int indexOfNewNode = newParentTreeNode.children.indexOf(newRecord);
			newParentTreeNode.children.add(isAbove ? indexOfNewNode - 1 : indexOfNewNode, oldRecord);
		}
		
		oldTreeNode = new ListTreeNode(oldRecord, newParentRecord, newDepth);
		nodes.put(oldRecord.get(primaryKeyName), oldTreeNode);
		
		super.changeRows(oldIndex, newIndex, isAbove);
	}
	
	/**
	 * Получение контекста по логической ссылке на узел
	 * @param treeNode	логическая ссылка на узел
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
	public void setExpanded(List<JepRecord> nodes, boolean expanded){
		if(nodes != null && nodes.size() > 0) {
			List<JepRecord> expandedValues = new ArrayList<JepRecord>(nodes);
			Iterator<JepRecord> iterator = expandedValues.iterator();
			while(iterator.hasNext()) {
				JepRecord record = iterator.next();
				// Удаляем значение, т.к. открытие узлов - это разовая (в данном случае) операция
				// и НЕ нужно повторно открывать указанные узлы (которые пользователь, возможно, уже закрыл).
				setExpanded(record, expanded);
				iterator.remove();
			}
		}
	}
	
	/**
	 * Раскрытие/закрытие записи грида
	 *  
	 * @param record		запись
	 * @param expanded		флаг открытия/закрытия
	 */
	public void setExpanded(JepRecord record, boolean expanded){
		setExpanded(findNode(record), expanded);
	}
	
	/**
	 * Раскрытие/закрытие узла грида
	 *  
	 * @param treeNode		узел грида
	 * @param expanded		флаг открытия/закрытия
	 */
	public void setExpanded(ListTreeNode treeNode, boolean expanded){
		if (treeNode == null) return;
		if (expanded){
			if (!treeNode.isOpen()){
				toggleChildren(getContextByTreeNode(treeNode));
			}
		}
		else {
			if (treeNode.isOpen()){
				toggleChildren(getContextByTreeNode(treeNode));
			}
		}
	}
}
