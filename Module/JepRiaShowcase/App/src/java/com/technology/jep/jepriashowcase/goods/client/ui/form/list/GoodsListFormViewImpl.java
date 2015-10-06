package com.technology.jep.jepriashowcase.goods.client.ui.form.list;
 
import static com.technology.jep.jepriashowcase.goods.client.GoodsClientConstant.goodsText;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.*;

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
 
public class GoodsListFormViewImpl extends ListFormViewImpl<GridManager> {
 
	public GoodsListFormViewImpl() {
		super(new GridManager());
 
		HeaderPanel gridPanel = new HeaderPanel();
		setWidget(gridPanel);
 
		gridPanel.setHeight("100%");
		gridPanel.setWidth("100%");
 
		JepGrid<JepRecord> grid = new JepGrid<JepRecord>(getGridId(), getColumnConfigurations(), true);
		PagingStandardBar pagingBar = new PagingStandardBar(25);
 
		gridPanel.setContentWidget(grid);
		gridPanel.setFooterWidget(pagingBar);
 
		list.setWidget(grid);
		list.setPagingToolBar(pagingBar);
	}
 
	private static NumberFormat defaultNumberFormatter = NumberFormat.getFormat("#");
 
	private static List<JepColumn> getColumnConfigurations() {
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
 
	private String getGridId() {
		return this.getClass().toString().replace("class ", "");
	}
}
