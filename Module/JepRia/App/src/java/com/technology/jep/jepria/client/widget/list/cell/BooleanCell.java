package com.technology.jep.jepria.client.widget.list.cell;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class BooleanCell extends AbstractCell<Boolean> {

	public BooleanCell(String... consumedEvents) {
		super(consumedEvents);
	}

	public BooleanCell(Set<String> consumedEvents) {
		super(consumedEvents);
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Boolean value, SafeHtmlBuilder sb) {
		
		String label = Boolean.TRUE.equals(value) ? JepTexts.yes() : (value == null) ? "" : JepTexts.no();
		sb.appendEscaped(label);
	}
}
