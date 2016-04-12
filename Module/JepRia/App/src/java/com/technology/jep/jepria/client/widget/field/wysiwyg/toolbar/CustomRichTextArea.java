package com.technology.jep.jepria.client.widget.field.wysiwyg.toolbar;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * Кастомизированный класс для переопределения стандартного {@link com.google.gwt.user.client.ui.RichTextArea}
 * 
 * Позволяет обрабатывать единообразно переносы строк во всех типах браузеров, а также решается проблема указания произвольного 
 * размера шрифта текста. 
 */
public class CustomRichTextArea extends RichTextArea {
	
	public CustomRichTextArea() {
		super();
		
		addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				 if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					 // Для разных типов браузеров нажатие на Enter приводил к различному форматированию
					 // в IE - добавляется тэг параграфа <p>, отделяющего одну строку от другой
					 // в FF - добавляется новый <div>, который переводит форматирование на новую строку
					 // В общем случае хотелось бы, чтобы это был перевод строки с помощью тэга <br>
					 Element el = appendNewLine();
					 if (el != null){
						 getFocusImpl().focus(el);
						 event.preventDefault();
					 }
				 }
			}
		});
	}
	
    /**
     * Добавление новой строки с возвращением элемента, переводящего курсор на новую строку 
     * 
     * @return	элемент разметки, соответствующий новой строке
     */
    public native Element appendNewLine() /*-{
	    var elem = this.@com.google.gwt.user.client.ui.UIObject::getElement()();
	    var doc = elem.contentDocument; 
	    var window = elem.contentWindow;
	    if (window && window.getSelection) {
	      var selection = window.getSelection();
	      var range = selection.getRangeAt(0);
	      var br = doc.createElement("br");
	      var zwsp = doc.createTextNode("\u200B");
	      var textNodeParent = doc.getSelection().anchorNode.parentNode;
	      var inSpan = textNodeParent.nodeName == "SPAN";
	      var span = doc.createElement("span");
	      
	      // if the carat is inside a <span>, move it out of the <span> tag
	      if (inSpan) {
	        range.setStartAfter(textNodeParent);
	        range.setEndAfter(textNodeParent);
	      }
	
	      // insert the <br>
	      range.deleteContents();
	      range.insertNode(br);
	      range.setStartAfter(br);
	      range.setEndAfter(br);
	      
	      br.scrollIntoView();
	      
	      // create a new span on the next line
	      if (inSpan) {
	        range.insertNode(span);
	        range.setStart(span, 0);
	        range.setEnd(span, 0);
	      }
	
	      // add a zero-width character
	      range.insertNode(zwsp);
	      range.setStartBefore(zwsp);
	      range.setEndBefore(zwsp);
	      
	      // insert the new range
	      selection.removeAllRanges();
	      selection.addRange(range);
	      
	      return span;	    	
	    }
	    return null;
	}-*/;
    
    /**
     * Метод для изменения размера текста в текстовой области
     * 
     * @param oldSize		размер шрифта для изменения 
     * @param newSize		новый размер шрифта в текстовой области
     */
    public native void changeSize(String oldSize, int newSize)/*-{
    	var elem = this.@com.google.gwt.user.client.ui.UIObject::getElement()();
	    var doc = elem.contentDocument; 
	    var fontElements = doc.getElementsByTagName("font");
	    for (var i = 0, len = fontElements.length; i < len; ++i) {
	        if (fontElements[i].size == oldSize) {
	            fontElements[i].removeAttribute("size");
	            fontElements[i].style.fontSize = newSize + "px";
	        }
	    }
    }-*/;
}
