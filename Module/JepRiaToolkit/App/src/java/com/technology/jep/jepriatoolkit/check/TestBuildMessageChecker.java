package com.technology.jep.jepriatoolkit.check;

import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.*;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepria.client.JepRiaClientConstant.TEST_BUILD_MESSAGE_CLASS;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class TestBuildMessageChecker extends Task {

  /**
   * Атрибут таска. Директория, в которой лежат jsp на проверку.
   */
  private String jspDir = null;
  
  public void setJspDir(String jspDir) {
    this.jspDir = jspDir;
  }
  
  @Override
  public void execute() throws BuildException {
    hideBuildMessage();
  }

  /**
   * Скрывает Test Build Message. <br/>
   * Делает поиск в jsp по классу контейнера, добавляет правила css.
   */
  private void hideBuildMessage() {
    /*
    echoMessage("Begin sleep");
    echoMessage("Time to start remote debug.");
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    echoMessage("End sleep");
    */
    if(jspDir == null) {
      echoMessage("jspDir is not set. Exit.");
      return;
    }
    
    File jspDirectory = new File(jspDir);
    File [] jspFiles = jspDirectory.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".jsp");
      }
    });

    try {
      String jspEncoding = UTF_8;
      //Регуляка для поиска тега с классом TEST_BUILD_MESSAGE_CLASS
      String regexBuildMessageClass = "(class=\"<%=\\s*JepRiaClientConstant\\.TEST_BUILD_MESSAGE_CLASS\\s*%>\"[^>]*>)";
      String hideBuildMessageCss = multipleConcat(
          "$1",
          "<style type=\"text/css\">",
            ".", TEST_BUILD_MESSAGE_CLASS, " {",
              "display: none;",
            "}",
          "</style>");
      
      for (File jsp : jspFiles) {
        String jspPath = jsp.getAbsolutePath();
        String jspData = readFromFile(jspPath, jspEncoding);
        
        if(jspData.contains(TEST_BUILD_MESSAGE_CLASS)) {
          writeToFile(jspData.replaceFirst(regexBuildMessageClass, hideBuildMessageCss), jspPath, jspEncoding);
        }
      }
    } catch (IOException e) {
      throw new BuildException(e);
    }
  }
}
