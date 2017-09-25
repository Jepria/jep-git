package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.ui.main;

import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.main.client.${moduleName}ClientConstant.*;

import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepria.client.ui.main.MainViewImpl;
import com.technology.jep.jepria.client.ui.main.ModuleConfiguration;

public class ${moduleName}MainViewImpl extends MainViewImpl {

  @Override
  protected List<ModuleConfiguration> getModuleConfigurations() {
    List<ModuleConfiguration> ret = new ArrayList<>();
    <#list forms as form>
    ret.add(new ModuleConfiguration(${form.formName?upper_case}_MODULE_ID, ${moduleName?uncap_first}Text.submodule_${form.formName?lower_case}_title()));
    </#list>
    return ret;
  }
}
