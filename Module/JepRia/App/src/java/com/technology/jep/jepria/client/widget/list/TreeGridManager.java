package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.SetSelectionModel;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.widget.list.cell.ListTreeNode;
import com.technology.jep.jepria.client.widget.list.cell.TreeCell;
import com.technology.jep.jepria.client.widget.toolbar.PagingToolBar;
import com.technology.jep.jepria.shared.field.TreeCellNames;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс, управляющий древовидной структурой на списочной форме 
 */
public class TreeGridManager<W extends AbstractCellTable<JepRecord>, P extends PagingToolBar, S extends SetSelectionModel<JepRecord>> extends
	GridManager<W, P, S> {
	
	/**
	 * Коллекция содержит узлы дерева (primaryKey связывается с узлом дерева) 
	 */
	private Map<Object, ListTreeNode> nodes = new HashMap<Object, ListTreeNode>();
	
	/**
	 * Наименование, по которому из record берется значение первичного ключа
	 */
	private String primaryKeyName;
	
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
	 * Наименование, по которому из record берется значение ключа родителя
	 */
	private String parentKeyName;
	
	/**
	 * Устанавливает наименовение для ключа родителя
	 * @param parentKeyName наименование для ключа родителя
	 */
	public void setParentKeyName(String parentKeyName){
		this.parentKeyName = parentKeyName;
	}
	
	/**
	 * Получает наименовение для ключа родителя
	 * @return наименование для ключа родителя
	 */
	public String getParentKeyName(){
		return parentKeyName;
	}

	
	/**
	 * Инициализируем названия ключей для работы с деревом по recordDefinition. <br>
	 * Для нестандартных случаев, есть set-методы
	 * @param recordDefinition
	 */
	public void initKeysFromRecordDefinition(JepRecordDefinition recordDefinition){
		
		//Обрабатываются только "простые" ключи
		primaryKeyName = recordDefinition.getPrimaryKey()[0];
		parentKeyName = TreeCellNames.PARENT_PREFFIX+primaryKeyName;
	}
	
	/**
	 * Загрузчик данных списка
	 */
	private DataLoader<JepRecord> loader;
	
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
		
		TreeCell treeCell = (TreeCell) widget.getColumn(0).getCell(); 
		treeCell.setTreeGridManager(this);
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
		
		int index = dataProvider.getList().indexOf(record);
		setChildrenInList(index, data);
	}
	
	/**
	 * Добавляет поддерево в список по индексу
	 * @param index индекс, после которого добавить поддерево
	 * @param data поддерево
	 */
	public void setChildrenInList(int index, List<JepRecord> data){
		
		dataProvider.getList().addAll(index + 1, data);
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
			if((Boolean) item.get(TreeCellNames.HAS_CHILDREN)){
				
				Object itemPrimaryKey = item.get(primaryKeyName);
				nodes.get(itemPrimaryKey).close();
				
				removeBranchFromList(nodes.get(itemPrimaryKey).children);
			}
		}
		
		dataProvider.getList().removeAll(removeNodes);
	}

	/**
	 * Обработка раскрытия/скрытия дерева. <br>
	 * Также запросы кешируются, очистка кеша происходит вместе с очисткой списка
	 * @param context контекст строки, по которой был осуществлен клик
	 */
	public void toogleChildren(final Context context) {
		
		final JepRecord rowRecord = (JepRecord) context.getKey();		
		Boolean hasChildren = (Boolean) (JepRiaUtil.isEmpty(rowRecord.get(TreeCellNames.HAS_CHILDREN)) ? false : rowRecord.get(TreeCellNames.HAS_CHILDREN));
		
		//Листовой узел
		if(!hasChildren)
			return;

		final Object primaryKey = (Object) rowRecord.get(primaryKeyName);		
		ListTreeNode currentNode = nodes.get(primaryKey);
		
		//Узла нет в кеше, то это первый клик по строке
		if(currentNode == null){
			currentNode = new ListTreeNode();
			nodes.put(primaryKey, currentNode);
		}

		//Детей нет в кеше, то осуществляется поиск
		if(currentNode.children == null){
			
			JepRecord searchChildren = new JepRecord();
			searchChildren.set(parentKeyName, primaryKey);

			final ListTreeNode parentNode = currentNode;
			mask(JepTexts.loadingPanel_dataLoading());
			loader.load(new PagingConfig(searchChildren), new AsyncCallback<List<JepRecord>>() {
				
				@Override
				public void onSuccess(List<JepRecord> subList) {

					parentNode.children = subList;
					
					for(JepRecord record: subList){
						nodes.put(record.get(primaryKeyName), new ListTreeNode(parentNode.getDepth() + 1));
					}
					
					setChildrenInList(context.getIndex(), subList);

					parentNode.open();
					unmask(); // Скроем индикатор "Загрузка данных...".
					
					widget.redraw();
				}
				
				public void onFailure(Throwable caught) {
					unmask(); // Скроем индикатор "Загрузка данных...".
				}
			});

		}else{
			
			if(currentNode.getIsOpen()){
				
				//Удаляется поддерево у текущей записи
				removeBranchFromList(currentNode.children);
			}else{
				
				setChildrenInList(context.getIndex(), currentNode.children);
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
		
		Object primaryKey = (Object) record.get(primaryKeyName);
		ListTreeNode cacheNode = findByPrimaryKey(primaryKey);
		
		//Если поддерево открыто, то удаляет и его
		if(
			cacheNode != null
			&& cacheNode.getIsOpen()
		){
			
			removeBranchFromList(cacheNode.children);
		}
	}
}
