package com.technology.jep.jepriashowcase.goods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.goods.client.GoodsClientConstant.goodsText;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_NAME_TEXT_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.client.JRSCGoodsAutomationConstant.JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PHOTO;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PORTFOLIO;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_SEGMENT_CODE_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_TYPE_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.MOTIVATION_TYPE_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.PURCHASING_PRICE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.UNIT_CODE;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepListField;
import com.technology.jep.jepria.client.widget.field.multistate.JepNumberField;
import com.technology.jep.jepria.client.widget.field.multistate.JepRadioField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTreeField;
import com.technology.jep.jepria.client.widget.field.multistate.large.JepFileField;
import com.technology.jep.jepria.client.widget.field.multistate.large.JepImageField;
import com.technology.jep.jepria.client.widget.field.tree.TreeField.CheckNodes;
 
public class GoodsDetailFormViewImpl extends DetailFormViewImpl implements GoodsDetailFormView {	
 
	public GoodsDetailFormViewImpl() {
		super(new FieldManager());
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
		JepTextField goodsNameTextField = new JepTextField(JRSC_GOODS_NAME_TEXT_FIELD_ID, goodsText.goods_detail_goods_name());
		goodsNameTextField.setMaxLength(255);
		JepComboBoxField goodsTypeComboBoxField = new JepComboBoxField(JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID, goodsText.goods_detail_goods_type());
		JepComboBoxField unitComboBoxField = new JepComboBoxField(JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID, goodsText.goods_detail_unit());
		JepRadioField motivationTypeRadioField = new JepRadioField(JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID, goodsText.goods_detail_motivation_type());

//		JepNumberField purchasingPriceMoneyField = new JepMoneyField(goodsText.goods_detail_purchasing_price()); //TODO не компилится, разобраться
		JepNumberField purchasingPriceMoneyField = new JepNumberField(JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID, goodsText.goods_detail_purchasing_price()); //TODO Времянка
		
		JepListField goodsSegmentListField = new JepListField(goodsText.goods_detail_goods_segment());
		goodsSegmentListField.setFieldHeight(120);
		goodsSegmentListField.setSelectAllCheckBoxVisible(true);
		JepTreeField goodsCatalogTreeField = new JepTreeField(goodsText.goods_detail_goods_catalog());
//		goodsCatalogTreeField.setCheckStyle(CheckCascade.PARENTS);
		goodsCatalogTreeField.setCheckNodes(CheckNodes.LEAF);
		
		JepImageField goodsPhotoImageField = new JepImageField(goodsText.goods_detail_goods_photo());
		JepFileField goodsPortfolioFileField = new JepFileField(goodsText.goods_detail_goods_portfolio());
		
		purchasingPriceMoneyField.setMaxLength(18);
		panel.add(goodsNameTextField);
		panel.add(goodsTypeComboBoxField);
		panel.add(unitComboBoxField);
		panel.add(motivationTypeRadioField);
		panel.add(purchasingPriceMoneyField);
		panel.add(goodsSegmentListField);
		panel.add(goodsCatalogTreeField);
		panel.add(goodsPhotoImageField);
		panel.add(goodsPortfolioFileField);
		
		setWidget(scrollPanel);
 
		fields.put(GOODS_NAME, goodsNameTextField);
		fields.put(GOODS_TYPE_CODE, goodsTypeComboBoxField);
		fields.put(UNIT_CODE, unitComboBoxField);
		fields.put(MOTIVATION_TYPE_CODE, motivationTypeRadioField);
		fields.put(PURCHASING_PRICE, purchasingPriceMoneyField);
		fields.put(GOODS_SEGMENT_CODE_LIST, goodsSegmentListField);
		fields.put(GOODS_CATALOG_ID_LIST, goodsCatalogTreeField);
		fields.put(GOODS_PHOTO, goodsPhotoImageField);
		fields.put(GOODS_PORTFOLIO, goodsPortfolioFileField);
	}
 
}
