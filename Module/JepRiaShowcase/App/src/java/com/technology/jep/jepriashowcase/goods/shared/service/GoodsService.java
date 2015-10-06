package com.technology.jep.jepriashowcase.goods.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataService;
 
@RemoteServiceRelativePath("GoodsService")
public interface GoodsService extends JepDataService {
	List<JepOption> getGoodsType() throws ApplicationException;
	List<JepOption> getUnit() throws ApplicationException;
	List<JepOption> getMotivationType() throws ApplicationException;
	List<JepOption> getGoodsCatalog(Integer parentGoodsCatalogId, Integer goodsId) throws ApplicationException;
	List<JepOption> getGoodsSegment() throws ApplicationException;
}
