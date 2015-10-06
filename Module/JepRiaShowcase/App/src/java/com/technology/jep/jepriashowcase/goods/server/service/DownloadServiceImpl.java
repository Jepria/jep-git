package com.technology.jep.jepriashowcase.goods.server.service;
 
import static com.technology.jep.jepriashowcase.goods.server.GoodsServerConstant.DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepriashowcase.goods.server.GoodsServerConstant.RESOURCE_BUNDLE_NAME;
 
import com.technology.jep.jepriashowcase.goods.shared.record.GoodsRecordDefinition;
import com.technology.jep.jepria.server.download.JepDownloadServlet;
 
public class DownloadServiceImpl extends JepDownloadServlet {
 
	public DownloadServiceImpl() {
		super(GoodsRecordDefinition.instance,
			DATA_SOURCE_JNDI_NAME, 
			RESOURCE_BUNDLE_NAME); 
	}
 
}
