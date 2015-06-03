package com.technology.jep.jepria.server.version.ejb;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;

/**
 * Интерфейс для поддержки версионности приложений.
 */
public interface JepVersion {

	/**
	 * Добавляет результат установки приложения.
	 * 
	 * @param moduleSvnRoot					путь к корневому каталогу
	 * @param moduleInitialSvnPath			первоначальный путь к корневому каталогу
	 * @param moduleVersion					версия модуля
	 * @param deploymentPath				путь для развертывания приложения
	 * @param installVersion				устанавливаемая версия приложения
	 * @param installDate					дата завершения установки
	 * @param operatorId					идентификатор пользователя
	 * @return идентификатор добавленной записи 
	 * @throws ApplicationException
	 */
	Integer createAppInstallResult(
			String moduleSvnRoot
			, String moduleInitialSvnPath
			, String moduleVersion
			, String deploymentPath
			, String installVersion
			, String installDate
			, Integer operatorId) throws ApplicationException;
}
