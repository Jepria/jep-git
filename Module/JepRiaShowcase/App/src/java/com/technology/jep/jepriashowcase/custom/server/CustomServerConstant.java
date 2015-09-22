package com.technology.jep.jepriashowcase.custom.server;

import com.technology.jep.jepria.server.JepRiaServerConstant;
/**
 * Константы для Custom.
 */
public class CustomServerConstant extends JepRiaServerConstant {
	/**
	 * JNDI-имя EJB модуля.
	 */
	public static final String BEAN_JNDI_NAME = "CustomBean";
	
	/**
	 * JNDI-имя источника данных модуля.
	 */
	public static final String DATA_SOURCE_JNDI_NAME = "jdbc/RFInfoDS";

	/**
	 * Идентификатор файла ресурсов.
	 */
	public static final String RESOURCE_BUNDLE_NAME = "com.technology.jep.jepriashowcase.custom.shared.text.CustomText";
	
}
