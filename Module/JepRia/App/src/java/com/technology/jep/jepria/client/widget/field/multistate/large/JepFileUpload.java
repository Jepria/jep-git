package com.technology.jep.jepria.client.widget.field.multistate.large;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FileUpload;

/**
 * Класс, расширяющий возможности стандартного GWT-виджета для загрузки файла,
 * способного получать размер файла на клиентской стороне (средствами HTML5).
 */
public class JepFileUpload extends FileUpload {
  
  /**
   * Получение размера выбранного файла (в Kbytes), если текущий браузер поддерживает HTML5
   * 
   * @return  размер файла
   */
  public Integer getFileSize() {
    try {
      return getFileSize(getElement()) / 1024; //in Kb
    }
    catch(JavaScriptException e) { // Если нет поддержки HTML5
      return null;
    }
  }
  
  /**
   * Получение размера файла у элемента 
   * 
   * @param data    DOM-элемент
   * @return размер выбранного файла
   */
  private native int getFileSize(final Element element) /*-{
    return element.files[0].size;
  }-*/;
}
