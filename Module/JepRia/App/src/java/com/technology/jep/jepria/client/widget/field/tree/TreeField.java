package com.technology.jep.jepria.client.widget.field.tree;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
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
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.CheckChangeHandler;
import com.technology.jep.jepria.client.widget.field.multistate.event.CheckChangeEvent.HasCheckChangeHandlers;
import com.technology.jep.jepria.client.widget.field.tree.images.TreeFieldResources;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

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

	
	private CellTree tree;
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
	
	
	private static final String JEP_RIA_TREE_FIELD_STYLE = "jepRia-TreeField-Input";
	
	private Map<V, TreeNodeInfo<V>> nodeMapOfDisplay = new HashMap<V, TreeNodeInfo<V>>();
	
	/* Resources: texts and images */
	private TreeFieldMessages messages = new TreeFieldMessages();
	private TreeFieldResources images = GWT.create(TreeFieldResources.class);
	
	private CheckNodes checkNodes = CheckNodes.BOTH;
	private CheckCascade checkStyle = CheckCascade.NONE;
	
	private SetSelectionModel<V> selectionModel;
	
	private boolean checkable = true;
	
	public TreeField(){
		addStyleName(JEP_RIA_TREE_FIELD_STYLE);
	}
	
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
				TreeNodeInfo<V> info = nodeMapOfDisplay.get(node.getValue());
				if (JepRiaUtil.isEmpty(info)) return;
				
				V currentNode = (V) node.getValue();
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

	public List<V> getCheckedSelection() {
		return new ArrayList<V>(selectionModel.getSelectedSet());
	}
	
	public void setPartialSelected(List<V> list){
		if (!JepRiaUtil.isEmpty(list) && !list.isEmpty()){
			this.partialSelectedNodes = list;
			for (V node : list){
				refreshNode(node);
			}
		}
	}
	
	public void clearSelection(){
		// очищаем логический список частично выделенных узлов
		partialSelectedNodes.clear();
		// также визуально убираем галочки на карте редактирования
		for (V option : getCheckedSelection()) {
			setChecked(option, false);
		}
	}

	public HandlerRegistration addCheckChangeHandler(CheckChangeHandler<V> handler) {
		return addHandler(handler, CheckChangeEvent.getType());
	}
	
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
	
	public boolean isNodeOpened(V value){
		return !JepRiaUtil.isEmpty(getChildrenNodes(value)) && isNodeOpened(tree.getRootTreeNode(), value);
	}
	
	public boolean isLeaf(Object value) {
		return JepRiaUtil.isEmpty(value) ? false : !(value instanceof JepParentOption);
	}
	
	public void showTree(){
		if (JepRiaUtil.isEmpty(getWidget())) {
			setWidget(tree);
			getElement().getStyle().setProperty("background", "none");
		}
	}
	
	public boolean isSelected(V value){
		return selectionModel.isSelected(value);
	}
	
	public void setChecked(V value, boolean checked){
		selectionModel.setSelected(value, checked);
	}
	
	/**
	 * Search specified node in the tree. Expand or collapse it. And return founded node or null if it's leaf. 
	 * 
	 * @param node				relative node
	 * @param value				specified value
	 * @param expand			flag to expand or collapse this node
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
	
	public void collapseAll(){
		setExpanded(null, false);
	}
	
	/**
	 * Refresh state of node with specified value
	 * 
	 * @param node		value of this node
	 */
	@SuppressWarnings("unchecked")
	public void refreshNode(V node){
		((TreeModel) tree.getTreeViewModel()).refreshNode(node);
	}
	
	public void setCheckNodes(CheckNodes checkNodes) {
		this.checkNodes = checkNodes;
	}

	public void setCheckStyle(CheckCascade checkStyle) {
		this.checkStyle = checkStyle;
	}
	
	public void setCheckable(boolean checkable){
		this.checkable = checkable;
	}
	
	public void setBorders(boolean borders){
		getElement().getStyle().setProperty("border", borders ? "1px solid #ccc" : "none");
	}
	
	public List<V> getChildrenNodes(V node){
		TreeNodeInfo<V> info = getNodeInfoByValue(node);
		return JepRiaUtil.isEmpty(info) ? null : info.getData();
	}
	
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
	
	class TreeModel implements TreeViewModel {
		
		private TreeDataProvider provider;
		
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
		
		@SuppressWarnings("unchecked")
		public <T> NodeInfo<?> getNodeInfo(T value) {
			provider = new TreeDataProvider((V) value);
			
			// Create a list of cell. These cells will make up the composite cell
			// Here I am constructing a composite cell with 2 parts that includes a checkbox.
			final List<HasCell<V, ?>> cellComponents = new ArrayList<HasCell<V, ?>>();
			
			// 1st part of Composite cell - Show a checkbox image and select it "selected property is true
			cellComponents.add(new HasCell<V, ImageResource>() {
			
				ImageResourceCell cell = new ImageResourceCell();
				
				public Cell<ImageResource> getCell() {
					return cell;
				}
			
				public FieldUpdater<V, ImageResource> getFieldUpdater() {
					return null;
				}
				
				public ImageResource getValue(V value) {
					boolean isLeaf = isLeaf(value);
					switch(checkNodes){
						// допустимо выделение только листьев, не отображаем соответствующую картинку
						case LEAF : { 
							if(!isLeaf) return null; 
							break; 
						}
						// допустимо выделение только родительских узлов, не отображаем соответствующую картинку
						case PARENT : { 
							if(isLeaf) return null; 
							break; 
						}
					}
					
					if (partialSelectedNodes.contains(value)){
						return images.partialChecked();
					}
					
					if (isNodeOpened(value) && checkStyle.equals(CheckCascade.PARENTS)) {
						int in = hasPartlySelectedChildren(value);
						switch(in){
							case 1: return images.checked();
							case 0: return images.partialChecked();
							case -1: 
							default: return images.unchecked();
						}
						
					} else {
						return isSelected(value) ? images.checked() : images.unchecked();
					}
				}
				
				public int hasPartlySelectedChildren(V value){
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
			});
			
			// 2nd part of Composite cell - Show a folder image for parent nodes
			cellComponents.add(new HasCell<V, ImageResource>() {
				
				ImageResourceCell cell = new ImageResourceCell();
				
				public Cell<ImageResource> getCell() {
					return cell;
				}
				
				public FieldUpdater<V, ImageResource> getFieldUpdater() {
					return null;
				}
				
				public ImageResource getValue(V object) {
					return isLeaf(object) ? null : isNodeOpened(object) ? images.folderOpened() : images.folderClosed();
				}
			});
			
			// 3rd part of Composite cell - Show Text for the Cell
			cellComponents.add(new HasCell<V, String>() {
				private static final String PADDING = "   "; 
				
				ClickableTextCell txtCell = new ClickableTextCell(){
					@Override
					public void render(Context context, SafeHtml value, SafeHtmlBuilder sb) {
						if (!JepRiaUtil.isEmpty(value)) {
							sb.appendEscaped(PADDING).append(value);
						}
					}
				};
				
				public Cell<String> getCell() {
					return txtCell;
				}
				
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
					Cell<X> cell = hasCell.getCell();
					X val = hasCell.getValue(value);
					sb.appendHtmlConstant("<span " + (cell instanceof ClickableTextCell ? "title=\"" + SafeHtmlUtils.htmlEscape((String) val) + "\"" : "") + ">");
					cell.render(context, val, sb);
					sb.appendHtmlConstant("</span>");
				}				
			};
			return new DefaultNodeInfo<V>(provider, compositeCell, selectionModel, selectionManager, null);
		}

		public boolean isLeaf(Object value) {
			return TreeField.this.isLeaf(value);
		}
		
		public void refreshNode(V value){
			provider.refreshNode(value);
		}
	}
	
	/**
	 * A custom {@link AsyncDataProvider}.
	 */
	class TreeDataProvider extends AsyncDataProvider<V> {
		
		private V expandNode;
		
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
		
		public void refreshDisplay(HasData<V> display, List<V> data){
			display.setRowData(display.getVisibleRange().getStart(), data);
			display.setRowCount(data.size(), true);
		}
		
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
