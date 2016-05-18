package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepriashowcase.featurerequest.client.FeatureRequestAutomationConstant.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class JRSCFeatureRequestPage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
	private static Logger logger = Logger.getLogger(JRSCFeatureRequestPage.class.getName());
	
	@FindBy(id = FEATUREREQUEST_FEATUREID_DETAILFORM_FIELD_ID)
	public WebElement featureIdField;
	
	@FindBy(id = FEATUREREQUEST_FEATURENAME_DETAILFORM_FIELD_ID)
	public WebElement featureNameField;
	
	@FindBy(id = FEATUREREQUEST_FEATURENAMEEN_DETAILFORM_FIELD_ID)
	public WebElement featureNameEnField;
	
// TODO implement DOM ID of a JepDateField
//	@FindBy(id = FEATUREREQUEST_FROMDATEINS_DETAILFORM_FIELD_ID)
//	public WebElement fromDateInsField;
//	
//	@FindBy(id = FEATUREREQUEST_TODATEINS_DETAILFORM_FIELD_ID)
//	public WebElement toDateInsField;
	
	@FindBy(id = FEATUREREQUEST_MAXROWCOUNT_DETAILFORM_FIELD_ID)
	public WebElement rowCountField;
	
	@FindBy(id = FEATUREREQUEST_DESCRIPTION_DETAILFORM_FIELD_ID)
	public WebElement descriptionField;
	
	// Singleton
    static private JRSCFeatureRequestPage<JepRiaShowcasePageManager> instance;
    static public JRSCFeatureRequestPage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
    	if(instance == null) {
    		instance = new JRSCFeatureRequestPage<JepRiaShowcasePageManager>(pageManager);
    		logger.info(instance.getClass() + " has created");
    	}
    	
    	return instance;
    }

    
    private JRSCFeatureRequestPage(P pages) {
        super(pages);
    }
}
