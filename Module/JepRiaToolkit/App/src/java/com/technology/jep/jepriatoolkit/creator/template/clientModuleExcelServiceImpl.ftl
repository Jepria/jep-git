package ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service;

import ${packagePrefix?lower_case}.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.record.${form.formName}RecordDefinition;
import com.technology.jep.jepria.server.download.excel.ExcelServlet;

public class ShowExcelServlet extends ExcelServlet {

  public ShowExcelServlet() {
    super(${form.formName}RecordDefinition.instance);
  }
}
