package com.technology.jep.jepriatoolkit.creator.module.adapter;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.OFF;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanAdapter extends XmlAdapter<String, Boolean> {
	@Override
	public Boolean unmarshal(String s) {
		return OFF.equalsIgnoreCase(s);
	}

	@Override
	public String marshal(Boolean c) {
		return Boolean.TRUE.equals(c) ? OFF : null;
	}
}
