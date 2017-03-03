package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_TIME_FORMAT;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;
import com.technology.jep.jepria.shared.field.JepTypeEnum;

public class JepTypedColumn extends JepColumn {

  /**
   * Создает столбец списка, типизированный JepTypeEnum'ом.
   * @param fieldName идентификатор столбца (DB-поля)
   * @param headerText заголовок столбца
   * @param width ширина столбца
   * @param type тип из таблицы:
   * <table frame="border">
   * <tr><td>Обычный текст</td><td>{@link JepTypeEnum#STRING}</td></tr>
   * <tr><td>Время {@code 12:34:56}</td><td>{@link JepTypeEnum#TIME}</td></tr>
   * <tr><td>Дата {@code 23.04.1987}</td><td>{@link JepTypeEnum#DATE}</td></tr>
   * <tr><td>Дата и время {@code 23.04.1987 12:34:56}</td><td>{@link JepTypeEnum#DATE_TIME}</td></tr>
   * <tr><td>Целое число {@code 1234}</td><td>{@link JepTypeEnum#INTEGER}</td></tr>
   * <tr><td>Дробное число {@code 123,4}</td><td>{@link JepTypeEnum#DOUBLE} или {@link JepTypeEnum#FLOAT}</td></tr>
   * <tr><td>Decimal-число {@code 1234,00}</td><td>{@link JepTypeEnum#BIGDECIMAL}</td></tr>
   * <tr><td>Checkbox {@code [v]}</td><td>{@link JepTypeEnum#BOOLEAN}</td></tr>
   * </table>
   */
  public JepTypedColumn(String fieldName, String headerText, double width, JepTypeEnum type) {
    super(fieldName, headerText, width, getCell(type));
  }

  private static Cell<?> getCell(JepTypeEnum type) {
    switch (type) {
      case TIME: return new DateCell(DateTimeFormat.getFormat(DEFAULT_TIME_FORMAT));
      case DATE: return new DateCell(DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT));
      case DATE_TIME: return new DateCell(DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT));
      case INTEGER: return new NumberCell(NumberFormat.getFormat("#"));
      case DOUBLE: case FLOAT: return new NumberCell(NumberFormat.getFormat("#.#"));
      case BIGDECIMAL: return new NumberCell(NumberFormat.getFormat("#0.00"));
      case BOOLEAN: return new JepCheckBoxCell();
      default: return new TextCell();
    }
  }
}
