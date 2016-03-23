package com.technology.jep.jepriashowcase.shopgoods.client.ui.form.list;
 
import static com.technology.jep.jepriashowcase.shopgoods.client.ShopGoodsClientConstant.shopGoodsText;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SHOP_GOODS_ID;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SHOP_NAME;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.JepColumn;
 
public class ShopGoodsListFormViewImpl extends StandardListFormViewImpl {
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(SHOP_GOODS_ID, shopGoodsText.shopGoods_list_shop_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_NAME, shopGoodsText.shopGoods_list_shop_name(), 150));
		columns.add(new JepColumn(GOODS_NAME, shopGoodsText.shopGoods_list_goods_name(), 150));
		return columns;
	}
}
