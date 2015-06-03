package com.technology.jep.jepria.client.widget.list.cell;

import java.util.List;

import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Класс, объекты которого хранят служебную информацию о узлах дерева
 */
public class ListTreeNode {
	
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
	 * Создает узел девера первого уровня
	 */
	public ListTreeNode(){
		this(1);
	}

	/**
	 * Создает узел дерево с произвольной глубиной
	 * @param depth глубина
	 */
	public ListTreeNode(int depth){
		
		this.depth = depth;
		this.isOpen = false;
		this.children = null;
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
		
		setIsOpen(true);
	}
	
	/**
	 * Меняет стату узла на "закрытый"
	 */
	public void close(){
		
		setIsOpen(false);
	}
	
	/**
	 * Меняет статус узла
	 * @param isOpen статус узла
	 */
	private void setIsOpen(boolean isOpen){
		this.isOpen = isOpen;
	}
	
	/**
	 * Получает статус узла
	 * @return статус узла
	 */
	public boolean getIsOpen(){
		return isOpen;
	}
	
	/**
	 * Получает глубину
	 * @return глубина
	 */
	public int getDepth(){
		return depth;
	}

}