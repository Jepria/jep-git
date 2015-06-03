package com.technology.jep.jepria.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Выбрасывается при обнаружении нереализованной функциональности
 */
public class NotImplementedYetException extends RuntimeException implements IsSerializable {
	private static final long serialVersionUID = -1260238884177943712L;

	public NotImplementedYetException() {
	}
	
	public NotImplementedYetException(String message) {
		super(message);
	}

}
