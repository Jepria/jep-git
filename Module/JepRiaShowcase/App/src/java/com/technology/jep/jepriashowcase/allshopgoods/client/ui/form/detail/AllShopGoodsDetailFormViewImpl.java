package com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientConstant.allShopGoodsText;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_GOODS_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_NAME;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
 
public class AllShopGoodsDetailFormViewImpl
	extends StandardDetailFormViewImpl 
	implements AllShopGoodsDetailFormView {	
 
	@Override
	protected LinkedHashMap<String, Widget> getFieldConfigurations() {
		return new LinkedHashMap<String, Widget>() {{
			put(SHOP_GOODS_ID, new JepIntegerField(allShopGoodsText.allShopGoods_detail_shop_goods_id()));
			put(SHOP_ID, new JepIntegerField(allShopGoodsText.allShopGoods_detail_shop_id()));
			put(SHOP_NAME, new JepTextField(allShopGoodsText.allShopGoods_detail_shop_name()));
			put(GOODS_ID, new JepIntegerField(allShopGoodsText.allShopGoods_detail_goods_id()));
			put(GOODS_NAME, new JepTextField(allShopGoodsText.allShopGoods_detail_goods_name()));
		}};
	}
 
}
