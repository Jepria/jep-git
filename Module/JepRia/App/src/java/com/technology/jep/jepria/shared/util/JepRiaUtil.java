package com.technology.jep.jepria.shared.util;

import static com.technology.jep.jepria.shared.JepRiaConstant.UNDEFINED_INT;

import java.util.List;

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
	 * @return возвращает true, если передано занчение null или переданная
	 *         строка состоит только из пробелов.
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
		} else if (obj instanceof ArrayList){
			return ((ArrayList)obj).size()==0;
		}
		return false;
	}

	/**
	 * Функция определяет: является ли переданная ей величина пустой
	 * (неопределенной).
	 * 
	 * @param sourceInteger исходная величина, которую проверяем
	 * 
	 * @return возвращает true, если передано занчение null или переданная
	 *         величина равна
	 *         {@link com.technology.jep.jepria.shared.JepRiaConstant#UNDEFINED_INT}.
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
	 * @return возвращает true, если переданная величина равна
	 *         {@link com.technology.jep.jepria.shared.JepRiaConstant#UNDEFINED_INT}.
	 */
	public static boolean isEmpty(int sourceInt) {
		return (sourceInt == UNDEFINED_INT) ? true : false;
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
	 * @return возвращает true, если переданная строка может быть преобразована  
	 *         к типу Integer.
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
