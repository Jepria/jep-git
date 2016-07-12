package com.technology.jep.jepriashowcase.goods.shared.service;
 
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.service.data.JepDataServiceAsync;
 
public interface GoodsServiceAsync extends JepDataServiceAsync {
  void getGoodsType(AsyncCallback<List<JepOption>> callback);
  void getUnit(AsyncCallback<List<JepOption>> callback);
  void getMotivationType(AsyncCallback<List<JepOption>> callback);
  void getGoodsCatalog(Integer parentGoodsCatalogId, Integer goodsId, AsyncCallback<List<JepOption>> callback);
  void getGoodsSegment(AsyncCallback<List<JepOption>> callback);
}
