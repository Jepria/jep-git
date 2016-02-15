package com.technology.jep.jepriashowcase.allshopgoods.server.service;
 
import com.technology.jep.jepriashowcase.allshopgoods.server.dao.AllShopGoods;
import com.technology.jep.jepriashowcase.allshopgoods.server.dao.AllShopGoodsDao;
import com.technology.jep.jepriashowcase.allshopgoods.shared.service.AllShopGoodsService;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepriashowcase.allshopgoods.shared.record.AllShopGoodsRecordDefinition;

import static com.technology.jep.jepriashowcase.allshopgoods.server.AllShopGoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
 
@RemoteServiceRelativePath("AllShopGoodsService")
public class AllShopGoodsServiceImpl extends JepDataServiceServlet<AllShopGoods> implements AllShopGoodsService  {
 
	private static final long serialVersionUID = 1L;
 
	public AllShopGoodsServiceImpl() {
		super(AllShopGoodsRecordDefinition.instance,
				TransactionFactory.process(new AllShopGoodsDao(), DATA_SOURCE_JNDI_NAME));
	}
}
