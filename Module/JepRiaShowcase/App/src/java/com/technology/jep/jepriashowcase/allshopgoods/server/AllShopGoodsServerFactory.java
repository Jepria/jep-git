package com.technology.jep.jepriashowcase.allshopgoods.server;

import static com.technology.jep.jepriashowcase.allshopgoods.server.AllShopGoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.allshopgoods.server.dao.AllShopGoods;
import com.technology.jep.jepriashowcase.allshopgoods.server.dao.AllShopGoodsDao;

public class AllShopGoodsServerFactory extends ServerFactory<AllShopGoods> {

  private AllShopGoodsServerFactory() {
    super(new AllShopGoodsDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final AllShopGoodsServerFactory instance = new AllShopGoodsServerFactory();
  
}
