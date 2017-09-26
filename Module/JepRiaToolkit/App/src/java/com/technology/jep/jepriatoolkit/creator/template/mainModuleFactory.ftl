package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client;
 
import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;
 
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;

import java.util.ArrayList;
import java.util.List;

<#list forms as form>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.${form.formName}ClientFactoryImpl;
</#list>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main.${moduleName}MainModulePresenter;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main.${moduleName}MainViewImpl;

import com.technology.jep.jepria.client.ui.main.MainView;
import com.technology.jep.jepria.client.ui.main.ModuleBinding;
import com.technology.jep.jepria.client.ui.eventbus.main.MainEventBus;
import com.technology.jep.jepria.client.ui.main.MainClientFactory;
import com.technology.jep.jepria.client.ui.main.MainClientFactoryImpl;
import com.technology.jep.jepria.client.ui.main.MainModulePresenter;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;

public class ${moduleName}MainClientFactoryImpl extends MainClientFactoryImpl<MainEventBus, JepMainServiceAsync> {

  private static final IsWidget mainView = new ${moduleName}MainViewImpl();
      
  public static MainClientFactory<MainEventBus, JepMainServiceAsync> getInstance() {
    if(instance == null) {
      instance = GWT.create(${moduleName}MainClientFactoryImpl.class);
    }
    return instance;
  }
 
  private ${moduleName}MainClientFactoryImpl() {}
  
  @Override
  public MainModulePresenter<? extends MainView, MainEventBus, JepMainServiceAsync, ? extends MainClientFactory<MainEventBus, JepMainServiceAsync>>
      createMainModulePresenter() {
    return new ${moduleName}MainModulePresenter(this);
  }
 
  @Override
  protected List<ModuleBinding> getModuleBindings() {
    List<ModuleBinding> ret = new ArrayList<ModuleBinding>();
    <#list forms as form>
    ret.add(new ModuleBinding(${form.formName?upper_case}_MODULE_ID, ${form.formName}ClientFactoryImpl.creator));
    </#list>
    return ret;
  }
  
  @Override
  public IsWidget getMainView() {
    return mainView;
  }
}
