package com.technology.jep.jepria.client.widget.field.tree;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import com.google.gwt.user.cellview.client.CellTree.CellTreeMessages;

public class TreeFieldMessages implements CellTreeMessages {
    public String showMore() {
        return JepTexts.tree_showMore();
    }
 
    public String emptyTree() {
        return JepTexts.tree_empty();
    }
    
}