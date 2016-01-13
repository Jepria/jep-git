package com.technology.jep.jepriashowcase.custom.server.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.EndTransactionHandler;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.EndTransactionHandlerImpl;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.TransactionHandler;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.StartTransactionHandler;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.StartTransactionHandlerImpl;
import com.technology.jep.jepriashowcase.custom.server.dao.annotation.handler.TransactionHandlerImpl;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MultipleTransactional {
	String name() default "ModuleTransaction";
	Class<? extends TransactionHandler> handler() default TransactionHandlerImpl.class;
	Class<? extends StartTransactionHandler> startTransactionHandler() default StartTransactionHandlerImpl.class;
	Class<? extends EndTransactionHandler> endTransactionHandler() default EndTransactionHandlerImpl.class;
}
