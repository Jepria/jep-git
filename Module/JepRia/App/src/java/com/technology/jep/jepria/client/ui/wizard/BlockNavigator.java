package com.technology.jep.jepria.client.ui.wizard;

import java.util.List;

public interface BlockNavigator {

  void setBlocks(List<Block> blocks);  
  
  Block nextBlock();
  Block previousBlock();
  Block previousCentralBlock();
  
  Block getStartBlock();
  Block getCurrentBlock();
  
  boolean isLastBlock();
  boolean isFirstBlock();
  
}
