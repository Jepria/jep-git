package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepriashowcase.feature.client.FeatureAutomationConstant.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class JRSCFeaturePage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
	private static Logger logger = Logger.getLogger(JRSCFeaturePage.class.getName());
	
	@FindBy(id = FEATURE_FEATUREID_DETAILFORM_FIELD_ID)
	public WebElement featureIdField;
	
	@FindBy(id = FEATURE_FEATURENAME_DETAILFORM_FIELD_ID)
	public WebElement featureNameField;
	
	@FindBy(id = FEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID)
	public WebElement featureNameEnField;
	
// TODO implement DOM ID of a JepDateField
//	@FindBy(id = FEATURE_FROMDATEINS_DETAILFORM_FIELD_ID)
//	public WebElement fromDateInsField;
//	
//	@FindBy(id = FEATURE_TODATEINS_DETAILFORM_FIELD_ID)
//	public WebElement toDateInsField;
	
	@FindBy(id = FEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID)
	public WebElement maxRowCountField;
	
	@FindBy(id = FEATURE_DESCRIPTION_DETAILFORM_FIELD_ID)
	public WebElement descriptionField;
	
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
