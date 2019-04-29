package com.technology.jep.jepria.client.widget.field.multistate.large;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;
import static com.technology.jep.jepria.client.JepRiaClientConstant.UPLOAD_SUCCESS_SUBSTRING;
import static com.technology.jep.jepria.client.ui.WorkstateEnum.EDIT;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_MIME_TYPE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_RECORD_KEY;
import static com.technology.jep.jepria.shared.JepRiaConstant.FILE_SIZE_HIDDEN_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.PRIMARY_KEY_HIDDEN_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.IS_DELETED_FILE_HIDDEN_FIELD_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.technology.jep.jepria.client.JepScheduledCommand;
import com.technology.jep.jepria.client.ui.WorkstateEnum;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.client.widget.event.JepEvent;
import com.technology.jep.jepria.client.widget.event.JepEventType;
import com.technology.jep.jepria.client.widget.field.multistate.JepMultiStateField;
import com.technology.jep.jepria.shared.record.lob.JepFileReference;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

public abstract class JepLargeField<V extends Widget> extends JepMultiStateField<JepFileUpload, V> {

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
  private Hidden hiddenPrimaryKeyField;
  
  /**
   * Скрытое поле, содержащее размер загружаемого файла, участвующее в upload submit.
   */
  private Hidden hiddenSizeField;
  
  /**
   * Значение поля.
   */
  private JepFileReference<?> fileReference = null;
  
  /**
   * Размер выбранного файла.
   */
  private Integer fileSize;
  
  /**
   * Обработчики событий выбора файла
   */
  protected HandlerRegistration fileChooseHandler;
  
  /**
   * Скрытое поле, содержащее информацию о необходимости удаления файла, участвующее в upload submit.
   */
  private Hidden isDeletedField;
  
  /**
   * Панель, на которой располагается карту Просмотра для отображения на карте Редактирования (используется в режиме {@code WorkstateEnum.EDIT}).
   */
  protected V editablePanelViewCard;
  
  /**
   * Панель для кнопок (удалить/отмена) (используется в режиме {@code WorkstateEnum.EDIT}).
   */
  private SimplePanel editablePanelTools;
  
  /**
   * Икнока для удаления файла (используется в режиме {@code WorkstateEnum.EDIT}).
   */
  private Image deleteFileIcon;
  
  /**
   * Иконка для отмены действия (используется в режиме {@code WorkstateEnum.EDIT}).
   */
  private Image undoIcon;
  
  /**
   * Список разрешенных расширений файлов
   */
  private final List<String> allowedExtensions = new ArrayList<String>(); 
  
  @Deprecated
  public JepLargeField(String fieldLabel) {
    this(null, fieldLabel, null);
  }
  
  /**
   * Конструктор.
   * @param fieldIdAsWebEl ID данного Jep-поля как Web-элемента.
   * @param fieldLabel Метка поля.
   * @param inputName Значение HTML-атрибута name тега input.
   */
  public JepLargeField(String fieldIdAsWebEl, String fieldLabel, String inputName) {
    super(fieldIdAsWebEl, fieldLabel);
    if (!JepRiaUtil.isEmpty(inputName)) setFieldName(inputName);
  }
  
  /**
   * Возвращает список разрешенный расширений.
   * @return Список разрешенный расширений.
   */
  public List<String> getAllowedExtensions() {
    return allowedExtensions;
  }
  
  /**
   * {@inheritDoc}
   */
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
        
        if (isDeleted()) {  // Делаем поле доступным, так как после выставления признака isDeleted, 
          setEnabled(true);  //оно disabled и не будет отправлено на сервер.
        }
        
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
    mainPanel.add(formPanel);
    
    // Uploader add at this form
    editableCard = new JepFileUpload();
    editablePanel.add(editableCard);
    
    editablePanelViewCard = createViewCard();
    editablePanel.add(editablePanelViewCard);
    
    editablePanelTools = new SimplePanel();
    editablePanel.add(editablePanelTools);
    
