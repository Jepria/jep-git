package com.technology.jep.jepria.auto.module;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.page.PlainPage;
import com.technology.jep.jepria.auto.widget.tree.TreeItemFilter;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

/**
 * Базовый интерфейс автоматизации стандартного модуля JepRia. <br/>
 * TODO: В основном содержит автоматизацию детальной формы. Реализовать для списочной. Разбить на несколько классов?
 */
public interface JepRiaModuleAuto {
  
  /**
   * Возможные варианты результата сохранения.
   */
  public enum SaveResultEnum {
    /**
     * В результате выполнения Save отобразилось pop-окно Alert (как правило, с предупреждениями о некорректно заполненной форме)
     */
    ALERT_MESSAGE_BOX,

    /**
     * В результате выполнения Save отобразилось pop-окно Error (как правило, с сообщением об ошибке)
     */
    ERROR_MESSAGE_BOX,
    
    /**
     * Успешное соханение (в результате выполнения Save в StatusBar изменился текст [как правило, на 'Просмотр объекта'])
     */
    SUCCESS
  }
  
  /**
   * Проверка, загрузился ли модуль (делегирование к {@link PlainPage#ensurePageLoaded()}).
   */
  void ensureModuleLoaded();
  
  /**
   * Перейти в указанное состояние
   */
  void setWorkstate(WorkstateEnum workstate);

  /**
   * Перейти в состояние редактирования записи, выбранной по первичному ключу
   * В качестве идентификаторов полей первичного ключа выступают ID Web-элементов полей ввода
   * 
   * @param primaryKey - первичный ключ.
   */
  void edit(Map<String, String> primaryKey);
  
  /**
   * Перейти в состояние редактирования записи, выбранной по первичному ключу
   * В качестве идентификаторов полей первичного ключа выступают ID Web-элементов полей ввода
   * 
   * @param primaryKey - первичный ключ.
   * @param gridId - Идентификатор списочной формы.
   */
  void edit(Map<String, String> primaryKey, String gridId);
  
  /**
   * Перейти в состояние редактирования записи, выбранной по id
   * 
   * @param idFieldName - имя поля id записи
   * @param id - значение id
   */
  void edit(String idFieldName, String id);

  /**
   * Удаление записи со списочной формы.
   * Перед вызовом данного метода, необходимая запись должна быть выделена. 
   * @return workstate, в котором находится приложение после удаления.
   */
  WorkstateEnum deleteSelectedRow();
  
  /**
   * Удаление записи с детальной формы.
   * Перед вызовом данного метода, необходимая запись должна быть в состоянии редактирования либо просмотра. 
   * @return workstate, в котором находится приложение после удаления.
   */
  WorkstateEnum deleteDetail();

  
  /**
   * Выделить элемент списка списочной формы по индексу.
   * @param index - Номер строчки на списочной форме (начинается с 0).
   * @param gridId - Идентификатор списочной формы.
   */
  public void selectItem(int index, String gridId);
  
  /**
   * Выделить элемент списка списочной формы по индексу
   */
  public void selectItem(int index);
  
  /**
   * Выделить элемент списка списочной формы по индексу(С зажатой кнопкой Ctrl для множественного выделения).
   */
  public void selectItems(int index, String gridId);
  
  /**
   * Выделить элемент списка списочной формы по ключу
   * 
   * @deprecated выполняйте поиск вручную в прикладном тестовом классе instead.
   */
  @Deprecated
  public void selectItem(Map<String, String> key);
  
  /**
   * Сохранить
   * 
   * @return одно из значений SaveResultEnum
   */
  SaveResultEnum save();

  /**
   * Переход на форму поиска.<br>
   * Для собственно выполнения поиска по параметрам, заданным в поисковой форме,
   * необходимо использовать {@link #doSearch}
   */
  void find();
  
  /**
   * Выполнение поиска по параметрам, заданным в поисковой форме
   */
  void doSearch();

  /**
   * Выполнение поиска по шаблону
   */
  void doSearch(Map<String, String> template);

