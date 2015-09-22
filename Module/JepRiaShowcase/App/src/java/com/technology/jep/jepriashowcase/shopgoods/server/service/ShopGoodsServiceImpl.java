package com.technology.jep.jepriashowcase.shopgoods.server.service;
 
import static com.technology.jep.jepriashowcase.shopgoods.server.ShopGoodsServerConstant.BEAN_JNDI_NAME;
import static com.technology.jep.jepriashowcase.shopgoods.shared.ShopGoodsConstant.OPTION_COUNT;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.server.util.JepServerUtil;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepriashowcase.shopgoods.server.ejb.ShopGoods;
import com.technology.jep.jepriashowcase.shopgoods.shared.record.ShopGoodsRecordDefinition;
import com.technology.jep.jepriashowcase.shopgoods.shared.service.ShopGoodsService;
 
@RemoteServiceRelativePath("ShopGoodsService")
public class ShopGoodsServiceImpl extends JepDataServiceServlet implements ShopGoodsService  {
 
	private static final long serialVersionUID = 1L;
 
	public ShopGoodsServiceImpl() {
		super(ShopGoodsRecordDefinition.instance, BEAN_JNDI_NAME);
	}
	
	public List<JepOption> getShop(String shopName) throws ApplicationException {
		List<JepOption> result = null;
		try {
			ShopGoods shopGoods = (ShopGoods) JepServerUtil.ejbLookup(ejbName);
			result = shopGoods.getShop(shopName, OPTION_COUNT + 1);
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		return result;
	}
	
	public List<JepOption> getGoods(String goodsName) throws ApplicationException {
		List<JepOption> result = null;
		try {
			ShopGoods shopGoods = (ShopGoods) JepServerUtil.ejbLookup(ejbName);
			result = shopGoods.getGoods(goodsName, OPTION_COUNT + 1);
		} catch (Throwable th) {
			throw new ApplicationException(th.getLocalizedMessage(), th);
		}
		return result;
	}
}
