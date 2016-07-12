package com.technology.jep.jepria.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum.CENTER;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.gwt.user.client.ui.FlexTable;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class BlockContainerImpl extends FlexTable implements BlockContainer {
  
  private BlockManager blocks;
  private BlockNavigator controller;

  // references on current column and row
  private int colCount = 0, rowCount = 0;
  
  public BlockContainerImpl(BlockManager manager){
    this.blocks = manager;
    this.controller = this.blocks.getController();
    
    Block startBlock = controller.getStartBlock();
    assert startBlock.getPosition() == CENTER : "The initial block should have central position";
    
    // initialize central block as first step of wizard
    setCentralBlock(startBlock);
  }
  
  @Override
  public void addBlock(Block block) {
    switch(block.getPosition()){
      case DOWN : 
        addDownBlock(block);
        break;
      case RIGHT : 
        addRightBlock(block);
        break;
      case CENTER :
        setCentralBlock(block);
        break;
    }
  }
  
  @Override
  public void goToNextBlock(){
    Block next = controller.nextBlock();
    if (!JepRiaUtil.isEmpty(next)){
      addBlock(next);
    }
  }
  
  @Override
  public void goToPreviousBlock(){
    Block currentBlock = controller.getCurrentBlock();
    // find position for a current block
    switch(currentBlock.getPosition()){
      case DOWN : removeDownBlock(currentBlock); break;
      case RIGHT : removeRightBlock(currentBlock); break;
      case CENTER : {
        setCentralBlock(controller.previousCentralBlock());  
        break;
      }
    }
    // go to previous block
    controller.previousBlock();
  }
  
  private void setCentralBlock(Block block){
    setWidget(0, 0, block);
  }
  
  private void addDownBlock(Block block){
    setWidget(++rowCount, 0, block);
  }
  
  private void removeDownBlock(Block block){
    if (remove(block)) rowCount--;
  }
  
  private void addRightBlock(Block block){
    setWidget(0, ++colCount, block);
  }
  
  private void removeRightBlock(Block block){
    if (remove(block)) colCount--;
  }

  @Override
  public void setBlockManager(BlockManager blockManager) {
    this.blocks = blockManager;
  }

  @Override
  public BlockManager getBlockManager() {
    return blocks;
  }
}
