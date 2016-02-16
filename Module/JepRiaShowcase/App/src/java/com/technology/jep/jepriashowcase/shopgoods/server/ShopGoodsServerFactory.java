package com.technology.jep.jepriashowcase.shopgoods.server;

import static com.technology.jep.jepriashowcase.shopgoods.server.ShopGoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.shopgoods.server.dao.ShopGoods;
import com.technology.jep.jepriashowcase.shopgoods.server.dao.ShopGoodsDao;

public class ShopGoodsServerFactory implements ServerFactory<ShopGoods> {

	private ShopGoodsServerFactory(){}

	public static final ServerFactory<ShopGoods> instance = new ShopGoodsServerFactory();
	
	private ShopGoods dao;
	
	@Override
	public ShopGoods getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new ShopGoodsDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
