package com.technology.jep.jepriashowcase.auto;

import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAppAuto;
import com.technology.jep.jepriashowcase.arsenic.auto.ArsenicAuto;
import com.technology.jep.jepriashowcase.arsenic.auto.ArsenicAutoImpl;
import com.technology.jep.jepriashowcase.custom.auto.CustomAuto;
import com.technology.jep.jepriashowcase.custom.auto.CustomAutoImpl;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAuto;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAutoImpl;
import com.technology.jep.jepriashowcase.feature.auto.FeatureAuto;
import com.technology.jep.jepriashowcase.feature.auto.FeatureAutoImpl;

public class JepRiaShowcaseAutoImpl extends ApplicationEntranceAppAuto implements JepRiaShowcaseAuto {
  private CustomAuto customAuto;
  private GoodsAuto goodsAuto;
  private ArsenicAuto arsenicAuto;
  private FeatureAuto featureAuto;

  public JepRiaShowcaseAutoImpl(String baseUrl,
      String browserName,
      String browserVersion,
      String browserPlatform,
      String browserPath,
      String driverPath,
      String jepriaVersion,
      String username,
      String password) {
    super(baseUrl, browserName, browserVersion, browserPlatform, browserPath, driverPath, jepriaVersion, username, password);
    
    customAuto = new CustomAutoImpl<JepRiaShowcaseAuto>(this, new JepRiaShowcasePageManager());
    goodsAuto = getGoodsAuto(true);
    arsenicAuto = getArsenicAuto(true);
    featureAuto = getFeatureAuto(true);
  }
  
  @Override
  public void start(String baseUrl) {
    super.start(baseUrl);
    this.getCustomAuto().openMainPage(baseUrl); // TODO MainPage ?
  }


  @Override
  public CustomAuto getCustomAuto() {
    return customAuto;
  }

  @Override
  public GoodsAuto getGoodsAuto(boolean newInstance) {
    if(goodsAuto == null || newInstance) {
      goodsAuto = new GoodsAutoImpl<JepRiaShowcaseAuto, JepRiaShowcasePageManager>(this, new JepRiaShowcasePageManager());
    }
    return goodsAuto;
  }
  
  @Override
  public ArsenicAuto getArsenicAuto(boolean newInstance) {
    if(arsenicAuto == null || newInstance) {
      arsenicAuto = new ArsenicAutoImpl<JepRiaShowcaseAuto, JepRiaShowcasePageManager>(this, new JepRiaShowcasePageManager());
    }
    return arsenicAuto;
  }

  @Override
  public FeatureAuto getFeatureAuto(boolean newInstance) {
    if(featureAuto == null || newInstance) {
      featureAuto = new FeatureAutoImpl<JepRiaShowcaseAuto, JepRiaShowcasePageManager>(this, new JepRiaShowcasePageManager());
    }
    return featureAuto;
  }
}
