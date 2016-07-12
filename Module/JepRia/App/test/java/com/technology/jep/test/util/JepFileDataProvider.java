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
    
    List<Object[]> ret = new ArrayList<Object[]>();
    
    List<String> rawLines = JepFileDataProvider.getRawLinesFromFile(arguments.get("filePath"));
    StringBuilder dataSb = new StringBuilder();
    
    for (String line: rawLines) {
      line = line.trim();
      if(line.length() > 0 && line.charAt(0) == '#') { // Пропускаем комментарий
        continue;
      }
      
      dataSb.append(line.toString()).append("\n");
    }
    
    String data = dataSb.toString().replaceAll("\\|\n", "\\|");
    
    data = data.replaceAll("^\n+", "").replaceAll("\n\n+", "\n").replaceAll("\n+$", "");
    
    for (String argSet: data.split("\n")) {
      ret.add(argSet.split("\\|"));
    }
    
    return ret.iterator();
    
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