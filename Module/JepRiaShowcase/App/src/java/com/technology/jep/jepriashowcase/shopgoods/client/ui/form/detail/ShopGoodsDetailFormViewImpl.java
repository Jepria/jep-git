package com.technology.jep.jepriashowcase.shopgoods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.shopgoods.client.ShopGoodsClientConstant.shopGoodsText;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.GOODS_QUANTITY;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SELL_PRICE;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SHOP_ID;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepComboBoxField;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepMoneyField;
 
public class ShopGoodsDetailFormViewImpl
	extends StandardDetailFormViewImpl 
	implements ShopGoodsDetailFormView {	
 
	@Override
	protected LinkedHashMap<String, Widget> getFieldConfigurations() {
		return new LinkedHashMap<String, Widget>() {{
			put(GOODS_ID, new JepIntegerField(shopGoodsText.shopGoods_detail_goods_id()));
			
			JepComboBoxField shopIdComboBoxField = new JepComboBoxField(shopGoodsText.shopGoods_detail_shop_id());
			shopIdComboBoxField.setEmptyText(shopGoodsText.shopGoods_detail_shop_id_emptyText());
			put(SHOP_ID, shopIdComboBoxField);
			
			JepMoneyField goodsQuantityMoneyField = new JepMoneyField(shopGoodsText.shopGoods_detail_goods_quantity());
			goodsQuantityMoneyField.setMaxNumberCharactersAfterDecimalSeparator(4);
			goodsQuantityMoneyField.setMaxLength(20);
			put(GOODS_QUANTITY, goodsQuantityMoneyField);
			
			JepMoneyField sellPriceMoneyField = new JepMoneyField(shopGoodsText.shopGoods_detail_sell_price());
			sellPriceMoneyField.setMaxLength(18);
			put(SELL_PRICE, sellPriceMoneyField);
		}};
	}
}
