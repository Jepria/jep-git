package com.technology.jep.jepria.server.dao.transaction.handler;

import com.technology.jep.jepria.server.dao.CallContext;

/**
 * Интерфейс обработчика завершения транзакции.<br/>
 * При кастомной реализации обработчика рекомендуется наследовать стандартную
 * реализацию {@link EndTransactionHandlerImpl} вместо того, чтобы реализовывать
 * с нуля.
 */
public interface EndTransactionHandler {
	/**
	 * Метод, выполняемый после завершения транзакции.<br/>
	 * При кастомной реализации необходимо выполнение следующих требований:
	 * <ul>
	 *   <li>Если в ходе транзакции не возникло исключения, то необходимо
	 *   зафиксировать транзакцию, вызвав {@link CallContext#commit()}.</li>
	 *   <li>Если было перехвачено исключение (<code>caught != null</code>), необходимо 
	 *   откатить транзакцию, вызвав {@link CallContext#rollback()}.</li>
	 *   <li>В любом случае необходимо освободить ресурсы, вызвав {@link CallContext#end()}.
	 *   <li>Если в ходе транзакции возникло исключение, или же оно возникло во время
	 *   commit либо rollback, следует выбросить последнее возникшее исключение.</li>
	 * </ul>
	 * @param caught перехваченное исключение
	 * @throws Exception последнее выброшенное исключение
	 */
	void handle(Throwable caught) throws Exception;
}
