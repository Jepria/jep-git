package com.technology.jep.jepria.auto.entrance;

import com.technology.jep.jepria.auto.HasText;


public interface AutoBase {
	/**
	 * Ожидание изменения текста в заданном компоненте HasText
	 * 
	 * @param hasText - объект типа HasText
	 * @param currentDisplayText текущий текст заданного компонента HasText
	 */
	void waitTextToBeChanged(HasText hasText, String currentDisplayText);
	
	/**
	 * Проверка готовности компонента к работе
	 * @return true, если компонент готов, иначе - false
	 */
	boolean isReady();
	
	/**
	 * TODO ...
	 */
	void openMainPage(String url);
}
