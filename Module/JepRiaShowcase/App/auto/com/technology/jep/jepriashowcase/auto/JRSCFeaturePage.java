package com.technology.jep.jepriashowcase.auto;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class JRSCFeaturePage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
	private static Logger logger = Logger.getLogger(JRSCFeaturePage.class.getName());
	
	// Singleton
    static private JRSCFeaturePage<JepRiaShowcasePageManager> instance;
    static public JRSCFeaturePage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
    	if(instance == null) {
    		instance = new JRSCFeaturePage<JepRiaShowcasePageManager>(pageManager);
    		logger.info(instance.getClass() + " has created");
    	}
    	
    	return instance;
    }
    
    private JRSCFeaturePage(P pages) {
        super(pages);
    }
}
