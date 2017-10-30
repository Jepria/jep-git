package org.jepria.validator.core.base;

import java.util.Objects;

/**
 * Класс, представляющий сообщение, генерируемое валидатором в процессе валидации
 */
public class Message {
  
  public final Object level;
  public final String text;
  public final MarkSpan markSpan;
  
  /**
   * Сообщение с текстом, без уровня и указания фрагмента
   */
  public Message(String text) {
    this(null, text, null);
  }
  
  /**
   * @param level уровень сообщения
   * @param text текст сообщения
   * @param markSpan фрагмент валидируемого ресурса, к которому относится сообщение 
   */
  public Message(Object level, String text, MarkSpan markSpan) {
    this.level = level;
    this.text = text;
    this.markSpan = markSpan;
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    if (level != null) {
      sb.append(level + ": ");
    }
    
    if (markSpan != null) {
      sb.append("[SPAN_").append(markSpan.lineBegin).append(':').append(markSpan.colBegin).append('_')
          .append(markSpan.lineEnd).append(':').append(markSpan.colEnd).append(']');
    }
    sb.append(": ");
    
    if (text != null) {
      sb.append(text);
    }
    return sb.toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.level, this.markSpan, this.text);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof Message)) {
      return false;
    }
    Message objMsg = (Message)obj;
    return (this.level == null ? objMsg.level == null : this.level.equals(objMsg.level))
        && (this.markSpan == null ? objMsg.markSpan == null : this.markSpan.equals(objMsg.markSpan))
        && (this.text == null ? objMsg.text == null : this.text.equals(objMsg.text));
  }
}