    // Добавляем обработчик события "выбора файла" для получения размерности выбранного файла.
    initFileChooseHandler();
    
    // Hidden Field for primary key
    hiddenPrimaryKeyField = new Hidden(PRIMARY_KEY_HIDDEN_FIELD_NAME);
    editablePanel.add(hiddenPrimaryKeyField);
    
    // Hidden Field for file size if specified
    hiddenSizeField = new Hidden(FILE_SIZE_HIDDEN_FIELD_NAME);
    editablePanel.add(hiddenSizeField);
    
    // Hidden Field for isDeleted flag
    isDeletedField = new Hidden(IS_DELETED_FILE_HIDDEN_FIELD_NAME);
    editablePanel.add(isDeletedField);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void addViewCard() {
    V newViewCard = createViewCard();
    if (!(newViewCard instanceof HTML)) { // для виджетов, не являющихся HTML
      viewCard.removeFromParent();
      viewCard = newViewCard;
    }
    viewPanel.add(viewCard);
  }
  
  /**
   * Создает карту Просмотра (объект класса HTML)
   * @return карта Просмотра.
   */
  @SuppressWarnings("unchecked")
  protected V createViewCard() {
    return (V) new HTML();
  }
  
  /**
   * Обработчик нового состояния.
   * 
   * @param newWorkstate новое состояние
   */
  protected void onChangeWorkstate(WorkstateEnum newWorkstate) {
    if (EDIT.equals(newWorkstate)) {
      resetEditableCard();
    }
    prepareEditableTools(getValue(), newWorkstate); // Отобржаем/скрываем панель редактирования
    super.onChangeWorkstate(newWorkstate);
  }
  
  /**
   * {@inheritDoc}
   */
  public void setEnabled(boolean enabled) {
    getInputElement().setPropertyBoolean("disabled", !enabled);
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
    if (editableCard.isAttached()) {
      formPanel.reset(); // NOT RESET HIDDEN FIELDS
      isDeletedField.setValue(Boolean.FALSE.toString()); // Сбрасываем признак удаления.
    }
  }
  
  /**
   * Построение URL для загрузки файла
   * 
   * @param reference ссылка JepFileReference, формируемая в бине
   * @return соответствующий URL
   */
  protected String buildDownloadUrl(Object reference) {
    if (reference instanceof JepFileReference) {
      JepFileReference<?> fileReference = (JepFileReference<?>) reference;
      StringBuilder sbUrl = new StringBuilder();
      
      sbUrl.append(downloadServletUrl);
      sbUrl.append("?");
      sbUrl.append(DOWNLOAD_FIELD_NAME);
      sbUrl.append("=");
      sbUrl.append(getFieldName());
      sbUrl.append("&");
      sbUrl.append(DOWNLOAD_RECORD_KEY);
      sbUrl.append("=");
      sbUrl.append(fileReference.getRecordKey());
      
      String mimeType = fileReference.getMimeType();
      if (mimeType != null) {
        sbUrl.append("&");
        sbUrl.append(DOWNLOAD_MIME_TYPE);
        sbUrl.append("=");
        sbUrl.append(mimeType);
      } else {
        return null;  // При пустом mime-type для Blob-поля считаем, что поле пусто.
      }
      sbUrl.append("&");  // "Защита" от кэширования
      sbUrl.append(viewCount++);  // "Защита" от кэширования
      return sbUrl.toString();
    } 
    return null;
  }  
  
  /**
   * Получение ссылки на сабмит-форму
   * 
   * @return ссылка на сабмит-форму
   */
  public FormPanel getFormPanel() {
    return formPanel;
  }
  
  /**
   * Получение ссылки на hidden-поле, содержащее первичный ключ
   * @return ссылка на hidden-поле
   */
  public Hidden getHiddenPrimaryKeyField() {
    return hiddenPrimaryKeyField;
  }
  
