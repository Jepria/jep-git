package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.auto;

import com.technology.jep.jepria.auto.application.JepRiaApplicationAuto;
<#list forms as form>
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto.${form.formName}Auto;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.auto.${form.formName}AutoImpl;
</#list>

public class ${moduleName}AutoImpl extends JepRiaApplicationAuto implements ${moduleName}Auto {

  <#list forms as form>
  private ${form.formName}Auto ${form.formName?uncap_first}Auto;
  </#list>
  
  public ${moduleName}AutoImpl(String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password, 
      String dbURL,
      String dbUser,
      String dbPassword) {
    
    super(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username,
        password, dbURL, dbUser, dbPassword);
  }
  
  <#list forms as form>
  @Override
  public ${form.formName}Auto get${form.formName}Auto(boolean newInstance) {
    if(${form.formName?uncap_first}Auto == null || newInstance) {
      ${form.formName?uncap_first}Auto = new ${form.formName}AutoImpl();
    }
    return ${form.formName?uncap_first}Auto;
  }
  
  </#list>
}
