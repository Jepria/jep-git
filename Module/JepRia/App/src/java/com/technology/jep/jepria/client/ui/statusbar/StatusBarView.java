package com.technology.jep.jepria.client.ui.statusbar;

import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.JepView;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Интерфейс view панели состояния.
 */
public interface StatusBarView extends JepView<JepPresenter<?,?>> {

  /**
   * Высота по умолчанию.
   */
  int DEFAULT_HEIGHT = 22;

  /**
   * Установка высоты.
   *
   * @param height высота
   */
  void setHeight(int height);

  /**
   * Установка заголовка по заданному режиму работы.
   *
   * @param workstate состояние работы
   */
  void showWorkstate(WorkstateEnum workstate);
}