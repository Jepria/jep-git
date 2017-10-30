package org.jepria.validator.core.base;

import java.util.Objects;
import java.util.Optional;

import com.github.javaparser.Position;

/**
 * Класс, представляющий помеченный фрагмент некоторого ресурса.
 */
public class MarkSpan {
  
  /**
   * Starts with 1, included into the span.
   * For example, the span of letters 'A' will be [lineBegin:2,colBegin:1,lineEnd:3,colEnd:3]
   * <pre>
-.-.-
-AAAA
AAA-.
-.-.-</pre>
   */
  public int lineBegin;
  
  /**
   * Starts with 0, included into the span.
   * For example, the span of letters 'A' will be [lineBegin:2,colBegin:1,lineEnd:3,colEnd:3]
   * <pre>
-.-.-
-AAAA
AAA-.
-.-.-</pre>
   */
  public int colBegin;
  
  /**
   * Starts with 1, included into the span.
   * For example, the span of letters 'A' will be [lineBegin:2,colBegin:1,lineEnd:3,colEnd:3]
   * <pre>
-.-.-
-AAAA
AAA-.
-.-.-</pre>
   */
  public int lineEnd;
  
  /**
   * Starts with 0, excluded from the span.
   * For example, the span of letters 'A' will be [lineBegin:2,colBegin:1,lineEnd:3,colEnd:3]
   * <pre>
-.-.-
-AAAA
AAA-.
-.-.-</pre>
   */
  public int colEnd;
  
  private MarkSpan(int lineBegin, int colBegin, int lineEnd, int colEnd) {
    this.lineBegin = lineBegin;
    this.colBegin = colBegin;
    this.lineEnd = lineEnd;
    this.colEnd = colEnd;
  }

  /**
   * Конструктор, адаптированный для {@link com.github.javaparser.ast.nodeTypes.NodeWithRange#getBegin() NodeWithRange#getBegin()},
   * {@link com.github.javaparser.ast.nodeTypes.NodeWithRange#getEnd() NodeWithRange#getEnd()}.
   */
  public static MarkSpan of(Optional<Position> posBegin, Optional<Position> posEnd) {
    if (posBegin != null && posBegin.isPresent() &&
        posEnd != null && posEnd.isPresent()) {
      return new MarkSpan(posBegin.get().line, posBegin.get().column - 1, posEnd.get().line, posEnd.get().column);
    } else {
      return null;
    }
  }
  
  public static MarkSpan of(int lineBegin, int colBegin, int lineEnd, int colEnd) {
    return new MarkSpan(lineBegin, colBegin, lineEnd, colEnd);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.colBegin, this.colEnd, this.lineBegin, this.lineEnd);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof MarkSpan)) {
      return false;
    }
    MarkSpan objSpan = (MarkSpan)obj;
    return objSpan.colBegin == this.colBegin 
        && objSpan.colEnd == this.colEnd 
        && objSpan.lineBegin == this.lineBegin
        && objSpan.lineEnd == this.lineEnd;
  }
}
