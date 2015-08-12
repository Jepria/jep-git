package com.technology.jep.jepriareport.server.large;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.report.JepReportParameters;
import com.technology.jep.jepriareport.server.JepReportRecord;


/**
 * Источник данных для больших отчётов
 */
public class JepLargeReportDataSource implements JRDataSource
{
	/**
	 * Штатный режим
	 */
	private final static int DEFAULT_MODE = 0;
	
	/**
	 * Режим тестирования исчерпания памяти 
	 */
	private final static int OUT_OF_MEMORY_TEST_MODE = 1;
	
	/**
	 * Штатный режим (по умолчанию) 
	 */
	private final static int mode = DEFAULT_MODE;
//	private final static int mode = OUT_OF_MEMORY_TEST_MODE;
	private final static int MAX_ROW_NUMBER_TEST_MODE = 1000000; 
	private int currentRowNumber = 0;
	
	private JepLargeReport largeReport = null;
	
	private int blockSize;
	private List<JepReportRecord> nextBlock = null;
	
	/**
	 * Текущая запись отчёта 
	 */
	private JepReportRecord currentReportRecord = null;
	
	
	/**
	 * Текущий индекс блока 
	 */
	private int currentIndex = 0;


	/**
	 * Конструктор источника данных для большого отчёта
	 * 
	 * @param beanJndiName имя EJB, реализующего интерфейс JepLargeReport
	 * @param parameters входные параметры отчёта
	 * @throws ApplicationException
	 */
	public JepLargeReportDataSource(String beanJndiName, JepReportParameters parameters) throws ApplicationException {
		try {
			largeReport = (JepLargeReport) ejbLookup(beanJndiName);
			blockSize = largeReport.begin(parameters);
		} catch (Throwable th) {
			throw new ApplicationException("DataSource init error", th);
		}
	}

	public boolean next() throws JRException
	{
		boolean result = false;
		switch(mode) {
		case OUT_OF_MEMORY_TEST_MODE:
			if(nextBlock == null) {
				nextBlock = largeReport.nextBlock(blockSize);
				currentIndex = 0;
			}
			if(currentRowNumber++ < MAX_ROW_NUMBER_TEST_MODE) {
				if(nextBlock != null) {
					currentReportRecord = nextBlock.get(0);	// Возвращается один и тот же первый объект
					result = true; 
					if(currentRowNumber % 1000 == 0) {
						System.out.println("Получено " + currentRowNumber + " записей");
					}
				}
			}
			break;
		default:
			if(nextBlock == null) {
				nextBlock = largeReport.nextBlock(blockSize);
				currentIndex = 0;
			}
			if(nextBlock != null) {	// Берём текущий блок
				if(currentIndex + 1 < nextBlock.size()) {
					currentReportRecord = nextBlock.get(currentIndex++);
					result = true; 
				} else {	// Берём новый блок
					nextBlock = largeReport.nextBlock(blockSize);
					if(nextBlock != null && nextBlock.size() > 0) {
						currentIndex = 0;
						currentReportRecord = nextBlock.get(currentIndex++);
						result = true; 
					}
				}
			}
			break;
		}
		
		if(!result) {
			largeReport.end();
		}
		
		return result; 
	}

	public Object getFieldValue(JRField field) throws JRException
	{
		return currentReportRecord.getFieldValue(field.getName());
	}
	
	/**
	 * Получение Local-интерфейса EJB.
	 * 
	 * @param beanName имя бина. Например, для "java:comp/env/ejb/Ejb3TemplateBean" в данном параметре будет содержаться "Ejb3TemplateBean".
	 * 
	 * @return Local-интерфейс EJB
	 * @throws NamingException
	 */
	private static Object ejbLookup(String beanName) throws NamingException {
		return new InitialContext().lookup("java:comp/env/ejb/" + beanName);
	}
}
