package com.technology.jep.jepriashowcase.shopgoods.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;
 
@RemoteServiceRelativePath("ShopGoodsService")
public interface ShopGoodsService extends JepDataService {
	List<JepOption> getShop(String shopName) throws ApplicationException;
	List<JepOption> getGoods(String goodsName) throws ApplicationException;
}
