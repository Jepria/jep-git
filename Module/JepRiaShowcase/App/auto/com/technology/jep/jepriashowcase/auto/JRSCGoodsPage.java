package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_NAME_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class JRSCGoodsPage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
  private static Logger logger = Logger.getLogger(JRSCGoodsPage.class.getName());
  
    @FindBy(id = JRSC_GOODS_NAME_TEXT_FIELD_ID)
    public WebElement goodsNameField;
    
    @FindBy(id = JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID)
    public WebElement purchasingPriceField;

    @FindBy(id = JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID)
    public WebElement goodsTypeField;

    @FindBy(id = JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID)
    public WebElement motivationField;

    @FindBy(id = JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID)
    public WebElement unitField;
        
  // Singleton
    static private JRSCGoodsPage<JepRiaShowcasePageManager> instance;
    static public JRSCGoodsPage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
      if(instance == null) {
        instance = new JRSCGoodsPage<JepRiaShowcasePageManager>(pageManager);
        logger.info(instance.getClass() + " has created");
      }
      
      return instance;
    }

    
    private JRSCGoodsPage(P pages) {
        super(pages);
    }
}
