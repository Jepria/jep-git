package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.service;

import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.server.${form.formName}ServerConstant.RESOURCE_BUNDLE_NAME;

import com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.shared.record.${form.formName}RecordDefinition;
import com.technology.jep.jepria.server.upload.JepUploadServlet;

public class UploadServiceImpl extends JepUploadServlet {

	public UploadServiceImpl() {
		super(${form.formName}RecordDefinition.instance, DATA_SOURCE_JNDI_NAME, RESOURCE_BUNDLE_NAME);
	}
}
