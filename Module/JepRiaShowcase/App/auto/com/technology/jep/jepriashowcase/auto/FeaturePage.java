package com.technology.jep.jepriashowcase.auto;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class FeaturePage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
  private static Logger logger = Logger.getLogger(FeaturePage.class.getName());
  
  // Singleton
    static private FeaturePage<JepRiaShowcasePageManager> instance;
    static public FeaturePage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
      if(instance == null) {
        instance = new FeaturePage<JepRiaShowcasePageManager>(pageManager);
        logger.info(instance.getClass() + " has created");
      }
      
      return instance;
    }
    
    private FeaturePage(P pages) {
        super(pages);
    }
}
