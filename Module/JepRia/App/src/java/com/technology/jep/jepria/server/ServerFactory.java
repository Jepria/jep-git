package com.technology.jep.jepria.server;

public interface ServerFactory<D> {
	D getDao();
}
