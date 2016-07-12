package com.technology.jep.jepriashowcase.main.client.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ApplicationResource extends ClientBundle {

  public static final ApplicationResource instance = GWT.create(ApplicationResource.class);
  
  @Source(Style.DEFAULT_CSS)
  Style resource();

  public interface Style extends CssResource {
    /**
     * The path to the default CSS styles used by this resource.
     */
    String DEFAULT_CSS = "style.css";

    /**
     * Applied to every cell.
     */
    @ClassName("red")
    String red();
  }
}
