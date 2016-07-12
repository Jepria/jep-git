package com.technology.jep.jepria.client.ui.wizard;

import java.util.List;

import com.technology.jep.jepria.client.ui.WorkstateEnum;

public interface BlockManager {

  BlockNavigator getController();
  
  void changeWorkstate(WorkstateEnum workstate);
  
  List<BlockClientFactory<?>> getBlockClientFactories();  
  
}
