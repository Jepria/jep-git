package com.technology.jep.jepriashowcase.goods.server;

import static com.technology.jep.jepriashowcase.goods.server.GoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepria.server.dao.transaction.TransactionFactory;
import com.technology.jep.jepriashowcase.goods.server.dao.Goods;
import com.technology.jep.jepriashowcase.goods.server.dao.GoodsDao;

public class GoodsServerFactory implements ServerFactory<Goods> {

	private GoodsServerFactory(){}

	public static final ServerFactory<Goods> instance = new GoodsServerFactory();
	
	private Goods dao;
	
	@Override
	public Goods getDao() {
		if (dao == null) {
			dao = TransactionFactory.process(new GoodsDao(), DATA_SOURCE_JNDI_NAME);
		}
		return dao;
	}

}
