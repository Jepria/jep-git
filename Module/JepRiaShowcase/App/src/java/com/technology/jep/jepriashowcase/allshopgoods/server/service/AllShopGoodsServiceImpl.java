package com.technology.jep.jepriashowcase.allshopgoods.server.service;
 
import com.technology.jep.jepriashowcase.allshopgoods.shared.service.AllShopGoodsService;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepriashowcase.allshopgoods.shared.record.AllShopGoodsRecordDefinition;
import static com.technology.jep.jepriashowcase.allshopgoods.server.AllShopGoodsServerConstant.BEAN_JNDI_NAME;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
 
@RemoteServiceRelativePath("AllShopGoodsService")
public class AllShopGoodsServiceImpl extends JepDataServiceServlet implements AllShopGoodsService  {
 
	private static final long serialVersionUID = 1L;
 
	public AllShopGoodsServiceImpl() {
		super(AllShopGoodsRecordDefinition.instance, BEAN_JNDI_NAME);
	}
}
