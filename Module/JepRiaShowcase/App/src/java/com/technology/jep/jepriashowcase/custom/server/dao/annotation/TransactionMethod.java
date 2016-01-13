package com.technology.jep.jepriashowcase.custom.server.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TransactionMethod {
	String name() default "ModuleTransaction";
	int runOrder();
}
