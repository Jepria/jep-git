package com.technology.jep.jepria.auto.entrance.pages;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;
import com.technology.jep.jepria.auto.pages.PageManagerBase;



public class JepRiaApplicationPageManager extends ApplicationEntrancePageManager {
	
    public JepRiaApplicationPage<PageManagerBase> getApplicationPage() {
    	if(applicationPage == null) {
            applicationPage = initElements(JepRiaApplicationPage.getInstance(this));
    	}
    	
		return (JepRiaApplicationPage<PageManagerBase>) applicationPage;
	}
}
