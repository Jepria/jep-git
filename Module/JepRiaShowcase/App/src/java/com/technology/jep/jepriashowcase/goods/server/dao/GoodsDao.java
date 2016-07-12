package com.technology.jep.jepriashowcase.goods.server.dao;
 
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.DESCENDANT_GOODS_LINK;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST_FOR_CHECKED;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_ID_LIST_FOR_EXPAND;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_CATALOG_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_ID_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_LINK;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PHOTO;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PHOTO_EXTENSION;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PHOTO_MIME_TYPE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PORTFOLIO;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PORTFOLIO_EXTENSION;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_PORTFOLIO_MIME_TYPE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_SEGMENT_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_SEGMENT_CODE_LIST;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_SEGMENT_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_TYPE_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.GOODS_TYPE_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.HAS_CHILD;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.MOTIVATION_TYPE_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.MOTIVATION_TYPE_NAME;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.PURCHASING_PRICE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.SUPPLIER_ID;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.UNIT_CODE;
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.UNIT_NAME;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.technology.jep.jepria.server.dao.JepDaoStandard;
import com.technology.jep.jepria.server.dao.ResultSetMapper;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.field.option.JepParentOption;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.util.Mutable;

public class GoodsDao extends JepDaoStandard implements Goods {
 
  @SuppressWarnings("unchecked")
  public List<JepRecord> find(JepRecord templateRecord, Mutable<Boolean> autoRefreshFlag, Integer maxRowCount, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin  " 
        +  "? := pkg_jepriashowcase.findGoods(" 
            + "goodsIdList => ? " 
            + ", supplierId => ? " 
            + ", goodsName => ? " 
            + ", goodsTypeCode => ? " 
            + ", goodsSegmentCodeList => ? " 
            + ", goodsCatalogIdList => ? " 
          + ", maxRowCount => ? " 
          + ", operatorId => ? " 
        + ");"
     + " end;";
    
    String goodsIdList = templateRecord.get(GOODS_ID) != null ? templateRecord.get(GOODS_ID).toString() : (String) templateRecord.get(GOODS_ID_LIST);
    
    return super.find( sqlQuery,
        new ResultSetMapper<JepRecord>() {
          public void map(ResultSet rs, JepRecord record) throws SQLException {
            Integer goodsId = getInteger(rs, GOODS_ID);
            record.set(GOODS_ID, goodsId);
            record.set(SUPPLIER_ID, getInteger(rs, SUPPLIER_ID));
            record.set(GOODS_NAME, rs.getString(GOODS_NAME));
            
            JepOption option = getOption(rs, GOODS_TYPE_CODE, GOODS_TYPE_NAME);
            record.set(GOODS_TYPE_CODE, option);
            record.set(GOODS_TYPE_NAME, option.getName());
            
            option = getOption(rs, UNIT_CODE, UNIT_NAME);
            record.set(UNIT_CODE, option);
            record.set(UNIT_NAME, option.getName());
            
            option = getOption(rs, MOTIVATION_TYPE_CODE, MOTIVATION_TYPE_NAME);
            record.set(MOTIVATION_TYPE_CODE, option);
            record.set(MOTIVATION_TYPE_NAME, option.getName());
            
            record.set(PURCHASING_PRICE, rs.getBigDecimal(PURCHASING_PRICE));
            
            record.set(GOODS_PHOTO, getFileReference(rs, null, GOODS_ID, GOODS_PHOTO_EXTENSION, GOODS_PHOTO_MIME_TYPE));
            record.set(GOODS_PORTFOLIO, getFileReference(rs, null, GOODS_ID, GOODS_PORTFOLIO_EXTENSION, GOODS_PORTFOLIO_MIME_TYPE));
            
            List<JepOption> expandedNodes = new ArrayList<JepOption>();
            List<JepOption> checkedNodes = new ArrayList<JepOption>();
            try {
              for (TreeNode node : getExpandedNodesForGoodsCatalog(null, goodsId)){
                if (node.expanded){
                  expandedNodes.add(node.node);
                }
                if (node.checked){
                  checkedNodes.add(node.node);
                }
              }
            } catch (ApplicationException e) {
              e.printStackTrace();
            }
            record.set(GOODS_CATALOG_ID_LIST_FOR_EXPAND, expandedNodes);
            record.set(GOODS_CATALOG_ID_LIST_FOR_CHECKED, checkedNodes);
          }
        }
        , goodsIdList
        , templateRecord.get(SUPPLIER_ID)
        , templateRecord.get(GOODS_NAME)
        , JepOption.<String>getValue(templateRecord.get(GOODS_TYPE_CODE))
        , JepOption.getOptionValuesAsString((List<JepOption>) templateRecord.get(GOODS_SEGMENT_CODE_LIST))
        , JepOption.getOptionValuesAsString((List<JepOption>) templateRecord.get(GOODS_CATALOG_ID_LIST))
        , maxRowCount 
        , operatorId);
  }
  public void delete(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
        + "pkg_jepriashowcase.deleteGoods(" 
            + "goodsId => ? " 
          + ", operatorId => ? " 
        + ");"
      + "end;";
    super.delete(sqlQuery 
        , record.get(GOODS_ID) 
        , operatorId);
  }
 
