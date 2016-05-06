package com.technology.jep.jepria.auto.util;

/*
 * Bean representing a browser. It contains name, version, platform fields and path.
 *
 * @author Sebastiano Armeli-Battana
 */
public class Browser {

    private String name;
    private String version;
    private String platform;
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPath(String path) {
        this.path = path;
    }

	public String getPath() {
        return path;
	}

}