package com.technology.jep.jepria.client.widget.list.cell;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class JepCheckBoxCell extends CheckboxCell {
  
  private static final SafeHtml INPUT_CHECKED_READONLY = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" checked disabled style=\"margin: 0px;\"/>");
  private static final SafeHtml INPUT_UNCHECKED_READONLY = SafeHtmlUtils.fromSafeConstant("<input type=\"checkbox\" tabindex=\"-1\" disabled style=\"margin: 0px;\"/>");
  
  @Override
  public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
    if (context != null) {
      // Get the view data.
      Object key = context.getKey();
      Boolean viewData = getViewData(key);
      if (viewData != null && viewData.equals(value)) {
        clearViewData(key);
        viewData = null;
      }
  
      if (value != null && ((viewData != null) ? viewData : value)) {
        sb.append(INPUT_CHECKED_READONLY);
      } else {
        sb.append(INPUT_UNCHECKED_READONLY);
      }
    } else {
      if (value != null && value) {
        sb.append(INPUT_CHECKED_READONLY);
      } else {
        sb.append(INPUT_UNCHECKED_READONLY);
      }
    }
  }
}
