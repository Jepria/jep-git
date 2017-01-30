package com.technology.jep.jepriatoolkit.switcher;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_CONFIG_FILE_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_CONFIG_PATH_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class BuildConfigSwitcher extends Task {

  private String packageName, moduleName;
  private String currentConfig, targetConfig;
  private String configPath;
  private List<String> targetConfigFiles, currentConfigFiles, commonConfigFiles, updateConfigFiles;

  /**
   * Основной метод, который выполняет переключение BuildConfig
   */
  @Override
  public void execute() throws BuildException {

    // получаем список файлов target конфигурации, если конфиг не найден (null) - критическая ошибка!
    targetConfigFiles = getFileList(BUILD_CONFIG_PATH_PREFIX + targetConfig);
    if (targetConfigFiles == null) {
      throw new BuildException(JepRiaToolkitUtil.multipleConcat(
          ERROR_PREFIX, "Target configuration \"", targetConfig, "\" not found!"));
    }
    printList("Target configuration: " + targetConfig, targetConfigFiles);
    updateConfigFiles = new ArrayList<String>(targetConfigFiles);

    // получаем список файлов current конфигурации из build.config
    currentConfig = getCurrentConfigName();
    currentConfigFiles = loadConfigFileList();

    if (currentConfigFiles != null) {
      printList("Current configuration: " + currentConfig, currentConfigFiles);
      
      // если есть текущий профиль:
      // сравниваем пофайлово исходники и current конфигцрацию, если исходники новее - критическая ошибка!
      File configElement, srcElement;
      long configElementLastModified, srcElementLastModified;
      
      try {
        for (String element : currentConfigFiles) {
          configElement = new File(BUILD_CONFIG_PATH_PREFIX + currentConfig + "\\" + element);
          srcElement = new File(element);
          
          if (configElement.isFile()) {
            configElementLastModified = configElement.lastModified();

            if (srcElement.exists()) {
              srcElementLastModified = srcElement.lastModified();
              
              // если отличается содержимое (или длинна) и иходник изменен последним
              if (checkBinaryChanged(configElement, srcElement) 
                  && srcElementLastModified > configElementLastModified) {
                // выводим в лог имя файла
                JepRiaToolkitUtil.echoMessage(ERROR_PREFIX + srcElement.getPath() + " - changed!");
                // и аварийно завершаем таск
                throw new BuildException(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX,
                  "Current build configuration has changed - backup before applying new configuration! See log for details."));
              }
            }
          }
        }
      } catch (IOException e) {
        // e.printStackTrace();
        JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
      }

      // Строим список common как пересечение сurrent и target конфигураций
      commonConfigFiles = new ArrayList<String>(); 
      for (String element : currentConfigFiles) {
        if (updateConfigFiles.contains(element)) {
          // заносим в common
          commonConfigFiles.add(element);
          // выносим из target
          updateConfigFiles.remove(element);
        }
      }
      //printList("Common files (target & current):", commonConfigFiles);
      //printList("Update files (target - current):", updateConfigFiles);
      
      // Сравниваем исходники с target по списку common, файлы без изменений добавляем обратно в target
      try {
        for (String element : commonConfigFiles) {
          configElement = new File(BUILD_CONFIG_PATH_PREFIX + targetConfig + "\\" + element);
          srcElement = new File(element);
          
          if (configElement.isFile() && srcElement.exists()) {
            if (checkBinaryChanged(configElement, srcElement)) {
              // если есть изменения переносим элемент в список update (будем заменять)
              updateConfigFiles.add(element);
            } else {
              // если изменений нет удаляем элемент из списка current (не будем заменять)
              currentConfigFiles.remove(element);
            }
          }
        }
      } catch (IOException e) {
        // e.printStackTrace();
        JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
      }
      
      // удаляем build.config
      currentConfigFiles.add(BUILD_CONFIG_FILE_NAME);
      //new File(BUILD_CONFIG_FILE_NAME).delete();
      
      // Удаляем из исходников current конфигурацию
      //System.out.println("Deleting:");
      for (String element : currentConfigFiles) {
        File elementFile = new File(element);
        //System.out.print("  " + element);
        //System.out.print(elementFile.exists() ? " ." : " - ");
        if (elementFile.exists()) {
          //System.out.print(elementFile.setWritable(true) ? " " : "R ");
          //System.out.println(elementFile.delete() ? " - OK" : " - Failed");
          elementFile.delete();
        }
      }
      

    } else {
      // если нет текущего профиля:
      JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil
          .multipleConcat(WARNING_PREFIX, "Current configuration undefined - '" + BUILD_CONFIG_FILE_NAME + "' not found!"));
    }

    // Копируем в исходники target конфигурацию
    printList("Files to be updated:", updateConfigFiles);

    try {
      for (String element : updateConfigFiles) {
        JepRiaToolkitUtil.copyFile(new File(BUILD_CONFIG_PATH_PREFIX + targetConfig + "\\" + element), new File(DOT + "\\" + element));
      }
    } catch (IOException e) {
      // e.printStackTrace();
      throw new BuildException(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }

    // Сохраняем в build.config полный список файлов target конфигурации
    saveConfigFileList(targetConfig, targetConfigFiles);
  }

  /*
   * получаем имя текущей конфигурации
   */
  public static String getCurrentConfigName() {
    String configName = null;

    if (new File(BUILD_CONFIG_FILE_NAME).exists()) {
      try {
        InputStream fis = new FileInputStream(BUILD_CONFIG_FILE_NAME);
        InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        String line = null;
  
        while ((line = br.readLine()) != null && configName == null) {
          if (line.startsWith(":"))
            configName = line.substring(1).replace("\r", "").replace("\n", "");
        }
  
        br.close();
        isr.close();
        fis.close();
      } catch (Exception e) {
        // e.printStackTrace();
        JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(
            ERROR_PREFIX, e.getLocalizedMessage()));
      }
    }

    return configName;
  }

  /*
   * получаем из .config список файлов текущего профиля
   */
  private List<String> loadConfigFileList() {
    ArrayList<String> resultList = null;
    String line;
    if (new File(BUILD_CONFIG_FILE_NAME).exists()) {
      try {
        InputStream fis = new FileInputStream(BUILD_CONFIG_FILE_NAME);
        InputStreamReader isr = new InputStreamReader(fis,
            Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
  
        resultList = new ArrayList<String>();
  
        while ((line = br.readLine()) != null) {
          if (!line.startsWith(":"))
            resultList.add(line.replace("\r", "").replace("\n", ""));
        }
  
        br.close();
        isr.close();
        fis.close();
      } catch (Exception e) {
        // e.printStackTrace();
        JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(
            ERROR_PREFIX, e.getLocalizedMessage()));
      }
    }
    
    return resultList;
  }

  /*
   * сохраняем список файлов провиля в .config
   */
  private void saveConfigFileList(String configName, List<String> fileList) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(BUILD_CONFIG_FILE_NAME, "UTF-8");

      out.println(":" + configName);
      for (String item : fileList) {
        out.println(item);
      }

      out.flush();
    } catch (Exception e) {
      // e.printStackTrace();
      JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(
          ERROR_PREFIX, e.getLocalizedMessage()));
    } finally {
      if (out != null)
        out.close();
    }
  };

  /**
   * Сравниваем побайтово два файла, если размеры разные - сразу true
   */

  private boolean checkBinaryChanged(File configFile, File srcFile)
      throws IOException {
    boolean diffFound = false;

    if (configFile.length() != srcFile.length()) {
      diffFound = true;
    } else {
      InputStream configInputStream = new BufferedInputStream(new FileInputStream(configFile));
      InputStream srcInputStream = new BufferedInputStream(new FileInputStream(srcFile));
      int configFileData, srcFileData;

      while (!diffFound
          && (configFileData = configInputStream.read()) != -1) {
        if (configFileData != srcInputStream.read())
          diffFound = true;
        
      }
      
      srcInputStream.close();
      configInputStream.close();
    }

    return diffFound;
  }

  public void setTargetConfig(String targetConfig) {
    this.targetConfig = targetConfig;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  private void printList(String header, List<String> stringList) {
    System.out.print(header);
    System.out.println(stringList.isEmpty() ? " none." : "");
    for (String item : stringList) {
      System.out.println("  " + item);
    }
  }
  
  /*
   * Получение списка файлов по указанному пути
   */
  private List<String> getFileList(String path) {
    List<String> resultList = new ArrayList<String>();

    try {
      File pathFile = new File(path);
      String[] folderFiles = pathFile.list();

      if (folderFiles != null) {
        File element;

        for (String folderFile : folderFiles) {
          element = new File(path + "\\" + folderFile);

          if (element.isDirectory()) {
            resultList.addAll(getFileList(element.getPath()));
          } else if (element.isFile()) {
            resultList.add(element.getPath().replace(BUILD_CONFIG_PATH_PREFIX + targetConfig + "\\", ""));
          }
        }
      } else {
        // path not found
        resultList = null;
      }
    } catch (Exception e) {
      // e.printStackTrace();
      JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }

    return resultList;
  }
}
