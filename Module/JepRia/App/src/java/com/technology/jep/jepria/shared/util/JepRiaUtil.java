package com.technology.jep.jepria.shared.util;

import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.lob.JepClob;

import java.util.List;

import static com.technology.jep.jepria.shared.JepRiaConstant.UNDEFINED_INT;

public class JepRiaUtil {

	public static Object[] cloneArray(Object[] array) {
		Object[] result = new Object[array.length];
		for(int i = 0; i < array.length; i++) {
			result[i] = array[i];
		}
		return result;
	}
	

	/**
	 * Функция определяет: является ли переданная ей строка пустой.
	 * 
	 * @param sourceString исходная строка, которую проверяем
	 * 
	 * @return возвращает true, если передано занчение null или переданная строка состоит только из пробелов.
	 */
	public static boolean isEmpty(String sourceString) {
		return (sourceString == null || sourceString.trim().length() == 0) ? true : false;
	}

	/**
	 * Функция определяет: является ли переданный объект пустым.
	 * 
	 * @param obj проверяемый объект
	 */
	public static boolean isEmpty(Object obj) {
		if(obj == null) {
			return true;
		} else if(obj instanceof String) {
			return isEmpty((String)obj);
		} else if(obj instanceof Integer) {
			return isEmpty((Integer)obj);
		} else if (obj instanceof List){
			return isEmpty((List<?>)obj);
		} else if (obj instanceof JepOption) {
			return isEmpty((JepOption) obj);
		} else if (obj instanceof JepClob) {
			return isEmpty((JepClob) obj);
		}
		return false;
	}

	/**
	 * Функция определяет: является ли переданная ей величина пустой
	 * (неопределенной).
	 * 
	 * @param sourceInteger исходная величина, которую проверяем
	 * 
	 * @return возвращает true, если передано занчение null или переданная величина равна
	 * {@link com.technology.jep.jepria.shared.JepRiaConstant#UNDEFINED_INT}.
	 */
	public static boolean isEmpty(Integer sourceInteger) {
		return (sourceInteger == null || sourceInteger.intValue() == UNDEFINED_INT) ? true : false;
	}

	/**
	 * Функция определяет: является ли переданная ей величина пустой
	 * (неопределенной).
	 * 
	 * @param sourceInt исходная величина, которую проверяем
	 * 
	 * @return возвращает true, если переданная величина равна {@link com.technology.jep.jepria.shared.JepRiaConstant#UNDEFINED_INT}.
	 */
	public static boolean isEmpty(int sourceInt) {
		return (sourceInt == UNDEFINED_INT) ? true : false;
	}
	/**
	 * Определяет, является ли переданный элемент пустым.<br>
	 * Возвращает true, если передано null либо если передан пустой элемент.
	 * @param option {@link com.technology.jep.jepria.shared.field.option.JepOption#JepOption}
	 * @return true, если значение пусто, false - в противном случае
	 */
	public static boolean isEmpty(JepOption option) {
		return (option == null || option.equals(JepOption.EMPTY_OPTION)) ? true : false;
	}

	/**
	 * Определяет, является ли переданный элемент пустым.<br>
	 * Возвращает true, если передано null либо если передан пустой элемент.
	 * @param clob {@link com.technology.jep.jepria.shared.record.lob.JepClob}
	 * @return true, если значение пусто, false - в противном случае
	 */
	public static boolean isEmpty(JepClob clob) {
		return (clob == null || JepRiaUtil.isEmpty(clob.getBigText())) ? true : false;
	}
	
	/**
	 * Определяет, является ли переданный список пустым.<br>
	 * Возвращает true, если передано null либо если передан пустой список.
	 * @param sourceList список
	 * @return true, если значение пусто, false - в противном случае
	 */
	public static boolean isEmpty(List<?> sourceList) {
		return (sourceList == null || sourceList.isEmpty());
	}

	/**
	 * Сравнение объектов на равенство.
	 * 
	 * @param obj1			первый сравниваемый объект
	 * @param obj2			второй сравниваемый объект
	 * @return признак равенства сравниваемых объектов
	 */
	public static boolean equalWithNull(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		} else if (obj1 == null) {
			return false;
		} else {
			return obj1.equals(obj2);
		}
	}
	
	/**
	 * Функция определяет: может ли переданная ей строка быть преобразована 
	 * к типу Integer.
	 * 
	 * @param value строка, которую проверяем
	 * 
	 * @return возвращает true, если переданная строка может быть преобразована к типу Integer.
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Добавление строки в конец основной (если она не содержит добавляемую).
	 * 
	 * @param baseLine			основная строка
	 * @param newLine			добавляемая строка
	 * @return строка с прибавлением новой
	 */
	public final static String appendStrIfNotPresent(String baseLine, String newLine){
		if (JepRiaUtil.isEmpty(baseLine)) baseLine = "";
		int idx = indexOfLine(baseLine, newLine);

		// Only add the style if it's not already present.
		if (idx == -1) {
			if (baseLine.length() > 0) {
				return baseLine + " " + newLine;
			} 
			else {
				return newLine;
			}
		}
		return baseLine; 
	}
	
	/**
	 * Определение индекса вхождения строки в строке
	 * 
	 * @param nameList			основная строка
	 * @param name				проверяемая на вхождение строка
	 * @return индекс вхождения строки
	 */
	public final static int indexOfLine(String nameList, String name) {
		int idx = nameList.indexOf(name);

		// Calculate matching index.
		while (idx != -1) {
			if (idx == 0 || nameList.charAt(idx - 1) == ' ') {
				int last = idx + name.length();
				int lastPos = nameList.length();
				if ((last == lastPos)
						|| ((last < lastPos) && (nameList.charAt(last) == ' '))) {
					break;
				}
			}
			idx = nameList.indexOf(name, idx + 1);
		}

		return idx;
	}
	
	/**
	 * Удаление строки из основной (если она содержит удаляемую).
	 * 
	 * @param baseLine основная строка
	 * @param removedLine удаляемая строка
	 * @return новая строка
	 */
	public final static String removeStrIfPresent(String baseLine, String removedLine) {
		if (JepRiaUtil.isEmpty(baseLine)) {
			return baseLine;
		}
		int idx = indexOfLine(baseLine, removedLine);
		// Don't try to remove the style if it's not there.
		if (idx != -1) {
			// Get the leading and trailing parts, without the removed name.
			String begin = baseLine.substring(0, idx).trim();
			String end = baseLine.substring(idx + removedLine.length()).trim();

			// Some contortions to make sure we don't leave extra spaces.
			String newClassName;
			if (begin.length() == 0) {
				newClassName = end;
			} else if (end.length() == 0) {
				newClassName = begin;
			} else {
				newClassName = begin + " " + end;
			}
			return newClassName;
		}
		return baseLine;
	}
}
