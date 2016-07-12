package com.technology.jep.jepriashowcase.allshopgoods.shared.record;
 
import static com.technology.jep.jepriashowcase.allshopgoods.shared.field.AllShopGoodsFieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class AllShopGoodsRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static AllShopGoodsRecordDefinition instance = new AllShopGoodsRecordDefinition();
 
  private AllShopGoodsRecordDefinition() {
    super(buildTypeMap()
      , new String[]{ALL_SHOP_GOODS_ID, SHOP_ID, GOODS_ID}
    );
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
    typeMap.put(ALL_SHOP_GOODS_ID, INTEGER);
    typeMap.put(SHOP_GOODS_ID, INTEGER);
    typeMap.put(SHOP_ID, INTEGER);
    typeMap.put(SHOP_NAME, STRING);
    typeMap.put(GOODS_ID, INTEGER);
    typeMap.put(GOODS_NAME, STRING);
    typeMap.put(GOODS_QUANTITY, INTEGER);
    typeMap.put(SELL_PRICE, BIGDECIMAL);
    return typeMap;
  }
}
