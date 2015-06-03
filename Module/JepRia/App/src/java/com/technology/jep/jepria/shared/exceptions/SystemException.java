package com.technology.jep.jepria.shared.exceptions;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Общий предок исключений, "порождаемых" платформой (J2EE-контейнером, базой данных и т.п.).
 * TODO реализовать вложенность
 */
public class SystemException extends RuntimeException implements IsSerializable {
	private static final long serialVersionUID = 1L;
	
	// cause и stackTrace сохраняются для обхода особенностей механизмов передачи исключений в Gwt
	// TODO Подумать, как обойтись без этого 
	// TODO Подумать, как показать ApplicationException, выбрасываемый EJB
	private Throwable cause = null;
	private StackTraceElement[] stackTrace = null;

	public SystemException() {
	}
	
	public SystemException(String message, Throwable cause) {
		super(message, cause);
		this.cause = cause;
		if(cause != null) { 
			this.stackTrace = cause.getStackTrace();
		}
	}
	
	public SystemException(String message) {
		this(message, null);
	}
	
	public Throwable getCause() {
		return cause;
	}

	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

}
