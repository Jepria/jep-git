package com.technology.jep.jepriashowcase.goods.client.ui.form.detail;
 
import static com.technology.jep.jepria.client.ui.WorkstateEnum.CREATE;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepriashowcase.goods.client.GoodsClientConstant.scopeModuleIds;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.DESCENDANT_GOODS_LINK;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST_FOR_CHECKED;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST_FOR_EXPAND;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_LINK;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PHOTO;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PORTFOLIO;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_SEGMENT_CODE_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_TYPE_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.MOTIVATION_TYPE_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.PURCHASING_PRICE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.UNIT_CODE;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.client.async.DataLoader;
import com.technology.jep.jepria.client.async.FirstTimeUseAsyncCallback;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.event.JepListener;
import com.technology.jep.jepria.client.widget.field.multistate.JepTreeField;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsServiceAsync;
 
public class GoodsDetailFormPresenter<E extends PlainEventBus, S extends GoodsServiceAsync> 
		extends DetailFormPresenter<GoodsDetailFormView, E, S, StandardClientFactory<E, S>> { 
 
	public GoodsDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
		super(scopeModuleIds, place, clientFactory);
	}
	
	public void bind() {
		super.bind();
		// Здесь размещается код связывания presenter-а и view
		
		fields.get(GOODS_TYPE_CODE).addListener(JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
			@Override
			public void handleEvent(final JepEvent event) {
				service.getGoodsType(new FirstTimeUseAsyncCallback<List<JepOption>>(event) {
					public void onSuccessLoad(List<JepOption> result){
						fields.setFieldOptions(GOODS_TYPE_CODE, result);
					}
				});
			}
		});
		
		fields.get(UNIT_CODE).addListener(JepEventType.FIRST_TIME_USE_EVENT, new JepListener() {
			@Override
			public void handleEvent(final JepEvent event) {
				service.getUnit(new FirstTimeUseAsyncCallback<List<JepOption>>(event) {
					public void onSuccessLoad(List<JepOption> result){
						fields.setFieldOptions(UNIT_CODE, result);
					}
				});
			}
		});
		
		service.getMotivationType(new JepAsyncCallback<List<JepOption>>() {
			public void onSuccess(List<JepOption> result){
				fields.setFieldOptions(MOTIVATION_TYPE_CODE, result);
			}
		});
		
		service.getGoodsSegment(new JepAsyncCallback<List<JepOption>>() {
			public void onSuccess(List<JepOption> result){
				fields.setFieldOptions(GOODS_SEGMENT_CODE_LIST, result);
			}
		});

		final JepTreeField treeField = (JepTreeField) fields.get(GOODS_CATALOG_ID_LIST);
        
        treeField.setLoader(new DataLoader<JepOption>() {
      		public void load(final Object loadConfig, final AsyncCallback<List<JepOption>> callback) {
      			Integer goodsId = null;
      			if (EDIT.equals(_workstate) || VIEW_DETAILS.equals(_workstate)){
      				goodsId = currentRecord.get(GOODS_ID);
      			}
      			final Integer id = goodsId;
      			if (!JepRiaUtil.isEmpty(id)){
      				treeField.setLoadingImage(true);
      			}
           		service.getGoodsCatalog(JepOption.<Integer>getValue(loadConfig), goodsId, new JepAsyncCallback<List<JepOption>>() {
					@Override
					public void onSuccess(List<JepOption> result) {
						if (!JepRiaUtil.isEmpty(id)){
							treeField.setLoadingImage(false);
		      			}
						callback.onSuccess(filterizedOptions(result));
						if (result.size() > 0) {
							final List<JepOption> checkedNodes = new ArrayList<JepOption>();
							final List<JepOption> expandedNodes = new ArrayList<JepOption>();
							
							for (JepOption treeOption : result){
								if (Boolean.TRUE.equals(treeOption.get(GOODS_LINK))){
									checkedNodes.add(treeOption);
								}
								if (Boolean.TRUE.equals(treeOption.get(DESCENDANT_GOODS_LINK))){
									expandedNodes.add(treeOption);
								}
							}
							
							if (!JepRiaUtil.isEmpty(expandedNodes)) {
								treeField.setExpanded(filterizedOptions(expandedNodes));
							}
							if (!JepRiaUtil.isEmpty(checkedNodes)) {
								treeField.setValue(filterizedOptions(checkedNodes));
							}
						}
					}
					@Override
					public void onFailure(Throwable th){
						callback.onFailure(th);
					}
				});
      		}
        });
	}
 
	@SuppressWarnings("unchecked")
	protected void adjustToWorkstate(WorkstateEnum workstate) {
		if (EDIT.equals(workstate) || VIEW_DETAILS.equals(workstate)){
			JepTreeField treeField = (JepTreeField) fields.get(GOODS_CATALOG_ID_LIST);
			treeField.setEditable(false);
			treeField.clear();
			List<JepOption> expandedValues = (List<JepOption>) currentRecord.get(GOODS_CATALOG_ID_LIST_FOR_EXPAND);
			List<JepOption> checkedValues = (List<JepOption>) currentRecord.get(GOODS_CATALOG_ID_LIST_FOR_CHECKED);
			if (!JepRiaUtil.isEmpty(expandedValues)) {
				// раскроем узлы дерева
				treeField.setExpanded(filterizedOptions(expandedValues));
			}
			if (!JepRiaUtil.isEmpty(checkedValues)) {
				// проставим узлы дерева
				treeField.setValue(filterizedOptions(checkedValues));
			}
		}
		
		fields.setFieldVisible(UNIT_CODE, !SEARCH.equals(workstate));
		fields.setFieldVisible(MOTIVATION_TYPE_CODE, !SEARCH.equals(workstate));
		fields.setFieldVisible(PURCHASING_PRICE, !SEARCH.equals(workstate));
		fields.setFieldVisible(GOODS_SEGMENT_CODE_LIST, SEARCH.equals(workstate));
		
		fields.setFieldAllowBlank(GOODS_NAME, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
		fields.setFieldAllowBlank(GOODS_TYPE_CODE, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
		fields.setFieldAllowBlank(UNIT_CODE, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
		fields.setFieldAllowBlank(PURCHASING_PRICE, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
		fields.setFieldAllowBlank(MOTIVATION_TYPE_CODE, !(CREATE.equals(workstate) || EDIT.equals(workstate)));
		
		fields.setFieldVisible(GOODS_PHOTO, !SEARCH.equals(workstate));
		fields.setFieldVisible(GOODS_PORTFOLIO, !SEARCH.equals(workstate));
	}
	
	public JepOption filterizedOption(JepOption option){
		return option instanceof JepParentOption ? new JepParentOption(option.getName(), (Integer) option.getValue()) : new JepOption(option.getName(), option.getValue()); 
	}
	
	public List<JepOption> filterizedOptions(List<JepOption> options){
		Set<JepOption> result = new LinkedHashSet<JepOption>();
		for (JepOption opt : options){
			result.add(filterizedOption(opt));
		}
		return new ArrayList<JepOption>(result);
	}
}
