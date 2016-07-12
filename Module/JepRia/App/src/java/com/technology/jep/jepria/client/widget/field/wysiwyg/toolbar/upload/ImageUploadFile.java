package com.technology.jep.jepria.client.widget.field.wysiwyg.toolbar.upload;

import com.google.gwt.dom.client.InputElement;

public class ImageUploadFile extends InputElement {
  
  public static final int MAX_IMAGE_SIZE = 500; // in Kbs
    public static final String[] AVAILABLE_IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"}; 
    
    protected ImageUploadFile() {}

    public final native String getBase64data() /*-{
        return this.base64data;
    }-*/;
    
    public final String getFileExtension() {
        final String name = getName();
        String[] tokens = name.split("\\.");
        if (tokens != null) {
            return tokens[tokens.length - 1];
        } else {
            return name;
        }
    }
    
    public final boolean hasImageExtension() {
        final String fileExtension = getFileExtension();
        for (String extension : AVAILABLE_IMAGE_EXTENSION) {
            if (fileExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
    
    public final boolean isValid(){
      return hasImageExtension() && (getSize() <= MAX_IMAGE_SIZE * 1024);
    }
}
