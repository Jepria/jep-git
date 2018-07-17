package com.technology.jep.jepriatoolkit.version.signer.version;

import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;
import org.apache.tools.ant.BuildException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
/*
{
  "library": {
    "JepRia": "10.9.1"
  },
  "compile": {
    "time_stamp": "01.01.2018 12:12:00",
    "host_name": "msk-dit-20546-1",
    "user_name": "AbrarovAM",
    "UUID": "550e8400-e29b-41d4-a716-446655440000",
    "is_debug": "true"
  },
  "svn": {
    "project_name": "Scoring",
    "module_name": "Application",
    "revision": "54471:56851M",
    "tag_version": "2.2.1"
  }
}
*/
public class VersionUtil {
    public static Version createVersion(String buildConfig, String libraryVersion, String hostName, String userName, String projectName, String moduleName, String svnRevision, String svnTag) {
        Version version = new Version();
        try {
            // список библиотек через запятую: JepRia:10.1.0, GWT:6.3.0
            String[] libVersionPair = libraryVersion.split(",");
            for (String lib : libVersionPair) {
                // название библиотеки версия через :
                String[] t = lib.split(":");
                if (t.length<2){
                    version.addLibInf("JepRIA",t[0]);// не указано название библиотеки только версия, JepRIA?
                } else version.addLibInf(t[0],t[1]);
            }
        } catch (PatternSyntaxException e) {
            throw new BuildException(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
        }

        version.addCompileInf("UUID", UUID.randomUUID().toString());
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
}
