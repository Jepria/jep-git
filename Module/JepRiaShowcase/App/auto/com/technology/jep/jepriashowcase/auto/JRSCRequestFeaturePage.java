package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepriashowcase.requestfeature.client.RequestFeatureAutomationConstant.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class JRSCRequestFeaturePage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
	private static Logger logger = Logger.getLogger(JRSCRequestFeaturePage.class.getName());
	
	@FindBy(id = REQUESTFEATURE_FEATUREID_DETAILFORM_FIELD_ID)
	public WebElement featureIdField;
	
	@FindBy(id = REQUESTFEATURE_FEATURENAME_DETAILFORM_FIELD_ID)
	public WebElement featureNameField;
	
	@FindBy(id = REQUESTFEATURE_FEATURENAMEEN_DETAILFORM_FIELD_ID)
	public WebElement featureNameEnField;
	
// TODO implement DOM ID of a JepDateField
//	@FindBy(id = REQUESTFEATURE_FROMDATEINS_DETAILFORM_FIELD_ID)
//	public WebElement fromDateInsField;
//	
//	@FindBy(id = REQUESTFEATURE_TODATEINS_DETAILFORM_FIELD_ID)
//	public WebElement toDateInsField;
	
	@FindBy(id = REQUESTFEATURE_MAXROWCOUNT_DETAILFORM_FIELD_ID)
	public WebElement rowCountField;
	
	@FindBy(id = REQUESTFEATURE_DESCRIPTION_DETAILFORM_FIELD_ID)
	public WebElement descriptionField;
	
	// Singleton
    static private JRSCRequestFeaturePage<JepRiaShowcasePageManager> instance;
    static public JRSCRequestFeaturePage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
    	if(instance == null) {
    		instance = new JRSCRequestFeaturePage<JepRiaShowcasePageManager>(pageManager);
    		logger.info(instance.getClass() + " has created");
    	}
    	
    	return instance;
    }

    
    private JRSCRequestFeaturePage(P pages) {
        super(pages);
    }
}
