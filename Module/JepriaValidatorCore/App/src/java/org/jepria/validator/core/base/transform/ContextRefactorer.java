package org.jepria.validator.core.base.transform;

import java.io.OutputStream;

/**
 * Инструментарий рефакторинга контекста
 */
public interface ContextRefactorer extends Refactorer {
  /**
   * Создаёт <b>новый</b> ресурс с заданным относительным путём, вместе с необходимыми промежуточными директориями.
   * <br>
   * @param pathName относительный контекстный путь
   * @return поток для записи в созданный ресурс
   * 
   * @throws org.jepria.validator.core.base.exception.ResourceAlreadyExistsException если такой ресурс уже существует
   */
  OutputStream writeNewResource(String pathName);
  
  
  /**
   * Создаёт <b>новую</b> директорию с заданным относительным путём, вместе с необходимыми промежуточными директориями.
   * <br><br>
   * Вызывать метод целесообразно только для создания директории, которая останется пустой,
   * поскольку методы {@link #writeNewResource} и {@link ResourceRefactorer#renameThisResource}
   * создают необходимые им промежуточные директории
   * <br>
   * 
   * @param pathName относительный контекстный путь
   * 
   * @throws org.jepria.validator.core.base.exception.ResourceDoesNotExistException если такая директория уже существует
   * <br><br>
   * <i>
   * Если директории не существует, но существует одноименный ресурс с тем же расположением, то новая директория создаётся рядом с ресурсом
   * </i>
   */
  void createNewDirectory(String pathName);
}