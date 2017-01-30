package com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client;
 
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;
 
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;

<#list forms as form>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientFactoryImpl;
</#list>
import com.technology.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main.${moduleName}MainModulePresenter;

import com.technology.jep.jepria.client.ModuleItem;
import com.technology.jep.jepria.client.ui.main.MainView;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.client.async.LoadAsyncCallback;
import com.technology.jep.jepria.client.async.LoadPlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;

public class ${moduleName}ClientFactoryImpl<E extends MainEventBus, S extends JepMainServiceAsync>
  extends MainClientFactoryImpl<E, S>
    implements MainClientFactory<E, S> {
    
  public static MainClientFactory<MainEventBus, JepMainServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(${moduleName}ClientFactoryImpl.class);
    }
    return instance;
  }
 
   private ${moduleName}ClientFactoryImpl() {
    super(
      <#list forms as form>
        <#if form_index != 0>, </#if>new ModuleItem(${form.formName?upper_case}_MODULE_ID, ${moduleName?uncap_first}Text.submodule_${form.formName?lower_case}_title())
      </#list>
    );
 
    initActivityMappers(this);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Activity createMainModulePresenter() {
    return new ${moduleName}MainModulePresenter(this);
  }
 
  public void getPlainClientFactory(String moduleId, final LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback) {
    <#list forms as form>
    <#if form_index != 0>else </#if>if(${form.formName?upper_case}_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(${moduleName}ClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + ${form.formName?upper_case}_MODULE_ID);
          return ${form.formName}ClientFactoryImpl.getInstance();
        }
      });
    }
    </#list>
  }
}
