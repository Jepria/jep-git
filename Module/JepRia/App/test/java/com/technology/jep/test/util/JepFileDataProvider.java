package com.technology.jep.test.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.DataProvider;

public class JepFileDataProvider {
	@DataProvider(name = "dataFromFile")
	public static Iterator<Object[]> getDataFromFile(Method testMethod) throws Exception {
		Map<String, String> arguments = DataProviderUtils.resolveDataProviderArguments(testMethod);
		List<String> lines = JepFileDataProvider.getRawLinesFromFile(arguments.get("filePath"));
		List<Object[]> data = new ArrayList<Object[]>();
		
		for (String line : lines) {
			line = line.trim();
			if(line.length() == 0 || line.charAt(0) == '#') { // Пропускаем комментарий
				continue;
			}
			
			data.add(line.split("\\|"));
		}
		
		return data.iterator();
	}

	public static List<String> getRawLinesFromFile(Method testMethod) throws Exception {
		Map<String, String> arguments = DataProviderUtils.resolveDataProviderArguments(testMethod);
		return JepFileDataProvider.getRawLinesFromFile(arguments.get("filePath"));
	}

	public static List<String> getRawLinesFromFile(String filePath) throws IOException {
		InputStream is = new FileInputStream(new File(filePath));
		List<String> lines = IOUtils.readLines(is, "UTF-8");
		is.close();
		return lines;
	}
}