package com.technology.jep.jepriashowcase.shopgoods.server.dao;
 
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
public interface ShopGoods extends JepDataStandard {
	List<JepOption> getShop(String shopName, Integer rowCount) throws ApplicationException;
	List<JepOption> getGoods(String goodsName, Integer rowCount) throws ApplicationException;
	
}
