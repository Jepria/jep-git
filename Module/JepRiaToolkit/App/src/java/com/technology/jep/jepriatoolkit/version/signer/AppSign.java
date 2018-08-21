package com.technology.jep.jepriatoolkit.version.signer;

import com.technology.jep.jepriatoolkit.util.Json;
import com.technology.jep.jepriatoolkit.version.signer.version.Version;
import com.technology.jep.jepriatoolkit.version.signer.version.VersionUtil;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.*;


public class AppSign extends Task {
    // атрибуты таска
    private String libraryVersion, hostName, userName, projectName, moduleName, svnRevision, svnTag;
    private String taskResult, buildConfig;

    @Override
    public void execute() throws BuildException {
        checkParameter(libraryVersion, "Incorrect parameter: libraryVersion!");
        checkParameter(moduleName, "Incorrect parameter: moduleName!");
        checkParameter(buildConfig, "Incorrect parameter: buildConfig!");
        echoMessage("Try to gen application info into JSON");
        // создаем данные для version.json
        Version appVersion = VersionUtil.createVersion(buildConfig, libraryVersion, hostName, userName, projectName, moduleName, svnRevision, svnTag);
        Json json = Json.object()
                .set("library", Json.make(appVersion.getLibrary()))
                .set("compile", Json.make(appVersion.getCompile()))
                .set("svn", Json.make(appVersion.getSvn()));
        PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
        ph.setProperty(taskResult, json, false);
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
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public String getBuildConfig() {
        return buildConfig;
    }

    public void setBuildConfig(String buildConfig) {
        this.buildConfig = buildConfig;
    }
}