  /**
   * Установка name (HTML-атрибута) input поля.
   * 
   * @param name name (HTML-атрибута) input поля.
   */
  public void setFieldName(String name) {
    editableCard.setName(name);
  }
  
  /**
   * Получение name (HTML-атрибута) input поля.
   * 
   * @return name (HTML-атрибута) input поля.
   */
  public String getFieldName() {
    return editableCard.getName();
  }
  
  /**
   * Установка команды, которая выполнится до сабмита формы
   * 
   * @param command    отложенная команда  
   */
  public void setBeforeSubmitCommand(ScheduledCommand command) {
    this.beforeSubmitCommand = command;
  }
  
  /**
   * Установка команды, которая выполнится после сабмита формы
   * 
   * @param command    отложенная команда  
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
    if (!Objects.equals(oldValue, value)) {
      fileReference = (JepFileReference<?>) value;
      setViewValue(value);
      prepareEditableTools(fileReference, _workstate);
    }
  }
  
  /**
   * Подготовка инструментов редактирования поля.
   * @param value Значение поля.
   * @param workstate Состояние.
   */
  private void prepareEditableTools(JepFileReference<?> value, WorkstateEnum workstate) {
    
    // Если в режиме EDIT и если поле не пустое,
    boolean isEditNotEmptyValue = EDIT.equals(workstate) && !JepRiaUtil.isEmpty(value);
    
    // то отображаем функциональные элементы карты Редактирования:
    //  ссылка на скачивание
    editablePanelViewCard.setVisible(isEditNotEmptyValue);
    
    //  панель кнопок
    editablePanelTools.clear();
    
    // кнопка удаления
    if (isEditNotEmptyValue) {
      showDeleteFileIcon();
    }
  }
  
  /**
   * Инициализация иконки и обработчика удаления файла.
   */
  private void showDeleteFileIcon() {
    if (deleteFileIcon == null) {
      
      deleteFileIcon = new Image(JepImages.delete());
      deleteFileIcon.addStyleName(FIELD_INDICATOR_STYLE);
      deleteFileIcon.addStyleName(JepLargeFieldStyleConstant.JEP_LARGE_FIELD_DELETE_FILE_ICON_CLASS);

      deleteFileIcon.setTitle(JepTexts.largeField_button_deleteFile());
      deleteFileIcon.setAltText(JepTexts.largeField_button_deleteFile());
      
      deleteFileIcon.addClickHandler(event -> {
        onDeleteFile(event);
      });
      
      // Ширина ячейки инструментов редактирования равна ширине иконке "Удалить файл"
      // TODO: гибкая установка ширины ячейки инструментов редактирования
      editablePanelTools.setWidth(deleteFileIcon.getWidth() + Unit.PX.getType());
    }
    
    editablePanelTools.setWidget(deleteFileIcon);
  }

  /**
   * Инициализация иконки и обработчика отмены действия.
   */
  private void showUndoIcon() {
    if (undoIcon == null) {
      
      undoIcon = new Image(JepImages.undo());
      undoIcon.setWidth(undoIcon.getWidth() + Unit.PX.getType());
      undoIcon.addStyleName(FIELD_INDICATOR_STYLE);
      undoIcon.addStyleName(JepLargeFieldStyleConstant.JEP_LARGE_FIELD_UNDO_ICON_CLASS);

      undoIcon.setTitle(JepTexts.button_cancel_alt());
      undoIcon.setAltText(JepTexts.button_cancel_alt());
      
      undoIcon.addClickHandler(event -> {
        onUndo(event);
      });
    }
    
    editablePanelTools.setWidget(undoIcon);
  }
  
  /**
   * Обработчика нажатия по иконке "Отмена".
   */
  private void onUndo(ClickEvent event) {
    if (isDeleted()) { // отмена удаления файла
      showDeleteFileIcon();
      changeIsDeleted(false);
    } else if (isFileSelected()) { // отмена выбора файла
      // TBD
      // будет два случая в CREATE - сбросить значение
      // в EDIT - восстановить прежнее
    }
  }

