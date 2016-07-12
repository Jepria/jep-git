package com.technology.jep.jepria.client.message;

import static com.technology.jep.jepria.client.JepRiaClientConstant.JepImages;
import static com.technology.jep.jepria.client.JepRiaClientConstant.JepTexts;

/**
 * Нестатичность методов-обёрток вызвана необходимостью реализации интерфейса JepMessageBox.
 */
public class JepMessageBoxImpl implements JepMessageBox {
  
  public static JepMessageBoxImpl instance = new JepMessageBoxImpl();
  
  private JepMessageBoxImpl() {
  }
  
  /**
   * Подтверждение запроса на удаление записи/ей.
   * 
   * @param isMultiple признак массового удаления записей
   * @param callback   функция колбэка, вызываемого после удаления
   */
  public MessageBox confirmDeletion(boolean isMultiple, ConfirmCallback callback) {
    ConfirmMessageBox messageBox = new ConfirmMessageBox(
        JepTexts.deletion_dialog_title(),
          isMultiple ? JepTexts.deletion_dialog_messageTexts() : JepTexts.deletion_dialog_messageText(), callback);
    messageBox.show();
    
    return messageBox;
  }

  /**
   * Вызов диалога ошибки.
   * 
   * @param th исключительная ситуация
   * @param message сообщение об ошибке
   */
  public MessageBox showError(Throwable th, String message) {
    message = message == null ? "" : message + ": ";  
    message = message + prepareFineMessage(th);
    
    ErrorDialog errorDialog = new ErrorDialog(JepTexts.errors_dialog_title(), th, message);
    errorDialog.show();
    
    return errorDialog;
  }

  /**
   * Вызов диалога ошибки.
   * 
   * @param th исключительная ситуация
   */
  public MessageBox showError(Throwable th) {
    return showError(th, null);
  }
  
  /**
   * Вызов диалога ошибки.
   * 
   * @param message сообщение об ошибке
   */
  public MessageBox showError(String message) {
    AlertMessageBox alertMessageBox = new AlertMessageBox(JepTexts.errors_dialog_title(), message);
    alertMessageBox.setIcon(JepImages.error());
    alertMessageBox.show();
    
    return alertMessageBox;
  }

  /**
   * Вызов диалога предупреждения.
   * 
   * @param title заголовок предупреждения
   * @param message сообщение
   */
  public MessageBox alert(String title, String message) {
    AlertMessageBox alertMessageBox = new AlertMessageBox(title, message);
    alertMessageBox.show();
    
    return alertMessageBox;
  }
  
  /**
   * Вызов диалога предупреждения.
   * 
   * @param message сообщение
   */
  public MessageBox alert(String message) {
    return alert(JepTexts.alert_dialog_title(), message);
  }

  /**
   * Подготовка отображаемого сообщения. По умолчанию берётся сообщение
   * исключения-первоисточника (самого "нижнего")
   * 
   * @param th исходное ("верхнее") исключение
   * @return отображаемое сообщение
   */
  public String prepareFineMessage(Throwable th) {
    while (th.getCause() != null) {
      th = th.getCause();
    }
    return getDbOracleErrorText(th.getMessage() == null ? th.toString() : th.getMessage());
  }

  /**
   * Получение текста, очищенного от служебной информации базы данных Oracle.<br/>
   * Возвращается только первая ошибка - источник исключения.
   * 
   * @param errorText полный текст сообщения
   * @return очищенный текст
   */
  private String getDbOracleErrorText(String errorText) {
    String result = errorText;
    String[] lines = errorText.split("\n");
    StringBuffer sbResult = new StringBuffer();
    // Выявление пользовательских сообщений
    boolean isUserMessage = false;
    for (int j = 0; j < lines.length; j++) {
      String line = lines[j];
      // Определяем строки с ошибкой не меньше ORA-20000
      int oraIndex = line.indexOf("ORA-");
      if (oraIndex >= 0) {
        isUserMessage = false;
        // Проверка, что не меньше 20000
        int digit = Character.digit(line.charAt(oraIndex + 4), 10);
        if (digit >= 2) {
          // Проверка цифр
          int i = oraIndex + 5;
          int max = oraIndex + 9;
          for (; i < max; i++) {
            if (!Character.isDigit(line.charAt(i))) {
              break;
            }
          }
          if (i == max) { // Все цифры
            sbResult.setLength(0);
            sbResult.append(line.substring(i + 2));
            isUserMessage = true;
          }
        }
      } else { // Строка без "ORA-"
        if (isUserMessage) {
          if (sbResult.length() > 0) {
            sbResult.append("\n");
          }
          sbResult.append(line);
        }
      }
    }
    
    if (sbResult.length() > 0) { // Если пользовательские сообщения есть
      result = sbResult.toString();
    }

    return result;
  }

  /**
   * Трассировка стэка ошибок.
   * 
   * @param th исключительная ситуация
   */
  public String getStackTrace(Throwable th) {
    StringBuffer sbResult = new StringBuffer();
    sbResult.append(th.getClass().getName());
    sbResult.append(": ");
    sbResult.append(th.getLocalizedMessage());
    sbResult.append("\n");
    StackTraceElement[] stackTrace = th.getStackTrace();
    for (int i = 0; i < stackTrace.length; i++) {
      if (i > 0) {
        sbResult.append("\n");
      }
      sbResult.append("at ");
      sbResult.append(stackTrace[i].toString());
    }
    return sbResult.toString();
  }
}
