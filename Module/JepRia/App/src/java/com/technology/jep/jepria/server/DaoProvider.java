package com.technology.jep.jepria.server;

public interface DaoProvider<D> {
	D getDao();
	String getDataSourceJndiName();
}
