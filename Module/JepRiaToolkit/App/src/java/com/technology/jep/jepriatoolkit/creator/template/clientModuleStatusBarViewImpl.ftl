package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.statusbar;
 
import com.technology.jep.jepria.client.ui.statusbar.StatusBarViewImpl;
 
public class ${form.formName}StatusBarViewImpl extends StatusBarViewImpl {
 
  public ${form.formName}StatusBarViewImpl() {
    super();
     asWidget().setVisible(false);
  }
}