  /**
   * Получение workstate из html-атрибута StatusBar модуля
   */
  WorkstateEnum getWorkstateFromStatusBar();
  
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
   * Установка заданного значения элементу ввода поля типа Large по заданному id поля.
   * Отличается от аналогичного метода {@link #setFieldValue(String, String)} лишь тем,
   * что перед установкой значения не происходит очистка поля при помощи <code>Ctrl+A,DEL</code>
   * 
   * @param fieldId id поля, id элемента ввода которого будет использовано для задания значения
   * @param pathToFile абсолютный путь до файла на текущей машине (локального).<br>
   * Примеры:<br>
   * <code>"C:\\Folder\\TheFoldest\\file.jpg"</code><br>
   * <code>"C:/Folder/TheFoldest/file.jpg"</code>
   */
  void setLargeFieldValue(String fieldId, String pathToFile);
  
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
  
  /**
   * Выбор элементов JepTreeField по заданным путям, без предварительной очистки уже отмеченных узлов.
   * @param treeFieldId id JepTree-поля
   * @param itemPaths массив непустых путей опций, которые следует отметить в дереве.
   * В случае если в дереве отсутствует хотя бы одна из требуемых опций либо промежуточный узел в пути,
   * выбрасывается исключение {@link com.technology.jep.jepria.auto.exception.WrongOptionException}.
   * В случае если входной параметр некорректен, выбрасывается {@link java.lang.IllegalArgumentException}
   * Синтаксис путей опций: {FolderName/}OptionName или {FolderName/}>FolderName;
   * '/' в именах элементов нужно экранировать: '\/'.
   * Предполагается, что имена элементов дерева не начинаются с '>' (иначе нужно внедрять экранирование этого символа).
   * Предполагается, что нигде в дереве рядом не находится двух одноименных листьев или папок (но допускаются лист и папка с одним именем).
   * Символ '>' может стоять перед именем последнего (подлежащего отмечанию) элемента в том случае, если это папка.
   * Это различает ситуации, когда в дереве рядом находятся одноименные лист и папка.
   */
  void selectTreeItems(String treeFieldId, String itemPaths[]);
  
  /**
   * Выбор всех элементов JepTreeField по заданному Id, с помощью флажка "Выделить все".
   * Если данный флажок отсутствует, метод не производит ничего.
   * @param treeFieldId id JepTree-поля
   * @param selectAll true для выбора всех элементов, false для снятия всех флажков.
   * @return true если флажок "Выделить все" доступен в данном JepTreeField, иначе false.
   */
  boolean selectAllTreeItems(String treeFieldId, boolean selectAll);
  
  /**
   * Получение массива путей до узлов, проходящих через фильтр, поля JepTreeField.
   * 
   * @param treeFieldId id JepTreeField'а
   * @param filter фильтр узлов дерева при обходе
   * @return массив путей в дереве отмеченных (выбранных) узлов.
   * Синтаксис путей опций: {FolderName/}OptionName или {FolderName/}>FolderName;
   * '/' в именах элементов экранируются: '\/'.
   * Узлы в полученном массиве располагаются в порядке их отображения в списке JepTreeField'а,
   * поэтому сравнение массивов в классе *AutoTest необходимо производить без учета порядка!
   */
  String[] getTreeFieldNodesByFilter(String treeFieldId, TreeItemFilter filter);
  
  /**
   * Проверка видимости Jep-поля по заданному ID (атрибута aria-hidden)
   * @param fieldId id Jep-поля
   */
  boolean isFieldVisible(String fieldId);
  
  /**
   * Проверка доступности Jep-поля по заданному ID (атрибута disabled)
   * @param fieldId id Jep-поля
   */
  boolean isFieldEnabled(String fieldId);
  
  /**
   * Проверка редактируемости Jep-поля по заданному ID (атрибута jep-card-type)
   * @param fieldId id Jep-поля
   */
  boolean isFieldEditable(String fieldId);
  
  /**
   * Проверка необязательности Jep-поля по заданному ID (наличия маркера обязательности (*) )
   * @param fieldId id Jep-поля
   */
  boolean isFieldAllowBlank(String fieldId);
  
  
  
  
  // The methods below are for LISTFORM, not DETAILFORM! TODO extract them into another class?
  
  List<String> getGridHeaders(String gridId);
  
  boolean isGridEmpty(String gridId);
  
  /**
   * @return a list of rows, where each row is a list of cell-objects, or empty list if there is no grid (or it is empty).
   * Each object in the resultant grid may be either of a type WebElement
   * (if a cell contains something inside, such as input) or of a type String
   * (if there is only a text). The value of a particular cell can be
   * obtained using the row number and a column index,
   * known from the result of #getGridHeaders().<br>
   * <i>If an object is a WebElement with a tagname '<b>tag</b>', then its position in
   * the grid as DOM is:<pre>
   * &lt;tbody id='gridId_BODY'&gt;
   *   &lt;tr&gt;
   *     &lt;td&gt;
   *       &lt;div&gt;
   *         <b>&lt;tag/&gt;</b>
   *       &lt;/div&gt;
   *     &lt;/td&gt;
   *     ...
   *   &lt;/tr&gt;
   *   ...
   * &lt;/tbody&gt;
   * </pre></i>
   */
  List<List<Object>> getGridDataRowwise(String gridId); 

  /**
   * Производит настройку столбцов списочной формы.
   * 
   * @param gridId
   * @param columns Порядок и наличие столбцов списочной формы приводится в соответствие с данным массивом.
   * В случае, если среди столбцов формы нет какого-либо значения из данного массива, выбрасывается
   * {@link java.lang.IllegalArgumentException}.
   */
  void doGridColumnSettings(String gridId, String[] columns);

  WebElement getGridRowElement(int rowIndex, String gridId);

  void dragAndDropGridRow(int draggableRowIndex, int targerRowIndex,
      String gridId);

  void dragAndDropGridRows(List<Integer> draggableRowIndexList,
      int targetRowIndex, String gridId);

  void dragAndDropGridRowsAfterTarget(List<Integer> draggableRowIndexList,
      int targetRowIndex, String gridId);

  void dragAndDropGridRowAfterTarget(int draggableRowIndex, int targetRowIndex,
      String gridId);

  void dragAndDropGridRowsBeforeTarget(List<Integer> draggableRowIndexList,
      int targetRowIndex, String gridId);

  void dragAndDropGridRowBeforeTarget(int draggableRowIndex, int targetRowIndex,
      String gridId);

  void sortByColumn(String gridId, Integer columnId);

  String getGridRowCount();

  void setGridRowCount(String rowCount);

}