  public void update(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
      +  "pkg_jepriashowcase.updateGoods(" 
            + "goodsId => ? " 
            + ", supplierId => ? " 
            + ", goodsName => ? " 
            + ", goodsTypeCode => ? " 
            + ", unitCode => ? " 
            + ", purchasingPrice => ? " 
            + ", motivationTypeCode => ? " 
            + ", goodsPhotoMimeType => ? " 
            + ", goodsPhotoExtension => ? " 
            + ", goodsPortfolioMimeType => ? " 
            + ", goodsPortfolioExtension => ? " 
          + ", operatorId => ? " 
      + ");"
     + "end;";
    super.update(sqlQuery 
        , record.get(GOODS_ID)
        , record.get(SUPPLIER_ID)
        , record.get(GOODS_NAME)
        , JepOption.<String>getValue(record.get(GOODS_TYPE_CODE))
        , JepOption.<String>getValue(record.get(UNIT_CODE))
        , record.get(PURCHASING_PRICE)
        , JepOption.<String>getValue(record.get(MOTIVATION_TYPE_CODE))
        , JepFileReference.getMimeType(record.get(GOODS_PHOTO))
        , JepFileReference.getFileExtension(record.get(GOODS_PHOTO))
        , JepFileReference.getMimeType(record.get(GOODS_PORTFOLIO))
        , JepFileReference.getFileExtension(record.get(GOODS_PORTFOLIO))
        , operatorId);
    
    setGoodsCatalogLink(record, operatorId);
  }
 
  public Integer create(JepRecord record, Integer operatorId) throws ApplicationException {
    String sqlQuery = 
      "begin " 
        + "? := pkg_jepriashowcase.createGoods(" 
            + "supplierId => ? " 
            + ", goodsName => ? " 
            + ", goodsTypeCode => ? " 
            + ", unitCode => ? " 
            + ", purchasingPrice => ? " 
            + ", motivationTypeCode => ? " 
            + ", goodsPhotoMimeType => ? " 
            + ", goodsPhotoExtension => ? " 
            + ", goodsPortfolioMimeType => ? " 
            + ", goodsPortfolioExtension => ? " 
          + ", operatorId => ? " 
        + ");"
      + "end;";
    Integer recordId = super.create(sqlQuery, 
        Integer.class 
        , record.get(SUPPLIER_ID)
        , record.get(GOODS_NAME)
        , JepOption.<String>getValue(record.get(GOODS_TYPE_CODE))
        , JepOption.<String>getValue(record.get(UNIT_CODE))
        , record.get(PURCHASING_PRICE)
        , JepOption.<String>getValue(record.get(MOTIVATION_TYPE_CODE))
        , JepFileReference.getMimeType(record.get(GOODS_PHOTO))
        , JepFileReference.getFileExtension(record.get(GOODS_PHOTO))
        , JepFileReference.getMimeType(record.get(GOODS_PORTFOLIO))
        , JepFileReference.getFileExtension(record.get(GOODS_PORTFOLIO))
        , operatorId);
    
    record.set(GOODS_ID, recordId);
    setGoodsCatalogLink(record, operatorId);
    return recordId;
  }
 
