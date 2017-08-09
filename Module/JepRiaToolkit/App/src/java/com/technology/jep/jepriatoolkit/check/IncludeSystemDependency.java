package com.technology.jep.jepriatoolkit.check;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

/**
 * Таск, копирующий файл с системными зависимостями из библиотеки jepria-dependency.jar в текущую папку (рядом с build.xml)
 */
public class IncludeSystemDependency extends Task {
  
  /**
   * Имя свойства-флага, отвечающего за однократное подключение системных зависимостей (действие распространяется на один запуск ant) 
   */
  public static final String ALREADY_INCLUDED_PROPERTY_NAME = "system-dependency-included";
  
  /**
   * Путь к библиотеке jepria-dependency.jar, задаваемый в прикладном dependency.properties файле
   */
  private String libPath;
  
  /**
   * Имя файла, содержащего системные зависимости, внутри jar-библиотеки
   */
  private String sourceFilename = "dependency.properties";
  
  /**
   * Имя файла в текущей директории (App), под которым распакуется файл с ситемными зависимостями из библиотеки
   */
  private String destFilename = "dependency-jepria.properties";
  
  
  public void setLibPath(String libPath) {
    this.libPath = libPath;
  }
  
  public void setSourceFilename(String sourceFilename) {
    this.sourceFilename = sourceFilename;
  }
  
  public void setDestFilename(String destFilename) {
    this.destFilename = destFilename;
  }
  
  @Override
  public void execute() throws BuildException {
    
    if (isFirstExecution()) {
      
      includeSystemDependency();
      firstExecutionComplete();
      
    } else {
      // System dependency has already been included
    }
  }
  
  /**
   * Проверяет свойство-флаг, отвечающий за однократное подключение системных зависимостей
   * @return true если цель вызывается из ant впервые, иначе false
   */
  private boolean isFirstExecution() {
    return PropertyHelper.getPropertyHelper(getProject()).getProperty(ALREADY_INCLUDED_PROPERTY_NAME) == null;
  }

  /**
   *  Устанавливает свойство-флаг для обеспечения однократного подключения системных зависимостей
   */
  private void firstExecutionComplete() {
    PropertyHelper.getPropertyHelper(getProject()).setProperty(ALREADY_INCLUDED_PROPERTY_NAME, "true", true);
  }
  
  /**
   * Собственно тело цели
   */
  private void includeSystemDependency() {
    
    JepRiaToolkitUtil.echoMessage("Including system dependency...");
  
    File systemDependencyLibFile = new File(libPath);
    
    if (!systemDependencyLibFile.exists()) {
      JepRiaToolkitUtil.echoMessage("Library not found: " + libPath);
      return;
    }
    
    try (JarFile systemDependencyLibJar = new JarFile(systemDependencyLibFile)) {
      ZipEntry entry = systemDependencyLibJar.getEntry(sourceFilename);
      
      if (entry == null) {
        JepRiaToolkitUtil.echoMessage("No file '" + sourceFilename + "' found in the library");
        return;
      }
        
      File destFile = new File(destFilename);
      if (destFile.exists()) {
        JepRiaToolkitUtil.echoMessage("The existing file '" + destFilename + "' will be replaced");
      }
      
      // Распакуем файл
      InputStream is = systemDependencyLibJar.getInputStream(entry);
      OutputStream os = new FileOutputStream(destFile);
      
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
          os.write(buffer, 0, bytesRead);
      }
      
      os.close();
      is.close();
      
      JepRiaToolkitUtil.echoMessage("System dependency including succeeded");
      
    } catch (IOException e) {
      e.printStackTrace();
      throw new BuildException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), e);
    }
  }
}
