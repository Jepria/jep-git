package com.technology.jep.jepria.client.ui.wizard;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.technology.jep.jepria.shared.util.JepRiaUtil;

public class BlockNavigatorImpl implements BlockNavigator {

  private Block startBlock, currentBlock;
  private List<Block> blocks;
  
  public BlockNavigatorImpl(List<Block> list){
    setBlocks(list);
  }
  
  public void setBlocks(List<Block> list){
    LinkedList<Block> blocks = new LinkedList<Block>(list);
    this.startBlock = blocks.getFirst();
    this.currentBlock = startBlock;
    this.blocks = blocks;
  }
  
  public void setBlocks(Block... blocks){
    setBlocks(Arrays.asList(blocks));
  }
  
  @Override
  public boolean isLastBlock(){
    return blocks.indexOf(currentBlock) == blocks.size() - 1;
  }  
  
  @Override
  public boolean isFirstBlock(){
    return blocks.indexOf(currentBlock) == 0;
  }
  
  @Override
  public Block nextBlock(){
    boolean canMoveOn = !isLastBlock();
    if (canMoveOn){
      // fetch the next block
      return this.currentBlock = blocks.get(getCurrentIndex() + 1);
    }
    return null;
  }
  
  @Override
  public Block previousBlock(){
    boolean canMoveOn = !isFirstBlock(); 
    if (canMoveOn){
      // fetch the previous block
      return this.currentBlock = blocks.get(getCurrentIndex() - 1);
    }
    return null;
  }
  
  @Override
  public Block previousCentralBlock(){
    
    Block previous = previousBlock();
    if (JepRiaUtil.isEmpty(previous)){
      return startBlock;
    }
    
    int backSteps = 1;
    while (!JepRiaUtil.isEmpty(previous) && !previous.isCentral()){
      previous = previousBlock();
      if (JepRiaUtil.isEmpty(previous)){
        previous = startBlock;
      }
      backSteps++;
    }
    for (int i = 0; i < backSteps; i++){
      nextBlock();
    }
    
    return previous;
  }
    
  @Override
  public Block getStartBlock(){
    return startBlock;
  }
  
  @Override
  public Block getCurrentBlock(){
    return currentBlock;
  }

  private int getCurrentIndex(){
    return blocks.indexOf(currentBlock);
  }
}
