package com.technology.jep.jepria.client.ui;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface JepView<P extends JepActivity> extends IsWidget {
  void setPresenter(P presenter);
  void setWidget(Widget widget);
}
