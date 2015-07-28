package com.technology.jep.jepria.client.widget.field.multistate.large;

import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_EXTENSION;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME_PREFIX;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME;

import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;

@SuppressWarnings("unchecked")
public class JepFileField extends JepLargeField<HTML> {
	
	/**
	 * Ссылка на картинку "скрепку", отображаемую в режиме просмотра
	 */
	private String imageUrl = "images/attach.gif";
	
	/**
	 * Всплывающая подсказка для ссылки на скачивание файла 
	 */
	private String toolTip = "Download";
	
	/**
	 * Наименование скачиваемого файла
	 */
	private String downloadFileName;
	
	/**
	 * Наименование префикса скачиваемого файла
	 */
	private String downloadFileNamePrefix;
		
	public JepFileField(){
		this("");
	}
	
	public JepFileField(String labelText) {
		super(labelText);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setViewValue(Object reference) {
		viewCard.setHTML(buildFileRef(reference));
	}
	
	/**
	 * Получение URL для карты просмотра
	 * 
	 * @return		ссылка на изображение для карты просмотра
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	
	/**
	 * Определение URL изображения для карты просмотра
	 * 
	 * @param imageUrl	ссылка для изображения
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * Получение всплывающей подсказки (getter)
	 * 
	 * @return		всплывающая подсказка
	 */
	public String getFieldToolTip() {
		return toolTip;
	}
	
	/**
	 * Определение всплывающей подсказки (setter)
	 * 
	 * @param toolTip всплывающая подсказка
	 */
	public void setFieldToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	
	/**
	 * Получение имени скачиваемого файла (getter)
	 * 
	 * @return		наименование скачиваемого файла
	 */
	public String getDownloadFileName(){
		return downloadFileName;
	}
	
	/**
	 * Определение имени скачиваемого файла (setter)
	 * 
	 * @param downloadFileName наименование скачиваемого файла
	 */
	public void setDownloadFileName(String downloadFileName){
		this.downloadFileName = downloadFileName;
	}
	
	/**
	 * Получение префикса для имени скачиваемого файла (getter)
	 * 
	 * @return		префикс для наименования скачиваемого файла
	 */
	public String getDownloadFileNamePrefix(){
		return downloadFileNamePrefix;
	}
	
	/**
	 * Определение префикса для имени скачиваемого файла (setter)
	 * 
	 * @param downloadFileNamePrefix наименование префикса для скачиваемого файла
	 */
	public void setDownloadFileNamePrefix(String downloadFileNamePrefix){
		this.downloadFileNamePrefix = downloadFileNamePrefix;
	}
	
	/**
	 * Построение ссылки для ссылки на скачиваемый файл
	 * 
	 * @param reference			ссылка JepFileReference
	 * @return ссылка для скачивания
	 */
	private String buildFileRef(Object reference) {
		String downloadUrl = buildDownloadUrl(reference);
		StringBuilder sbRef = new StringBuilder();

		if(downloadUrl != null){
			sbRef.append("<a href=\"");
			sbRef.append(downloadUrl);
			sbRef.append("\"");
			sbRef.append("target=\"_blank\"");
			sbRef.append(">");
		
			sbRef.append("<img src=\"");
			sbRef.append(getImageUrl());
			sbRef.append("\"");
			sbRef.append(" ");
			sbRef.append("title=\"");
			sbRef.append(getFieldToolTip());
			sbRef.append("\"");
			sbRef.append("/>");
		
			sbRef.append("</a>");
		} else {
			sbRef.append("");
		}
		
		return sbRef.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String buildDownloadUrl(Object reference) {
		String downloadUrl = super.buildDownloadUrl(reference);
			
		if(downloadUrl != null) {
			StringBuilder sbRef = new StringBuilder();
			String fileExtension = ((JepFileReference)reference).getFileExtension();
			
			sbRef.append(downloadUrl);
			sbRef.append("&");
			sbRef.append(DOWNLOAD_CONTENT_DISPOSITION);
			sbRef.append("=");
			sbRef.append(DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT);
			if(downloadFileName != null){
				sbRef.append("&");
				sbRef.append(DOWNLOAD_FILE_NAME);
				sbRef.append("=");
				sbRef.append(downloadFileName);
			}
			if(downloadFileNamePrefix != null){
				sbRef.append("&");
				sbRef.append(DOWNLOAD_FILE_NAME_PREFIX);
				sbRef.append("=");
				sbRef.append(downloadFileNamePrefix);
			}
			if(fileExtension != null) {
				sbRef.append("&");
				sbRef.append(DOWNLOAD_EXTENSION);
				sbRef.append("=");
				sbRef.append(fileExtension);
			}
			return sbRef.toString();
			
		} else {
			return null;
		}		
	}
}
