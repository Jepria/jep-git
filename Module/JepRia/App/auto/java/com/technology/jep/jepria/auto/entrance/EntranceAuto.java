package com.technology.jep.jepria.auto.entrance;



/**
 * Базовый интерфейс функциональности доступа
 */
public interface EntranceAuto extends AutoBase {
	/**
	 * Вход в систему
	 * 
	 * @param username имя пользователя
	 * @param password пароль
	 */
	void login(String username, String password);
	
	/**
	 * Проверка был ли вход
	 * 
	 * @return true, если вход выполнен, иначе - false
	 */
	boolean isLoggedIn();
	
	/**
	 * Выход из приложения
	 */
	void logout();
}
