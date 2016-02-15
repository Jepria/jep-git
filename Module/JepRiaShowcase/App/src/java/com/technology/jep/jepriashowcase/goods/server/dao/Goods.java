package com.technology.jep.jepriashowcase.goods.server.dao;
 
import java.util.List;

import com.technology.jep.jepria.server.ejb.JepDataStandard;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
 
public interface Goods extends JepDataStandard {
	List<JepOption> getGoodsType() throws ApplicationException;
	List<JepOption> getUnit() throws ApplicationException;
	List<JepOption> getMotivationType() throws ApplicationException;
	List<JepOption> getGoodsCatalog(Integer parentGoodsCatalogId, Integer goodsId) throws ApplicationException;
	List<JepOption> getGoodsSegment() throws ApplicationException;
}
