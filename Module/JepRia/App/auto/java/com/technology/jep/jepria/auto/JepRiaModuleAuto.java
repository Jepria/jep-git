package com.technology.jep.jepria.auto;

import java.util.Map;

import com.technology.jep.jepria.auto.entrance.EntranceAuto;
import com.technology.jep.jepria.auto.widget.field.Field;
import com.technology.jep.jepria.auto.widget.statusbar.StatusBar;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Базовый интерфейс автоматизации стандартного модуля JepRia
 */
public interface JepRiaModuleAuto extends EntranceAuto {
	/**
	 * Перейти в указанное состояние
	 */
	void setWorkstate(WorkstateEnum workstate);

	/**
	 * Перейти в состояние редактирования (самый общий случай) 
	 * 
	 * @param template - шаблон, по которому выбираются записи.
	 * Поскольку на поисковой форме первичный ключ присутствует далеко не всегда, в общем случае нужная (для редактирования) запись
	 * будет так или иначе выбираться из нескольких на списочной форме.
	 * 
	 * @param recordSelector - класс, необходимый при получении более одной записей на списочной форме, он используется для определения,
	 * какая из множества записей будет редактироваться. 
	 */
	void edit(Map<String, String> template, RecordSelector recordSelector);
	
	/**
	 * Перейти в состояние редактирования записи, выбранной по первичному ключу
	 * В качестве идентификаторов полей первичного ключа выступают ID Web-элементов полей ввода
	 * 
	 * @param primaryKey - первичный ключ.
	 */
	void edit(Map<String, String> primaryKey);
	
	/**
	 * Перейти в состояние редактирования записи, выбранной по id
	 * 
	 * @param idFieldName - имя поля id записи
	 * @param id - значение id
	 */
	void edit(String idFieldName, String id);

	/**
	 * Удаление записи 
	 * 
	 * @param key ключ, по которому выбирается запись 
	 */
	void delete(Map<String, String> key);
	
	/**
	 * Выделить элемент списка списочной формы по индексу
	 */
	public void selectItem(int index);
	
	/**
	 * Выделить элемент списка списочной формы по ключу
	 */
	public void selectItem(Map<String, String> key);
	
	/**
	 * Получение поля детальной формы по идентификатору
	 * 
	 * @param fieldId идентификатор поля детальной формы
	 */
	Field getField(String fieldId);

	/**
	 * Сохранить
	 * 
	 * @return одно из значений SaveResultEnum
	 */
	SaveResultEnum save();

	/**
	 * Выполнение поиска по параметрам, заданным в поисковой форме
	 */
	void find();

	/**
	 * Выполнение поиска по шаблону
	 */
	void find(Map<String, String> template);

	/**
	 * Получение элемента панели состояния
	 * @return элемент панели состояния
	 */
	StatusBar getStatusBar();
	
	/**
	 * Нажатие кнопки с идентификатором buttonId
	 * @param buttonId - id кнопки
	 */
	void clickButton(String buttonId);
	
	/**
	 * Установка заданного значения полю по заданному id элемента ввода 
	 * 
	 * @param fieldInputId id элемента ввода
	 * @param value устанавливаемое значение
	 */
	void setFieldValue(String fieldInputId, String value);

	/**
	 * Получение значения поля по заданному id элемента ввода
	 *  
	 * @param fieldInputId id элемента ввода
	 * @return значение поля
	 */
	String getFieldValue(String fieldInputId);
	
	/**
	 * Проверка отображения MessageBox
	 * @return true, если MessageBox отображается
	 */
	boolean checkMessageBox(String messageBoxId);

	/**
	 * Установка заданного значения полю по заданному id элемента ввода 
	 * 
	 * @param comboBoxFieldInputId
	 * @param menuItemText
	 */
	void selectComboBoxMenuItem(String comboBoxFieldInputId, String menuItemText);
}
