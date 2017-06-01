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
<#else>
import com.technology.jep.jepria.client.ui.plain.StandardModulePresenter;
</#if>
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactoryImpl;
<#if form.isJepToolBarPresenter>
import com.technology.jep.jepria.client.ui.toolbar.ToolBarPresenter;
<#else>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar.${form.formName}ToolBarPresenter;
</#if>
<#if form.isJepToolBarView>
import com.technology.jep.jepria.client.ui.toolbar.ToolBarViewImpl;
<#else>
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
 
public class ${form.formName}ClientFactoryImpl<E extends <#if hasCustomButtons>${form.formName}<#else>Plain</#if>EventBus, S extends ${form.formName}ServiceAsync>
  extends com.technology.jep.jepria.client.ui.plain.StandardClientFactoryImpl<E, S> {
 
  private static final IsWidget ${form.formName?uncap_first}DetailFormView = new ${form.formName}DetailFormViewImpl();
  <#if !form.isJepToolBarView>
  private static final IsWidget ${form.formName?uncap_first}ToolBarView = new ${form.formName}ToolBarViewImpl();
  </#if>
  <#if form.isStatusBarOff>
  private static final IsWidget ${form.formName?uncap_first}StatusBarView = new ${form.formName}StatusBarViewImpl();
  </#if>
  private static final IsWidget ${form.formName?uncap_first}ListFormView = new ${form.formName}ListFormViewImpl();
 
  private static PlainClientFactoryImpl<PlainEventBus, JepDataServiceAsync> instance = null;
 
  public ${form.formName}ClientFactoryImpl() {
    super(${form.formName}RecordDefinition.instance);
    initActivityMappers(this);
  }
 
  static public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(${form.formName}ClientFactoryImpl.class);
    }
    return instance;
  }
 
  public JepPresenter createPlainModulePresenter(Place place) {
    return new <#if form.toolBarCustomButtonsOnBothForms?size != 0>${form.formName}<#else>Standard</#if>ModulePresenter(${form.formName?upper_case}_MODULE_ID, place, this);
  }
 
  public JepPresenter createDetailFormPresenter(Place place) {
    return new ${form.formName}DetailFormPresenter(place, this);
  }
 
  public JepPresenter createListFormPresenter(Place place) {
    return new <#if form.hasCustomListFormPresenter>${form.formName}</#if>ListFormPresenter(place, this);
  }
  <#if !form.isJepToolBarPresenter>
  
  public JepPresenter createToolBarPresenter(Place place) {
    return new ${form.formName}ToolBarPresenter(place, this);<#--if hasCustomButtons || form.isToolBarOff-->
  }
  </#if>
   <#if !form.isJepToolBarView>
   
  public IsWidget getToolBarView() {
    return ${form.formName?uncap_first}ToolBarView;
  }
   </#if>
   <#if form.isStatusBarOff>
   
   public IsWidget getStatusBarView() {
    return ${form.formName?uncap_first}StatusBarView;
  }
   </#if>
   
  public IsWidget getDetailFormView() {
    return ${form.formName?uncap_first}DetailFormView;
  }
 
  public IsWidget getListFormView() {
    return ${form.formName?uncap_first}ListFormView;
  }
 
  public S getService() {
    if(dataService == null) {
      dataService = (S) GWT.create(${form.formName}Service.class);
    }
    return dataService;
  }
  <#if hasCustomButtons>
  
  public E getEventBus() {
    if(eventBus == null) {
      eventBus = new ${form.formName}EventBus(this);
    }
    return (E) eventBus;
  }
  </#if>
}