package com.technology.jep.jepria.client.widget.field.multistate.large;

import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION_INLINE;
import static com.technology.jep.jepria.shared.JepRiaConstant.DOWNLOAD_CONTENT_DISPOSITION;

import com.google.gwt.user.client.ui.Image;

public class JepImageField extends JepLargeField<Image> {

  private static final String DATA_IMAGE_BASE64_SRC_ATTRIBUTE = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";

  @Deprecated
  public JepImageField(){
    this(null);
  }
  
  @Deprecated
  public JepImageField(String fieldLabel) {
    this(null, fieldLabel, null);
  }
  
  public JepImageField(String fieldIdAsWebEl, String fieldLabel) {
    this(fieldIdAsWebEl, fieldLabel, null);
  }
  
  /**
   * Конструктор.
   * @param fieldIdAsWebEl ID данного Jep-поля как Web-элемента.
   * @param fieldLabel Метка поля.
   * @param inputName Значение HTML-атрибута name тега input.
   */
  public JepImageField(String fieldIdAsWebEl, String fieldLabel, String inputName) {
    super(fieldIdAsWebEl, fieldLabel, inputName);
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected Image createViewCard() {
    return new Image();
  };
  
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void setViewValue(Image viewCard, Object reference) {
    String downloadUrl = buildDownloadUrl(reference);
    if(downloadUrl != null) {
      viewCard.setUrl(downloadUrl);
    } else {
      // 1px transparent image
      viewCard.setUrl(DATA_IMAGE_BASE64_SRC_ATTRIBUTE);
    }
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected String buildDownloadUrl(Object reference) {
    String downloadUrl = super.buildDownloadUrl(reference);
    StringBuilder sbRef = new StringBuilder();
  
    if(downloadUrl != null) {
      sbRef.append(downloadUrl);
      sbRef.append("&");
      sbRef.append(DOWNLOAD_CONTENT_DISPOSITION);
      sbRef.append("=");
      sbRef.append(DOWNLOAD_CONTENT_DISPOSITION_INLINE);
    } else {
      return null;
    }
    
    return sbRef.toString();
  }
}
