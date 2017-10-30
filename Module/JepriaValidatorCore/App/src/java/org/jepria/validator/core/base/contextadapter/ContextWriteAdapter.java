package org.jepria.validator.core.base.contextadapter;

import java.io.OutputStream;

import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.resource.Resource;

/**
 * Интерфейс-адаптер записи ресурсов во внешнем контексте (интерфейс недоступен в прикладных валидационных правилах).
 * <br><br>
 * <i>
 * <b>Конвенция:</b> Реализации данного интерфейса вместе с реализацией {@link ContextReadAdapter} должны обеспечивать
 * <b>персистентность</b> дерева ресурсов в рамках валидации множества ресурсов одним валидационным правилом.
 * Это означает, что при валидации нескольких ресурсов одним правилом, доступный для чтения контекст в оном правиле 
 * должен быть одинаковым по составу независимо от валидируемого ресурса 
 * и от произведенных ранее этим же правилом трансформаций контекста.
 * Другими словами, коммит любых записей в контекст должен происходить после валидации всех ресурсов одним правилом.
 * </i>
 * 
 * @see {@link org.jepria.validator.core.base.transform.ContentRefactorer},
 * {@link org.jepria.validator.core.base.transform.ResourceRefactorer},
 * {@link org.jepria.validator.core.base.transform.ContextRefactorer} &mdash; смежные интерфейсы,
 * с помощью которых реализуются прикладные трансформации.
 * Методы смежных интерфейсов адаптируют данный интерфейс к разработчику прикладных трансформаций
 */
public interface ContextWriteAdapter {
  /**
   * Удаляет ресурс во внешнем конексте
   * 
   * @param resource ресурс, который требуется удалить
   * 
   * @see org.jepria.validator.core.base.transform.ResourceRefactorer#deleteThisResource() &mdash; смежный метод
   */
  void deleteResource(Resource resource);
  
  /**
   * Переименовывает ресурс во внешнем конексте
   * 
   * @param resource ресурс, который требуется переименовать
   * @param newPathName новое контекстное имя, которое следует задать ресурсу
   * 
   * @see org.jepria.validator.core.base.transform.ResourceRefactorer#renameThisResource(String) &mdash; смежный метод
   */
  void renameResource(Resource resource, String newPathName);
  
  /**
   * @param resource ресурс, рефакторинг которого выполняется
   * @return новый выходной поток во внешнем контексте для рефакторинга ресурса
   */
  OutputStream newRefactoringStream(PlainResource resource);
  
  /**
   * @see org.jepria.validator.core.base.transform.ContextRefactorer#writeNewResource(String) &mdash; смежный метод
   */
  OutputStream writeNewResource(String pathName);
  
  /**
   * @see org.jepria.validator.core.base.transform.ContextRefactorer#createNewDirectory(String) &mdash; смежный метод
   */
  void createNewDirectory(String pathName);
}