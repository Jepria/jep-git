package com.technology.jep.jepria.auto.widget.statusbar;

import com.technology.jep.jepria.auto.pages.JepRiaModulePage;

public class StatusBarImpl implements StatusBar {

  JepRiaModulePage page;
  
  public StatusBarImpl(JepRiaModulePage page) {
    this.page = page;
  }

  @Override
  public String getText() {
    page.ensurePageLoaded();
    return page.getStatusBarText();
  }

}
