package org.jepria.validator.core.base.transform;

import java.io.OutputStream;

/**
 * Интсрументарий рефакторинга содержимого ресурса 
 */
public interface ContentRefactorer extends Refactorer {
  /**
   * @return выходной поток для записи измененного содержимого ресурса
   */
  OutputStream getRefactoringStream();
}