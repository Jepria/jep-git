package com.technology.jep.jepriatoolkit.creator.module.adapter;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OFF;

public class ReverseBooleanAdapter extends BooleanAdapter {
	@Override
	public Boolean unmarshal(String s) {
		return !OFF.equalsIgnoreCase(s);
	}

	@Override
	public String marshal(Boolean c) {
		return Boolean.FALSE.equals(c) ? OFF : null;
	}

}
