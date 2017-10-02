package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;

import com.allen_sauer.gwt.log.client.Log; 
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;


<#list forms as form>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientFactoryImpl;
</#list>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main.${moduleName}MainModulePresenter;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main.${moduleName}MainViewImpl;

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

public class ${moduleName}MainClientFactoryImpl extends MainClientFactoryImpl<MainEventBus, JepMainServiceAsync> {

  private static final IsWidget mainView = new ${moduleName}MainViewImpl();
      
  public static MainClientFactory<MainEventBus, JepMainServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(${moduleName}MainClientFactoryImpl.class);
    }
    return instance;
  }
 
  private ${moduleName}MainClientFactoryImpl() {
    super(
      <#list forms as form>
        <#if form_index != 0>, </#if>${form.formName?upper_case}_MODULE_ID
      </#list>
    );
  }
  
  @Override
  public MainModulePresenter<? extends MainView, MainEventBus, JepMainServiceAsync, ? extends MainClientFactory<MainEventBus, JepMainServiceAsync>>
      createMainModulePresenter() {
    return new ${moduleName}MainModulePresenter(this);
  }

  @Override
  public void getPlainClientFactory(String moduleId, final LoadAsyncCallback<PlainClientFactory<PlainEventBus, JepDataServiceAsync>> callback) {
    // Для эффективного кодоразделения при GWT-компиляции (см. http://www.gwtproject.org/doc/latest/DevGuideCodeSplitting.html)
    // необходимо получать инстанс каждой plain-фабрики модуля с помощью GWT.runAsync, в отдельной ветке if-else, зависящей от ID модуля.
    
    <#list forms as form>
    <#if form_index != 0>else </#if>if(${form.formName?upper_case}_MODULE_ID.equals(moduleId)) {
      GWT.runAsync(new LoadPlainClientFactory(callback) {
        public PlainClientFactory<PlainEventBus, JepDataServiceAsync> getPlainClientFactory() {
          Log.trace(${moduleName}MainClientFactoryImpl.this.getClass() + ".getPlainClientFactory: moduleId = " + moduleId);
          return ${form.formName}ClientFactoryImpl.getInstance();
        }
      });
    }
    </#list>
  }
  
  @Override
  public IsWidget getMainView() {
    return mainView;
  }
}
