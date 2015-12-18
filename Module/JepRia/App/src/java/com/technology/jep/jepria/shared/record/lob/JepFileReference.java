package com.technology.jep.jepria.shared.record.lob;

import static com.technology.jep.jepria.shared.util.JepRiaUtil.isEmpty;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Ссылка на *_LOB-поле, идентифицирующая его в пределах таблицы БД.
 */
public class JepFileReference implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String recordKey;
	private Number recKey;
	
	private String fileExtension;
	private String mimeType;
	
	public JepFileReference() {}
	
	public JepFileReference(
			Object key,
			String fileExtension,
			String mimeType) {
		this(
			null,
			key,
			fileExtension,
			mimeType);
	}
	
	public JepFileReference(
			String fileName,
			Object key,
			String fileExtension,
			String mimeType) {
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.mimeType = mimeType;
		
		if (key instanceof Number){
			this.recKey = (Number) key;
		}
		else if (key instanceof String){
			this.recordKey = (String) key;
		}
	}
		
	/**
	 * Получение значения имени файла из объекта типа {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}.<br/>
	 * Если в качестве параметра передан null или переданный параметр не является наследником 
	 * {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}, то возвращаемый результат будет null.<br/>
	 * Пример использования в прикладном модуле:
	 * <pre>
	 *   ...
	 *   String fileName = JepFileReference.getFileName(record.get(fieldName));
	 *   ...
	 * </pre>
	 *
	 * @param value объект (обычно поле записи {@link com.technology.jep.jepria.shared.record.JepRecord}), из которого необходимо получить 
	 * имя файла
	 * @return имя файла
	 */
	public static String getFileName(Object value) {
		String fileName = null;

		if (value != null && (value instanceof JepFileReference)) {
			fileName = ((JepFileReference)value).getFileName();
		}

		return fileName;
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Object getRecordKey() {
		return isEmpty(recordKey) ? recKey : recordKey;
	}

	/**
	 * Получение значения расширения имени файла из объекта типа {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}.<br/>
	 * Если в качестве параметра передан null или переданный параметр не является наследником 
	 * {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}, то возвращаемый результат будет null.<br/>
	 * Пример использования в прикладном модуле:
	 * <pre>
	 *   ...
	 *   String fileExtension = JepFileReference.getFileExtension(record.get(fieldName));
	 *   ...
	 * </pre>
	 *
	 * @param value объект (обычно поле записи {@link com.technology.jep.jepria.shared.record.JepRecord}), из которого необходимо получить 
	 * расширение имени файла
	 * @return расширение имени файла
	 */
	public static String getFileExtension(Object value) {
		String fileExtension = null;

		if (value != null && (value instanceof JepFileReference)) {
			fileExtension = ((JepFileReference)value).getFileExtension();
		}

		return fileExtension;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Получение значения MIME-типа файла из объекта типа {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}.<br/>
	 * Если в качестве параметра передан null или переданный параметр не является наследником 
	 * {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}, то возвращаемый результат будет null.<br/>
	 * Пример использования в прикладном модуле:
	 * <pre>
	 *   ...
	 *   String mimeType = JepFileReference.getMimeType(record.get(fieldName));
	 *   ...
	 * </pre>
	 *
	 * @param value объект (обычно поле записи {@link com.technology.jep.jepria.shared.record.JepRecord}), из которого необходимо получить 
	 * MIME-тип файла
	 * @return MIME-тип файла
	 */
	public static String getMimeType(Object value) {
		String mimeType = null;

		if (value != null && (value instanceof JepFileReference)) {
			mimeType = ((JepFileReference)value).getMimeType();
		}

		return mimeType;
	}

	public String getMimeType() {
		return mimeType;
	}
	
}
