package com.technology.jep.jepriareport.server.large;

import java.util.List;

import javax.ejb.*;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.report.JepReportParameters;
import com.technology.jep.jepriareport.server.JepReportRecord;

/**
 * Интерфейс EJB для работы с большими отчётами
 */
public interface JepLargeReport {
	/**
	 * Начало длинной транзакции чтения записей отчёта
	 * 
	 * @param parameters параметры отчёта
	 * @return рекомендуемый размер буфера
	 * @throws ApplicationException
	 */
	int begin(JepReportParameters parameters) throws ApplicationException;

	/**
	 * Чтение очередного блока записей
	 * 
	 * @param size размер запрашиваемого блока
	 * @return список записей блока. Признаком конца отчёта (отсутствия новых
	 *         записей) является возвращение null.
	 */
	List<JepReportRecord> nextBlock(int size);

	/**
	 * Окончание работы После выполнения этого метода stateful bean должен быть
	 * удалён. Для удаления bean необходимо в классе реализации перед методом
	 * указать декларацию Remove.
	 */
	@Remove
	void end();

	/**
	 * Откат длинной транзакции. После выполнения этого метода stateful bean
	 * должен быть удалён. Для удаления bean необходимо в классе реализации
	 * перед методом указать декларацию Remove.
	 */
	@Remove
	void cancel();

}
