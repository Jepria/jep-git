package com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientConstant.allShopGoodsText;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_GOODS_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_NAME;

import com.technology.jep.jepria.client.ui.form.detail.StandardDetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
 
public class AllShopGoodsDetailFormViewImpl
	extends StandardDetailFormViewImpl 
	implements AllShopGoodsDetailFormView {	
 
	public AllShopGoodsDetailFormViewImpl() {
		JepIntegerField shopGoodsIdIntegerField = new JepIntegerField(allShopGoodsText.allShopGoods_detail_shop_goods_id());
		JepIntegerField shopIdIntegerField = new JepIntegerField(allShopGoodsText.allShopGoods_detail_shop_id());
		JepTextField shopNameTextField = new JepTextField(allShopGoodsText.allShopGoods_detail_shop_name());
		JepIntegerField goodsIdIntegerField = new JepIntegerField(allShopGoodsText.allShopGoods_detail_goods_id());
		JepTextField goodsNameTextField = new JepTextField(allShopGoodsText.allShopGoods_detail_goods_name());
		panel.add(shopGoodsIdIntegerField);
		panel.add(shopIdIntegerField);
		panel.add(shopNameTextField);
		panel.add(goodsIdIntegerField);
		panel.add(goodsNameTextField);
		
		fields.put(SHOP_GOODS_ID, shopGoodsIdIntegerField);
		fields.put(SHOP_ID, shopIdIntegerField);
		fields.put(SHOP_NAME, shopNameTextField);
		fields.put(GOODS_ID, goodsIdIntegerField);
		fields.put(GOODS_NAME, goodsNameTextField);
	}
 
}
