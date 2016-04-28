package com.technology.jep.jepriatoolkit.log;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.END_OF_LINE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.INFO_PREFIX;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.writeToFile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class Logger {

	private static Map<String, String> messages = new LinkedHashMap<String, String>();
	private static Map<String, String> lazyMessages = new LinkedHashMap<String, String>();
	
	private static final String ALL_INFO_ABOUT_APPLICATION = null;
	
	private Logger(){}
	
	public static void clearMessages(){
		messages.clear();
		lazyMessages.clear();
	}
	
	public static void appendMessage(String content){
		String existedContent = messages.get(ALL_INFO_ABOUT_APPLICATION);
		if (!JepRiaToolkitUtil.isEmpty(existedContent)){
			content = multipleConcat(existedContent, END_OF_LINE, content);
		}
		messages.put(ALL_INFO_ABOUT_APPLICATION, content);
	}
	
	public static void appendMessageToForm(String moduleId, String content){
		String existedContent = messages.get(moduleId);
		if (!JepRiaToolkitUtil.isEmpty(existedContent)){
			content = multipleConcat(existedContent, END_OF_LINE, content);
		}
		messages.put(moduleId, content);
	}
	
	public static void appendMessageToTheEndOfForm(String moduleId, String content){
		String existedContent = lazyMessages.get(moduleId);
		if (!JepRiaToolkitUtil.isEmpty(existedContent)){
			content = multipleConcat(existedContent, END_OF_LINE, content);
		}
		lazyMessages.put(moduleId, content);
	}
	
	public static void printMessages(String fileName){
		echoMessage(multipleConcat(INFO_PREFIX, "Please look into the file '", fileName, "' to get all process information!"));
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : messages.entrySet()){
			builder.append(builder.toString().isEmpty() ? "" : "****************").append(END_OF_LINE);
			builder.append(entry.getValue()).append(END_OF_LINE);
			String key = entry.getKey();
			if (lazyMessages.containsKey(key)){
				builder.append(lazyMessages.get(key)).append(END_OF_LINE);
			}
		}
		try {
			writeToFile(builder.toString(), fileName);
		}
		finally {
			clearMessages();	
		}
	}
}
