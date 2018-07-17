package com.technology.jep.jepriatoolkit.version.signer.version;

import com.technology.jep.jepriatoolkit.util.Pair;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    "module_name": "Application",
    "revision": "54471:56851M",
    "tag_version": "2.2.1"
  }
}
*/
public class Version {
    private Map<String, String> library;
    private Map<String, String> compile;
    private Map<String, String> svn;
    public Version() {
        library = new HashMap<String, String>();
        compile = new HashMap<String, String>();
        svn = new HashMap<String, String>();
    }
    public void addLibInf(String name, String value){
        library.put(name, value);
    }
    public void addCompileInf(String name, String value){
        compile.put(name, value);
    }
    public void addSvnInf(String name, String value){
        svn.put(name, value);
    }
    public Map<String, String> getLibrary() {
        return library;
    }

    public void setLibrary(Map<String, String> library) {
        this.library = library;
    }

    public Map<String, String> getCompile() {
        return compile;
    }

    public void setCompile(Map<String, String> compile) {
        this.compile = compile;
    }

    public Map<String, String> getSvn() {
        return svn;
    }

    public void setSvn(Map<String, String> svn) {
        this.svn = svn;
    }
}
