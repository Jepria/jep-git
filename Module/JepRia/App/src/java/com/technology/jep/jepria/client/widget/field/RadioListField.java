package com.technology.jep.jepria.client.widget.field;

import static com.technology.jep.jepria.client.JepRiaClientConstant.FIELD_DEFAULT_HEIGHT;
import static com.technology.jep.jepria.client.JepRiaClientConstant.MAIN_FONT_STYLE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.shared.field.option.JepOption;

public class RadioListField<T extends JepOption> extends VerticalPanel implements HasValue<T> {

  private T currentValue;
  private Map<T, RadioButton> radios = new HashMap<T, RadioButton>();
  private final String RADIO_GROUP_NAME = "RadioButtonGroup_" + Random.nextInt();
  private final static String RADIO_ITEM_STYLE = "item";
  
  public void setOptions(List<T> options){
    for (T option : options){
      final RadioButton radio = new RadioButton(RADIO_GROUP_NAME, option.getName()){
        @Override
        public void onAttach(){
          super.onAttach();
          
          Element radioButtonElement = getElement();
          for (int i = 0; i < radioButtonElement.getChildCount(); i++){
            Node child = radioButtonElement.getChild(i);
            // Найдем соответствующие input-элемент и label-элемент.
            if (child instanceof Element){
              Element labelElement = (Element) child; 
              if(((Element) child).getTagName().equalsIgnoreCase("label")){
                labelElement.addClassName(RADIO_ITEM_STYLE);
                labelElement.addClassName(MAIN_FONT_STYLE);
              }
              else if (((Element) child).getTagName().equalsIgnoreCase("input")){
                Element inputElement = (Element) child; 
                inputElement.addClassName(RADIO_ITEM_STYLE);
              }
            }    
          }
        }
      };
      radio.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
          if (event.getValue()){
            setValue(getValueByField(radio), true);
          }
        }
      });
      
      radios.put(option, radio);
      
      add(radio);
      setCellHeight(radio, (FIELD_DEFAULT_HEIGHT + 2) + Unit.PX.getType());
    }
  }
  
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public T getValue() {
    return currentValue;
  }

  public void setValue(T value) {
    setValue(value, false);
  }

  public void setValue(T value, boolean fireEvents) {
    final T oldValue = getValue();
    
    this.currentValue = value;
    for (Entry<T, RadioButton> entry: radios.entrySet()){
      entry.getValue().setValue(entry.getKey().equals(this.currentValue));
    }
    
    if (fireEvents) {
      ValueChangeEvent.fireIfNotEqual(this, oldValue, this.currentValue);
    }
  }
  
  public T getValueByField(RadioButton button){
    for (Entry<T, RadioButton> entry: radios.entrySet()){
      // find checked radio button
      if (entry.getValue().equals(button)){
        return entry.getKey();
      }
    }
    return null;
  }
  
  public void setEnabled(boolean enabled) {
    radios.values().forEach(radio -> radio.setEnabled(enabled));
  }
}
