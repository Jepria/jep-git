package com.technology.jep.jepriashowcase.request.client;
 
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.*;
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.request.shared.RequestConstant;
import com.technology.jep.jepriashowcase.request.shared.text.RequestText;
 
public class RequestClientConstant extends RequestConstant {
 
	public static String[] scopeModuleIds = {REQUEST_MODULE_ID, REQUESTPROCESS_MODULE_ID}; 
 
	public static RequestText requestText = (RequestText) GWT.create(RequestText.class);
	
	public final static String REQUEST_DATE_CUSTOM_VALIDATOR_ID = "REQUEST_DATE_CUSTOM_VALIDATOR_ID";
	
	public final static String BEGIN_END_DATE_CUSTOM_VALIDATOR_ID = "BEGIN_END_DATE_CUSTOM_VALIDATOR_ID";
}
