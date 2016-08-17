package com.technology.jep.jepriashowcase.goods.auto;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;
import static com.technology.jep.jepria.client.JepRiaAutomationConstant.JEP_FIELD_INPUT_POSTFIX;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_GOODS_NAME_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_GOODS_TYPE_COMBOBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_MOTIVATION_RADIO_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_PHOTO_IMAGE_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_PORTFOLIO_FILE_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.GoodsAutomationConstant.GOODS_UNIT_COMBOBOX_FIELD_ID;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.technology.jep.jepria.auto.JepRiaModuleAutoImpl;
import com.technology.jep.jepria.auto.exceptions.WrongOptionException;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcaseAuto;
import com.technology.jep.jepriashowcase.auto.JepRiaShowcasePageManager;

public class GoodsAutoImpl<A extends JepRiaShowcaseAuto, P extends JepRiaShowcasePageManager> extends JepRiaModuleAutoImpl<A, P> implements GoodsAuto {
  private static Logger logger = Logger.getLogger(GoodsAutoImpl.class.getName());

  public GoodsAutoImpl(A app, P pageManager) {
    super(app, pageManager);
  }

  @Override
  public void setGoodsName(String goodsNameValue) {
    setFieldValue(GOODS_GOODS_NAME_TEXT_FIELD_ID, goodsNameValue);
  }

  @Override
  public void setPurchasingPrice(String purchasingPrice) {
    setFieldValue(GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID, purchasingPrice);
  }

  @Override
  public String getGoodsName() {
    return getFieldValue(GOODS_GOODS_NAME_TEXT_FIELD_ID);
  }

  @Override
  public String getPurchasingPrice() {
    return getFieldValue(GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID);
  }

  @Override
  public void setGoodsType(String goodsType) {
    selectComboBoxMenuItem(GOODS_GOODS_TYPE_COMBOBOX_FIELD_ID, goodsType);
  }

  @Override
  public String getGoodsType() {
    return getFieldValue(GOODS_GOODS_TYPE_COMBOBOX_FIELD_ID);
  }

  @Override
  public void setUnit(String unit) {
    selectComboBoxMenuItem(GOODS_UNIT_COMBOBOX_FIELD_ID, unit);
  }

  @Override
  public String getUnit() {
    return getFieldValue(GOODS_UNIT_COMBOBOX_FIELD_ID);
  }


  @Override
  public void setSegment(String goodsSegment) {
    throw new NotImplementedYetException();
  }

  @Override
  public void setCatalogSections(Set<String> goodsCatalogSections) {
    throw new NotImplementedYetException();
  }
  
  @Override
  public void setMotivation(String motivation) {
    // FIXME поддержка Selenium-тестирования элемента RadioButton пока не поддерживается,
    // поэтому весь код, который должен располагаться в JepRiaModuleAutoImpl, располагается здесь.
    
    pages.goodsPage.ensurePageLoaded();
    
    try {
      
      // TODO Обработку radioButton перенести в JepRia
      WebElement radioButtonSpan = pages.goodsPage.getWebDriver().findElement(By.xpath( 
          String.format("//*[@id='%s']//span[descendant::label[contains(text(),'%s')]]",
              GOODS_MOTIVATION_RADIO_FIELD_ID + JEP_FIELD_INPUT_POSTFIX,
              motivation))); 
      
      WebElement radioButtonInput = radioButtonSpan.findElement(By.xpath("./input"));
      getWait().until(elementToBeClickable(radioButtonInput));

      radioButtonInput.click();
    } catch(NoSuchElementException ex) {
      throw new WrongOptionException("Wrong radio option", ex);      
    }
  }

  @Override
  public String getMotivation() {
    // FIXME поддержка Selenium-тестирования элемента RadioButton пока не поддерживается,
    // поэтому весь код, который должен располагаться в JepRiaModuleAutoImpl, располагается здесь.
    
    String result;
    pages.goodsPage.ensurePageLoaded();
    
    String checkedRadioButtonXPath = "//table[@id='"
        + GOODS_MOTIVATION_RADIO_FIELD_ID
        + "']//span[@class='gwt-RadioButton']//input[@checked]/following-sibling::label";
    
    try {
      WebElement fieldInput = pages.goodsPage.motivationField.findElement(By.xpath(checkedRadioButtonXPath));
      result = fieldInput.getText();
    } catch(NoSuchElementException ex) {
      result = "";
    }
    
    return result;
  }
  
  @Override
  public void setPhoto(String pathToFile) {
    setLargeFieldValue(GOODS_PHOTO_IMAGE_FIELD_ID, pathToFile);
  }
  
  @Override
  public void setPortfolio(String pathToFile) {
    setLargeFieldValue(GOODS_PORTFOLIO_FILE_FIELD_ID, pathToFile);
  }
  
  @Override
  public String getSegment() {
    throw new NotImplementedYetException();
  }

  @Override
  public Set<String> getCatalogSections() {
    throw new NotImplementedYetException();
  }

  @Override
  public void fillSearchForm(
      String goodsName, 
      String goodsType, 
      String unit, 
      String goodsSegment,
      Set<String> goodsCatalogSections) {
    
    setGoodsName(goodsName);
    setGoodsType(goodsType);
    setUnit(unit);
    setSegment(goodsSegment);
    setCatalogSections(goodsCatalogSections);
  }

  @Override
  public void fillCreateForm(
      String goodsName, 
      String goodsType, 
      String unit, 
      String motivation, 
      String purchasingPrice, 
      String goodsPhoto, 
      String goodsSpecification) {
    
    setGoodsName(goodsName);
    setGoodsType(goodsType);
    setUnit(unit);
    setMotivation(motivation);
    setPurchasingPrice(purchasingPrice);
  }


  @Override
  public void fillEditForm(
      String goodsName, 
      String goodsType, 
      String unit, 
      String motivation, 
      String purchasingPrice, 
      String goodsPhoto, 
      String goodsSpecification) {
    
    setGoodsName(goodsName);
    setGoodsType(goodsType);
    setUnit(unit);
    setMotivation(motivation);
    setPurchasingPrice(purchasingPrice);
  }
}
