package com.technology.jep.jepriashowcase.search.client.ui.pagingtoolbar;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.technology.jep.jepria.client.widget.button.Separator;
import com.technology.jep.jepria.client.widget.toolbar.PagingStandardBar;

public class PagingSimpleBar extends PagingStandardBar {

	private static final String BOLD_BUTTON_STYLE = "gwt-Button boldButton";
	
	public PagingSimpleBar(int pgSize) {
		super(pgSize);
	}
	
	@Override
	protected void addButtonsAtLeft() {
		first = makeButton(null, null, JepTexts.button_beginning_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				first();
			}
		}, false);
		first.setStyleName(BOLD_BUTTON_STYLE);
		
		buttonsPanel.add(first);

		prev = makeButton(null, null, JepTexts.button_previous_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				previous();
			}
		}, false);
		buttonsPanel.add(prev);
	}
	
	@Override
	protected void addButtonsAtRight() {
		next = makeButton(null, null, JepTexts.button_next_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				next();
			}
		}, false);
		buttonsPanel.add(next);

		last = makeButton(null, null, JepTexts.button_ending_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				last();
			}
		}, false);
		last.setStyleName(BOLD_BUTTON_STYLE);
		buttonsPanel.add(last);
		
		buttonsPanel.add(new Separator());

		refresh = makeButton(null, null, JepTexts.button_refresh_alt(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		}, false);
		buttonsPanel.add(refresh);
	}

}
