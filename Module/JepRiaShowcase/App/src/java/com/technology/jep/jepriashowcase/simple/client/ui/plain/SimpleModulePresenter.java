package com.technology.jep.jepriashowcase.simple.client.ui.plain;

import com.allen_sauer.gwt.log.client.Log;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.SEARCH;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.SUPPLIER_MODULE_ID;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.URL_CUSTOM_MODULE;
import static com.technology.jep.jepriashowcase.simple.client.SimpleClientConstant.simpleText;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Window;
import com.technology.jep.jepria.client.message.ErrorDialog;
import com.technology.jep.jepria.client.history.scope.JepScope;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainModulePresenter;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;


public class SimpleModulePresenter<V 
	extends SimpleModuleView, E extends PlainEventBus, S extends JepDataServiceAsync, F extends PlainClientFactory<E, S>> 
	extends PlainModulePresenter<V, E, S, F> {
	
	public SimpleModulePresenter(String moduleId, Place place, F clientFactory) {
		super(moduleId, place, clientFactory);
	}
	
	protected void bind() {
		super.bind();
		
		view.addSupplierButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				JepScope enterModuleScope = new JepScope(new String[] {SUPPLIER_MODULE_ID}, new WorkstateEnum[] {SEARCH});
				JepScopeStack.instance.clear();
				JepScopeStack.instance.push(enterModuleScope);
				clientFactory.getMainClientFactory().getEventBus().enterModule(SUPPLIER_MODULE_ID);
			}
		});

		view.addErrorButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ErrorDialog errorDialog = new ErrorDialog(simpleText.simple_errorDialog_title(), new Throwable(simpleText.simple_errorDialog_throwableMessage()), 
						simpleText.simple_errorDialog_message());
				errorDialog.show();
			}
		});
		
		view.addCustomModuleClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.Location.assign(URL_CUSTOM_MODULE);
			}
		});
	}
	
	/**
	 * Установка главного виджета(-контейнера) приложения.<br/>
	 * В методе используется вызов вида : <code>mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());</code> <br/>
	 * При этом, при передаче <code>null</code> в качестве главного виджета приложения, текущий главный виджет удаляется с RootPanel'и.<br/>
	 * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой главный виджет приложения или скрыть текущий.
	 */
	protected void setMainView() {
		Log.trace(this.getClass() + ".setMainView()");
		mainEventBus.setMainView(view.asWidget());
	}
}

