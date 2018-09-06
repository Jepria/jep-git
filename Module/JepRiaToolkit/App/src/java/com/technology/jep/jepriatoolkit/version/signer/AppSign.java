package com.technology.jep.jepriatoolkit.version.signer;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.checkParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;
import com.technology.jep.jepriatoolkit.util.Json;
import com.technology.jep.jepriatoolkit.version.signer.version.Version;


public class AppSign extends Task {
  // входные атрибуты таска
  private String libraryVersion, hostName, userName, projectName, moduleName, svnRevision, svnTag;
  private String buildConfig;

  // выходные параметры
  private String taskResultPropertyName, buildUUIDPropertyName;

  @Override
  public void execute() throws BuildException {
    checkParameter(libraryVersion, "Incorrect parameter: libraryVersion!");
    checkParameter(moduleName, "Incorrect parameter: moduleName!");
    checkParameter(buildConfig, "Incorrect parameter: buildConfig!");
    echoMessage("Try to gen application info into JSON");
    // создаем данные для version.json
    Version appVersion = createVersion();
    Json json = Json.object()
        .set("library", Json.make(appVersion.getLibrary()))
        .set("compile", Json.make(appVersion.getCompile()))
        .set("svn", Json.make(appVersion.getSvn()));
    PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
    ph.setProperty(taskResultPropertyName, json, false);
    ph.setProperty(buildUUIDPropertyName, buildUUID, false);
  }
  
  private String buildUUID;
  
  private Version createVersion() {
    Version version = new Version();
    try {
      // список библиотек через запятую: JepRia:10.1.0, GWT:6.3.0
      String[] libVersionPair = libraryVersion.split(",");
      for (String lib : libVersionPair) {
        // название библиотеки версия через :
        String[] t = lib.split(":");
        if (t.length<2){
          version.addLibInf("Jepria",t[0]);// не указано название библиотеки только версия, JepRIA?
        } else version.addLibInf(t[0],t[1]);
      }
    } catch (PatternSyntaxException e) {
      throw new BuildException(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }

    buildUUID = UUID.randomUUID().toString();
    
    version.addCompileInf("UUID", buildUUID);
    version.addCompileInf("time_stamp",new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()));
    if (buildConfig.equals("debug")) {
      version.addCompileInf("host_name", hostName);
      version.addCompileInf("user_name", userName);
    }

    version.addSvnInf("repo_name",projectName);
    version.addSvnInf("module_name",moduleName);
    version.addSvnInf("revision",svnRevision);
    version.addSvnInf("tag_version",svnTag);

    return version;
  }

  public String getLibraryVersion() {
    return libraryVersion;
  }

  public void setLibraryVersion(String libraryVersion) {
    this.libraryVersion = libraryVersion;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getSvnRevision() {
    return svnRevision;
  }

  public void setSvnRevision(String svnRevision) {
    this.svnRevision = svnRevision;
  }

  public String getSvnTag() {
    return svnTag;
  }

  public void setSvnTag(String svnTag) {
    this.svnTag = svnTag;
  }

  public String getTaskResult() {
    return taskResultPropertyName;
  }

  public void setTaskResult(String taskResult) {
    this.taskResultPropertyName = taskResult;
  }
  
  public String getBuildUUID() {
    return buildUUIDPropertyName;
  }

  public void setBuildUUID(String buildUUID) {
    this.buildUUIDPropertyName = buildUUID;
  }

  public String getBuildConfig() {
    return buildConfig;
  }

  public void setBuildConfig(String buildConfig) {
    this.buildConfig = buildConfig;
  }
}
