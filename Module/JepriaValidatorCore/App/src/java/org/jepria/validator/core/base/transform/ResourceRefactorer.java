package org.jepria.validator.core.base.transform;

/**
 * Инструментарий рефакторинга ресурса в контексте
 */
public interface ResourceRefactorer extends Refactorer {
  /**
   * Удаляет данный ресурс в контексте
   */
  void deleteThisResource();
  
  /**
   * Переименовывает данный ресурс в контексте
   * Если ресурс под новым контекстным именем существует, то к целевому имени добавится уникальный постфикс
   * (чтобы операция переименования состялась без замены существующего реурса)
   *  
   * @param newPathName новое контекстное имя, которое следует задать ресурсу
   */
  void renameThisResource(String newPathName);
}