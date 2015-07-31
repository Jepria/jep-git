package com.technology.jep.jepria.client.widget.field.wysiwyg.toolbar.upload;

import com.google.gwt.dom.client.NativeEvent;

public class ProgressEvent extends NativeEvent {
    
	protected ProgressEvent() {}

    /**
     * This parameter specifies the total number of bytes already loaded.
     */
    public final native int getLoaded() /*-{
        return this.loaded;
    }-*/;

    /**
     * This specifies the total number of bytes to be loaded. If lengthComputable
     * is false, this must be zero.
     */
    public final native int getTotal() /*-{
        return this.total;
    }-*/;

    /**
     * If the user agent has reliable information about the value of total,
     * then this should be true. If the user agent does not have reliable
     * information about the vale of total, this should be false.
     */
    public final native boolean isLengthComputable() /*-{
        return this.lengthComputable;
    }-*/;

    /**
     * Returns a file's data in string format, or null, depending on the
     * read method that has been called on the FileReader object.
     */
    public final native String getResult() /*-{
        return this.target.result;
    }-*/;

    /**
     * The state of the FileReader, which can only be set to one of the following
     */
    public final native int getReadyState() /*-{
        return this.target.readyState;
    }-*/;

    /**
     * The error code associated with the file read error.
     * @return
     */
    public final native int getErrorCode() /*-{
        return this.target.error.code;
    }-*/;
}
