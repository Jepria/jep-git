package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepriashowcase.requestfunct.client.RequestFunctAutomationConstant.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class JRSCRequestFunctPage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
	private static Logger logger = Logger.getLogger(JRSCRequestFunctPage.class.getName());
	
	@FindBy(id = REQUESTFUNCT_FEATUREID_DETAILFORM_FIELD_INPUT_ID)
	public WebElement featureIdField;
	
	@FindBy(id = REQUESTFUNCT_FEATURENAME_DETAILFORM_FIELD_INPUT_ID)
	public WebElement featureNameField;
	
	@FindBy(id = REQUESTFUNCT_FEATURENAMEEN_DETAILFORM_FIELD_INPUT_ID)
	public WebElement featureNameEnField;
	
	@FindBy(id = REQUESTFUNCT_FROMDATEINS_DETAILFORM_FIELD_INPUT_ID)
	public WebElement fromDateInsField;
	
	@FindBy(id = REQUESTFUNCT_TODATEINS_DETAILFORM_FIELD_INPUT_ID)
	public WebElement toDateInsField;
	
	@FindBy(id = REQUESTFUNCT_ROWCOUNT_DETAILFORM_FIELD_INPUT_ID)
	public WebElement rowCountField;
	
	@FindBy(id = REQUESTFUNCT_DESCRIPTION_DETAILFORM_FIELD_INPUT_ID)
	public WebElement descriptionField;
	
	// Singleton
    static private JRSCRequestFunctPage<JepRiaShowcasePageManager> instance;
    static public JRSCRequestFunctPage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
    	if(instance == null) {
    		instance = new JRSCRequestFunctPage<JepRiaShowcasePageManager>(pageManager);
    		logger.info(instance.getClass() + " has created");
    	}
    	
    	return instance;
    }

    
    private JRSCRequestFunctPage(P pages) {
        super(pages);
    }
}
