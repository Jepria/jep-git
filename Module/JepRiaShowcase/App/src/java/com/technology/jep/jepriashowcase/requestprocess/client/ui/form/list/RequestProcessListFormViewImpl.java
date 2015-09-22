package com.technology.jep.jepriashowcase.requestprocess.client.ui.form.list;
 
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepriashowcase.requestprocess.client.RequestProcessClientConstant.requestProcessText;
import static com.technology.jep.jepriashowcase.requestprocess.shared.field.RequestProcessFieldNames.*;

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
 
public class RequestProcessListFormViewImpl extends ListFormViewImpl<GridManager> {
 
	public RequestProcessListFormViewImpl() {
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
	private static DateTimeFormat defaultDateFormatter = DateTimeFormat.getFormat(DEFAULT_DATE_FORMAT);
 
	private static List<JepColumn> getColumnConfigurations() {
		final List<JepColumn> columns = new ArrayList<JepColumn>();
		columns.add(new JepColumn(REQUEST_PROCESS_ID, requestProcessText.requestProcess_list_request_process_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(REQUEST_ID, requestProcessText.requestProcess_list_request_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(PROCESS_COMMENT, requestProcessText.requestProcess_list_process_comment(), 150));
		columns.add(new JepColumn(DATE_INS, requestProcessText.requestProcess_list_date_ins(), 150, new DateCell(defaultDateFormatter)));
		columns.add(new JepColumn(OPERATOR_ID, requestProcessText.requestProcess_list_operator_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(OPERATOR_NAME, requestProcessText.requestProcess_list_operator_name(), 150));
		return columns;
	}
 
	private String getGridId() {
		return this.getClass().toString().replace("class ", "");
	}
}
