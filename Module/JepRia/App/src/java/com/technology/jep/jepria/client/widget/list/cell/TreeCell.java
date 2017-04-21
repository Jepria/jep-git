package com.technology.jep.jepria.client.widget.list.cell;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Image;
import com.technology.jep.jepria.client.widget.list.HasTreeGridManager;
import com.technology.jep.jepria.client.widget.list.TreeGridManager;
import com.technology.jep.jepria.shared.field.TreeCellNames;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Реализация ячейки списка для работы с древовидной структурой на списочной форме
 * @param <C> Тип, который отображает ячейка. Должен поддерживать функцию toString
 */
public class TreeCell<C> extends AbstractCell<C> implements HasTreeGridManager {

  /**
   * Количество разделителей равное ширине иконке (+/-) и одному уровню вложенности
   */
  private int depthStep = 4;
  
  /**
   * Разделитель
   */
  private String treeDelimeter = "&nbsp;";
  
  /**
   * Ссылка на объект, управляющий списком
   */
  protected TreeGridManager<?, ?, ?> treeGridManager;
  
  /**
   * Устанавливает ссылку на объект, управляющий списком
   * @param treeGridManager объект, управляющий списком
   */
  public void setTreeGridManager(TreeGridManager<?, ?, ?> treeGridManager){
    this.treeGridManager = treeGridManager;
  }
  
  /**
   * Ресурс иконки узла, у которого отсутствуют дочерние элементы
   */
  private final ImageResource treeNoChildrenIcon = JepImages.treeNoChildren();
  
  /**
   * Ресурс иконки плюса
   */
  private final ImageResource plusIcon = JepImages.plus();
  
  /**
   * Ресурс иконки минуса
   */
  private final ImageResource minusIcon = JepImages.minus();
  
  /**
   * Ресурс иконки раскрытого узла дерева
   */
  private final ImageResource folderCollapsedIcon = JepImages.folderCollapsed();
  
  
  /**
   * Конструктор, в котором определяются события, на которые будет реагировать ячейка
   */
  public TreeCell() {
    super(CLICK, KEYDOWN);
  }
  
  /**
   * Рендерит ячейку таблицы
   * @param context контекст ячейки 
   *     (информация о местоположении ячейки на списке, 
   *       значение JepRecord для строки, в которой находится ячейчка) 
   * @param value значение ячейки
   * @param sb builder, в который помещается конечное содержимое ячейки 
   */
  @Override
  public void render(Context context,
      C value, SafeHtmlBuilder sb) {

    //Если нет контекста, то не получится корректно отобразить ячейку
    if(context == null){
      return;
    }
    
    JepRecord record = (JepRecord) context.getKey();
    ListTreeNode node = treeGridManager.findNode(record);
    
    
    int depth = 1;
    boolean isOpen = false;
    
    if(node != null){
      
      depth = node.getDepth();
      isOpen = node.isOpen();
    }
    
    
    //Если нет детей, то также сдвигаем на размер иконки (+/-)
    if(Boolean.FALSE.equals(record.get(TreeCellNames.HAS_CHILDREN))){
      depth++;
    }
    
    for(depth *= depthStep; depth > depthStep; depth--){
      
      sb.append(SafeHtmlUtils.fromTrustedString(treeDelimeter));
    }
    
    ImageResource nodeIconResource = treeNoChildrenIcon;
    
    //Если есть поддерево, ставим иконку управления деревом
    if(Boolean.TRUE.equals(record.get(TreeCellNames.HAS_CHILDREN))){
      
      ImageResource imageResource = plusIcon;
      if(isOpen){
        imageResource = minusIcon;
      }
      
      final Image icon = new Image(imageResource); 
      icon.setWidth("10px");
      icon.setHeight("10px");
      icon.getElement().getStyle().setCursor(Cursor.POINTER);
      
      sb.append(new SafeHtml() {
        
        @Override
        public String asString() {
          return icon.toString() + treeDelimeter;
        }
      });
      
      nodeIconResource = folderCollapsedIcon;
    }

    final Image nodeIcon = new Image(nodeIconResource);
    
    sb.append(new SafeHtml() {
      
      @Override
      public String asString() {
        return nodeIcon.toString() + treeDelimeter + treeDelimeter;
      }
    });
    
      if (value != null) {
        sb.append(SafeHtmlUtils.fromSafeConstant(value.toString()));
      }  
  }
  
  /**
   * Обработка событий ячейки
   * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
   */
  @Override
  public void onBrowserEvent(Context context,
      Element parent, C value, NativeEvent event,
      ValueUpdater<C> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    
      if (CLICK.equals(event.getType())) {
        
        EventTarget eventTarget = event.getEventTarget();
          if (!Element.is(eventTarget)) {
            return;
          }
          
          //Проверяем, что клик был по иконке
          if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
              
            treeGridManager.toggleChildren(context);
          }
      }
  }
  
  /**
   * Обработка события типа enterKeyDown
   */
  @Override
  protected void onEnterKeyDown(Context context, Element parent, C value,
    NativeEvent event, ValueUpdater<C> valueUpdater) {

    treeGridManager.toggleChildren(context);
  }
}
