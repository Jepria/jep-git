package com.technology.jep.jepriareport.server;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.technology.jep.jepria.shared.record.JepRecord;


/**
 * Источник данных для работы с SimpleViewObject
 */
public class JepReportDataSource implements JRDataSource
{
	/**
	 * Штатный режим
	 */
	private final static int DEFAULT_MODE = 0;
	
	protected JepReportRecords recordList = null;
	
	/**
	 * Режим тестирования исчерпания памяти 
	 */
	private final static int OUT_OF_MEMORY_TEST_MODE = 1;
	private final static int mode = DEFAULT_MODE;
//	private final static int mode = OUT_OF_MEMORY_TEST_MODE;
	private final static int MAX_ROW_NUMBER_TEST_MODE = 100000; 
	private int currentRowNumber = 0;

	public JepReportDataSource(JepReportRecords records) {
		this.recordList = records;
		this.recordList.beforeFirst();
	}

//	private static final int DEBUG_RECORD_NUMBER = 100;	// TODO Убрать после отладки
//	private int debugNumber = DEBUG_RECORD_NUMBER;		// TODO Убрать после отладки

	/**
	 * Конструктор источника данных
	 * Используется для анализа дизайна отчёта с целью динамической настройки
	 * При этом необходимо, чтобы дизайн отчёта (.jrxml) размещался в дистрибутиве модуля (ear) в директории файлов отчётов.
	 * TODO Внести в описание это требование !!!
	 * 
	 * @param records представление списочных данных
	 * @param reportDesignPath - путь файла jrxml, содержащего дизайн отчёта
	 * @throws JRException
	 */
	public JepReportDataSource(JepReportRecords records, String reportDesignPath) throws JRException {
		this(records);
		// Настройка view на параметры подотчёта
		JasperDesign jasperDesign = JRXmlLoader.load(reportDesignPath);
		int columnNumber = jasperDesign.getColumnCount();
		if(columnNumber > 1) {
			// Оба способа размещения "сначала по-вертикали" и "сначала по-горизонтали" в
			// JasperReports выполняются в режиме JRReport.PRINT_ORDER_HORIZONTAL (workaround).
			// При способе "сначала по-вертикали" горизонтальный режим JasperReports компенсируется горизонтальным режимом
			// SimpleViewObject
			if(jasperDesign.getPrintOrder() == JRReport.PRINT_ORDER_VERTICAL) {
				records.setPrintOrder(JepReportRecords.PrintOrder.HORIZONTAL);
				jasperDesign.setPrintOrder(JRReport.PRINT_ORDER_HORIZONTAL);
				JasperCompileManager.compileReportToFile(jasperDesign, reportDesignPath.replaceAll("jrxml", "jasper"));
			} else {
				records.setPrintOrder(JepReportRecords.PrintOrder.VERTICAL);
			}
			records.setColumnNumber(columnNumber);
		}
	}

	public JepReportDataSource(List<JepRecord> recordList,	String reportDesignPath) throws JRException {
		this(new JepReportRecords(recordList), reportDesignPath);
	}

//	/**
//	 * Конструктор источника данных
//	 * Используется для анализа дизайна отчёта с целью динамической настройки
//	 * При этом необходимо, чтобы дизайн отчёта (.jrxml) размещался в дистрибутиве модуля (ear) в директории файлов отчётов.
//	 * TODO Упразднить аналогичный конструктор с SimpleViewObject
//	 * TODO Внести в описание это требование !!!
//	 * 
//	 * @param reportRecords список результирующих записей отчёта
//	 * @param reportDesignPath - путь файла jrxml, содержащего дизайн отчёта
//	 */
//	public JepReportDataSource(JepReportRecords reportRecords, String reportDesignPath) throws JRException {
//		// Настройка reportRecords на параметры подотчёта
//		this.reportRecords = reportRecords;
//		
//		JasperDesign jasperDesign = JRXmlLoader.load(reportDesignPath);
//		int columnNumber = jasperDesign.getColumnCount();
//		if(columnNumber > 1) {
//			// Оба способа размещения "сначала по-вертикали" и "сначала по-горизонтали" в
//			// JasperReports выполняются в режиме JRReport.PRINT_ORDER_HORIZONTAL (workaround).
//			// При способе "сначала по-вертикали" горизонтальный режим JasperReports компенсируется горизонтальным режимом
//			// SimpleViewObject
//			if(jasperDesign.getPrintOrder() == JRReport.PRINT_ORDER_VERTICAL) {
//				reportRecords.setPrintOrder(JepReportRecords.PrintOrder.HORIZONTAL);
//				jasperDesign.setPrintOrder(JRReport.PRINT_ORDER_HORIZONTAL);
//				JasperCompileManager.compileReportToFile(jasperDesign, reportDesignPath.replaceAll("jrxml", "jasper"));
//			} else {
//				reportRecords.setPrintOrder(JepReportRecords.PrintOrder.VERTICAL);
//			}
//			reportRecords.setColumnNumber(columnNumber);
//		}
//	}

	public boolean next() throws JRException {
		boolean result = false;
		switch(mode) {
		case OUT_OF_MEMORY_TEST_MODE:
			if(currentRowNumber++ < MAX_ROW_NUMBER_TEST_MODE) {
				recordList.first();	// Возвращается один и тот же первый объект
				result = true; 
				if(currentRowNumber % 1000 == 0) {
					System.out.println("Получено " + currentRowNumber + " записей");
				}
			}
			break;
		default:
			if(recordList.hasNext()) {
				recordList.next();
				result = true; 
			}
			break;
		}
		
		return result; 
	}

	public Object getFieldValue(JRField field) throws JRException
	{
		Object result = null;
		String fieldName = field.getName();
		/* AVG 19.02
		 * 
		 * viewObject.getAttributeType(fieldName) 
		 * валится с ошибкой обращения к null, если тип аттрибута нестандартный   
		 */    
    //      if(viewObject.getAttributeType(fieldName) == SimpleViewObject.DATE_TYPE) {
    //    	result = viewObject.getAttributeDateAsString(fieldName,  "dd.MM.yyyy");	// Зашиваем конкретный формат, чтобы не возиться в приложении (скорее всего времянка) 
    //    } else {
		
    	result = recordList.getCurrentValue(fieldName);
    //    }
    //  	System.out.println(this.getClass().getName() + ".getFieldValue(" + fieldName + ") = " + result);
		return result;
	}

	public boolean beforeFirst() throws JRException
	{
		this.recordList.beforeFirst();
		return true;
	}
	
}
