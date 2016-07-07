package com.technology.jep.jepriashowcase.goods.auto;

import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_NAME_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID;

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
		setFieldValue(JRSC_GOODS_NAME_TEXT_FIELD_ID, goodsNameValue);
	}

	@Override
	public void setPurchasingPrice(String purchasingPrice) {
		setFieldValue(JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID, purchasingPrice);
	}

	@Override
	public String getGoodsName() {
		return getFieldValue(JRSC_GOODS_NAME_TEXT_FIELD_ID);
	}

	@Override
	public String getPurchasingPrice() {
		return getFieldValue(JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID);
	}

	@Override
	public void setGoodsType(String goodsType) {
		selectComboBoxMenuItem(JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID, goodsType);
	}

	@Override
	public String getGoodsType() {
		return getFieldValue(JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID);
	}

	@Override
	public void setUnit(String unit) {
		selectComboBoxMenuItem(JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID, unit);
	}

	@Override
	public String getUnit() {
		return getFieldValue(JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID);
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
			String radioButtonInputXPath = "//table[@id='"
					+ JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID +"']//span[@class='gwt-RadioButton']"
					+ "//.[contains(text(),'"
					+ motivation
					+ "')]/preceding-sibling::input";
			
			WebElement fieldInput = pages.goodsPage.motivationField.findElement(By.xpath(radioButtonInputXPath));
			//		getWait().until(elementToBeClickable(fieldInput));

			fieldInput.click();
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
				+ JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID
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
