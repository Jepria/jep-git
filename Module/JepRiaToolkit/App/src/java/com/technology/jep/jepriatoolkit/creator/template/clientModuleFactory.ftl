package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
 
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
<#assign hasCustomButtons = (form.toolBarCustomButtons?size != 0)>
<#if hasCustomButtons>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.eventbus.${form.formName}EventBus;
</#if>
<#if form.toolBarCustomButtonsOnBothForms?size != 0>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.plain.${form.formName}ModulePresenter;
</#if>
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
<#if !form.isJepToolBarPresenter>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar.${form.formName}ToolBarPresenter;
</#if>
<#if !form.isJepToolBarView>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar.${form.formName}ToolBarViewImpl;
</#if>
<#if form.isStatusBarOff>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.statusbar.${form.formName}StatusBarViewImpl;
</#if>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.detail.${form.formName}DetailFormPresenter;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.detail.${form.formName}DetailFormViewImpl;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.list.${form.formName}ListFormViewImpl;
<#if form.hasCustomListFormPresenter>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.form.list.${form.formName}ListFormPresenter;
<#else>
import com.technology.jep.jepria.client.ui.form.list.ListFormPresenter;
</#if>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}Service;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.service.${form.formName}ServiceAsync;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.record.${form.formName}RecordDefinition;
import com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl;

<#assign ClassName = form.formName + "ClientFactoryImpl">
<#if hasCustomButtons>
  <#assign EventBusClassName = form.formName + "EventBus">
<#else>
  <#assign EventBusClassName = "PlainEventBus">
</#if>
<#assign ServiceClassName = form.formName + "ServiceAsync">

public class ${ClassName}
  extends StandardClientFactoryImpl<${EventBusClassName}, ${ServiceClassName}> {
 
  private static final IsWidget detailFormView = new ${form.formName}DetailFormViewImpl();
  <#if !form.isJepToolBarView>
  private static final IsWidget toolBarView = new ${form.formName}ToolBarViewImpl();
  </#if>
  <#if form.isStatusBarOff>
  private static final IsWidget statusBarView = new ${form.formName}StatusBarViewImpl();
  </#if>
  private static final IsWidget listFormView = new ${form.formName}ListFormViewImpl();
 
  private static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
  private ${form.formName}ClientFactoryImpl() {
    super(${form.formName?upper_case}_MODULE_ID, ${form.formName}RecordDefinition.instance);
  }
 
  public static PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if (instance == null) {
      instance = GWT.create(${form.formName}ClientFactoryImpl.class);
    }
    return instance;
  }
  <#if form.toolBarCustomButtonsOnBothForms?size != 0>

  @Override
  public JepPresenter<${EventBusClassName}, ${ClassName}> createPlainModulePresenter(Place place) {
    return new ${form.formName}ModulePresenter(${form.formName?upper_case}_MODULE_ID, place, this);
  }
  </#if>

  @Override
  public JepPresenter<${EventBusClassName}, ${ClassName}> createDetailFormPresenter(Place place) {
    return new ${form.formName}DetailFormPresenter(place, this);
  }

  @Override
  public JepPresenter<${EventBusClassName}, ${ClassName}> createListFormPresenter(Place place) {
    return new <#if form.hasCustomListFormPresenter>${form.formName}ListFormPresenter<#else>ListFormPresenter<${form.formName}ListFormViewImpl, ${EventBusClassName}, ${ServiceClassName}, ${ClassName}></#if>(place, this);
  }
  <#if !form.isJepToolBarPresenter>
  
  @Override
  public JepPresenter<${EventBusClassName}, ${ClassName}> createToolBarPresenter(Place place) {
    return new ${form.formName}ToolBarPresenter(place, this);<#--if hasCustomButtons || form.isToolBarOff-->
  }
  </#if>
   <#if !form.isJepToolBarView>
   
  @Override
  public IsWidget getToolBarView() {
    return toolBarView;
  }
  </#if>
  <#if form.isStatusBarOff>
  
  @Override 
  public IsWidget getStatusBarView() {
    return statusBarView;
  }
  </#if>
  
  @Override
  public IsWidget getDetailFormView() {
    return detailFormView;
  }

  @Override
  public IsWidget getListFormView() {
    return listFormView;
  }

  @Override
  public ${ServiceClassName} createService() {
    return GWT.create(${form.formName}Service.class);
  }
  <#if hasCustomButtons>
  
  @Override
  public ${EventBusClassName} createEventBus() {
    return new ${form.formName}EventBus();
  }
  </#if>
}