package com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.detail;
 
import static com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientConstant.allShopGoodsText;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.*;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.technology.jep.jepria.client.ui.form.detail.DetailFormViewImpl;
import com.technology.jep.jepria.client.widget.field.FieldManager;
import com.technology.jep.jepria.client.widget.field.multistate.JepIntegerField;
import com.technology.jep.jepria.client.widget.field.multistate.JepTextField;
 
public class AllShopGoodsDetailFormViewImpl
	extends DetailFormViewImpl 
	implements AllShopGoodsDetailFormView {	
 
	public AllShopGoodsDetailFormViewImpl() {
		super(new FieldManager());
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setSize("100%", "100%");
		
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().getStyle().setMarginTop(5, Unit.PX);
		scrollPanel.add(panel);
 
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
		
		setWidget(scrollPanel);
 
		fields.put(SHOP_GOODS_ID, shopGoodsIdIntegerField);
		fields.put(SHOP_ID, shopIdIntegerField);
		fields.put(SHOP_NAME, shopNameTextField);
		fields.put(GOODS_ID, goodsIdIntegerField);
		fields.put(GOODS_NAME, goodsNameTextField);
	}
 
}
