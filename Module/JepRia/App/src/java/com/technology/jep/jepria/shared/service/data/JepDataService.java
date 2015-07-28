package com.technology.jep.jepria.shared.service.data;

import java.util.List;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.SystemException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.load.FindConfig;
import com.technology.jep.jepria.shared.load.PagingConfig;
import com.technology.jep.jepria.shared.load.PagingResult;
import com.technology.jep.jepria.shared.load.SortConfig;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Сервис данных: обеспечивает основные функции работы с объектами предметной области.
 */
public interface JepDataService extends RemoteService {

	/**
	 * Создание объекта.
	 * 
	 * @param createConfig конфигурация создаваемой записи
	 * @return результирующая запись
	 * @throws SystemException
	 */
	JepRecord create(FindConfig createConfig) throws SystemException;
	
	/**
	 * Удаление объекта.
	 * 
	 * @param deleteConfig конфигурация удаляемой записи
	 * @throws SystemException
	 */
	void delete(FindConfig deleteConfig) throws SystemException; 
	
	/**
	 * Обновление объекта.
	 * 
	 * @param updateConfig конфигурация записи с новыми значениями
	 * @return результирующая запись
	 * @throws SystemException
	 */
	JepRecord update(FindConfig updateConfig) throws SystemException; 
	
	/**
	 * Поиск.
	 * 
	 * @param pagingConfig параметры поиска
	 * @return результат поиска
	 * @throws SystemException
	 */
	PagingResult<JepRecord> find(PagingConfig pagingConfig) throws SystemException;
	
	/**
	 * Сортировка.
	 * 
	 * @param sortConfig параметры сортировки
	 * @return результат сортировки
	 * @throws SystemException
	 */
	PagingResult<JepRecord> sort(SortConfig sortConfig) throws SystemException;
	
	/**
	 * Листание данных.
	 * 
	 * @param pagingConfig параметры листания данных
	 * @return результат листания
	 * @throws SystemException
	 */
	PagingResult<JepRecord> paging(PagingConfig pagingConfig) throws SystemException;
	
	/**
	 * Подготавливает данные для формирования Excel-отчета.
	 * 
	 * @param pagingConfig параметры поиска
	 * @param reportHeaders список содержащий названия колонок
	 * @param reportFields список содержащий идентификаторы полей, из которых брать данные для колонок
	 * @throws SystemException
	 */
	void prepareExcel(PagingConfig pagingConfig, List<JepRecord> selectedRecords, List<String> reportHeaders, List<String> reportFields) throws SystemException;

	/**
	 * Метод проверки необходимости автообновления.
	 * 
	 * @param listUID уникальный идентификатор списка
	 * @return TRUE, если автообновление нужно; FALSE в противном случае
	 * @throws SystemException
	 */
	Boolean isRefreshNeeded(Integer listUID) throws SystemException;

	/**
	 * Записывает в сессию атрибуты для выгрузки файла и возвращает id загрузки.<br>
	 * Заголовку Content-disposition задаётся значение "attachment".
	 * 
	 * @param fileName имя файла
	 * @param mimeType mime-тип
	 * @param fieldName имя поля в таблице
	 * @param recordKey ключ записи
	 * @return id загрузки
	 */
	Integer prepareDownload(String fileName, String mimeType, String fieldName, String recordKey);

	/**
	 * Записывает в сессию атрибуты для выгрузки файла и возвращает id загрузки.<br>
	 * Если значение fileName пусто, то имя выгружаемого файла формируется как
	 * fileNamePrefix + recordKey + "." + extension.
	 * 
	 * @param fileName имя файла
	 * @param mimeType mime-тип
	 * @param fieldName имя поля в таблице
	 * @param recordKey ключ записи
	 * @param contentDisposition значение заголовка Content-disposition
	 * @param extension расширение
	 * @param fileNamePrefix префикс
	 * @return id загрузки
	 */
	Integer prepareDownload(String fileName, String mimeType, String fieldName, String recordKey,
		String contentDisposition, String extension, String fileNamePrefix);
	
}
