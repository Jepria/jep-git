package com.technology.jep.jepria.auto.entrance;

import com.technology.jep.jepria.auto.manager.WDAutoAbstract;

public class ApplicationEntranceAppAuto extends WDAutoAbstract implements EntranceAppAuto {
  private AuthorizationAuto entranceAuto;
  
  public ApplicationEntranceAppAuto(String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password) {
    super(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    entranceAuto = new ApplicationEntranceAuto(this);
  }

  @Override
  public void start(String baseUrl) {
    super.start(baseUrl);
    getEntranceAuto().openMainPage(baseUrl);
  }
  

  @Override
    public AuthorizationAuto getEntranceAuto() {
        return entranceAuto;
    }
}
