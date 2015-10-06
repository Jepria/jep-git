package com.technology.jep.jepriashowcase.supplier.client.ui.form.detail;
 
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.VIEW_DETAILS;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.scopeModuleIds;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.ADD_DOWN_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.ADD_RIGHT_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.NEXT_BUTTON_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.PREVIOUS_BUTTON_ID;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.message.ConfirmCallback;
import com.technology.jep.jepria.client.message.ConfirmMessageBox;
import com.technology.jep.jepria.client.message.MessageBox;
import com.technology.jep.jepria.client.message.PredefinedButton;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.eventbus.plain.event.DoDeleteEvent;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormPresenter;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.wizard.Block;
import com.technology.jep.jepria.client.ui.wizard.BlockContainer;
import com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum;
import com.technology.jep.jepria.client.widget.field.multistate.JepMoneyField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.goods.client.GoodsClientFactoryImpl;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsServiceAsync;
import com.technology.jep.jepriashowcase.supplier.shared.service.SupplierServiceAsync;
 
public class SupplierDetailFormPresenter<E extends PlainEventBus, S extends SupplierServiceAsync> 
		extends DetailFormPresenter<SupplierDetailFormView, E, S, StandardClientFactory<E, S>> { 
 
	public SupplierDetailFormPresenter(Place place, StandardClientFactory<E, S> clientFactory) {
		super(scopeModuleIds, place, clientFactory);
	}
	
	private BlockContainer wizardBlock = view.getBlockContainer();
	
	public void bind(){
		super.bind();
		
		view.addButtonClickHandler(PREVIOUS_BUTTON_ID, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wizardBlock.goToPreviousBlock();
				view.initButtonProperties();
			}
		});
		
		view.addButtonClickHandler(NEXT_BUTTON_ID, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				wizardBlock.goToNextBlock();
				view.initButtonProperties();
			}
		});
		
		view.addButtonClickHandler(ADD_DOWN_BUTTON_ID, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				VerticalPanel panel = new VerticalPanel();
				JepTextField textField = new JepTextField("Down Text Field");
				panel.add(textField);
				JepMoneyField moneyField = new JepMoneyField("Down Money Field");
				panel.add(moneyField);
				Block rightBlock = new Block(panel, BlockPositionEnum.DOWN);
				rightBlock.setCaptionText("Down");
				wizardBlock.addBlock(rightBlock);
			}
		});
		
		view.addButtonClickHandler(ADD_RIGHT_BUTTON_ID, new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				VerticalPanel panel = new VerticalPanel();
				JepTextField textField = new JepTextField("Right Text Field");
				panel.add(textField);
				JepMoneyField moneyField = new JepMoneyField("Right Money Field");
				panel.add(moneyField);
				Block rightBlock = new Block(panel, BlockPositionEnum.RIGHT);
				
				rightBlock.setCaptionText("Right");
				wizardBlock.addBlock(rightBlock);
			}
		});
		
	}
  
	protected void changeWorkstate(WorkstateEnum workstate){
		super.changeWorkstate(workstate);
		
		wizardBlock.getBlockManager().changeWorkstate(workstate);
	}
	
	private boolean doubleConfirmed = false;
	
	public void onDoDelete(DoDeleteEvent event) {
		doubleConfirmed = false;
		// Проверим состояние, чтобы обеспечить срабатывание данного обработчика только при активной детальной форме.
		if(VIEW_DETAILS.equals(_workstate) || EDIT.equals(_workstate)) {
			MessageBox box = messageBox.confirmDeletion(false, new ConfirmCallback() {
				public void onConfirm(Boolean yes) {
					onDeleteConfirmation(yes, currentRecord);
				}
			});
			
			box.addButtonClickHandler(PredefinedButton.YES, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					GoodsServiceAsync goodsService = (GoodsServiceAsync) GoodsClientFactoryImpl.getInstance().getService();
					goodsService.find(new PagingConfig(currentRecord), new JepAsyncCallback<PagingResult<JepRecord>>() {
						@Override
						public void onSuccess(PagingResult<JepRecord> result) {
							if (result.getSize() > 0){
								new ConfirmMessageBox(JepTexts.deletion_dialog_title(), supplierText.supplier_has_goods_alt(), new ConfirmCallback() {
									@Override
									public void onConfirm(Boolean result) {
										if (result){
											doubleConfirmed = true;
											onDeleteConfirmation(true, currentRecord);
										}
									}
								}).show();
							}
							else {
								doubleConfirmed = true;
								onDeleteConfirmation(true, currentRecord);
							}
						}
					});
				}
			});
		}
	}
 
	@Override
	protected void onDeleteConfirmation(Boolean yes, final JepRecord record) {
		if (doubleConfirmed){
			super.onDeleteConfirmation(yes, record);
		}
	}
}
