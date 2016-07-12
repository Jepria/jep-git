package com.technology.jep.jepria.client.widget.field.tree.images;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.CellTree;

public interface TreeFieldResources extends CellTree.Resources {
  
  @Source("checked.gif")
  ImageResource checked();
  
  @Source("partialChecked.gif")
  ImageResource partialChecked();
  
  @Source("unchecked.gif")
  ImageResource unchecked();
  
  @ImageOptions(flipRtl = true)
  @Source("treeCollapsed.png")
  ImageResource cellTreeClosedItem();
  
  @ImageOptions(flipRtl = true)
  @Source("treeExpanded.png")
  ImageResource cellTreeOpenItem();
  
  @Source("folderClosed.png")
  ImageResource folderClosed();
  
  @Source("folderOpened.png")
  ImageResource folderOpened();
  
  @ImageOptions(flipRtl = true)
    @Source("cellTreeLoading.gif")
    ImageResource cellTreeLoading();
  
  @Source("TreeField.css")
    public CellTree.Style cellTreeStyle();
  
}
