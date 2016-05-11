package com.technology.jep.jepriashowcase.auto;

import com.technology.jep.jepria.auto.entrance.EntranceAppAuto;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepriashowcase.custom.auto.CustomAuto;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAuto;
import com.technology.jep.jepriashowcase.requestfeature.auto.RequestFeatureAuto;

public interface JepRiaShowcaseAuto extends JepRiaAuto, EntranceAppAuto {
	CustomAuto getCustomAuto();
	
	GoodsAuto getGoodsAuto(boolean newInstance);
	RequestFeatureAuto getRequestFeatureAuto(boolean newInstance);
}
