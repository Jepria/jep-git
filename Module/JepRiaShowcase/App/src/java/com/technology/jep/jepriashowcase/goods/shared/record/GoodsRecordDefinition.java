package com.technology.jep.jepriashowcase.goods.shared.record;
 
import static com.technology.jep.jepriashowcase.goods.shared.field.GoodsFieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.lob.JepLobRecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class GoodsRecordDefinition extends JepLobRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static GoodsRecordDefinition instance = new GoodsRecordDefinition();
 
  private GoodsRecordDefinition() {
    super(buildTypeMap()
      , new String[]{GOODS_ID}
      , "jrs_goods"
      , buildFieldMap());
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    typeMap.put(GOODS_ID, INTEGER);
    typeMap.put(GOODS_ID_LIST, STRING);
    typeMap.put(SUPPLIER_ID, INTEGER);
    typeMap.put(GOODS_NAME, STRING);
    typeMap.put(GOODS_TYPE_CODE, STRING);
    typeMap.put(GOODS_TYPE_NAME, STRING);
    typeMap.put(GOODS_SEGMENT_CODE_LIST, STRING);
    typeMap.put(GOODS_CATALOG_ID_LIST, STRING);
    typeMap.put(UNIT_CODE, STRING);
    typeMap.put(UNIT_NAME, STRING);
    typeMap.put(PURCHASING_PRICE, BIGDECIMAL);
    typeMap.put(MOTIVATION_TYPE_CODE, STRING);
    typeMap.put(MOTIVATION_TYPE_NAME, STRING);
    typeMap.put(GOODS_PHOTO, BINARY_FILE);
    typeMap.put(GOODS_PHOTO_MIME_TYPE, STRING);
    typeMap.put(GOODS_PHOTO_EXTENSION, STRING);
    typeMap.put(GOODS_PORTFOLIO, BINARY_FILE);
    typeMap.put(GOODS_PORTFOLIO_MIME_TYPE, STRING);
    typeMap.put(GOODS_PORTFOLIO_EXTENSION, STRING);
    typeMap.put(EXTENSION, STRING);
    typeMap.put(MIME_TYPE, STRING);
    return typeMap;
  }
 
  private static Map<String, String> buildFieldMap() {
    Map<String, String> fieldMap = new HashMap<String, String>();
    fieldMap.put(GOODS_ID, GOODS_ID);
    fieldMap.put(GOODS_PHOTO, GOODS_PHOTO);
    fieldMap.put(GOODS_PORTFOLIO, GOODS_PORTFOLIO);
    return fieldMap;
  }
 
}
