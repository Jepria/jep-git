package com.technology.jep.jepria.client.ui.wizard;

import com.google.gwt.user.client.ui.IsWidget;

public interface BlockContainer extends IsWidget {

  void addBlock(Block block);
  
  void goToNextBlock();
  void goToPreviousBlock();
  
  void setBlockManager(BlockManager blockManager);
  BlockManager getBlockManager();
}
