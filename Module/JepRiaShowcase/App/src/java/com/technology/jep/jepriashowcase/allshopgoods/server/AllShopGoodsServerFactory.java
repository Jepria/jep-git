package com.technology.jep.jepriashowcase.allshopgoods.server;

import static com.technology.jep.jepriashowcase.allshopgoods.server.AllShopGoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.allshopgoods.server.dao.AllShopGoods;
import com.technology.jep.jepriashowcase.allshopgoods.server.dao.AllShopGoodsDao;

public class AllShopGoodsServerFactory implements ServerFactory<AllShopGoods> {

	private AllShopGoodsServerFactory(){}

	public static final ServerFactory<AllShopGoods> instance = new AllShopGoodsServerFactory();
	
	private AllShopGoods dao;
	
	@Override
	public AllShopGoods getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new AllShopGoodsDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
