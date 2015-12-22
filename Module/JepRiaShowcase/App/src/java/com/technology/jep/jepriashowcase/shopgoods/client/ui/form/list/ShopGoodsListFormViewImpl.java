package com.technology.jep.jepriashowcase.shopgoods.client.ui.form.list;
 
import static com.technology.jep.jepriashowcase.shopgoods.client.ShopGoodsClientConstant.shopGoodsText;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;
import com.technology.jep.jepria.shared.record.JepRecord;
 
public class ShopGoodsListFormViewImpl extends ListFormViewImpl<GridManager> {
 
	public ShopGoodsListFormViewImpl() {
		super(new GridManager());
 
		HeaderPanel gridPanel = new HeaderPanel();
		setWidget(gridPanel);
 
		gridPanel.setHeight("100%");
		gridPanel.setWidth("100%");
 
		JepGrid<JepRecord> grid = new JepGrid<JepRecord>(getClass().getCanonicalName(), getColumnConfigurations(), true);
		PagingStandardBar pagingBar = new PagingStandardBar(25);
 
		gridPanel.setContentWidget(grid);
		gridPanel.setFooterWidget(pagingBar);
 
		list.setWidget(grid);
		list.setPagingToolBar(pagingBar);
	}
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
 
	private static List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(SHOP_GOODS_ID, shopGoodsText.shopGoods_list_shop_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_NAME, shopGoodsText.shopGoods_list_shop_name(), 150));
		columns.add(new JepColumn(GOODS_NAME, shopGoodsText.shopGoods_list_goods_name(), 150));
		return columns;
	}
}
