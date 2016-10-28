package com.technology.jep.jepria.auto.widget.statusbar;

import com.technology.jep.jepria.auto.module.page.JepRiaModulePage;
import com.technology.jep.jepria.auto.util.HasText;

public class StatusBar implements HasText {

  JepRiaModulePage page;
  
  public StatusBar(JepRiaModulePage page) {
    this.page = page;
  }

  public String getText() {
    page.ensurePageLoaded();
    return page.getStatusBarText();
  }

}
