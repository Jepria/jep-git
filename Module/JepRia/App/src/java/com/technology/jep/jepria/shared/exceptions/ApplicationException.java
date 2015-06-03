package com.technology.jep.jepria.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Общий предок исключений, "выбрасывааемых" приложением.
 */
public class ApplicationException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 1L;

	public ApplicationException() {
	}
	
	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
}
