package com.technology.jep.jepria.client.ui.toolbar;

import java.util.Set;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.JepPresenter;
import com.technology.jep.jepria.client.ui.JepView;
import com.technology.jep.jepria.client.widget.button.JepButton;
import com.technology.jep.jepria.client.widget.button.JepButton.IconPosition;
import com.technology.jep.jepria.client.widget.button.Separator;
import com.technology.jep.jepria.client.widget.event.JepListener;

/**
 * Интерфейс view инструментальной панели.
 */
public interface ToolBarView extends JepView<JepPresenter> {
  // Идентификаторы встроенных элементов инструментальной панели.
  /**
   * Высота по умолчанию.
   */
  int DEFAULT_HEIGHT = 22;
  /**
   * Увеличенная высота (для тулбара с кнопками, содержащими иконку над текстом или под текстом.
   */
  int BIG_HEIGHT = 42;

  /**
   * Установка высоты.
   *
   * @param height высота
   */
  void setHeight(int height);

  /**
   * Получение кнопки по идентификатору.
   *
   * @param buttonId идентификатор
   * @return кнопка
   */
  JepButton getButton(String buttonId);

  /**
   * Установка доступности кнопки.
   *
   * @param buttonId идентификатор
   * @param enabled true &mdash; доступна, false &mdash; недоступна.
   * @return true в случае успеха, false в случае неудачи (кнопка не существует)
   */
  boolean setButtonEnabled(String buttonId, boolean enabled);

  /**
   * Добавление виджета на тулбар.
   *
   * @param id идентификатор
   * @param w виджет
   */
  void addItem(String id, Widget w);

  /**
   * Вставка виджета на тулбар в заданной позиции.
   *
   * @param id идентификатор
   * @param w виджет
   * @param beforeIndex индекс, перед которым виджет будет вставлен
   */
  void insertItem(String id, Widget w, int beforeIndex);

  /**
   * Получение виджета по идентификатору.
   *
   * @param id идентификатор
   * @return виджет
   */
  Widget getItem(String id);

  /**
   * Удаление виджета с тулбара.
   *
   * @param id идентификатор
   */
  void removeItem(String id);

  /**
   * Установка видимости виджета.
   *
   * @param id идентификатор виджета
   * @param visible true - виджет отображается, false - виджет скрыт
   * @return true - виджет присутствует на тулбаре, false - виджет отсутствует на тулбаре 
   */
  boolean setItemVisible(String id, boolean visible);

  /**
   * Удаление всего содержимого тулбара.
   */
  void removeAll();

  /**
   * Добавление кнопки с текстом.<br>
   * Текст используется как для кнопки, так и для всплывающей подсказки.
   * 
   * @param buttonId идентификатор кнопки
   * @param text текст на кнопке и для всплывающей подсказки
    * @return добавленная кнопка
   */
  JepButton addButton(String buttonId, String text);

  /**
   * Добавление кнопки с текстом и всплывающей подсказкой.
   * 
   * @param id идентификатор кнопки
   * @param text текст на кнопке
   * @param title всплывающая подсказка для кнопки
    * @return добавленная кнопка
   */
  JepButton addButton(String id, String text, String title);

  /**
   * Добавление кнопки с иконкой и всплывающей подсказкой.
   *
   * @param id идентификатор кнопки
   * @param icon иконка
   * @param title всплывающая подсказка для кнопки
    * @return добавленная кнопка
   */
  JepButton addButton(String id, ImageResource icon, String title);

  /**
   * Добавление кнопки с иконкой, текстом и всплывающей подсказкой.
   *
   * @param id идентификатор кнопки
   * @param icon иконка
   * @param text текст на кнопке
   * @param title всплывающая подсказка для кнопки
    * @return добавленная кнопка
   */
  JepButton addButton(String id, ImageResource icon, String text, String title);

  /**
   * Добавление кнопки с текстом и иконкой, расположенной в указанной позиции.<br>
   * Текст используется как для кнопки, так и для всплывающей подсказки.
   * 
   * @param id идентификатор кнопки
   * @param icon иконка
   * @param text текст на кнопке
   * @param position расположение иконки
    * @return добавленная кнопка
   */
  JepButton addButton(String id, ImageResource icon, String text, IconPosition position);

  /**
   * Добавление кнопки с текстом, всплывающей подсказкой и иконкой, расположенной в указанной позиции.
   * 
   * @param id идентификатор кнопки
   * @param icon иконка
   * @param text текст на кнопке
   * @param title всплывающая подсказка для кнопки
   * @param position расположение иконки
    * @return добавленная кнопка
   */
  JepButton addButton(String id, ImageResource icon, String text, String title, IconPosition position);

  /**
   * Добавление разделителя.
   *
   * @param separatorId идентификатор разделителя
    * @return добавленный разделитель
   */
  Separator addSeparator(String separatorId);

  /**
   * Установка доступности кнопок только переданного набора.
   *
   * @param buttonIds кнопки, которые должны быть доступны
   */
  void setButtonsEnabling(Set<String> buttonIds);
  
  /**
   * Добавление слушателя нажатия кнопки Enter.
   * 
   * @param listener слушатель нажатия кнопки Enter
   */
  void addEnterClickListener(JepListener listener);
}
