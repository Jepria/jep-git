package com.technology.jep.jepria.client.widget.field.multistate.large;

import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_MIME_TYPE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_RECORD_KEY;
import static com.technology.jep.jepria.shared.JepRiaConstant.FILE_SIZE_HIDDEN_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.PRIMARY_KEY_HIDDEN_FIELD_NAME;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepScheduledCommand;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public abstract class JepLargeField<V extends Widget> extends JepMultiStateField<FileUpload, V> {

	/**
	 * Поле, предназначенное для борьбы с кэшированием (проявляется в Internet Explorer).
	 */
	private int viewCount = 0;
		
	/**
	 * Базовый URL модуля.
	 */
	private static final String MODULE_URL = GWT.getModuleBaseURL();
	
	/**
	 * URL для загрузки по умолчанию.
	 */
	private static final String DEFAULT_UPLOAD_SERVLET_URL = MODULE_URL + "upload";
	
	/**
	 * URL для выгрузки по умолчанию
	 */
	private static final String DEFAULT_DOWNLOAD_SERVLET_URL = MODULE_URL + "download";
	
	/**
	 * Отложенная команда, которая выполниться до сабмита формы (если она определена).
	 */
	private ScheduledCommand beforeSubmitCommand;
	
	/**
	 * Отложенная команда, которая выполниться после сабмита формы (если она определена).
	 */
	private JepScheduledCommand<String> afterSubmitCommand;
	
	/**
	 * URL для выгрузки.
	 */
	private String downloadServletUrl = DEFAULT_DOWNLOAD_SERVLET_URL;
	
	/**
	 * Форма панели для файлового загрузчика.
	 */
	private FormPanel formPanel;
	
	/**
	 * Скрытое поле, содержащее первичный ключ записи, участвующее в upload submit.
	 */
	private Hidden hiddenPrimaryKeyField, hiddenSizeField;
	
	/**
	 * Значение поля.
	 */
	private JepFileReference fileReference = null;
	
	public JepLargeField(String fieldLabel) {
		super(fieldLabel);
	}
	
	@Override
	protected void addEditableCard() {
		formPanel = new FormPanel();
		formPanel.setAction(DEFAULT_UPLOAD_SERVLET_URL);
		// Multipart form for uploading
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		
		formPanel.addSubmitHandler(new SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if (!JepRiaUtil.isEmpty(beforeSubmitCommand)) {
					beforeSubmitCommand.execute();
				}
			}
		});
		
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (!JepRiaUtil.isEmpty(afterSubmitCommand)) {
					afterSubmitCommand.setData(event.getResults());
					afterSubmitCommand.execute();
				}
			}
		});
		// editablePanel will be deattached from deckpanel 
		// (pay attention on the code inside setWidget method)
		formPanel.setWidget(editablePanel);
		// initialize form panel as new editable panel
		add(formPanel);
		
		// Uploader add at this form
		editableCard = new FileUpload();
		editablePanel.add(editableCard);
		
		// Hidden Field for primary key
		hiddenPrimaryKeyField = new Hidden(PRIMARY_KEY_HIDDEN_FIELD_NAME);
		editablePanel.add(hiddenPrimaryKeyField);
		
		// Hidden Field for file size if specified
		hiddenSizeField = new Hidden(FILE_SIZE_HIDDEN_FIELD_NAME);
		editablePanel.add(hiddenSizeField);
	}
	
	/**
	 * Обработчик нового состояния.
	 * 
	 * @param newWorkstate новое состояние
	 */
	protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
		if (EDIT.equals(newWorkstate)){
			resetEditableCard();
		}
		super.onChangeWorkstate(newWorkstate);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		super.clear();
		this.fileReference = null;
		resetEditableCard();
	}
	
	/**
	 * Сброс значения загрузчика
	 */
	protected void resetEditableCard() {
		if (editableCard.isAttached()){
			formPanel.reset();
		}
	}
	
	/**
	 * Построение URL для загрузки файла
	 * 
	 * @param reference ссылка JepFileReference, формируемая в бине
	 * @return соответствующий URL
	 */
	protected String buildDownloadUrl(Object reference) {
		if(reference instanceof JepFileReference) {
			JepFileReference fileReference = (JepFileReference) reference;
			StringBuilder sbUrl = new StringBuilder();
			
			sbUrl.append(downloadServletUrl);
			sbUrl.append("?");
			sbUrl.append(DOWNLOAD_FIELD_NAME);
			sbUrl.append("=");
			sbUrl.append(getFieldId());
			sbUrl.append("&");
			sbUrl.append(DOWNLOAD_RECORD_KEY);
			sbUrl.append("=");
			sbUrl.append(fileReference.getRecordKey());
			
			String mimeType = fileReference.getMimeType();
			if(mimeType != null) {
				sbUrl.append("&");
				sbUrl.append(DOWNLOAD_MIME_TYPE);
				sbUrl.append("=");
				sbUrl.append(mimeType);
			} else {
				return null;	// При пустом mime-type для Blob-поля считаем, что поле пусто.
			}
			sbUrl.append("&");			// "Защита" от кэширования
			sbUrl.append(viewCount++);	// "Защита" от кэширования
			return sbUrl.toString();
		} 
		return null;
	}	
	
	/**
	 * Получение ссылки на сабмит-форму
	 * 
	 * @return 				ссылка на сабмит-форму
	 */
	public FormPanel getFormPanel(){
		return formPanel;
	}
	
	/**
	 * Получение ссылки на hidden-поле, содержащее первичный ключ
	 * @return				ссылка на hidden-поле
	 */
	public Hidden getHiddenPrimaryKeyField(){
		return hiddenPrimaryKeyField;
	}
	
	/**
	 * Установка идентификатора поля
	 * 
	 * @param id			идентификатор поля
	 */
	public void setFieldId(String id){
		editableCard.setName(id);
	}
	
	/**
	 * Получение идентификатора поля
	 * 
	 * @return идентификатор поля
	 */
	public String getFieldId(){
		return editableCard.getName();
	}
	
	/**
	 * Установка команды, которая выполнится до сабмита формы
	 * 
	 * @param command		отложенная команда	
	 */
	public void setBeforeSubmitCommand(ScheduledCommand command) {
		this.beforeSubmitCommand = command;
	}
	
	/**
	 * Установка команды, которая выполнится после сабмита формы
	 * 
	 * @param command		отложенная команда	
	 */
	public void setAfterSubmitCommand(JepScheduledCommand<String> command) {
		this.afterSubmitCommand = command;
	}
	
	/**
	 * Установка значения поля.<br/>
	 * Особенности перегруженного метода:
	 * <ul>
	 *  <li>При установке значения поля, устанавливается значение ТОЛЬКО для карты Просмотра: компоненту загрузки файла выставить значение программно
	 * - невозможно.</li>
	 * </ul>
	 *
	 * @param value значение поля - объект наследник {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}
	 */
	@Override
	public void setValue(Object value) {
		Object oldValue = getValue();
		if(!JepRiaUtil.equalWithNull(oldValue, value)) {
			this.fileReference = (JepFileReference) value;
			setViewValue(value);
		}		
	}
	
	/**
	 * Получение значения поля.<br/>
	 * На основе значения карты Редактирования формируется объект наследник {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}.<br/>
	 * Особенности реализации метода:
	 * <ul>
	 *   <li>
	 *     Только если пользователем выбран файл(-ы) для загрузки, тогда ПЕРЕЗАПИСЫВАЕТСЯ имя файла в {@link #fileReference} вместо значения, которое
	 *     (возможно) там было изначально (например: пришло с сервера/из ejb/из базы данных).
	 *   </li>
	 * </ul>
	 * <br/>
	 * 
	 * @return значение поля - объект наследник {@link com.technology.jep.jepria.shared.record.lob.JepFileReference}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JepFileReference getValue() {
		String fileName = editableCard.getFilename();
		if(fileReference == null) {
			fileReference = new JepFileReference();
		}

		// Только если пользователем выбран файл(-ы) для загрузки, тогда ПЕРЕЗАПИСЫВАЕМ имя файла в fileReference вместо значения, которое
		// (возможно) там было изначально (например: пришло с сервера/из ejb/из базы данных).
		if (!JepRiaUtil.isEmpty(fileName)) {
			fileReference.setFileName(fileName);
		}
		
		return fileReference;
	}
	
	/**
	 * Получение признака наличия выбранного пользователем файла(-ов).
	 * 
	 * @return true - файл(-ы) выбран, false - файл(-ы) не выбран
	 */
	public boolean isFileSelected() {
		return !JepRiaUtil.isEmpty(editableCard.getFilename());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidgetIndex(Widget child) {
		if (child.equals(editablePanel)){
			child = formPanel;
		}
		return super.getWidgetIndex(child);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setEmptyText(String emptyText){
		throw new UnsupportedOperationException("LargeField can't set empty text!");
	}
	
	/**
	 * Set limit for uploaded file in Kbytes
	 * 
	 * @param length allowed file size (Kbytes)
	 */
	public void setMaxUploadFileSize(int length){
		hiddenSizeField.setValue(length + "");
	}
	
	/**
	 * Установка URL сервлета выгрузки файла.
	 * @param downloadServletUrl URL сервлета выгрузки
	 */
	public void setDownloadServletUrl(String downloadServletUrl) {
		this.downloadServletUrl = downloadServletUrl;
	}
	
	/**
	 * Установка URL сервлета загрузки файла на сервер.
	 * @param uploadServletUrl URL сервлета загрузки
	 */
	public void setUploadServletUrl(String uploadServletUrl) {
		formPanel.setAction(uploadServletUrl);
	}
}
