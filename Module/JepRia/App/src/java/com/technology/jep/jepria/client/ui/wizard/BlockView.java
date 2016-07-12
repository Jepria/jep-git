package com.technology.jep.jepria.client.ui.wizard;

import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.widget.field.FieldManager;

public interface BlockView extends IsWidget {
  
  /**
   * Установка управляющего полями блока визарда класса.
   *
   * @param fieldManager устанавливается управляющий класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы выставляются уже на прикладном уровне.
   */
  void setFieldManager(FieldManager fieldManager);

  /**
   * Получение управляющего полями блока визарда класса.
   *
   * @return получение управляющего класса по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
   * При необходимости, дополнительные управляющие классы получаются уже на прикладном уровне.
   */
  FieldManager getFieldManager();
}
