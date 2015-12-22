package com.technology.jep.jepriashowcase.request.client.ui.form.list;
 
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepriashowcase.request.client.RequestClientConstant.requestText;
import static com.technology.jep.jepriashowcase.request.shared.field.RequestFieldNames.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;
import com.technology.jep.jepria.shared.record.JepRecord;
 
public class RequestListFormViewImpl extends ListFormViewImpl<GridManager> {
 
	public RequestListFormViewImpl() {
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
	private static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
 
	private static List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(REQUEST_ID, requestText.request_list_request_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(REQUEST_DATE, requestText.request_list_request_date(), 150, new DateCell(defaultDateFormatter)));
		columns.add(new JepColumn(REQUEST_STATUS_NAME, requestText.request_list_request_status_name(), 150));
		columns.add(new JepColumn(SHOP_ID, requestText.request_list_shop_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SHOP_NAME, requestText.request_list_shop_name(), 150));
		columns.add(new JepColumn(GOODS_ID, requestText.request_list_goods_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(GOODS_NAME, requestText.request_list_goods_name(), 150));
		return columns;
	}
}
