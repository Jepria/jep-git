package com.technology.jep.jepriatoolkit.version;

public class ApplicationSetting {
  
  private String svnRoot;
  private String initialPath;
  private String moduleVersion;
  private String installVersion;
  private String deploymentPath;
  private String svnPath;
  private String svnVersionInfo;
  private String login;
  private String password;
  
  public ApplicationSetting(String svnRoot, String initPath, String modVersion, String instVersion, String deployPath, String svnPath, String svnVersionInfo, String login, String password){
    setSvnRoot(svnRoot);
    setInitialPath(initPath);
    setModuleVersion(modVersion);
    setInstallVersion(instVersion);
    setDeploymentPath(deployPath);
    setSvnPath(svnPath);
    setSvnVersionInfo(svnVersionInfo);
    setLogin(login);
    setPassword(password);
  }
  
  public String getSvnRoot() {
    return svnRoot;
  }
  public void setSvnRoot(String svnRoot) {
    this.svnRoot = svnRoot;
  }
  public String getInitialPath() {
    return initialPath;
  }
  public void setInitialPath(String initialPath) {
    this.initialPath = initialPath;
  }
  public String getModuleVersion() {
    return moduleVersion;
  }
  public void setModuleVersion(String moduleVersion) {
    this.moduleVersion = moduleVersion;
  }
  public String getInstallVersion() {
    return installVersion;
  }
  public void setInstallVersion(String installVersion) {
    this.installVersion = installVersion;
  }
  public String getDeploymentPath() {
    return deploymentPath;
  }
  public void setDeploymentPath(String deploymentPath) {
    this.deploymentPath = deploymentPath;
  }
  public String getLogin() {
    return login;
  }
  public void setLogin(String login) {
    this.login = login;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public String getSvnPath() {
    return svnPath;
  }

  public void setSvnPath(String svnPath) {
    this.svnPath = svnPath;
  }

  public String getSvnVersionInfo() {
    return svnVersionInfo;
  }

  public void setSvnVersionInfo(String svnVersionInfo) {
    this.svnVersionInfo = svnVersionInfo;
  }  
  
}
