package com.technology.jep.jepriashowcase.allshopgoods.client.ui.form.list;
 
import static com.technology.jep.jepriashowcase.allshopgoods.client.AllShopGoodsClientConstant.allShopGoodsText;
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.list.ListManager;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;
import com.technology.jep.jepria.shared.record.JepRecord;
 
public class AllShopGoodsListFormViewImpl extends ListFormViewImpl<GridManager> {
 
	public AllShopGoodsListFormViewImpl() {
		super(new GridManager());
 
		HeaderPanel gridPanel = new HeaderPanel();
		setWidget(gridPanel);
 
		gridPanel.setHeight("100%");
		gridPanel.setWidth("100%");
 
		JepGrid<JepRecord> grid = new JepGrid<JepRecord>(getGridId(), getColumnConfigurations());
		grid.setWrapHeaders(true);
		grid.setDndEnabled(true);
		PagingStandardBar pagingBar = new PagingStandardBar(25);
 
		gridPanel.setContentWidget(grid);
		gridPanel.setFooterWidget(pagingBar);
 
		list.setWidget(grid);
		list.setPagingToolBar(pagingBar);
	}
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
 
	private static List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(SHOP_GOODS_ID, allShopGoodsText.allShopGoods_list_shop_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_ID, allShopGoodsText.allShopGoods_list_shop_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_NAME, allShopGoodsText.allShopGoods_list_shop_name(), 150));
		columns.add(new JepColumn(GOODS_ID, allShopGoodsText.allShopGoods_list_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(GOODS_NAME, allShopGoodsText.allShopGoods_list_goods_name(), 150));
		return columns;
	}
 
	private String getGridId() {
		return this.getClass().toString().replace("class ", "");
	}
}
