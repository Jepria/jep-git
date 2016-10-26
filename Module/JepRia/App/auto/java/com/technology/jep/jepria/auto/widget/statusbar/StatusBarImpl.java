package com.technology.jep.jepria.auto.widget.statusbar;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class StatusBarImpl implements StatusBar {

  JepRiaApplicationPage page;
  
  public StatusBarImpl(JepRiaApplicationPage page) {
    this.page = page;
  }

  @Override
  public String getText() {
    return page
        .ensurePageLoaded()
        .getStatusBarText();
  }

}
