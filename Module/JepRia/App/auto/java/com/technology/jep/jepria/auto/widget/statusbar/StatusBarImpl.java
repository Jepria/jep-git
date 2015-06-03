package com.technology.jep.jepria.auto.widget.statusbar;

import com.technology.jep.jepria.auto.entrance.pages.JepRiaApplicationPageManager;

public class StatusBarImpl<P extends JepRiaApplicationPageManager> implements StatusBar {

	P pages;
	
	public StatusBarImpl(P pages) {
		this.pages = pages;
	}

	@Override
	public String getText() {
		return pages
				.getApplicationPage()
				.ensurePageLoaded()
				.getStatusBarText();
	}

}
