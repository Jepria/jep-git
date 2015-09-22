package com.technology.jep.jepriashowcase.supplier.client.ui.form.list;
 
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.SELECTED;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;

import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.message.ConfirmCallback;
import com.technology.jep.jepria.client.message.ConfirmMessageBox;
import com.technology.jep.jepria.client.message.MessageBox;
import com.technology.jep.jepria.client.message.PredefinedButton;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoDeleteEvent;
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
import com.technology.jep.jepria.client.ui.form.list.ListFormView;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.goods.client.GoodsClientFactoryImpl;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsServiceAsync;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;
public class SupplierListFormPresenter<V extends ListFormView, E extends PlainEventBus, S extends SupplierServiceAsync, F extends StandardClientFactory<E, S>> 
	extends ListFormPresenter<V, E, S, F> { 
 
	public SupplierListFormPresenter(Place place, F clientFactory) {
		super(place, clientFactory);
	}
	
	private boolean doubleConfirmed = false;
	
	@SuppressWarnings("unchecked")
	public void onDoDelete(DoDeleteEvent event) {
		doubleConfirmed = false;
		// Проверим состояние, чтобы обеспечить срабатывание данного обработчика только при активной списочной форме.
		if(SELECTED.equals(_workstate)) {
			
			final Set<JepRecord> records = list.getSelectionModel().getSelectedSet();
			MessageBox box = messageBox.confirmDeletion(records.size() > 1, new ConfirmCallback() {
				public void onConfirm(Boolean yes) {
					if(yes) {
						onDeleteConfirmation(records);
					}
				}
			});
			
			box.addButtonClickHandler(PredefinedButton.YES, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					for (JepRecord record : records){
						GoodsServiceAsync goodsService = (GoodsServiceAsync) GoodsClientFactoryImpl.getInstance().getService();
						goodsService.find(new PagingConfig(record), new JepAsyncCallback<PagingResult<JepRecord>>() {
							@Override
							public void onSuccess(PagingResult<JepRecord> result) {
								if (result.getSize() > 0){
									new ConfirmMessageBox(JepTexts.deletion_dialog_title(), supplierText.supplier_has_goods_alt(), new ConfirmCallback() {
										@Override
										public void onConfirm(Boolean result) {
											if (result){
												doubleConfirmed = true;
												onDeleteConfirmation(records);
											}
										}
									}).show();
								}
								else {
									doubleConfirmed = true;
									onDeleteConfirmation(records);
								}
							}
						});
					}
				}
			});
		}
		
	}
	
	@Override
	protected void onDeleteConfirmation(Set<JepRecord> records) {
		if (doubleConfirmed){
			super.onDeleteConfirmation(records);
		}
	}
}
