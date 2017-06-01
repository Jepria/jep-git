package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service;

import static ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.DATA_SOURCE_JNDI_NAME;

import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.record.${form.formName}RecordDefinition;
import com.technology.jep.jepria.server.download.JepDownloadServlet;

public class DownloadServiceImpl extends JepDownloadServlet {

  public DownloadServiceImpl() {
    super(${form.formName}RecordDefinition.instance, DATA_SOURCE_JNDI_NAME);
  }
}
