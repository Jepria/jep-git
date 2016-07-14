package com.technology.jep.jepriashowcase.goods.server.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.technology.jep.jepria.server.service.JepDataServiceServlet;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.util.DefaultComparator;
import com.technology.jep.jepriashowcase.goods.server.GoodsServerFactory;
import com.technology.jep.jepriashowcase.goods.server.dao.Goods;
import com.technology.jep.jepriashowcase.goods.shared.record.GoodsRecordDefinition;
import com.technology.jep.jepriashowcase.goods.shared.service.GoodsService;
 
@RemoteServiceRelativePath("GoodsService")
public class GoodsServiceImpl extends JepDataServiceServlet<Goods> implements GoodsService  {
 
  private static final long serialVersionUID = 1L;
  
  /**
   * Схема сравнения.
   */
  protected Comparator<Object> comparator;
 
  public GoodsServiceImpl() {
    super(GoodsRecordDefinition.instance, 
        GoodsServerFactory.instance);
    this.comparator = DefaultComparator.instance;
  }
  
  public List<JepOption> getGoodsType() throws ApplicationException {
    List<JepOption> result = null;
    try {
      result = dao.getGoodsType();
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
  
  public List<JepOption> getUnit() throws ApplicationException {
    List<JepOption> result = null;
    try {
      result = dao.getUnit();
      // To sort our list we should use comparator with some business logic
      // in compare method
      Collections.sort(result, new Comparator<JepOption>() {
        @Override
        public int compare(JepOption o1, JepOption o2) {
          // In that case we sort list according to name option
          return comparator.compare(o1.getName(), o2.getName());
        }
      });
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
  
  public List<JepOption> getMotivationType() throws ApplicationException {
    List<JepOption> result = null;
    try {
      result = dao.getMotivationType();
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
  
  public List<JepOption> getGoodsCatalog(Integer parentGoodsCatalogId, Integer goodsId) throws ApplicationException {
    List<JepOption> result = null;

    try {
      result = dao.getGoodsCatalog(parentGoodsCatalogId, goodsId);
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
  
  public List<JepOption> getGoodsSegment() throws ApplicationException {
    List<JepOption> result = null;

    try {
      result = dao.getGoodsSegment();
    } catch (Throwable th) {
      throw new ApplicationException(th.getLocalizedMessage(), th);
    }
    return result;
  }
}