  /**
   * Обработчика нажатия по иконке "Удаление файла".
   */
  private void onDeleteFile(ClickEvent event) {
    showUndoIcon();
    changeIsDeleted(true);
  }

  /**
   * Изменяет значение isDeleted и применяет изменения к отображению карт поля. 
   * @param isDeleted
   */
  private void changeIsDeleted(boolean isDeleted) {
    
    editablePanelViewCard.setVisible(!isDeleted);
    
    isDeletedField.setValue(String.valueOf(isDeleted));
    
    // Если стоит признак isDeleted, поле становится disabled, 
    // в onSubmit (если признак все еще установлен) disabled снимается. 
    setEnabled(!isDeleted);
    
    notifyListeners(JepEventType.CHANGE_IS_DELETED_FILE_EVENT, new JepEvent(JepLargeField.this, isDeleted));
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
  public JepFileReference<?> getValue() {

    if (fileReference == null) {
      fileReference = new JepFileReference<Object>();
    }

    JepFileReference<?> currentValue;
    
    boolean isDeleted = isDeleted();
    
    if (isDeleted) {
      currentValue = new JepFileReference<>();
    } else {
      // Только если пользователем выбран файл(-ы) для загрузки, тогда ПЕРЕЗАПИСЫВАЕМ имя файла в fileReference вместо значения, которое
      // (возможно) там было изначально (например: пришло с сервера/из ejb/из базы данных).
      String fileName = editableCard.getFilename();
      if (!JepRiaUtil.isEmpty(fileName)) {
        fileReference.setFileName(fileName);
        fileReference.setFileExtension(JepFileReference.detectFileExtension(fileName));
      }
      
      currentValue = fileReference;
    }
    
    // Устанавливаем признак необходимости удаления файла.
    currentValue.setDeleted(isDeleted);
    
    return currentValue;
  }
  
  /**
   * Получение признака, что файл(-ы) готовы к загрузке. <br/> 
   * true, если выполняется хотя бы одно из условий:
   * <ul>
   *  <li>Файл(-ы) выбран.</li>
   *  <li>Стоит признак удаления файла(-ов) (для режима редактирования).</li>
   * </ul>
   * @return Признак готовности поля к загрузке.
   */
  public boolean isFileReadyToUpload() {
    return isFileSelected() || isDeleted();
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
   * Получение признака удаления файла(-ов).
   * @return Признака удаления файла(-ов).
   */
  public boolean isDeleted() {
    return (isDeletedField != null) && Boolean.valueOf(isDeletedField.getValue());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid() {
    
    // Перед проверкой, очищаем предыдущие ошибки.
    clearInvalid();
    
    boolean isEmpty = JepRiaUtil.isEmpty(getValue()) || isDeleted();
    if (!allowBlank && isEmpty) {
      markInvalid(JepTexts.field_blankText());
      return false;
    }

    if (!isEmpty) {
      Integer maxUploadFileSize = JepRiaUtil.isEmpty(hiddenSizeField.getValue()) ? null : Integer.decode(hiddenSizeField.getValue());
      // Если задан максимальный размер загружаемого файла, а также имеется клиентская поддержка получения размера файла
      // проверяем данные значения на допустимость
      if (!JepRiaUtil.isEmpty(maxUploadFileSize) && !JepRiaUtil.isEmpty(fileSize) && fileSize > maxUploadFileSize) {
        markInvalid(JepClientUtil.substitute(JepTexts.errors_file_uploadFileSizeError(), maxUploadFileSize, fileSize));
        return false;
      } else if (allowedExtensions.size() > 0 && !JepRiaUtil.isEmpty(getValue().getFileExtension())) { // валидация только если заданы допустимые расширения
        if (!allowedExtensions.contains(getValue().getFileExtension().toLowerCase())) {
          markInvalid(
              JepClientUtil.substitute(
                  JepTexts.errors_file_uploadExtension(), String.join(", ", allowedExtensions)
              )
          );
          return false;
        }
      }
    }
    
    return true;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public int getWidgetIndex(Widget child) {
    if (child.equals(editablePanel)) {
      child = formPanel;
    }
    return super.getWidgetIndex(child);
  }
  
  /**
   * Set limit for uploaded file in Kbytes
   * 
   * @param length allowed file size (Kbytes)
   */
  public void setMaxUploadFileSize(int length) {
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
  
  /**
   * Инициализация обработчика "выбора файла" {@link com.google.gwt.event.dom.client.ChangeHandler}.
   */
  protected void initFileChooseHandler() {
    if (fileChooseHandler == null) {
      fileChooseHandler = editableCard.addChangeHandler(new ChangeHandler() {
        @Override
        public void onChange(ChangeEvent event) {
          fileChooseEventHandler(event);
        }
      });
    }
  }
  
  /**
   * Обработка события выбора файла в поле.
   * 
   * @param event  срабатываемое событие
   * 
   * Особенности:<br/>
   * В случае необходимости добавления функциональности в обработку события, необходимо перекрыть данный метод,
   * а не добавлять новый обработчик события, чтобы обеспечить единую точку входа и обработки данного события.
   */
  protected void fileChooseEventHandler(ChangeEvent event) {
    this.fileSize = editableCard.getFileSize();
    
    if (EDIT.equals(_workstate)) { // При выборе нового файла скрываем отображение текущего значения и инструментальную панель.
      editablePanelViewCard.setVisible(false);
      editablePanelTools.clear();
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void applyStyle() {
    super.applyStyle();
    addStyleName(JepLargeFieldStyleConstant.JEP_LARGE_FIELD_CLASS);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void setFieldWidth(int fieldWidth) {
    super.setFieldWidth(fieldWidth);
    editablePanelViewCard.setWidth(fieldWidth == 0 ? "" : (fieldWidth + Unit.PX.getType()));
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void setViewValue(Object value) {
    setViewValue(editablePanelViewCard, value);
    setViewValue(viewCard, value);
  }
  
  /**
   * Установка значения для карты Просмотра. Метод для переопределения в потомках.
   * @param viewCard карта Просмотра
   * @param value значение
   */
  abstract protected void setViewValue(V viewCard, Object value);
  
  /**
   * Проверка, что сабмит был успешен.<br/>
   * По умолчанию возвращает true, если получен пустой ответ или ответ содержит подстроку 
   * &quot;success&quot;. В противном случае возвращает false. Может быть переопределён 
   * в классе-наследнике. <br/><br/>
   * TODO: хранить resultHtml в поле класса, а не как параметр функции.
   * @param resultHtml полученный от сервера результат сабмита в виде строки
   * @return true в случае успешного сабмита, false в противном случае
   */
  public boolean isSubmitSuccessful(String resultHtml) {
    return JepRiaUtil.isEmpty(resultHtml) || resultHtml.contains(UPLOAD_SUCCESS_SUBSTRING);
  }
  
  /**
   * Класс содержит константы, связанные с стилями и автоматизацией класса JepLargeField.
   */
  public static final class JepLargeFieldStyleConstant {
    
    private JepLargeFieldStyleConstant() {}
    
    /**
     * Наименование CSS-класса поля JepLargeField.
     */
    public static final String JEP_LARGE_FIELD_CLASS = "jepRia-jepLargeField";
    
    /**
     * Наименование CSS-класса кнопки "Удалить файл" (режим EDIT).
     */
    public static final String JEP_LARGE_FIELD_DELETE_FILE_ICON_CLASS = "jepRia-jepLargeField-deleteFileIcon";
    
    /**
     * Наименование CSS-класса кнопки отмены удаления (режим EDIT).
     */
    public static final String JEP_LARGE_FIELD_UNDO_ICON_CLASS = "jepRia-jepLargeField-undoIcon";
  }
}