  public List<JepOption> getGoodsType() throws ApplicationException {
    String sqlQuery = 
      " begin " 
      + " ? := pkg_jepriashowcase.getGoodsType;" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(rs.getString(GOODS_TYPE_CODE));
            dto.setName(rs.getString(GOODS_TYPE_NAME));
          }
        }
    );
  }
  
  public List<JepOption> getUnit() throws ApplicationException {
    String sqlQuery = 
      " begin " 
      + " ? := pkg_jepriashowcase.getUnit;" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(rs.getString(UNIT_CODE));
            dto.setName(rs.getString(UNIT_NAME));
          }
        }
    );
  }
  
  public List<JepOption> getMotivationType() throws ApplicationException {
    String sqlQuery = 
      " begin " 
      + " ? := pkg_jepriashowcase.getMotivationType;" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(rs.getString(MOTIVATION_TYPE_CODE));
            dto.setName(rs.getString(MOTIVATION_TYPE_NAME));
          }
        }
    );
  }
  
  public List<JepOption> getGoodsCatalog(Integer parentGoodsCatalogId, final Integer goodsId) throws ApplicationException {
    String sqlQuery = 
        "begin ? := pkg_jepriashowcase.getgoodscatalog("
          + " parentgoodscatalogid => ?"
          + ", goodsId => ?"
          + ");"
        + " end;";
    
    List<JepOption> result = super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(getInteger(rs, GOODS_CATALOG_ID));
            dto.setName(rs.getString(GOODS_CATALOG_NAME));
            dto.set(HAS_CHILD, rs.getBoolean(HAS_CHILD));
            dto.set(GOODS_LINK, rs.getBoolean(GOODS_LINK));
            dto.set(DESCENDANT_GOODS_LINK, rs.getBoolean(DESCENDANT_GOODS_LINK));
          }
        }
        , parentGoodsCatalogId, goodsId);
    
    List<JepOption> res = new ArrayList<JepOption>(result.size());
    
    for (JepOption option : result){
      if (Boolean.TRUE.equals(option.get(HAS_CHILD))){
        Boolean goodsLink = option.get(GOODS_LINK),
            descenadantGoodsLink = option.get(DESCENDANT_GOODS_LINK);
        option = new JepParentOption(option.getName(), JepOption.<Integer>getValue(option));
        option.set(GOODS_LINK, goodsLink);
        option.set(DESCENDANT_GOODS_LINK, descenadantGoodsLink);
      }
      res.add(option);
    }
    return res;
  }
  
  private List<TreeNode> getExpandedNodesForGoodsCatalog(Integer parentGoodsCatalogId, Integer goodsId) throws ApplicationException {
    List<TreeNode> result = new ArrayList<TreeNode>();
    List<JepOption> goodsCatalog = getGoodsCatalog(parentGoodsCatalogId, goodsId);
    for (JepOption opt : goodsCatalog){
      TreeNode node = new TreeNode();
      node.node = opt;
      node.expanded = Boolean.TRUE.equals(opt.get(DESCENDANT_GOODS_LINK));
      node.checked = Boolean.TRUE.equals(opt.get(GOODS_LINK));
      if (node.expanded || node.checked){
        result.add(node);
        result.addAll(getExpandedNodesForGoodsCatalog((Integer) opt.getValue(), goodsId));
      }
    }
    return result;
  }
  
  public List<JepOption> getGoodsSegment() throws ApplicationException {
    String sqlQuery = 
      " begin " 
      + " ? := pkg_jepriashowcase.getGoodsSegment;" 
      + " end;";
 
    return super.getOptions(
        sqlQuery,
        new ResultSetMapper<JepOption>() {
          public void map(ResultSet rs, JepOption dto) throws SQLException {
            dto.setValue(rs.getString(GOODS_SEGMENT_CODE));
            dto.setName(rs.getString(GOODS_SEGMENT_NAME));
          }
        }
    );
  }
  
  private void setGoodsCatalogLink(
      JepRecord record
      , Integer operatorId) throws ApplicationException {
    String sqlQuery = 
        "begin " 
        +  "pkg_jepriashowcase.setGoodsCatalogLink(" 
              + "goodsId => ? " 
              + ", goodsCatalogIdList => ? " 
            + ", operatorId => ? " 
        + ");"
       + "end;";
      super.delete(sqlQuery 
          , record.get(GOODS_ID)
          , JepOption.getOptionValuesAsString((List<JepOption>) record.get(GOODS_CATALOG_ID_LIST))
          , operatorId);
  }
  
  class TreeNode {
    JepOption node;
    boolean checked;
    boolean expanded;
  }
}
