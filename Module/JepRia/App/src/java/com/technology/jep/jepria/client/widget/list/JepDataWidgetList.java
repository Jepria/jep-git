package com.technology.jep.jepria.client.widget.list;

import static com.technology.jep.jepria.shared.load.PagingConfig.DEFAULT_PAGE_SIZE;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.technology.jep.jepria.shared.record.JepRecord;

public class JepDataWidgetList extends AbstractHasData<JepRecord> {

  private static VerticalPanel dataPanel = new VerticalPanel();
  private final Element childContainer;

  public JepDataWidgetList() {
    super(dataPanel, DEFAULT_PAGE_SIZE, null);
    dataPanel.setWidth("100%");

    childContainer = Document.get().createTableElement();
  }

  @Override
  protected boolean dependsOnSelection() {
    return false;
  }

  @Override
  protected Element getChildContainer() {
    return childContainer;
  }

  @Override
  protected Element getKeyboardSelectedElement() {
    return null;
  }

  @Override
  protected boolean isKeyboardNavigationSuppressed() {
    return false;
  }

  @Override
  protected void renderRowValues(SafeHtmlBuilder sb, List<JepRecord> values, int start, SelectionModel<? super JepRecord> selectionModel)
      throws UnsupportedOperationException {

    dataPanel.clear();
    for (JepRecord data : values) {
      dataPanel.add(getDataWidget(data));
    }
  }

  @Override
  protected boolean resetFocusOnCell() {
    return false;
  }

  @Override
  protected void setKeyboardSelected(int index, boolean selected, boolean stealFocus) {
  }

  protected Widget getDataWidget(JepRecord data) {
    return new Label(data.toString());
  }
}
