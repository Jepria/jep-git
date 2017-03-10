package com.technology.jep.jepriatoolkit.version;

public class MapXml {
  
  private String svnRoot;
  private String initialPath;
  private String moduleVersion;
  
  public MapXml(String svnRoot, String initPath, String modVersion) {
    setSvnRoot(svnRoot);
    setInitialPath(initPath);
    setModuleVersion(modVersion);
  }
  
  public String getSvnRoot() {
    return svnRoot;
  }
  
  private void setSvnRoot(String svnRoot) {
    this.svnRoot = svnRoot;
  }
  
  public String getInitialPath() {
    return initialPath;
  }
  
  private void setInitialPath(String initialPath) {
    this.initialPath = initialPath;
  }
  
  public String getModuleVersion() {
    return moduleVersion;
  }
  
  private void setModuleVersion(String moduleVersion) {
    this.moduleVersion = moduleVersion;
  }
}
