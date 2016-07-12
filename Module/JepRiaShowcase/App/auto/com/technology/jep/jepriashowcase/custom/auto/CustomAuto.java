package com.technology.jep.jepriashowcase.custom.auto;

import com.technology.jep.jepria.auto.entrance.EntranceAuto;
import com.technology.jep.jepriashowcase.goods.auto.GoodsAuto;

public interface CustomAuto extends EntranceAuto {

  /**
   * Открытие и получение примера с полноэкранными модулями
   */
  GoodsAuto openFullScreenModules();
}
