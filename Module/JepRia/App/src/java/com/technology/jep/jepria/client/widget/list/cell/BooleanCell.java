package com.technology.jep.jepria.client.widget.list.cell;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

import java.util.Set;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Реализация ячейки, которая отображает на списочной форме булевое значения в текстовом виде
 */
public class BooleanCell extends AbstractCell<Boolean> {

	/**
	 * Конструктор ячейки
	 * @param consumedEvents события, используемые ячейкой
	 */
	public BooleanCell(String... consumedEvents) {
		super(consumedEvents);
	}

	/**
	 * Конструктор ячейки
	 * @param consumedEvents события, используемые ячейкой
	 */
	public BooleanCell(Set<String> consumedEvents) {
		super(consumedEvents);
	}

	/**
	 * Рендерит ячейку таблицы
	 * @param context контекст ячейки 
	 * @param value значение ячейки
	 * @param sb builder, в который помещается конечное содержимое ячейки 
	 */
	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Boolean value, SafeHtmlBuilder sb) {
		
		String label = Boolean.TRUE.equals(value) ? JepTexts.yes() : (value == null) ? "" : JepTexts.no();
		sb.appendEscaped(label);
	}
}
