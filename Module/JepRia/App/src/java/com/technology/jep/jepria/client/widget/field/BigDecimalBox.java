package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DECIMAL_FORMAT;

import java.math.BigDecimal;
import java.text.ParseException;

import com.google.gwt.dom.client.Document;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Parser;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueBox;

/**
 * Реализация текстового поля, предназначенного для ввода значений типа BigDecimal.
 */
public class BigDecimalBox extends ValueBox<BigDecimal> {
  
  /**
   * Парсер значений формата BigDecimal.
   */
  private static class BigDecimalParser implements Parser<BigDecimal> {
    private static BigDecimalParser INSTANCE;
    
    public static Parser<BigDecimal> instance() {
      if (INSTANCE == null) {
        INSTANCE = new BigDecimalParser();
      }
      return INSTANCE;
    }
    
    private BigDecimalParser() {      
    }
    
    public BigDecimal parse(CharSequence object) throws ParseException {
      if ("".equals(object.toString())) {
        return null;
        }
      
      try {
        return BigDecimal.valueOf(NumberFormat.getDecimalFormat().parse(object.toString()));
      } catch (NumberFormatException e) {
        throw new ParseException(e.getMessage(), 0);
      }
    }
  }

  /**
   * Рендерер значений формата BigDecimal.
   */
  public static class BigDecimalRenderer extends AbstractRenderer<BigDecimal> {
    private static BigDecimalRenderer INSTANCE;
    
    public static Renderer<BigDecimal> instance() {
      if (INSTANCE == null) {
        INSTANCE = new BigDecimalRenderer();
      }
      return INSTANCE;
    }
    
    private BigDecimalRenderer() {
    }
    
    public String render(BigDecimal object) {
      if (object == null) {
        return "";
      }
      
      return NumberFormat.getFormat(DEFAULT_DECIMAL_FORMAT).format(object);
    }    
  }
  
  public BigDecimalBox() {
    super(Document.get().createTextInputElement(), BigDecimalRenderer.instance(), 
      BigDecimalParser.instance());
  }

}
