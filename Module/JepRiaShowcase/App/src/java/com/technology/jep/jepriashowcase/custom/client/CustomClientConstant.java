package com.technology.jep.jepriashowcase.custom.client;

import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.custom.shared.CustomConstant;
import com.technology.jep.jepriashowcase.custom.shared.text.CustomText;

public class CustomClientConstant extends CustomConstant {
	
	/**
	 * Тексты клиентской части Custom.
	 */
	public static CustomText customText = (CustomText) GWT.create(CustomText.class);
}
