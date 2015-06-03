package com.technology.jep.jepria.auto.entrance.pages;

import com.technology.jep.jepria.auto.pages.PageManagerBase;

public class NavigationEntrancePageManager extends PageManagerBase {
    public LoginPage<NavigationEntrancePageManager> loginPage;
    public NavigationPage<NavigationEntrancePageManager> applicationMenuPage;

    public NavigationEntrancePageManager() {
        // RFI
//        loginPage = initElements(new RfiFrameLoginPage(this));
        loginPage = initElements(new DefaultLoginPage<NavigationEntrancePageManager>(this));
        applicationMenuPage = initElements(NavigationPage.getInstance(this));
    }
}
