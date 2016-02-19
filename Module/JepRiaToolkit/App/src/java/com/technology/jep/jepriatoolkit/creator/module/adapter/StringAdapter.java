package com.technology.jep.jepriatoolkit.creator.module.adapter;

import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NO_NAME;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringAdapter extends XmlAdapter<String, String> {
	@Override
	public String unmarshal(String s) {
		return s;
	}

	@Override
	public String marshal(String c) {
		return isEmpty(c) ? NO_NAME : c;
	}
}
