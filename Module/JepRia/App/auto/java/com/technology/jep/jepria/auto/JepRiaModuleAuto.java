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
	 * Установка заданного значения элементу ввода поля по заданному id поля 
	 * 
	 * @param fieldId id поля, id элемента ввода которого будет использовано для задания значения
	 * @param value устанавливаемое значение
	 */
	void setFieldValue(String fieldId, String value);

	/**
	 * Получение значения элемента ввода поля по заданному id поля
	 *  
	 * @param fieldId id поля, id элемента ввода которого будет использовано для получения значения
	 * @return значение поля
	 */
	String getFieldValue(String fieldId);
	
	/**
	 * Получение значений правого списка поля JepDualListField
	 *  
	 * @param jepDualListFieldId id JepDualListField'а
	 * @return массив имён находящихся в правой части (выбранных) опций.
	 * Имена в полученном массиве располагаются в порядке их отображения в правой части JepDualListField'а,
	 * поэтому сравнение массивов в классе *AutoTest необходимо производить без учета порядка!  
	 */
	String[] getDualListFieldValues(String jepDualListFieldId);
	
	/**
	 * Проверка отображения MessageBox
	 * @return true, если MessageBox отображается
	 */
	boolean checkMessageBox(String messageBoxId);
	
	/**
	 * Простой выбор элемента Комбо-бокса по заданному id.
	 * Предполагается, что если элемент есть в выпадающем списке, то он становится доступным при первой загрузке опций.
	 * Если искомой опции нет в выпадающем списке, выбрасывается исключение WrongOptionException.
	 * @param comboBoxFieldId id ComboBox-поля
	 * @param menuItem Имя опции, которую необходимо выбрать
	 */
	void selectComboBoxMenuItem(String comboBoxFieldId, String menuItem);
	
	/**
	 * Сложный выбор элемента Комбо-бокса по заданному id.
	 * Предполагается, что при первой загрузке опций не все опции появляются в списке (например, список слишком длинный),
	 * и искомая опция может стать доступной о мере ввода текста.
	 * Если искомой опции нет в выпадающем списке после введения всего текста, выбрасывается исключение WrongOptionException.
	 * @param comboBoxFieldId id ComboBox-поля
	 * @param menuItem Имя опции, которую необходимо выбрать
	 * @param minInputLength Минимальное количество символов, которые необходимо ввести для того, чтобы в данном Комбо-боксе
	 * началась загрузка опций (например, при поиске оператора, часто опции загружаются при вводе мимнимум 3 символов). 
	 * Минимально допустимое значение = 1 (устанавливается автоматически при задании меньшего значения).
	 */
	void selectComboBoxMenuItemWithCharByCharReloadingOptions(String comboBoxFieldId, String menuItem, int minInputLength);
	
	/**
	 * Выбор элементов DualListField по заданному Id.
	 * @param dualListFieldId id DualList-поля
	 * @param menuItems массив непустых имён опций, которые следует выбрать из левой части DualList-поля в правую.
	 * В случае если в левой части отсутствует хотя бы одна из требуемых опций, выбрасывается исключение WrongOptionException.
	 */
	void selectDualListMenuItems(String dualListFieldId, String menuItems[]);
	
	/**
	 * Простановка флажка CheckBoxField по заданному Id.
	 * @param checkBoxFieldId id CheckBox-поля
	 * @param checked требуемое для установки значение
	 */
	void setCheckBoxFieldValue(String checkBoxFieldId, boolean checked);
	
	/**
	 * Изменение значения флажка CheckBoxField по заданному Id на противоположное.
	 * @param checkBoxFieldId id CheckBox-поля
	 */
	void changeCheckBoxFieldValue(String checkBoxFieldId);
	
	/**
	 * Получение значения поля  CheckBoxField.
	 * 
	 * @param checkBoxFieldId id CheckBox-поля
	 */
	boolean getCheckBoxFieldValue(String checkBoxFieldId);
	
	/**
	 * Выбор элементов JepListField по заданному Id.
	 * @param listFieldId id JepList-поля
	 * @param menuItems массив непустых имён опций, которые следует отметить в списке.
	 * В случае если в списке отсутствует хотя бы одна из требуемых опций, выбрасывается исключение WrongOptionException.
	 */
	void selectListMenuItems(String listFieldId, String menuItems[]);
	
	/**
	 * Выбор всех элементов JepListField по заданному Id.
	 * @param listFieldId id JepList-поля
	 * @param selectAll true для выбора всех элементов, false для снятия всех флажков.
	 */
	void selectAllListMenuItems(String listFieldId, boolean selectAll);
	
	/**
	 * Получение отмеченных значений списка поля JepListField
	 *  
	 * @param jepListFieldId id JepListField'а
	 * @return массив имён отмеченных (выбранных) опций.
	 * Имена в полученном массиве располагаются в порядке их отображения в списке JepListField'а,
	 * поэтому сравнение массивов в классе *AutoTest необходимо производить без учета порядка!  
	 */
	String[] getListFieldValues(String jepListFieldId);
}
