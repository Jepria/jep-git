package com.technology.jep.jepria.client.widget.list.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Реализация ячейки, которая отображает на списочной форме числовое значение в
 * форматированном виде, где разделителем тысячных разрядов, по умолчанию,
 * является пробел или переопределенный символ. Также имеется возможность
 * переопределения пользовательского разделителя целой и десятичной частей.<br/>
 * Также поддерживается null, в этом случае в ячейке выводится пустая строка.
 */
public class JepNumberCell extends AbstractCell<Number> {

  /**
   * Формат числовых данных, по умолчанию.
   */
  private final static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("###,###,###,###");

  /**
   * Текущий формат числовых данных.
   */
  private final NumberFormat formatter;
  
  /**
   * Разделитель целой и десятичной частей.
   */
  protected String decimalSeparator;

  /**
   * Разделитель тысячных разрядов.
   */
  protected String groupingSeparator;

  /**
   * Создает объект JepNumberCell.<br/>
   * Реализация заключается в вызове конструктора базового класса
   * {@link com.google.gwt.cell.client.AbstractCell}.<br/>
   * Подробности смотрите в {@link com.google.gwt.cell.client.AbstractCell}
   * 
   * @see com.google.gwt.cell.client.AbstractCell
   */
  public JepNumberCell() {
    this(defaultNumberFormatter);
  }

  /**
   * Создает объект JepNumberCell.<br/>
   * В данном конструкторе предоставляется возможность переопределения шаблона
   * для форматирования, задаваемого в строковом виде.
   * 
   * @param format    строковое представление формата числовых данных
   * @see com.google.gwt.cell.client.AbstractCell
   */
  public JepNumberCell(String format) {
    this(NumberFormat.getFormat(format));
  }

  /**
   * Создает объект JepNumberCell.<br/>
   * В данном конструкторе предоставляется возможность переопределения шаблона
   * для форматирования, задаваемого в виде объекта
   * {@link com.google.gwt.i18n.client.NumberFormat}.
   * 
   * @param formatter  переопределенный числовой формат
   */
  public JepNumberCell(NumberFormat formatter) {
    this.formatter = formatter;
  }

  /**
   * Отрисовывает ячейку таблицы.
   * 
   * @param context     контекст ячейки
   * @param value       значение ячейки
   * @param sb          объект, в который помещается конечное содержимое ячейки
   */
  public void render(Context context, Number value, SafeHtmlBuilder sb) {
    String label = "";
    if (value != null) {
      label = formatter.format(value).replaceAll(
          LocaleInfo.getCurrentLocale().getNumberConstants().groupingSeparator(),
              JepRiaUtil.isEmpty(groupingSeparator) ? " " : groupingSeparator);
      if (!JepRiaUtil.isEmpty(decimalSeparator)) {
        label = label.replaceAll(LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator(), decimalSeparator);
      }
    }
    sb.appendEscaped(label);
  }

  /**
   * Переопределяет разделитель целой и десятичной частей.
   * 
   * @param decimalSeparator  переопределенный разделитель целой и десятичной частей
   */
  public void setDecimalSeparator(String decimalSeparator) {
    this.decimalSeparator = decimalSeparator;
  }

  /**
   * Переопределяет разделитель тысячных разрядов.
   * 
   * @param groupingSeparator  переопределенный разделитель тысячных разрядов
   */
  public void setGroupingSeparator(String groupingSeparator) {
    this.groupingSeparator = groupingSeparator;
  }
}
