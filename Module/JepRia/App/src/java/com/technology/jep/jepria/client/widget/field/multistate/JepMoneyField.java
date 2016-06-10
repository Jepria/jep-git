package com.technology.jep.jepria.client.widget.field.multistate;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DECIMAL_FORMAT;

import java.math.BigDecimal;
import java.text.ParseException;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.field.BigDecimalBox;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Поле для ввода числа денежного формата.
 */
@SuppressWarnings("unchecked")
public class JepMoneyField extends JepBaseNumberField<BigDecimalBox> {

	/**
	 * Количество символов, разрешенных для ввода после разделителя разрядов (по умолчанию, 2)
	 */
	private Integer maxNumberCharactersAfterDecimalSeparator = 2;
	
	public JepMoneyField(){
		this(null);
	}
	
	public JepMoneyField(String fieldLabel) {
		this(null, fieldLabel);
	}
	
	public JepMoneyField(String fieldIdAsWebEl, String fieldLabel) {
		super(fieldIdAsWebEl, fieldLabel);
		// Установка формата числа.
		setNumberFormat(NumberFormat.getFormat(DEFAULT_DECIMAL_FORMAT));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void addEditableCard() {
		editableCard = new BigDecimalBox(){
			@Override
			public void setValue(BigDecimal value) {
				super.setText(BigDecimalRenderer.instance().render(value).replaceAll(groupingSeparator, ""));
			}
		};
		editablePanel.add(editableCard);
		
		// Добавляем обработчик события "нажатия клавиши" для проверки ввода символов.
		initKeyPressHandler();
	}

	/**
	 * Получение разделителя разрядов
	 * 
	 * @return разделитель разрядов
	 */
	public String getGroupingSeparator() {
		return groupingSeparator;
	}
	
	/**
	 * Установка разделителя разрядов
	 * 
	 * @param groupingSeparator разделитель разрядов
	 */
	public void setGroupingSeparator(String groupingSeparator) {
		this.groupingSeparator = groupingSeparator;
	}
	
	/**
	 * Получение количества разрешенных для ввода символов после точки
	 * 
	 * @return количество разрешенных для ввода символов после точки
	 */
	public Integer getMaxNumberCharactersAfterDecimalSeparator() {
		return maxNumberCharactersAfterDecimalSeparator;
	}
	
	/**
	 * Установка количества разрешенных для ввода символов после точки
	 * 
	 * @param maxNumberCharactersAfterDecimalSeparator	количество разрешенных для ввода символов после точки
	 */
	public void setMaxNumberCharactersAfterDecimalSeparator(
			Integer maxNumberCharactersAfterDecimalSeparator) {
		this.maxNumberCharactersAfterDecimalSeparator = maxNumberCharactersAfterDecimalSeparator;
	}	
	
	/**
	 * Установка значения для карты Просмотра.<br/>
	 * Метод переопределён, чтобы в качестве разделителя групп разрядов использовался пробел.
	 * @param value значение для карты Просмотра
	 */
	@Override
	protected void setViewValue(Object value) {
		super.setViewValue(value);
		viewCard.setHTML(value != null ? viewCard.getHTML().replaceAll(groupingSeparator, " ") : null);
	}
	
	/**
	 * Обработка события ввода символов в поле.<br/>
	 * Особенность :
	 * <ul>
	 *  <li>данное событие вешается по умолчанию на карту для редактирования, 
	 * при этом проверяется сколько введено символов после десятичного разделителя точки.</li>
	 * </ul>
	 * 
	 * @param event	срабатываемое событие
	 * @return true - если ввод символов не был отменен, иначе - прерван
	 */
	@Override
	protected boolean keyPressEventHandler(DomEvent<?> event){
		boolean result = super.keyPressEventHandler(event); 
		if (result){
			/*
			 * Firefox имеет особенность: событие KeyPress генерируется не только при нажатии
			 * на алфавитно-цифровые клавиши, поэтому необходимо проверять значение charCode 
			 * на равенство нулю.
			 */
			NativeEvent nativeEvent = event.getNativeEvent();
			if (nativeEvent.getCharCode() == 0) {
				return true;
		    }
			/*
			 * Не реагируем, если нажата одна из клавиш Alt, Ctrl или Meta, 
			 * иначе не будут работать сочетания клавиш наподобие Ctrl-C, Ctrl-V.
			 */
			if (nativeEvent.getAltKey() || nativeEvent.getCtrlKey() || nativeEvent.getMetaKey()) {
				return true;
			}
			
			int keyCode = nativeEvent.getKeyCode();
			
			final StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(getRawValue()));
			sb.insert(editableCard.getCursorPos(), String.valueOf((char) keyCode));
			String aux = sb.toString();
			if (!isDecimalPartValid(aux)) {
				event.preventDefault();
				return false;
			}
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid(){
		boolean isValid = super.isValid();
		try {
			String value = editableCard.getText();
			// Проверка на наличие недопустимых символов необходима
			// для случаев копирования значения из буфера обмена.
			if (!isDecimalPartValid(value)){
				throw new ParseException(null, -1);
			}
		}
		catch(ParseException e){
			markInvalid(JepClientUtil.substitute(JepTexts.numberField_nanText(), getRawValue()));
			return false;
		}
		return isValid;
	}
	
	/**
	 * Метод, проверяющий является ли переданное значение десятичным числом.
	 * 
	 * @param value		проверяемое значение
	 * @return true, если значение - десятичное, в противном случае - false
	 */
	private boolean isDecimalPartValid(String value){
		if (!JepRiaUtil.isEmpty(value)) {
			//строку разбиваем посредством разделителя разрядов на 2 части
			final String[] vector = value.split("\\" + decimalSeparator, 2);
			if (vector.length > 1) {
				final String decimal = vector[1];
				//запрет ввода символов, если длина второй части строки превышает заданное количество
				if (decimal.length() > maxNumberCharactersAfterDecimalSeparator) {
					return false;
				}
			}
		}
		return true;
	}
}
