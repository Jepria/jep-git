package com.technology.jep.jepriashowcase.auto;

import com.technology.jep.jepria.auto.entrance.pages.JepRiaApplicationPageManager;

public class JepRiaShowcasePageManager extends JepRiaApplicationPageManager {

    public CustomerPage<JepRiaShowcasePageManager> customPage;
    public GoodsPage<JepRiaShowcasePageManager> goodsPage;
    public FeaturePage<JepRiaShowcasePageManager> featurePage;

    public JepRiaShowcasePageManager() {
      customPage = initElements(CustomerPage.getInstance(this));
      goodsPage = initElements(GoodsPage.getInstance(this));
      featurePage = initElements(FeaturePage.getInstance(this));
    }
}
