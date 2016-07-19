package com.technology.jep.jepriashowcase.auto;

import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.*;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.technology.jep.jepria.auto.pages.JepRiaApplicationPage;

public class GoodsPage<P extends JepRiaShowcasePageManager> extends JepRiaApplicationPage<P> {
  private static Logger logger = Logger.getLogger(GoodsPage.class.getName());
  
    @FindBy(id = GOODS_GOODS_NAME_TEXT_FIELD_ID)
    public WebElement goodsNameField;
    
    @FindBy(id = GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID)
    public WebElement purchasingPriceField;

    @FindBy(id = GOODS_GOODS_TYPE_COMBOBOX_FIELD_ID)
    public WebElement goodsTypeField;

    @FindBy(id = GOODS_MOTIVATION_RADIO_FIELD_ID)
    public WebElement motivationField;

    @FindBy(id = GOODS_UNIT_COMBOBOX_FIELD_ID)
    public WebElement unitField;
        
  // Singleton
    static private GoodsPage<JepRiaShowcasePageManager> instance;
    static public GoodsPage<JepRiaShowcasePageManager> getInstance(JepRiaShowcasePageManager pageManager) {
      if(instance == null) {
        instance = new GoodsPage<JepRiaShowcasePageManager>(pageManager);
        logger.info(instance.getClass() + " has created");
      }
      
      return instance;
    }

    
    private GoodsPage(P pages) {
        super(pages);
    }
}
