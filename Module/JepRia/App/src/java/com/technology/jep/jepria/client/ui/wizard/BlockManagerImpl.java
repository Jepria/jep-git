package com.technology.jep.jepria.client.ui.wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.technology.jep.jepria.client.history.place.JepWorkstatePlace;
import com.technology.jep.jepria.client.ui.WorkstateEnum;

public class BlockManagerImpl implements BlockManager {

  private BlockNavigator controller;
  private List<BlockClientFactory<?>> blockClientFactories;
  
  /**
   * Текущее состояние.
   */
  protected WorkstateEnum _workstate = null;
  
  public BlockManagerImpl(){}
  
  public BlockManagerImpl(BlockClientFactory<?>... blockClientFactories){
    this.blockClientFactories = Arrays.asList(blockClientFactories);
    this.controller = new BlockNavigatorImpl(asList(blockClientFactories));
  }

  private List<Block> asList(BlockClientFactory<?>... blockClientFactories) {
    List<Block> blocks = new ArrayList<Block>(blockClientFactories.length);
    for (BlockClientFactory<?> factory: blockClientFactories){
      blocks.add((Block) factory.getView().asWidget());
    }
    return blocks;
  }
  
  @Override
  public List<BlockClientFactory<?>> getBlockClientFactories() {
    return blockClientFactories;
  }
  
  @Override
  public void changeWorkstate(WorkstateEnum workstate) {
    // Только в случае, если действительно изменяется состояние.
    if(workstate != null && !workstate.equals(_workstate)) {
      onChangeWorkstate(workstate);
      _workstate = workstate;
    }
  }
  
  /**
   * Обработчик нового состояния
   * 
   * @param newWorkstate новое состояние
   */
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    for(BlockClientFactory<?> factory: getBlockClientFactories()){
      factory.getPlaceController().goTo(new JepWorkstatePlace(newWorkstate));
    }
  }

  @Override
  public BlockNavigator getController() {
    return controller;
  }
}