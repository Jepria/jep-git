package com.technology.${packageName?lower_case}.${moduleName?lower_case}.${form.formName?lower_case}.client.ui.toolbar.images;
 
import com.google.gwt.resources.client.ImageResource;
import com.technology.jep.jepria.client.images.JepImages;
 
public interface ${form.formName}Images extends JepImages {
  	<#list form.toolBarCustomButtons as button>
  	
	@Source("${button.image}.png")
	ImageResource ${button.image}();
 	</#list>
}
