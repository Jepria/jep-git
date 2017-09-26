package com.technology.jep.jepria.client.ui.form.list;

import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.JepView;
import com.technology.jep.jepria.client.widget.list.GridManager;

/**
 * Интерфейс View списочной формы.
 */
public interface ListFormView<L extends GridManager> extends JepView<JepPresenter<?, ?>> {

  /**
   * Установка управляющего списком формы класса.
   *
   * @param list устанавливается управляющий класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы выставляются уже на прикладном уровне.
   */
  void setListManager(L list);

  /**
   * Получение управляющего списком формы класса.
   *
   * @return получение управляющего класса по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы получаются уже на прикладном уровне.
   */
  L getListManager();

}
