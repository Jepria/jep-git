package com.technology.jep.jepriashowcase.custom.auto;

import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAuto;

public class CustomAutoImpl<A extends JepRiaShowcaseAuto> extends ApplicationEntranceAuto<A, JepRiaShowcasePageManager> implements CustomAuto {
  
  public CustomAutoImpl(A jepRiaShowcaseAuto, JepRiaShowcasePageManager pageManager) {
    super(jepRiaShowcaseAuto, pageManager);
  }

  @Override
  public GoodsAuto openFullScreenModules() {
    pages.customPage.ensurePageLoaded();
    assert(isLoggedIn());
    pages.customPage.fullScreenModulesButton.click();
    return this.applicationManager.getGoodsAuto(true);
  }

  @Override
  public boolean isReady() {
    pages.customPage.ensurePageLoaded();

    return true;
  }
}
