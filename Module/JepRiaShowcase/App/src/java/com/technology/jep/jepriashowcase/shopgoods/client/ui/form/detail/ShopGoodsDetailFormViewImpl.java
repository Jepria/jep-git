package com.technology.jep.jepriashowcase.shopgoods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.shopgoods.client.ShopGoodsClientConstant.shopGoodsText;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.*;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMoneyField;
 
public class ShopGoodsDetailFormViewImpl
	extends DetailFormViewImpl 
	implements ShopGoodsDetailFormView {	
 
	public ShopGoodsDetailFormViewImpl() {
		super(new FieldManager());
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
		JepIntegerField goodsIdIntegerField = new JepIntegerField(shopGoodsText.shopGoods_detail_goods_id());
		JepComboBoxField shopIdComboBoxField = new JepComboBoxField(shopGoodsText.shopGoods_detail_shop_id());
		shopIdComboBoxField.setEmptyText(shopGoodsText.shopGoods_detail_shop_id_emptyText());
		JepMoneyField goodsQuantityMoneyField = new JepMoneyField(shopGoodsText.shopGoods_detail_goods_quantity());
		goodsQuantityMoneyField.setMaxNumberCharactersAfterDecimalSeparator(4);
		goodsQuantityMoneyField.setMaxLength(20);
		
		JepMoneyField sellPriceMoneyField = new JepMoneyField(shopGoodsText.shopGoods_detail_sell_price());
		sellPriceMoneyField.setMaxLength(18);
		
		panel.add(goodsIdIntegerField);
		panel.add(shopIdComboBoxField);
		panel.add(goodsQuantityMoneyField);
		panel.add(sellPriceMoneyField);
		
		setWidget(scrollPanel);
 
		fields.put(GOODS_ID, goodsIdIntegerField);
		fields.put(SHOP_ID, shopIdComboBoxField);
		fields.put(GOODS_QUANTITY, goodsQuantityMoneyField);
		fields.put(SELL_PRICE, sellPriceMoneyField);
	}
 
}
