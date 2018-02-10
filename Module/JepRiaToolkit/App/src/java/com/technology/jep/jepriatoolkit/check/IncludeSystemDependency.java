package com.technology.jep.jepriatoolkit.check;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.tools.ant.BuildException;

import com.technology.jep.jepriatoolkit.ant.StaticTask;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

/**
 * Таск, копирующий файл с пакетом зависимостей из указанной библиотеки в текущую папку (рядом с build.xml)
 */
public class IncludeSystemDependency extends StaticTask {
  
  /**
   * Путь к jar-файлу с зависимостями
   */
  private String libPath;
  
  /**
   * Имя файла, содержащего пакет зависимостей, внутри jar-библиотеки
   */
  private String sourceFilename = "dependency.properties";
  
  /**
   * Имя файла в текущей директории (App), под которым распакуется файл с ситемными зависимостями из библиотеки
   */
  private String destFilename;
  
  
  public void setLibPath(String libPath) {
    this.libPath = libPath;
  }
  
  public void setSourceFilename(String sourceFilename) {
    this.sourceFilename = sourceFilename;
  }
  
  public void setDestFilename(String destFilename) {
    this.destFilename = destFilename;
  }
  
  private static void fail(String msg) {
    JepRiaToolkitUtil.echoMessage("FAIL: " + msg);
    throw new BuildException(msg);
  }
  
  @Override
  protected String getExecutionId() {
    return libPath;
  }
  
  @Override
  protected void executeStatic() throws BuildException {
    // check mandatory attributes
    if (libPath == null) {
      fail("libPath attribute is mandatory");
    }
    if (destFilename == null) {
      fail("destFilename attribute is mandatory");
    }
    //
    
    
    File packageLibFile = new File(libPath);
    
    final String libFilename = packageLibFile.getName();
    
    JepRiaToolkitUtil.echoMessage("Including dependency package: " + libFilename);
    
    if (!packageLibFile.exists()) {
      fail("File not found: " + libPath);
    }
    
    try (JarFile packageLibJar = new JarFile(packageLibFile)) {
      ZipEntry entry = packageLibJar.getEntry(sourceFilename);
      
      if (entry == null) {
        fail("No file '" + sourceFilename + "' found in the library");
      }
      
      File destFile = new File(destFilename);
      if (destFile.exists()) {
        JepRiaToolkitUtil.echoMessage("The existing file '" + destFilename + "' will be replaced");
      }
      
      // Распакуем файл
      InputStream is = packageLibJar.getInputStream(entry);
      OutputStream os = new FileOutputStream(destFile);
      
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = is.read(buffer)) != -1) {
          os.write(buffer, 0, bytesRead);
      }
      
      os.close();
      is.close();
      
      JepRiaToolkitUtil.echoMessage("Dependency package including succeeded");
      
    } catch (IOException e) {
      e.printStackTrace();
      throw new BuildException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), e);
    }
  }
}
