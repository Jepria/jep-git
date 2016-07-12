package com.technology.jep.jepria.client.widget.field.wysiwyg.toolbar;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

public class ColorPicker extends Composite implements MouseMoveHandler, MouseDownHandler, HasSelectionHandlers<String> {

    public static final String[] hexValues = {"00", "33", "66", "99", "CC", "FF"};
    public static final String BLACK = "#000000";
    
    private static final int COLS = 18;
    // Block height, width, padding
    private static final int BH = 10;
    private static final int BW = 10;
    private static final int BP = 1;
    private Canvas canvas = Canvas.createIfSupported();
    private String selectedColor;
    private int width;
    private Context2d ctx;

    public ColorPicker() {
    this(BLACK);
    }
    
    public ColorPicker(String defaultCol){
    this(defaultCol, 180, 160);
    }
    
    public ColorPicker(String defaultCol, int width, int height){
    this.width = width;
      
    canvas.setPixelSize(width, height);
    canvas.setCoordinateSpaceWidth(width);
    canvas.setCoordinateSpaceHeight(height);
    
    ctx = canvas.getContext2d();
    drawPalette();
    addHandlers();
    showSelected(defaultCol);
    
    initWidget(canvas);
    }


    public String getColor() {
      return selectedColor;
      
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
      int x = event.getX();
      int y = event.getY() - 40;
      if (y < 0 || x >= width) return;
      showSelected(getHexString(getColorIndex(x, y)));
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
      int x = event.getX();
      int y = event.getY() - 40;
      if (y < 0 || x >= width) return;
      setSelected(getColorIndex(x, y));
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
      return super.addHandler(handler, SelectionEvent.getType());
    }

    private void addHandlers() {
      canvas.addMouseMoveHandler(this);
      canvas.addMouseDownHandler(this);
    }

    private void drawPalette() {
      int i=0;
      for (int r=0; r<256; r+=51) {
        for (int g=0; g<256; g+=51) {
          for (int b=0; b<256; b+=51) {
            CssColor color = CssColor.make(r, g, b);
            ctx.setFillStyle(color);
            int x = BW * (i%COLS);
            int y = BH * (i/COLS) + 40;
            ctx.fillRect(x+BP, y+BP, BW-2*BP, BH-2*BP);
            i++;
          }
        }
      }
    }

    private void fireSelected() {
        SelectionEvent.fire(this, selectedColor);
    }

    private int getColorIndex(int x, int y) {
      // Get color index 0-215 using row, col
      int i = y / BH * COLS + x / BW;
      return i;
    }

    private void showSelected(String colorHex) {
      ctx.setFillStyle(colorHex);
      ctx.fillRect(0, 0, width, 22);
      
      // clear area of the text
      ctx.clearRect(0, 22, width, 15);  
      // show hex color
      ctx.setFillStyle(BLACK);
      ctx.setTextAlign(TextAlign.CENTER);
      ctx.fillText(colorHex, width/2, 32);
    }
    
    public void setSelected(int i) {
      selectedColor = getHexString(i);
      showSelected(selectedColor);
      fireSelected();
    }
    
    public String getHexString(int i) {
      int r = i / 36;
      int g = (i % 36) / 6;
      int b = i % 6;
      return "#" + hexValues[r] + hexValues[g] + hexValues[b];
    }
  }
