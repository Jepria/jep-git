package com.technology.jep.jepria.client.widget.field.masked;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.CHAR;
import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.DIGIT;
import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.LETTER;
import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.LETTER_OR_DIGIT;
import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.LITERAL;
import static com.technology.jep.jepria.client.widget.field.masked.MaskItemType.SIGN_OR_DIGIT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Маска, используемая в виджете JepMaskedTextField.<br>
 * Содержит описание строки, которую может ввести пользователь. 
 * Используются следующие обозначения:
 * <ul>
 *   <li>A - обязательное наличие в данной позиции буквы или цифры.</li>
 *   <li>a - необязательное наличие в данной позиции буквы или цифры.</li>
 *   <li>0 - обязательное наличие в данной позиции цифры.</li>
 *   <li>9 - необязательное наличие в данной позиции цифры.</li>
 *   <li>L - обязательное наличие в данной позиции буквы.</li>
 *   <li>l - необязательное наличие в данной позиции буквы.</li>
 *   <li>C - обязательное наличие в данной позиции произвольного символа.</li>
 *   <li>c - необязательное наличие в данной позиции произвольного символа.</li>
 *   <li># - обязательное наличие в данной позиции цифры или знака &quot;+&quot; или &quot;-&quot;.</li>
 *   <li>&lt; - последующие буквы будут приведены к нижнему регистру.</li>
 *   <li>&gt; - последующие буквы будут приведены к верхнему регистру.</li>
 *   <li>&lt;&gt; - прекращение преобразования регистра.</li>
 *   <li>\ - следующий символ является литералом (в коде необходимо экранировать данный символ: &quot;\\&quot;.</li>
 * </ul>
 * Все остальные символы воспринимаются как литералы.
 * Действуют следующие ограничения:
 * <ul>
 *   <li>Все необязательные символы должны находиться в конце маски.</li>
 *   <li>Все необязательные символы должны иметь один и тот же тип (т.е. конструкция вида
 *   &quot;cccllaa9&quot; не разрешается.</li>
 * </ul>
 */
public class Mask {
	
	/**
	 * Класс, описывающий позицию в маске.
	 */
	class MaskItem {
		public MaskItem(MaskItemType itemType, boolean toUpper, boolean toLower, boolean mandatory, char literal) {
			this.itemType = itemType;
			this.toUpper = toUpper;
			this.toLower = toLower;
			this.mandatory = mandatory;
			this.literal = literal;
		}
		
		/**
		 * Тип элемента.
		 */
		final MaskItemType itemType;
		/**
		 * Флаг, сигнализирующий о том, что элемент в данной позиции
		 * будет приведён к верхнему регистру.
		 */
		final boolean toUpper;
		/**
		 * Флаг, сигнализирующий о том, что элемент в данной позиции
		 * будет приведён к нижнему регистру.
		 */
		final boolean toLower;
		/**
		 * Флаг, сигнализирующий о том, что элемент в данной позиции
		 * является обязательным
		 */
		final boolean mandatory;
		/**
		 * Если тип элемента - литерал, то он содержится в данном поле.
		 */
		final char literal;
		
		/**
		 * Проверяет, соответствует ли символ данному элементу маски.
		 * @param ch символ
		 * @param allowEmptyChars флаг, разрешающий ввод пустого символа на обязательную позицию
		 * @return true, если соответствует, и false в противном случае
		 */
		boolean match(char ch, boolean allowEmptyChars) {
			if (itemType == LITERAL) return ch == literal || ch == '\0';
			if (ch == '\0') return !mandatory || allowEmptyChars;
			switch (itemType) {
				case LETTER_OR_DIGIT: return isLetterOrDigit(ch);
				case LETTER: return isLetter(ch);
				case DIGIT: return Character.isDigit(ch);
				case SIGN_OR_DIGIT: return Character.isDigit(ch) || ch == '-' || ch == '+';
				default: return true;
			}
		}
		
		/**
		 * Проверяет, соответствует ли символ данному элементу маски.<br>
		 * Пустые символы на обязательных позициях не допускаются.
		 * @param ch символ
		 * @return true, если соответствует, и false в противном случае
		 */
		boolean match(char ch) {
			return match(ch, false);
		}
	}
	
	/**
	 * Строковое представление маски.
	 */
	private final String stringValue;
	/**
	 * Список позиций маски.
	 */
	private final List<MaskItem> items;	
	/**
	 * Символ, используемый для вывода пустой обязательной позиции (по умолчанию - &quot;*&quot;).
	 */
	private char mandatoryChar = '*';	
	/**
	 * Символ, используемый для вывода пустой необязательной позиции (по умолчанию - &quot;_&quot;).
	 */
	private char optionalChar = '_';
	/**
	 * Таблица соответствия типов обязательных позиций и символов, 
	 * использующихся для вывода в пустой позиции.
	 */
	private Map<MaskItemType, Character> mandatoryCharMap = new HashMap<MaskItemType, Character>();
	/**
	 * Таблица соответствия типов необязательных позиций и символов, 
	 * использующихся для вывода в пустой позиции.
	 */
	private Map<MaskItemType, Character> optionalCharMap = new HashMap<MaskItemType, Character>();
	
	/**
	 * Создаёт маску из текстового представления.
	 * @param mask текстовое представление
	 */
	public Mask(String mask) {
		stringValue = mask;
		items = new ArrayList<MaskItem>();
		char[] chars = stringValue.toCharArray();
		boolean toUpper = false;
		boolean toLower = false;
		boolean skipNext = false;
		char optionalChar = '\0';
		for (int i = 0; i < chars.length; i++) {
			if (!skipNext) {
				char ch = chars[i];
				if (optionalChar != '\0' && ch != optionalChar && ch != '<' && ch != '>') {
					throw new IllegalArgumentException(JepTexts.maskedTextField_errorOptionalCharacters());
				}
				switch(ch) {
					case 'A': items.add(new MaskItem(LETTER_OR_DIGIT, toUpper, toLower, true, '\0')); break;
					case 'a': optionalChar = 'a'; items.add(new MaskItem(LETTER_OR_DIGIT, toUpper, toLower, false, '\0')); break;
					case 'C': items.add(new MaskItem(CHAR, toUpper, toLower, true, '\0')); break;
					case 'c': optionalChar = 'c'; items.add(new MaskItem(CHAR, toUpper, toLower, false, '\0')); break;
					case 'L': items.add(new MaskItem(LETTER, toUpper, toLower, true, '\0')); break;
					case 'l': optionalChar = 'l'; items.add(new MaskItem(LETTER, toUpper, toLower, false, '\0')); break;
					case '0': items.add(new MaskItem(DIGIT, toUpper, toLower, true, '\0')); break;
					case '9': optionalChar = '9'; items.add(new MaskItem(DIGIT, toUpper, toLower, false, '\0')); break;
					case '#': optionalChar = '#'; items.add(new MaskItem(SIGN_OR_DIGIT, toUpper, toLower, true, '\0')); break;
					case '<': {
						if (i < chars.length - 1) {
							char nextChar = chars[i+1];
							if (nextChar == '>') {
								toLower = false;
								toUpper = false;
								skipNext = true;
							}
							else {
								toLower = true;
								toUpper = false;
							}
						}
						break;
					}
					case '>': toUpper = true; toLower = false; break;
					case '\\': {
						if (i < chars.length - 1) {
							char nextChar = chars[i+1];
							items.add(new MaskItem(LITERAL, false, false, true, nextChar));
							skipNext = true;
						}
						break;
					}
					default: items.add(new MaskItem(LITERAL, false, false, true, ch)); break;				
				}
			}
			else {
				skipNext = false;
			}
		}
	}
	
	/**
	 * Вычисление эффективной длины массива символов.<br>
	 * Под эффективной длиной понимается номер позиции последнего ненулевого символа
	 * плюс один, либо ноль, если массив пуст.
	 * @param chars массив
	 * @return эффективная длина
	 */
	public static int getEffectiveLength(char[] chars) {
		int effectiveLength = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] != '\0') {
				effectiveLength = i + 1;
			}
		}
		return effectiveLength;
	}
	
	/**
	 * Проверка, пусто ли переданное значение.<br>
	 * Значение считается пустым, если все позиции, не являющиеся литералами, пусты.
	 * В позициях с литералами допускаются также символы, совпадающие с литералом.
	 * @param value значение
	 * @return true, если значение пусто, и false в противном случае
	 */
	public boolean isValueEmpty(char[] value) {
		checkInputArray(value);
		for (int i = 0; i < value.length; i++) {
			char ch = value[i];
			MaskItem item = items.get(i);
			if (ch != '\0') {
				if (item.itemType != LITERAL) {
					return false;
				}
				else {
					if (ch != item.literal) {
						throw new IllegalArgumentException(JepTexts.maskedTextField_errorCharacterNotMatchLiteral());
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Установка символа, который будет выводиться по умолчанию в пустой обязательной позиции.
	 * @param mandatoryChar символ
	 */
	public void setMandatoryChar(char mandatoryChar) {
		this.mandatoryChar = mandatoryChar;
	}
	/**
	 * Установка символа, который будет выводиться в пустой обязательной позиции данного типа.<br>
	 * Если в качестве символа передать null, то будет использоваться символ по умолчанию для 
	 * обязательных позиций.
	 * @param itemType тип символа
	 * @param ch символ
	 */
	public void setMandatoryChar(MaskItemType itemType, Character ch) {
		mandatoryCharMap.put(itemType, ch);
	}	
	/**
	 * Установка символа, который будет выводиться по умолчанию в пустой необязательной позиции.
	 * @param optionalChar символ
	 */
	public void setOptionalChar(char optionalChar) {
		this.optionalChar = optionalChar;
	}
	/**
	 * Установка символа, который будет выводиться в пустой необязательной позиции данного типа.<br>
	 * Если в качестве символа передать null, то будет использоваться символ по умолчанию для 
	 * необязательных позиций.
	 * @param itemType тип символа
	 * @param ch символ
	 */
	public void setOptionalChar(MaskItemType itemType, Character ch) {
		optionalCharMap.put(itemType, ch);
	}
	
	/**
	 * Возвращает длину маски (количество позиций).
	 * @return длина
	 */
	public int size() {
		return items.size();
	}
	
	/**
	 * Возвращает строковое представление маски.
	 * @return строковое представление маски
	 */
	@Override
	public String toString() {
		return stringValue;
	}
	
	/**
	 * Проверяет, соответствует ли массив символов маске.
	 * @param chars массив символов
	 * @param allowEmptyChars флаг, разрешающий пустые символы на обязательных позициях
	 * @return true, если массив соответствует маске, и false в противном случае
	 */
	public boolean match(char[] chars, boolean allowEmptyChars) {
		int length = allowEmptyChars ? getEffectiveLength(chars) : chars.length;
		if (length > size()) {
			return false;
		}
		/*
		 * Входной массив может быть короче маски, поэтому необходимо его дополнить.
		 */
		char[] newChars = new char[size()];
		System.arraycopy(chars, 0, newChars, 0, length);
		for (int i = 0; i < size(); i++) {
			if (!items.get(i).match(newChars[i], allowEmptyChars)) return false;
		}
		return true;
	}
	
	/**
	 * Проверяет, соответствует ли строка маске.
	 * @param str строка
	 * @return true, если строка соответствует маске, и false в противном случае
	 */
	public boolean match(String str) {
		if (str.length() > size()) {
			return false;
		}
		char[] chars = new char[size()];
		str.getChars(0, str.length(), chars, 0);
		return match(chars, false);
	}
	
	/**
	 * Проверяет, соответствует ли строка маске.
	 * @param str строка
	 * @param mask строковое представление маски
	 * @return true, если строка соответствует маске, и false в противном случае
	 */
	public static boolean match(String str, String mask) {
		return (new Mask(mask)).match(str);
	}

	/**
	 * Добавляет в переданный массив литералы.
	 * @param chars массив
	 */
	void applyLiterals(char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			if (items.get(i).itemType == LITERAL) {
				chars[i] = items.get(i).literal;
			}
		}
	}
	
	/**
	 * Проверяет, можно ли вставить символ в заданную позицию.
	 * @param chars массив символов, в который осуществляется вставка
	 * @param position позиция курсора
	 * @param ch символ
	 * @return true, если вставка допустима, и false в противном случае
	 */
	boolean canInsert(char[] chars, int position, char ch) {
		checkInputArray(chars);
		checkPosition(position);
		position = adjustCursorPosition(chars, position);
		
		if (position == size()) {
			// Курсор в конце - ничего ввести не можем.
			return false;
		}
		
		MaskItem item = items.get(position);
		
		if (item.itemType == LITERAL) {
			/*
			 * Встретили литерал - можем ввести только его.
			 */
			return ch == item.literal;
		}
		
		if (item.mandatory) {
			return item.match(ch) && (chars[position] == '\0' || canInsert(chars, position + 1, chars[position]));
		}
		else {
			return chars[size() - 1] == '\0' && item.match(ch);	
		}		
	}
	
	/**
	 * Проверяет, можно ли вставить строку в заданную позицию.
	 * @param chars массив символов, в который осуществляется вставка
	 * @param position позиция курсора
	 * @param inserted вставляемая строка
	 * @return true, если вставка допустима, и false в противном случае
	 */
	boolean canPaste(char[] chars, int position, String inserted) {
		checkInputArray(chars);
		checkPosition(position);
		position = adjustCursorPosition(chars, position);
		for (char ch : inserted.toCharArray()) {
			if (!canInsert(chars, position, ch)) {
				return false;
			}
			chars = insertChar(chars, position, ch);
			position++;
		}
		return true;
	}
	
	/**
	 * Очищает заданное length символов в массиве, начиная с position.
	 * @param chars массив символов
	 * @param position позиция курсора
	 * @param length количество очищаемых символов
	 * @return новый массив с очищенными символами либо исходный, если в нём ничего не изменилось
	 */
	char[] clearChars(char[] chars, int position, int length) {
		checkInputArray(chars);
		checkPosition(position);
		if (length < 1) {
			throw new IllegalArgumentException(JepTexts.maskedTextField_errorZeroSelectionLength());
		}
		if (position + length > size()) {
			throw new IllegalArgumentException(JepTexts.maskedTextField_errorSelectionExceedsMaskSize());
		}
		for (int i = position + length - 1; i >= position; i--) {
			chars = removeChar(chars, i);
		}
		return chars;
	}
	
	/**
	 * Формирование массива символов из строки, если она соответствует маске.<br>
	 * Если строка не соответствует маске, возвращает массив пустых символов длиной в маску.
	 * @param str строка
	 * @return представление строки в виде массива длиной в маску
	 */
	char[] getCharArray(String str) {
		char[] chars = new char[size()];
		if (match(str)) {
			str.getChars(0, str.length(), chars, 0);
		}
		return chars;
	}
	
	/**
	 * Возвращает позицию курсора после вставки символа в указанную позицию массива.
	 * @param chars массив символов
	 * @param position позиция курсора
	 * @param ch символ
	 * @return новая позиция курсора
	 */
	int getCursorPositionOnInsert(char[] chars, int position, char ch) {
		checkInputArray(chars);
		checkPosition(position);
		
		position = adjustCursorPosition(chars, position);
		
		if (position == size()) {
			return position;
		}
		
		MaskItem item = items.get(position);
		if (item.itemType == LITERAL) {
			// перепрыгиваем все литералы
			for (int i = 1; position + i < size(); i++) {
				if (items.get(position + i).itemType != LITERAL) {
					return position + i;
				}
			}
			return size();
		}
		if (item.mandatory) {
			if (!item.match(ch)) {
				return position;
			}
			else {
				// перепрыгиваем все литералы
				for (int i = 1; position + i < size(); i++) {
					if (items.get(position + i).itemType != LITERAL) {
						return position + i;
					}
				}
				return size();
			}
		}
		return chars[size() - 1] == '\0' && item.match(ch) ? position + 1 : position;		
	}

	/**
	 * Возвращает позицию курсора после попытки передвинуть его влево.<br>
	 * Если курсор находится в начале, возвращает 0. Если слева один или несколько
	 * литералов, курсор &quot;перепрыгивает&quot; через них.
	 * @param position исходная позиция курсора
	 * @return новая позиция курсора
	 */
	int getCursorPositionOnLeft(int position) {
		if (position == 0) {
			return position;
		}
		if (items.get(position - 1).itemType != LITERAL) {
			return position - 1;
		}
		while(position > 0 && items.get(position - 1).itemType == LITERAL) {
			position--;
		}
		
		return position;		
	}
	
	/**
	 * Возвращает позицию курсора после вставки.<br>
	 * @param chars массив символов
	 * @param position позиция курсора
	 * @param pasted вставляемая строка
	 * @return позиция курсора после вставки
	 */
	int getCursorPositionOnPaste(char[] chars, int position, String pasted) {
		checkInputArray(chars);
		checkPosition(position);
		position = adjustCursorPosition(chars, position);
		return position + pasted.length();
	}

	/**
	 * Возвращает позицию курсора после попытки передвинуть его вправо.<br>
	 * Если курсор находится в конце, возвращает size(). Если справа один или несколько
	 * литералов, курсор &quot;перепрыгивает&quot; через них.
	 * @param position исходная позиция курсора
	 * @return новая позиция курсора
	 */
	int getCursorPositionOnRight(int position) {
		if (position == size()) {
			return position;
		}
		if (items.get(position).itemType != LITERAL) {
			return position + 1;
		}
		while (position < size() && items.get(position).itemType == LITERAL) {
			position++;
		}
		return position;
	}
	
	/**
	 * Возвращает строковое представление массива символов с учётом маски.<br>
	 * Для пустых элементов выводится символ маски.
	 * @param charValue массив символов
	 * @param showEmptyChars если true, на месте пустых необязательных позиций выводятся символы маски
	 * @return строковое представление
	 */
	String getText(char[] charValue, boolean showEmptyChars) {
		String text = "";
		for (int i = 0; i < charValue.length; i++) {
			MaskItem item = items.get(i);
			if (item.itemType == LITERAL) {
				text += item.literal;
			}
			else {
				text += charValue[i] != '\0' ? charValue[i] : !item.mandatory && !showEmptyChars ? "" : getMaskChar(item);
			}
		}
		return text;
	}
	
	/**
	 * Возвращает результат вставки символа в массив в заданной позиции.
	 * @param chars массив символов, в который осуществляется вставка
	 * @param position позиция вставки
	 * @param ch символ
	 * @return новый массив с результатом вставки, либо старый, если ничего не изменилось
	 */
	char[] insertChar(char[] chars, int position, char ch) {
		checkInputArray(chars);
		checkPosition(position);
		position = adjustCursorPosition(chars, position);
		
		if (position == size()) {
			// Курсор в конце - ничего ввести не можем.
			return chars;
		}
		
		MaskItem item = items.get(position);
		
		if (item.itemType == LITERAL) {
			/*
			 * Встретили литерал - ничего не вводим.
			 */
			return chars;
		}
		
		if (item.toLower) {
			ch = Character.toLowerCase(ch);
		}
		else if (item.toUpper) {
			ch = Character.toUpperCase(ch);
		}
		
		char[] newChars = new char[size()];
		System.arraycopy(chars, 0, newChars, 0, size());
		
		if (item.mandatory) {
			if (item.match(ch)) {
				char oldChar = newChars[position];
				newChars[position] = ch;
				if (oldChar != '\0') {
					return insertChar(newChars, position + 1, oldChar);
				}
			}
			return newChars;
		}
		else {
			/*
			 * Пытаемся вставить символ на необязательную позицию, необходим сдвиг.
			 */
			if (chars[size() - 1] != '\0' || !item.match(ch)) {
				/*
				 * Последний символ занят, двигать некуда; либо этот символ сюда нельзя ввести.
				 */
				return newChars;
			}
			else {
				newChars[position] = ch;
				for(int i = position + 1; i < size(); i++) {
					newChars[i] = chars[i - 1];
				}
				return newChars;
			}
		}		
	}
	
	/**
	 * Возвращает результат вставки строки в массив символов в заданной позиции.
	 * @param chars массив символов
	 * @param position позиция курсора
	 * @param pasted вставляемая строка
	 * @return новый массив с результатом вставки, либо исходный, если ничего не изменилось
	 */
	char[] paste(char[] chars, int position, String pasted) {
		checkInputArray(chars);
		checkPosition(position);
		position = adjustCursorPosition(chars, position);
		char[] newChars = chars;
		for (char ch : pasted.toCharArray()) {
			if (canInsert(newChars, position, ch)) {
				newChars = insertChar(newChars, position, ch);
				position++;			
			}
			else {
				return chars;
			}
		}
		return newChars;
	}
	
	/**
	 * Улаление символа в массиве в заданной позиции.
	 * @param chars массив символов
	 * @param position позиция
	 * @return новый массив с удалённым символом, либо исходный, если ничего не изменилось
	 */
	char[] removeChar(char[] chars, int position) {
		checkPosition(position);
		checkInputArray(chars);
		MaskItem item = items.get(position);
		if (item.itemType == LITERAL) {
			return chars;
		}
		char[] newChars = new char[size()];
		for (int i = 0; i < position; i++) {
			newChars[i] = chars[i];
		}
		if (item.mandatory) {
			newChars[position] = '\0';
			for (int i = position + 1; i < size(); i++) {
				newChars[i] = i < chars.length ? chars[i] : '\0';
			}
		}
		else {
			for (int i = position; i < size() - 1; i++) {
				newChars[i] = i + 1 < chars.length ? chars[i+1] : '\0';
			}
		}
		
		return newChars;		
	}

	/**
	 * Служебный метод, осуществляющий корректировку позиции курсора.<br>
	 * Необходима, если слева от курсора есть вакантные позиции с необязательными символами.
	 * @param chars массив символов
	 * @param position позиция курсора
	 * @return скорректированная позиция курсора
	 */
	private int adjustCursorPosition(char[] chars, int position) {
		if (position == size() || !items.get(position).mandatory) {
			while (position > 0 && !items.get(position - 1).mandatory && chars[position - 1] == '\0') {
				position--;
			}
		}
		return position;
	}

	/**
	 * Служебный метод, осуществляющий выброс исключения, когда длина 
	 * входного массива не совпадает с длиной маски.
	 * @param chars массив символов
	 */
	private void checkInputArray(char[] chars) {
		if (chars.length != size()) {
			throw new IllegalArgumentException(JepTexts.maskedTextField_errorIllegalArraySize());
		}
	}

	/**
	 * Служебный метод, осуществляющий выброс исключения при некорректно
	 * заданной позиции курсора.
	 * @param position позиция курсора
	 */
	private void checkPosition(int position) {
		if (position < 0) {
			throw new IllegalArgumentException(JepTexts.maskedTextField_errorIllegalNegativeCursorPosition());
		}
		if (position > size()) {
			throw new IllegalArgumentException(JepTexts.maskedTextField_errorIllegalCursorPosition());
		}
	}
	
	/**
	 * Служебный метод, возвращающий символ маски для данного элемента.
	 * @param item элемент маски
	 * @return символ
	 */
	private String getMaskChar(MaskItem item) {
		boolean mandatory = item.mandatory;
		MaskItemType itemType = item.itemType;
		Character ch = mandatory ? mandatoryCharMap.get(itemType) : optionalCharMap.get(itemType);
		if (ch != null) {
			return ch.toString();
		}
		else {
			return String.valueOf(mandatory ? mandatoryChar : optionalChar);
		}
	}
	
	/**
	 * Проверяет, входит ли заданное число в диапазон, включая границы.
	 * @param value проверяемое значение
	 * @param min нижняя граница диапазона
	 * @param max верхняя граница диапазона
	 * @return true, если значение входит в диапазон, false в противном случае
	 */
	private static boolean inRange(int value, int min, int max) {
		return (value <= max) & (value >= min);
	}

	/**
	 * Проверяет, является ли переданный символ буквой.
	 * Использование данного метода вместо {@link Character#isDigit(char)}
	 * обусловлено неполноценной реализацией данного метода в GWT.
	 * @param ch символ
	 * @return true, если символ является буквой, false в противном случае
	 * @see <a href="https://github.com/gwtproject/gwt/issues/1989">https://github.com/gwtproject/gwt/issues/1989</a>
	 */
	private static boolean isLetter(char ch) {
		int val = (int) ch;

		return inRange(val, 65, 90) || inRange(val, 97, 122) || inRange(val, 192, 687) 
				|| inRange(val, 900, 1159) || inRange(val, 1162, 1315) || inRange(val, 1329, 1366) 
				|| inRange(val, 1377, 1415) || inRange(val, 1425, 1610);
	}
	
	/**
	 * Проверяет, является ли переданный символ буквой или цифрой.
	 * Использование данного метода вместо {@link Character#isLetterOrDigit(char)}
	 * обусловлено неполноценной реализацией данного метода в GWT.
	 * @param ch символ
	 * @return true, если символ является буквой или цифрой, false в противном случае
	 * @see <a href="https://github.com/gwtproject/gwt/issues/1989">https://github.com/gwtproject/gwt/issues/1989</a>
	 */
	private static boolean isLetterOrDigit(char ch) {
		return Character.isDigit(ch) || isLetter(ch);
	}
	
}
