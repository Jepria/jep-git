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

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepListField;
import com.technology.jep.jepria.client.widget.field.multistate.JepNumberField;
import com.technology.jep.jepria.client.widget.field.multistate.JepRadioField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTreeField;
import com.technology.jep.jepria.client.widget.field.multistate.large.JepFileField;
import com.technology.jep.jepria.client.widget.field.multistate.large.JepImageField;
import com.technology.jep.jepria.client.widget.field.tree.TreeField.CheckNodes;
 
public class GoodsDetailFormViewImpl extends StandardDetailFormViewImpl implements GoodsDetailFormView {	
 
	@Override
	protected LinkedHashMap<String, Widget> getFieldConfigurations() {
		return new LinkedHashMap<String, Widget>() {{
			JepTextField goodsNameTextField = new JepTextField(JRSC_GOODS_NAME_TEXT_FIELD_ID, goodsText.goods_detail_goods_name());
			goodsNameTextField.setMaxLength(255);
			put(GOODS_NAME, goodsNameTextField);
			
			put(GOODS_TYPE_CODE, new JepComboBoxField(JRSC_GOODS_TYPE_COMBOBOX_FIELD_ID, goodsText.goods_detail_goods_type()));
			put(UNIT_CODE, new JepComboBoxField(JRSC_GOODS_UNIT_COMBOBOX_FIELD_ID, goodsText.goods_detail_unit()));
			put(MOTIVATION_TYPE_CODE, new JepRadioField(JRSC_GOODS_MOTIVATION_RADIO_FIELD_ID, goodsText.goods_detail_motivation_type()));
			
			JepNumberField purchasingPriceMoneyField = new JepNumberField(JRSC_GOODS_PURCHASING_PRICE_NUMBER_FIELD_ID, goodsText.goods_detail_purchasing_price()); //TODO Времянка
			purchasingPriceMoneyField.setMaxLength(18);
			put(PURCHASING_PRICE, purchasingPriceMoneyField);
			
			JepListField goodsSegmentListField = new JepListField(goodsText.goods_detail_goods_segment());
			goodsSegmentListField.setFieldHeight(120);
			goodsSegmentListField.setSelectAllCheckBoxVisible(true);
			put(GOODS_SEGMENT_CODE_LIST, goodsSegmentListField);
			
			JepTreeField goodsCatalogTreeField = new JepTreeField(goodsText.goods_detail_goods_catalog());
//			goodsCatalogTreeField.setCheckStyle(CheckCascade.PARENTS);
			goodsCatalogTreeField.setCheckNodes(CheckNodes.LEAF);
			put(GOODS_CATALOG_ID_LIST, goodsCatalogTreeField);
			put(GOODS_PHOTO, new JepImageField(goodsText.goods_detail_goods_photo()));
			put(GOODS_PORTFOLIO, new JepFileField(goodsText.goods_detail_goods_portfolio()));
		}};
	}
 
}
