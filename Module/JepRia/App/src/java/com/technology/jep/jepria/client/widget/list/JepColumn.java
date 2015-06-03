package com.technology.jep.jepria.client.widget.list;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.technology.jep.jepria.client.widget.list.cell.JepCheckBoxCell;
import com.technology.jep.jepria.shared.record.JepRecord;

public class JepColumn<T, C> extends Column<T, C> {
	
	protected static String NORMAL_WRAP_STYLE = "jepGrid-normalWrap";
	protected String fieldName;
	protected String headerText;
	private double width;
	protected Boolean wrapText;

	public JepColumn(
			Cell<C> cell) {
		super(cell);
	}

	public JepColumn(
			String fieldName, 
			String headerText, 
			double width) {
		this(fieldName, headerText, width, (Cell<C>) new TextCell(), false);
	}

	public JepColumn(
			String fieldName, 
			String headerText, 
			double width,
			Cell<C> cell) {
		this(fieldName, headerText, width, cell, false);
	}

	public JepColumn(
			String fieldName,
			String headerText, 
			double width,
			Cell<C> cell, 
			Boolean wrapText) {
		
		super(cell);
		
		this.fieldName = fieldName;
		this.headerText = headerText;
		this.width = width;
		
		setDataStoreName(fieldName);
		
		HorizontalAlignmentConstant align = HasHorizontalAlignment.ALIGN_LEFT;
		
		if (cell instanceof JepCheckBoxCell)
			align = HasHorizontalAlignment.ALIGN_CENTER;
		if (cell instanceof NumberCell)
			align = HasHorizontalAlignment.ALIGN_RIGHT;

		setHorizontalAlignment(align);
		
		//setSortable(true);
		
		if (wrapText) {
			setCellStyleNames(NORMAL_WRAP_STYLE);
		}
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getHeaderText() {
		return headerText;
	}

	public Double getWidth() {
		return width;
	}

	@Override
	public C getValue(T object) {
		return object instanceof JepRecord ? (C) ((JepRecord)object).get(fieldName) : (C) (object.toString());
	}
}
