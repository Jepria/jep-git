package com.technology.jep.jepriashowcase.requestprocess.client;
 
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.requestprocess.shared.RequestProcessConstant;
import com.technology.jep.jepriashowcase.requestprocess.shared.text.RequestProcessText;
 
public class RequestProcessClientConstant extends RequestProcessConstant {
 
  public static RequestProcessText requestProcessText = (RequestProcessText) GWT.create(RequestProcessText.class);
}
