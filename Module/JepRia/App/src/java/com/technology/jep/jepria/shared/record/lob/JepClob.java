package com.technology.jep.jepria.shared.record.lob;

import java.io.BufferedReader;
import java.io.Serializable;
import java.sql.Clob;

/**
 * Класс предназначен для передачи значения типа {@link java.sql.Types#CLOB} как аргумента 
 * хранимой процедуры или функции.
 */
public class JepClob implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  /**
   * Текст для передачи большого текстового значения (более 32767 символов) в хранимую процедуру или функцию БД 
   */
  private String bigText; // suppose that more than 4kb (varchar2 limitation)

  /**
   * Создает обертку для аргумента типа {@link java.sql.Types#CLOB}
   * @param text
   */
  public JepClob(String text){
    setBigText(text);
  }

  public JepClob(Clob clob){
    if (clob == null) throw  new NullPointerException();
    
    StringBuilder str = new StringBuilder();
    
    try (BufferedReader bufferRead = new BufferedReader(clob.getCharacterStream())) {
      String bufferStr;
      while ((bufferStr = bufferRead.readLine()) != null) {
        str.append(bufferStr);
      }
    } catch (Exception  e) {
      e.printStackTrace();
    }
    
    setBigText(str.toString());
  }
  
  public String getBigText() {
    return bigText;
  }

  public void setBigText(String bigText) {
    this.bigText = bigText;
  }
}
