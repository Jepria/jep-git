package com.technology.jep.jepria.client.ui.main;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.JepActivity;
import com.technology.jep.jepria.client.ui.JepView;
import com.technology.jep.jepria.client.widget.event.JepListener;

public interface MainView extends JepView<JepActivity> {

  void setBody(Widget body);

  void addExitListener(JepListener listener);

  void addEnterModuleListener(String moduleId, JepListener listener);

  void setUsername(String username);

  void selectModuleItem(String moduleId);

  void setModuleItems(String[] moduleIds, String[] moduleItemTitles);

  void showModuleTabs(String[] moduleIds);
}