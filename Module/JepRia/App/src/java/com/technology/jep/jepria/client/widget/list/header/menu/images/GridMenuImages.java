package com.technology.jep.jepria.client.widget.list.header.menu.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface GridMenuImages extends ClientBundle {

  public static final GridMenuImages instance = GWT.create(GridMenuImages.class);
  
  @Source("up.gif")
  ImageResource upButton();
  
  @Source("down.gif")
  ImageResource downButton();
  
  @Source("setting.png")
  ImageResource setting();
}
