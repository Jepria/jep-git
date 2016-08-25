package com.technology.jep.jepria.server.test;

public class DataSourceDef {
  public String dataSourceName;
  public String jdbcUrl;
  public String username;
  public String password;
  
  public DataSourceDef(String dataSourceName, String jdbcUrl, String username, String password) {
    this.dataSourceName = dataSourceName;
    this.jdbcUrl = jdbcUrl;
    this.username = username;
    this.password = password;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((dataSourceName == null) ? 0 : dataSourceName.hashCode());
    result = prime * result + ((jdbcUrl == null) ? 0 : jdbcUrl.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((username == null) ? 0 : username.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DataSourceDef other = (DataSourceDef) obj;
    if (dataSourceName == null) {
      if (other.dataSourceName != null)
        return false;
    } else if (!dataSourceName.equals(other.dataSourceName))
      return false;
    if (jdbcUrl == null) {
      if (other.jdbcUrl != null)
        return false;
    } else if (!jdbcUrl.equals(other.jdbcUrl))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (username == null) {
      if (other.username != null)
        return false;
    } else if (!username.equals(other.username))
      return false;
    return true;
  }
}
