package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server;

import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao.${form.formName};
import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.dao.${form.formName}Dao;

public class ${form.formName}ServerFactory extends ServerFactory<${form.formName}> {

  private ${form.formName}ServerFactory() {
    super(new ${form.formName}Dao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final ${form.formName}ServerFactory instance = new ${form.formName}ServerFactory();

}
