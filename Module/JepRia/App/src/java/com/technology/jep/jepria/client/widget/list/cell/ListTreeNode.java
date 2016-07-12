package com.technology.jep.jepria.client.widget.list.cell;

import java.util.List;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс, объекты которого хранят служебную информацию о узлах дерева
 */
public class ListTreeNode {
  
  /**
   * Запись, соответствующая текущему узлу дерева
   */
  private JepRecord record;
  
  /**
   * Глубина
   */
  private int depth;
  
  /**
   * Открытость/закрытость узла
   */
  private boolean isOpen;
  
  /**
   * Кеш потомков  
   */
  public List<JepRecord> children;
  
  /**
   * Запись, соответствующая родительскому узлу дерева
   */
  private JepRecord parentRecord;
  
  public ListTreeNode(){}
  
  /**
   * Создает узел девера первого уровня
   */
  public ListTreeNode(JepRecord record){
    this(record, null, 1);
  }

  /**
   * Создает узел дерево с произвольной глубиной
   * @param depth глубина
   */
  public ListTreeNode(JepRecord record, JepRecord parentRecord, int depth){
    this.depth = depth;
    this.isOpen = false;
    this.children = null;
    this.record = record;
    this.parentRecord = parentRecord;
  }
  
  /**
   * Переключение видимости
   */
  public void toggleOpenStatus(){
    isOpen = !isOpen;
  }
  
  /**
   * Меняет стату узла на "открытый"
   */
  public void open(){
    setOpen(true);
  }
  
  /**
   * Меняет стату узла на "закрытый"
   */
  public void close(){
    setOpen(false);
  }
  
  /**
   * Меняет статус узла
   * @param isOpen статус узла
   */
  private void setOpen(boolean isOpen){
    this.isOpen = isOpen;
  }
  
  /**
   * Получает статус узла
   * @return статус узла
   */
  public boolean isOpen(){
    return isOpen;
  }
  
  /**
   * Получает глубину
   * @return глубина
   */
  public int getDepth(){
    return depth;
  }

  /**
   * Получение записи текущего узла
   * @return запись
   */
  public JepRecord getRecord() {
    return record;
  }
  
  /**
   * Получение записи родительского узла
   * @return запись
   */
  public JepRecord getParentRecord() {
    return parentRecord;
  }
  
}