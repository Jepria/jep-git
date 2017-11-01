package org.jepria.validator.core.base;
import org.jepria.validator.core.base.resource.Directory;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.resource.XmlResource;
import org.jepria.validator.core.base.transform.Transformation;


/**
 * Интерфейс валидационного правила. 
 */
// package: прикладные правила должны наследоваться от класса ValidatorRule, а не от этого интерфейса.
/*package*/interface IValidatorRule {

  /**
   * Вызывается для валидации всех остальных ресурсов контекста,
   * которые не были валидированы ни одним из методов <code>forSomething</code>
   * 
   * @return {@code null} если трансформация ресурса по результатам валидации не требуется, если требуется &mdash; то объект трансформации
   */
  Transformation forPlainResource(PlainResource resource);
  
  /**
   * Вызывается для валидации ресурсов <code>.java</code>
   * 
   * @return {@code null} если трансформация ресурса по результатам валидации не требуется, если требуется &mdash; то объект трансформации
   */
  Transformation forJavaResource(JavaResource resource);
  
  /**
   * Вызывается для валидации ресурсов <code>.xml</code>
   * 
   * @return {@code null} если трансформация ресурса по результатам валидации не требуется, если требуется &mdash; то объект трансформации
   */
  Transformation forXmlResource(XmlResource resource);
  
  /**
   * Вызывается для валидации директорий
   * 
   * @return {@code null} если трансформация директории по результатам валидации не требуется, если требуется &mdash; то объект трансформации
   */
  Transformation forDirectory(Directory resource);
}
