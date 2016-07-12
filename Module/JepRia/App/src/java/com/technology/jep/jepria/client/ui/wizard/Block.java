package com.technology.jep.jepria.client.ui.wizard;

import static com.technology.jep.jepria.client.ui.wizard.BlockPositionEnum.CENTER;

import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.widget.container.FieldSet;

public class Block extends FieldSet {

  private BlockPositionEnum position;
  
  public Block(Widget centralWidget){
    this(centralWidget, CENTER);
  }
  
  public Block(Widget widget, BlockPositionEnum blockPosition){
    this.position = blockPosition;
    setContentWidget(widget);
  }
  
  public BlockPositionEnum getPosition(){
    return position;
  }
  
  public boolean isCentral(){
    return CENTER.equals(position);
  }
}
