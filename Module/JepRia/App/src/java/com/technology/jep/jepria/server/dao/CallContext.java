package com.technology.jep.jepria.server.dao;

import java.sql.SQLException;

import com.technology.jep.jepria.server.db.Db;

/**
 * Класс, реализующий получение соединения, управление транзакцией и освобождение ресурсов.<br/>
 * Объект соединения хранится в ходе выполнения в {@code ThreadLocal}.
 */
public class CallContext {
	/**
	 * Контейнер для хранения контекста.
	 */
	private static ThreadLocal<CallContext> context = new ThreadLocal<CallContext>();
	
	/**
	 * Обёртка соединения с базой.
	 */
	private Db db = null;
	
	/**
	 * Создаёт объект контекста.
	 * @param dataSourceJndiName JNDI-имя источника данных
	 */
	private CallContext(String dataSourceJndiName) {
		db = new Db(dataSourceJndiName, false);
	}
	
	/**
	 * Метод, предваряющий начало транзакции.<br/>
	 * Создаёт новый экземпляр контекста и помещает в {@code ThreadLocal}.
	 * @param dataSourceJndiName JNDI-имя источника данных
	 */
	public static void begin(String dataSourceJndiName) {
		CallContext callContext = new CallContext(dataSourceJndiName);
		context.set(callContext);
	}
	
	/**
	 * Возвращает объект соединения.
	 * @return объект соединения
	 */
	public static Db getDb() {
		CallContext c = (CallContext) context.get();
		return c.db;
	}

	/**
	 * Метод, освобождающий ресурсы после завершения транзакции.<br/>
	 * Отвечает за закрытие соединения и других ресурсов, а также
	 * удаление контекста из {@code ThreadLocal}
	 */
	public static void end() {
		CallContext c = detach();
		if (c != null) {
			c.close();
		}
	}

	/**
	 * Закрывает соединение и очищает соответствующее поле.
	 */
	private void close() {
		if (db != null) {
			db.closeAll();
		}
		this.db = null;
	}

	/**
	 * Записывает контекст в {@code ThreadLocal}.
	 * @param callContext контекст
	 */
	public static void attach(CallContext callContext) {
	    context.set(callContext);
	}

	/**
	 * Возвращает контекст, удаляя его из {@code ThreadLocal}.
	 * @return контекст
	 */
	public static CallContext detach() {
		CallContext result = (CallContext) context.get();
		context.set(null);
		return result;
	}
	
	/**
	 * Выполняет фиксацию (commit) текущей транзакции.
	 * @throws SQLException в случае, если соединение выбросило исключение
	 */
	public static void commit() throws SQLException {
		getDb().commit();
	}
	
	/**
	 * Выполняет откат (rollback) текущей транзакции.
	 * @throws SQLException в случае, если соединение выбросило исключение
	 */
	public static void rollback() throws SQLException {
		getDb().rollback();
	}
}
