package com.technology.jep.jepriashowcase.shopgoods.shared.record;
 
import static com.technology.jep.jepriashowcase.shopgoods.shared.field.ShopGoodsFieldNames.*;
import static com.technology.jep.jepria.shared.field.JepTypeEnum.*;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
 
import java.util.HashMap;
import java.util.Map;
 
public class ShopGoodsRecordDefinition extends JepRecordDefinition {
 
  private static final long serialVersionUID = 1L;
 
  public static ShopGoodsRecordDefinition instance = new ShopGoodsRecordDefinition();
 
  private ShopGoodsRecordDefinition() {
    super(buildTypeMap()
      , new String[]{SHOP_GOODS_ID}
    );
  }
 
  private static Map<String, JepTypeEnum> buildTypeMap() {
    Map<String, JepTypeEnum> typeMap = new HashMap<String, JepTypeEnum>();
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
