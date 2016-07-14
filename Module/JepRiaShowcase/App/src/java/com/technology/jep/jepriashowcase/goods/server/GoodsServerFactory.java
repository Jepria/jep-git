package com.technology.jep.jepriashowcase.goods.server;

import static com.technology.jep.jepriashowcase.goods.server.GoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.goods.server.dao.Goods;
import com.technology.jep.jepriashowcase.goods.server.dao.GoodsDao;

public class GoodsServerFactory extends ServerFactory<Goods> {

  private GoodsServerFactory() {
    super(new GoodsDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final GoodsServerFactory instance = new GoodsServerFactory();
  
}
