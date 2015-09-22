package com.technology.jep.jepriashowcase.supplier.client.ui.form.list;
 
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepriashowcase.supplier.client.SupplierClientConstant.supplierText;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.BANKNAME;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.CONTRACT_FINISH_DATE;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.EXCLUSIVE_SUPPLIER_FLAG;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.FAX_NUMBER;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.PHONE_NUMBER;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.RECIPIENT_NAME;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SETTLEMENT_ACCOUNT;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_DESCRIPTION;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_ID;
import static com.technology.jep.jepriashowcase.supplier.shared.field.SupplierFieldNames.SUPPLIER_NAME;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.technology.jep.jepria.client.ui.form.list.ListFormViewImpl;
import com.technology.jep.jepria.client.widget.list.GridManager;
import com.technology.jep.jepria.client.widget.list.JepColumn;
import com.technology.jep.jepria.client.widget.list.JepGrid;
import com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepriashowcase.main.client.style.ApplicationResource;
 
public class SupplierListFormViewImpl extends ListFormViewImpl<GridManager> {
 
	public SupplierListFormViewImpl() {
		super(new GridManager());
 
		HeaderPanel gridPanel = new HeaderPanel();
		setWidget(gridPanel);
 
		gridPanel.setHeight("100%");
		gridPanel.setWidth("100%");
 
		JepGrid<JepRecord> grid = new JepGrid<JepRecord>(getGridId(), getColumnConfigurations(), true);
		// Подтягиваем css-ресурс как инлайн стиль
		ApplicationResource.instance.resource().ensureInjected();
		// Строки списочной формы, у которых CONTACT_FINISH_DATE истек, выделяем красным
		grid.setRowStyles(new RowStyles<JepRecord>() {
		    @Override
		    public String getStyleNames(JepRecord rowObject, int rowIndex) {
		    	Date currentDate = new Date();
		        if (rowObject.get(CONTRACT_FINISH_DATE) != null && currentDate.after((Date) rowObject.get(CONTRACT_FINISH_DATE))) {
		            return ApplicationResource.instance.resource().red();
		        }
		        return null;
		    }
		});
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
		columns.add(new JepColumn(SUPPLIER_ID, supplierText.supplier_list_supplier_id(), 150, new NumberCell(defaultNumberFormatter)));
		columns.add(new JepColumn(SUPPLIER_NAME, supplierText.supplier_list_supplier_name(), 150));
		columns.add(new JepColumn(CONTRACT_FINISH_DATE, supplierText.supplier_list_contract_finish_date(), 150, new DateCell(defaultDateFormatter)));
		columns.add(new JepColumn(EXCLUSIVE_SUPPLIER_FLAG, supplierText.supplier_list_exclusive_supplier_flag(), 150, new JepCheckBoxCell()));
		columns.add(new JepColumn(SUPPLIER_DESCRIPTION, supplierText.supplier_list_supplier_description(), 150));
		columns.add(new JepColumn(PHONE_NUMBER, supplierText.supplier_list_phoneNumber(), 100));
		columns.add(new JepColumn(FAX_NUMBER, supplierText.supplier_list_faxNumber(), 100));
		columns.add(new JepColumn(BANKNAME, supplierText.supplier_list_bankname(), 200));
		columns.add(new JepColumn(RECIPIENT_NAME, supplierText.supplier_list_recipientName(), 200));
		columns.add(new JepColumn(SETTLEMENT_ACCOUNT, supplierText.supplier_list_settlementAccount(), 150));
		return columns;
	}
 
	private String getGridId() {
		return this.getClass().toString().replace("class ", "");
	}
}
