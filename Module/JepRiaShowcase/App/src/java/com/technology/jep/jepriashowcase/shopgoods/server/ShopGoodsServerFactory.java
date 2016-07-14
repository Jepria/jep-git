package com.technology.jep.jepriashowcase.shopgoods.server;

import static com.technology.jep.jepriashowcase.shopgoods.server.ShopGoodsServerConstant.DATA_SOURCE_JNDI_NAME;

import com.technology.jep.jepria.server.ServerFactory;
import com.technology.jep.jepriashowcase.shopgoods.server.dao.ShopGoods;
import com.technology.jep.jepriashowcase.shopgoods.server.dao.ShopGoodsDao;

public class ShopGoodsServerFactory extends ServerFactory<ShopGoods> {

  private ShopGoodsServerFactory() {
    super(new ShopGoodsDao(), DATA_SOURCE_JNDI_NAME);
  }

  public static final ShopGoodsServerFactory instance = new ShopGoodsServerFactory();
  
}
