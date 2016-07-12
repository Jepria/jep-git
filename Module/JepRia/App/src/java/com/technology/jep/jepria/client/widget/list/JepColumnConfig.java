package com.technology.jep.jepria.client.widget.list;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Widget;

public class JepColumnConfig {

  private String header;
  private String id;
  private boolean visible;

  private String toolTip;
  private String style;
  private String dataIndex;
  private boolean sortable = true;
  private boolean fixed;
  private boolean resizable = true;
  private boolean menuDisabled;
  private boolean hidden;
  private int width;
  private NumberFormat numberFormat;
  private DateTimeFormat dateTimeFormat;
  private boolean groupable = true;
  private Widget widget;
  private String columnStyleName;
  private boolean rowHeader;
  
  public JepColumnConfig(Header header, Column column) {
    this.header = header.getValue().toString();
    this.id = column.getDataStoreName();
  }

  public String getHeader() {
    return header;
  }

  public String getId() {
    return id;
  }
}
