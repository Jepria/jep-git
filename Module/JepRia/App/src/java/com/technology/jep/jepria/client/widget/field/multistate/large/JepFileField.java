package com.technology.jep.jepria.client.widget.field.multistate.large;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_EXTENSION;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FILE_NAME_PREFIX;

import java.util.Objects;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.HTML;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

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
  
  @Deprecated
  public JepFileField() {
    this(null);
  }
  
  @Deprecated
  public JepFileField(String fieldLabel) {
    this(null, fieldLabel);
  }
  
  public JepFileField(String fieldIdAsWebEl, String fieldLabel) {
    this(fieldIdAsWebEl, fieldLabel, null);
  }
  
  /**
   * Конструктор.
   * @param fieldIdAsWebEl ID данного Jep-поля как Web-элемента.
   * @param fieldLabel Метка поля.
   * @param inputName Значение HTML-атрибута name тега input.
   */
  public JepFileField(String fieldIdAsWebEl, String fieldLabel, String inputName) {
    super(fieldIdAsWebEl, fieldLabel, inputName);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setViewValue(HTML viewCard, Object reference) {
    viewCard.setHTML(buildFileRef(reference));
  };
  
  /**
   * Получение URL для карты просмотра
   * 
   * @return    ссылка на изображение для карты просмотра
   */
  public String getImageUrl() {
    return imageUrl;
  }
  
  /**
   * Определение URL изображения для карты просмотра
   * 
   * @param imageUrl  ссылка для изображения
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
  
  /**
   * Получение всплывающей подсказки (getter)
   * 
   * @return    всплывающая подсказка
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
   * @return    наименование скачиваемого файла
   */
  public String getDownloadFileName() {
    return downloadFileName;
  }
  
  /**
   * Определение имени скачиваемого файла (setter)
   * 
   * @param downloadFileName наименование скачиваемого файла
   */
  public void setDownloadFileName(String downloadFileName) {
    this.downloadFileName = downloadFileName;
  }
  
  /**
   * Получение префикса для имени скачиваемого файла (getter)
   * 
   * @return    префикс для наименования скачиваемого файла
   */
  public String getDownloadFileNamePrefix() {
    return downloadFileNamePrefix;
  }
  
  /**
   * Определение префикса для имени скачиваемого файла (setter)
   * 
   * @param downloadFileNamePrefix наименование префикса для скачиваемого файла
   */
  public void setDownloadFileNamePrefix(String downloadFileNamePrefix) {
    this.downloadFileNamePrefix = downloadFileNamePrefix;
  }
  
  /**
   * Построение ссылки для ссылки на скачиваемый файл
   * 
   * @param reference      ссылка JepFileReference
   * @return ссылка для скачивания
   */
  private String buildFileRef(Object reference) {
    String downloadUrl = buildDownloadUrl(reference);
    StringBuilder sbRef = new StringBuilder();

    if (downloadUrl != null) {
      sbRef.append("<a href=\"");
      sbRef.append(downloadUrl);
      sbRef.append("\"");
      sbRef.append("target=\"_blank\"");
      sbRef.append("title=\"");
      sbRef.append(JepTexts.largeField_currentFile());
      sbRef.append("\"");
      sbRef.append(">");
      
      // Если есть имя файла, то отображается ссылка на скачивание по имени, иначе икон по imageUrl
      // TODO: Добавить возможность гибкой настройки отображения (например, если есть имя файла, но необходимо скачивание по иконке). 
      // TODO: Заменить умолчательную иконку на более понятную, а также чтобы высота была не более 14px (иначе строка "скачет").
      String fileName = null;
      if (reference instanceof JepFileReference && reference != null) {
        fileName = ((JepFileReference<?>) reference).getFileName();
      }
      if (!JepRiaUtil.isEmpty(fileName)) { 
        sbRef.append(fileName);
        sbRef.append("  "); // Пробелы, чтобы был отступ. TODO: Сделать в помощью css. 
      } else {
        sbRef.append("<img src=\"");
        sbRef.append(getImageUrl());  
        sbRef.append("\"");
        sbRef.append(" ");
        sbRef.append("title=\"");
        sbRef.append(getFieldToolTip());
        sbRef.append("\"");
        sbRef.append("/>");
      }
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
      
    if (downloadUrl != null) {
      StringBuilder sbRef = new StringBuilder();
      String fileExtension = ((JepFileReference<?>) reference).getFileExtension();
      
      sbRef.append(downloadUrl);
      sbRef.append("&");
      sbRef.append(DOWNLOAD_CONTENT_DISPOSITION);
      sbRef.append("=");
      sbRef.append(DOWNLOAD_CONTENT_DISPOSITION_ATTACHMENT);
      if (downloadFileName != null) {
        sbRef.append("&");
        sbRef.append(DOWNLOAD_FILE_NAME);
        sbRef.append("=");
        sbRef.append(downloadFileName);
      } else if (downloadFileNamePrefix != null) {
        sbRef.append("&");
        sbRef.append(DOWNLOAD_FILE_NAME_PREFIX);
        sbRef.append("=");
        sbRef.append(downloadFileNamePrefix);
      }
      
      if (fileExtension != null) {
        sbRef.append("&");
        sbRef.append(DOWNLOAD_EXTENSION);
        sbRef.append("=");
        sbRef.append(fileExtension);
      }
      
      return URL.encode(sbRef.toString());
      
    } else {
      return null;
    }
  }
  
  @Override
  public void setValue(Object value) {
    Object oldValue = getValue();
    if (!Objects.equals(oldValue, value)) {
      if (value != null) {
        setDownloadFileName(((JepFileReference<?>) value).getFileName());
      }
      super.setValue(value);
    }
  }
}
