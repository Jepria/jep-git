package com.technology.jep.jepriashowcase.auto;

import com.technology.jep.jepria.auto.entrance.pages.JepRiaApplicationPageManager;

public class JepRiaShowcasePageManager extends JepRiaApplicationPageManager {

    public JRSCCustomerPage<JepRiaShowcasePageManager> customPage;
    public JRSCGoodsPage<JepRiaShowcasePageManager> goodsPage;
    public JRSCRequestFeaturePage<JepRiaShowcasePageManager> requestFeaturePage;

    public JepRiaShowcasePageManager() {
    	customPage = initElements(JRSCCustomerPage.getInstance(this));
    	goodsPage = initElements(JRSCGoodsPage.getInstance(this));
    	requestFeaturePage = initElements(JRSCRequestFeaturePage.getInstance(this));
    }
}
