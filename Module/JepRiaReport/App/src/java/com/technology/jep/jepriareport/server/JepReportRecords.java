package com.technology.jep.jepriareport.server;

import java.util.List;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * За основу взят код SimpleViewObjectImpl из JepCommon
 * TODO Дорефакторить, есть подозрение, что осталось лишнее 
 */
public class JepReportRecords {
  /*
   * Способы вывода при табличном отображении
   */
  public enum PrintOrder { VERTICAL, HORIZONTAL }  

  protected List<JepRecord> records;
  private int firstIndex = -1;
  private int lastIndex = -1;

  private int rangeSize = 0;
  private int range = 0;
  
  /**
   * Порядковый номер текущей записи (начинается с 0)
   */
  private int rowNumber = -1;
  
  /**
   * Индекс текущей записи (отличается от порядкового номера в случаях "многостолбцового" горизонтального размещения записей)
   */
  private int currentIndex = -1;
  
  /**
   * Число столбцов отображения
   */
  private int columnNumber = 1;
  
  /**
   * Способ вывода при "многостолбцовом" отображении
    */
  private PrintOrder printOrder = PrintOrder.VERTICAL;
  
  /**
   * Размер столбца (максимальное число отображаемых записей в столбце) 
   */
  private int columnSize = -1;

  public JepReportRecords(List<JepRecord> recordList) {
    setRecords(recordList);
  }

  public JepRecord getCurrentRecord() {
    return records.get(currentIndex);
  }

  public void beforeFirst() {
    rowNumber = currentIndex = records.size() > 0 ? firstOffset() - 1 : -1;
  }

  public Object first() {
    rowNumber = currentIndex = firstOffset();
    return getCurrentRecord();
  }

  public Object last() {
    rowNumber = currentIndex = lastOffset();
    return getCurrentRecord();
  }

  public void afterLast() {
    rowNumber = currentIndex = records.size() > 0 ? lastOffset() + 1 : -1;
  }

  public Object next() {
    rowNumber++;
    setCurrentIndexByRowNumber();
    return getCurrentRecord();
  }

  public Object previous() {
    rowNumber--;
    setCurrentIndexByRowNumber();
    return getCurrentRecord();
  }

  public int getRangeSize() {
    return rangeSize;
  }

  public void setRangeSize(int rangeSize) {
    this.rangeSize = rangeSize;
  }

  protected int firstOffset() {
    return rangeSize > 0 ? firstIndex + rangeSize * range : firstIndex;
  }

  protected int lastOffset() {
    return rangeSize > 0 && rangeSize * range + rangeSize - 1 < lastIndex ? rangeSize * range + rangeSize - 1 : lastIndex;
  }

  public boolean hasNext() {
    return rowNumber < lastOffset();
  }

  public int getCurrentIndex() {
    return currentIndex;
  }

  public Object getCurrentValue(String name) {
    return getCurrentRecord().get(name);
  }

  /*
   * Методы, отвечающие за табличное отображение
   */

  public int getColumnNumber() {
    return columnNumber;
  }

  public void setColumnNumber(int columnNumber) {
    this.columnNumber = columnNumber;
  }

  public PrintOrder getPrintOrder() {
    return printOrder;
  }

  public void setPrintOrder(PrintOrder printOrder) {
    this.printOrder = printOrder;
  }

  private void setCurrentIndexByRowNumber() {
    if(printOrder == PrintOrder.VERTICAL) {
      currentIndex = rowNumber;
    } else {
      int currentColumn = rowNumber % columnNumber;
      currentIndex = rowNumber / columnNumber + currentColumn * getColumnSize();
    }
  }

  private int getColumnSize() {
    if(columnSize == -1) {
      int recordListSize = this.records.size();
      columnSize = recordListSize / columnNumber + ((recordListSize % columnNumber) == 0 ? 0 : 1);
    }
    return columnSize;  
  }

  private void setRecords(List<JepRecord> newRecords) {
    this.records = newRecords;
    int recordListSize = this.records.size();
    firstIndex = recordListSize > 0 ? 0 : -1;
    lastIndex = recordListSize > 0 ? recordListSize - 1 : -1;
    currentIndex = -1;
  }

}
