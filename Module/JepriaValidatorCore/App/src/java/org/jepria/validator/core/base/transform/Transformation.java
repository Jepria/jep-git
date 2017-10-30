package org.jepria.validator.core.base.transform;

/**
 * Класс, описывающий трансформацию (изменение) ресурса, 
 * производимую по результатам валидации конкретным валидационный правилом.
 * <br>
 * <br>
 * Трансформация может состоять из нескольких действий, описываемых классом {@link Action}.
 * Действия разделяются на следующие типы (действию каждого типа соответствует свой класс {@link Refactorer},
 * предоставляющий собственно инструменты для рефакторинга):
 * <ul>
 * <li>Рефакторинг <b>содержимого</b> ресурса &mdash; посредством {@link ContentRefactorer}</li>
 * <li>Рефакторинг <b>ресурса</b> в контексте: удаление, переименование &mdash; посредством {@link ResourceRefactorer}</li>
 * <li>Рефакторинг <b>контекста</b>, не связанного с ресурсом: создание новых ресурсов, операции над директориями 
 * &mdash; посредством {@link ContextRefactorer}</li>
 * </ul>
 * Для построения трансформации из определенных действий используется внутренний класс {@link Configurator}:
 * <pre>
 * // Определение действий, из которых состоит трансформация 
 * Transformation.Action&lt;ContentRefactorer&gt; contentRefactoringAction = new ... ;
 * Transformation.Action&lt;ResourceRefactorer&gt; resourceRefactoringAction = new ... ;
 * Transformation.Action&lt;ContextRefactorer&gt; contextRefactoringAction = new ... ;
 * 
 * // Создание трансформации с изменением только содержимого ресурса
 * Transformation contentRefactoring = Transformation.of().content(contentRefactoringAction);
 * 
 * // Создание трансформации с изменением ресурса в контексте и самого контекста
 * Transformation resourceAndContextRefactoring = Transformation
 *     .of().resource(resourceRefactoringAction)
 *     .andOf().context(contextRefactoringAction);
 *     
 * // Создание трансформации с изменением содержимого, ресурса в контексте и самого контекста
 * Transformation fullRefactoring = Transformation
 *     .of().content(contentRefactoringAction)
 *     .andOf().resource(resourceRefactoringAction)
 *     .andOf().context(contextRefactoringAction);
 * </pre>
 * Трансформация ресурса может выполнять только <b>одно</b> действие каждого типа:
 * <pre>
 * // Определение действий, из которых состоит трансформация
 * Transformation.Action&lt;ContentRefactorer&gt; contentRefactoringAction1 = new ... ;
 * Transformation.Action&lt;ContentRefactorer&gt; contentRefactoringAction2 = new ... ;
 * Transformation.Action&lt;ContentRefactorer&gt; contentRefactoringAction3 = new ... ;
 * 
 * // Созданная трансформация будет выполнять только последнее заданное действие одного типа,
 * // в данном случае это contentRefactoringAction3
 * Transformation multiRefactoring = Transformation
 *     .of().content(contentRefactoringAction1)
 *     .andOf().content(contentRefactoringAction2)
 *     .andOf().content(contentRefactoringAction3);
 * </pre>
 * 
 * 
 * 
 */
public class Transformation {
  
  /**
   * Интерфейс-команда, описывающая действие при трансформации ресурса
   *
   * @param <R> конкретный тип рефакторера, определяющий тип действия
   */
  public interface Action<R extends Refactorer> {
    /**
     * Реализация действия
     * @param refactorer рефакторер
     */
    void perform(R refactorer);
  }
  
  // private
  private Transformation() {}
  
  private Action<ContentRefactorer> contentRefactorer;
  private Action<ResourceRefactorer> resourceRefactorer;
  private Action<ContextRefactorer> contextRefactorer;
  
  public Action<ContentRefactorer> getContentRefactorer() {
    return contentRefactorer;
  }

  public Action<ResourceRefactorer> getResourceRefactorer() {
    return resourceRefactorer;
  }

  public Action<ContextRefactorer> getContextRefactorer() {
    return contextRefactorer;
  }


  
  
  // private
  private Configurator configurator;
  
  /**
   * Начинает конфигурировать новую трансформацию
   * @return новый экземпляр конфигуратора
   */
  public static Configurator of() {
    Transformation transformation = new Transformation();
    transformation.configurator = transformation.new Configurator();
    return transformation.configurator;
  }
  
  /**
   * Добавляет действие к конфигурируемой трансформации
   * @return тот же экземпляр конфигуратора
   */
  public Configurator andOf() {
    return configurator;
  }
  
  /**
   * Класс-конфигуратор действий трансформации
   */
  // public, not static
  public class Configurator {
    /**
     * Включает в конфигурируемую трансформацию ресурса действие по изменению содержимого этого ресурса
     * @param contentRefactorer описание действия по изменению содержимого ресурса
     * @return сконфигурированная трансформация
     */
    public Transformation content(Action<ContentRefactorer> contentRefactorer) {
      Transformation.this.contentRefactorer = contentRefactorer;
      return Transformation.this;
    }
    
    /**
     * Включает в конфигурируемую трансформацию ресурса действие по изменению этого ресурса в контексте
     * @param resourceRefactorer описание действия по изменению ресурса в контексте
     * @return сконфигурированный рефакторинг
     */
    public Transformation resource(Action<ResourceRefactorer> resourceRefactorer) {
      Transformation.this.resourceRefactorer = resourceRefactorer;
      return Transformation.this;
    }
    
    /**
     * Включает в конфигурируемую трансформацию ресурса действие по изменению контекста
     * @param contextRefactorer описание действия по изменению контекста
     * @return сконфигурированный рефакторинг
     */
    public Transformation context(Action<ContextRefactorer> contextRefactorer) {
      Transformation.this.contextRefactorer = contextRefactorer;
      return Transformation.this;
    }
  }
  
}
