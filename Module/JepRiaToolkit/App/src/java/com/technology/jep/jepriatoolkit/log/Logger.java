package com.technology.jep.jepriatoolkit.log;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.*;

import java.util.ArrayList;
import java.util.List;

public class Logger {

	private static List<String> messages = new ArrayList<String>();
	
	private Logger(){}
	
	public static void clearMessages(){
		messages.clear();
	}
	
	public static void appendMessage(String content){
		messages.add(content);
	}
	
	public static void printMessages(String fileName){
		echoMessage(multipleConcat(INFO_PREFIX, "Please look into the file '", fileName, "' to get all process information!"));
		StringBuilder builder = new StringBuilder();
		for (String message : messages){
			builder.append(message).append(END_OF_LINE);
		}
		writeToFile(builder.toString(), fileName);
	}
}
