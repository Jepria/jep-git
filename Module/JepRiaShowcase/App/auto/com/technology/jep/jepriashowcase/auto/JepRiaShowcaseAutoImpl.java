package com.technology.jep.jepriashowcase.auto;

import com.technology.jep.jepria.auto.entrance.ApplicationEntranceAppAuto;
import com.technology.jep.jepriashowcase.custom.auto.CustomAuto;
import com.technology.jep.jepriashowcase.custom.auto.CustomAutoImpl;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAuto;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAutoImpl;
import com.technology.jep.jepriashowcase.requestfeature.auto.RequestFeatureAuto;
import com.technology.jep.jepriashowcase.requestfeature.auto.RequestFeatureAutoImpl;

public class JepRiaShowcaseAutoImpl extends ApplicationEntranceAppAuto implements JepRiaShowcaseAuto {
	private CustomAuto customAuto;
	private GoodsAuto goodsAuto;
	private RequestFeatureAuto requestFeatureAuto;

	public JepRiaShowcaseAutoImpl(String baseUrl,
			String browserName,
			String browserVersion,
			String browserPlatform,
			String browserPath,
			String jepriaVersion,
			String username,
			String password) {
		super(baseUrl, browserName, browserVersion, browserPlatform, browserPath, jepriaVersion, username, password);
		
		customAuto = new CustomAutoImpl<JepRiaShowcaseAuto>(this, new JepRiaShowcasePageManager());
		goodsAuto = getGoodsAuto(true);
		requestFeatureAuto = getRequestFeatureAuto(true);
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
	public RequestFeatureAuto getRequestFeatureAuto(boolean newInstance) {
		if(requestFeatureAuto == null || newInstance) {
			requestFeatureAuto = new RequestFeatureAutoImpl<JepRiaShowcaseAuto, JepRiaShowcasePageManager>(this, new JepRiaShowcasePageManager());
		}
		return requestFeatureAuto;
	}
}
