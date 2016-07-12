package com.technology.jep.jepriashowcase.shopgoods.server.dao;
 
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.GOODS_QUANTITY;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SELL_PRICE;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SHOP_GOODS_ID;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SHOP_ID;
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.SHOP_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.util.Mutable;
 
public class ShopGoodsDao extends JepDaoStandard implements ShopGoods {
 
  public List<JepRecord> find( JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin  " 
        +  "? := pkg_jepriashowcase.findShopGoods(" 
            + "shopGoodsId => ? " 
            + ", shopId => ? " 
            + ", goodsId => ? " 
          + ", maxRowCount => ? " 
          + ", operatorId => ? " 
        + ");"
     + " end;";
    return super.find( sqlQuery,
        new ResultSetMapper<JepRecord>() {
          public void map(ResultSet rs, JepRecord record) throws SQLException {
            record.set(SHOP_GOODS_ID, getInteger(rs, SHOP_GOODS_ID));
            
            JepOption option = getOption(rs, SHOP_ID, SHOP_NAME);
            record.set(SHOP_ID, option);
            record.set(SHOP_NAME, option.getName());
            
            option = getOption(rs,  GOODS_ID, GOODS_NAME);
            record.set(GOODS_ID, option.getValue());
            record.set(GOODS_NAME, option.getName());
            
            record.set(GOODS_QUANTITY, rs.getBigDecimal(GOODS_QUANTITY));
            record.set(SELL_PRICE, rs.getBigDecimal(SELL_PRICE));
          }
        }
        , templateRecord.get(SHOP_GOODS_ID)
        , JepOption.<Integer>getValue(templateRecord.get(SHOP_ID))
        , templateRecord.get(GOODS_ID)
        , maxRowCount 
        , operatorId);
  }
  public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
        + "pkg_jepriashowcase.deleteShopGoods(" 
            + "shopId => ? " 
          + ", operatorId => ? " 
        + ");"
      + "end;";
    super.delete(sqlQuery 
        , record.get(SHOP_ID) 
        , operatorId);
  }
 
  public void update(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
      +  "pkg_jepriashowcase.updateShopGoods(" 
            + "shopGoodsId => ? " 
            + ", goodsQuantity => ? " 
            + ", sellPrice => ? " 
          + ", operatorId => ? " 
      + ");"
     + "end;";
    super.update(sqlQuery 
        , record.get(SHOP_GOODS_ID)
        , record.get(GOODS_QUANTITY)
        , record.get(SELL_PRICE)
        , operatorId);
  }
 
  public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
        + "? := pkg_jepriashowcase.createShopGoods(" 
            + "shopId => ? " 
            + ", goodsId => ? " 
            + ", goodsQuantity => ? " 
            + ", sellPrice => ? " 
          + ", operatorId => ? " 
        + ");"
      + "end;";
    return super.create(sqlQuery, 
        Integer.class 
        , JepOption.<Integer>getValue(record.get(SHOP_ID))
        , record.get(GOODS_ID)
        , record.get(GOODS_QUANTITY)
        , record.get(SELL_PRICE)
        , operatorId);
  }
 
  public List<JepOption> getShop(String shopName, Integer rowCount) throws ApplicationException {
    String sqlQuery = 
      " begin " 
      + " ? := pkg_jepriashowcase.getShop("
        + "shopName => ?"
        + ", maxRowCount => ?"
      + ");" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(getInteger(rs, SHOP_ID));
            dto.setName(rs.getString(SHOP_NAME));
          }
        }, shopName, rowCount
    );
  }
  
  public List<JepOption> getGoods(String goodsName, Integer maxRowCount) throws ApplicationException {
    String sqlQuery = 
      " begin " 
      + " ? := pkg_jepriashowcase.getGoods("
        + "goodsName => ?"
        + ", maxRowCount => ?"
      + ");" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(getInteger(rs, GOODS_ID));
            dto.setName(rs.getString(GOODS_NAME));
          }
        }, goodsName, maxRowCount
    );
  }
}
