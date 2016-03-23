package com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.list;
 
import static com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientConstant.allShopGoodsText;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_GOODS_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_ID;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.SHOP_NAME;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.JepColumn;
 
public class AllShopGoodsListFormViewImpl extends StandardListFormViewImpl {
 
	public AllShopGoodsListFormViewImpl() {
		grid.setWrapHeaders(true);
		grid.setDndEnabled(true);
	}
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(SHOP_GOODS_ID, allShopGoodsText.allShopGoods_list_shop_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_ID, allShopGoodsText.allShopGoods_list_shop_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_NAME, allShopGoodsText.allShopGoods_list_shop_name(), 150));
		columns.add(new JepColumn(GOODS_ID, allShopGoodsText.allShopGoods_list_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(GOODS_NAME, allShopGoodsText.allShopGoods_list_goods_name(), 150));
		return columns;
	}
}
