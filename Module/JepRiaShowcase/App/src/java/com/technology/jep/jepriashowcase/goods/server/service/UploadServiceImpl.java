package com.technology.jep.jepriashowcase.goods.server.service;
 
import static com.technology.jep.jepriashowcase.goods.server.GoodsServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.goods.server.GoodsServerConstant.RESOURCE_BUNDLE_NAME;
 
import com.technology.jep.jepriashowcase.goods.shared.record.GoodsRecordDefinition;
import com.technology.jep.jepria.server.upload.JepUploadServlet;
 
public class UploadServiceImpl extends JepUploadServlet {
 
	public UploadServiceImpl() {
		super(GoodsRecordDefinition.instance,
			DATA_SOURCE_JNDI_NAME, 
			RESOURCE_BUNDLE_NAME); 
	}
 
}
