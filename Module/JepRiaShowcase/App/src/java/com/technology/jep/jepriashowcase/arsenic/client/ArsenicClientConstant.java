package com.technology.jep.jepriashowcase.arsenic.client;
 
import com.google.gwt.core.client.GWT;
import com.technology.jep.jepriashowcase.arsenic.client.images.ArsenicImages;
import com.technology.jep.jepriashowcase.arsenic.shared.ArsenicConstant;
import com.technology.jep.jepriashowcase.arsenic.shared.text.ArsenicText;
 
public class ArsenicClientConstant extends ArsenicConstant {
 
	public static final ArsenicImages arsenicImages = (ArsenicImages) GWT.create(ArsenicImages.class);
	
	public static ArsenicText arsenicText = (ArsenicText) GWT.create(ArsenicText.class);
}
