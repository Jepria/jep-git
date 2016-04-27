package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar;
 
<#assign hasToolBarCustomButton=form.toolBarCustomButtons?size != 0>
<#list form.toolBarCustomButtons as button>
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar.${form.formName}ToolBarView.${button.buttonId?upper_case};
</#list>
import com.google.gwt.place.shared.Place;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.*;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarView.*;
import static com.technology.jep.jepria.client.ui.toolbar.ToolBarConstant.*;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactory;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarView;
import com.technology.jep.jepria.client.ui.toolbar.ToolBarPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
<#if hasToolBarCustomButton>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.${form.formName}EventBus;
<#else>
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
</#if>
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
<#if form.isToolBarOff>
import com.technology.jep.jepria.client.history.place.*;
</#if>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}ServiceAsync;
 
public class ${form.formName}ToolBarPresenter<V extends ToolBarView, E extends <#if hasToolBarCustomButton>${form.formName}<#else>Plain</#if>EventBus, S extends ${form.formName}ServiceAsync, F extends StandardClientFactory<E, S>>
	extends ToolBarPresenter<V, E, S, F> {
 
 	public ${form.formName}ToolBarPresenter(Place place, F clientFactory) {
		super(place, clientFactory);
	}
 
 	<#if !form.isToolBarOff && !form.hasToolBarPresenter??>
	public void bind() {
		<#list form.toolBarButtons as button>
		<#if !button.isSeparator>
		bindButton(
			${button.buttonId}, 
			new WorkstateEnum[]{${button.workStatesAsString}},
			new ClickHandler() {
				public void onClick(ClickEvent event) {
					${button.handler}
				}
			});
		</#if>
 		</#list>
	}
	</#if>
}
