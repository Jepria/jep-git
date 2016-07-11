package com.technology.jep.jepriashowcase.goods.client.ui.form.list;
 
import static com.technology.jep.jepriashowcase.goods.client.GoodsClientConstant.goodsText;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_TYPE_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.PURCHASING_PRICE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.SUPPLIER_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.UNIT_NAME;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.technology.jep.jepria.client.ui.form.list.StandardListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.JepColumn;
 
public class GoodsListFormViewImpl extends StandardListFormViewImpl {
 
	public GoodsListFormViewImpl() {
		super(GoodsListFormViewImpl.class.getCanonicalName());
	}

	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(SUPPLIER_ID, goodsText.goods_list_supplier_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(GOODS_ID, goodsText.goods_list_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(GOODS_NAME, goodsText.goods_list_goods_name(), 150));
		columns.add(new JepColumn(GOODS_TYPE_NAME, goodsText.goods_list_goods_type(), 150));
		columns.add(new JepColumn(UNIT_NAME, goodsText.goods_list_unit(), 150));
		JepColumn column = new JepColumn(PURCHASING_PRICE, goodsText.goods_list_purchasing_price(), 150, new NumberCell(NumberFormat.getDecimalFormat()));
		
		columns.add(column);
		return columns;
	}
}
