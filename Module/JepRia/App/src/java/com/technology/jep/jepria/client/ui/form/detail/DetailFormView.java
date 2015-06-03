package com.technology.jep.jepria.client.ui.form.detail;

import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.JepView;
import com.technology.jep.jepria.client.widget.field.FieldManager;

/**
 * Интерфейс View детальной формы.
 */
public interface DetailFormView extends JepView<JepPresenter> {

	/**
	 * Установка управляющего полями формы класса.
	 *
	 * @param fieldManager устанавливается управляющий класс по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
	 * При необходимости, дополнительные управляющие классы выставляются уже на прикладном уровне.
	 */
	void setFieldManager(FieldManager fieldManager);

	/**
	 * Получение управляющего полями формы класса.
	 *
	 * @return получение управляющего класса по умолчанию. В общем случае, управляющих классов может быть произвольное количество.
	 * При необходимости, дополнительные управляющие классы получаются уже на прикладном уровне.
	 */
	FieldManager getFieldManager();

}
