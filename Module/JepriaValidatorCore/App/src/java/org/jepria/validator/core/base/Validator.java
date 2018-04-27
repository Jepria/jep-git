package org.jepria.validator.core.base;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.validator.core.base.contextadapter.ContextReadAdapter;
import org.jepria.validator.core.base.contextadapter.ContextWriteAdapter;
import org.jepria.validator.core.base.exception.TransformationException;
import org.jepria.validator.core.base.resource.Directory;
import org.jepria.validator.core.base.resource.JavaResource;
import org.jepria.validator.core.base.resource.PlainResource;
import org.jepria.validator.core.base.resource.Resource;
import org.jepria.validator.core.base.resource.XmlResource;
import org.jepria.validator.core.base.transform.ContentRefactorer;
import org.jepria.validator.core.base.transform.ContextRefactorer;
import org.jepria.validator.core.base.transform.ResourceRefactorer;
import org.jepria.validator.core.base.transform.Transformation;
import org.jepria.validator.core.base.transform.Transformation.Action;

/**
 * Базовый класс для запуска валидации
 */
public class Validator {

  private ContextReadAdapter contextReadAdapter;
  
  private ContextWriteAdapter contextWriteAdapter;
  
  public ContextReadAdapter getContextReadAdapter() {
    return contextReadAdapter;
  }

  /**
   * Предоставляет валидатору адаптер для чтения контекста.
   * Предоставленный адаптер доступен в прикладных валидационных правилах ({@link ValidatorRule#getContextRead()})
   */
  public void setContextReadAdapter(ContextReadAdapter contextReadAdapter) {
    this.contextReadAdapter = contextReadAdapter;
  }

  public ContextWriteAdapter getContextRefactorAdapter() {
    return contextWriteAdapter;
  }

  /**
   * Предоставляет валидатору адаптер для записи в контекст.
   * Предоставленный адаптер недоступен в прикладных валидационных правилах
   */
  public void setContextWriteAdapter(ContextWriteAdapter contextRefactorAdapter) {
    this.contextWriteAdapter = contextRefactorAdapter;
  }
  
  /**
   * Метод, запускающий в контексте (во множестве ресурсов) валидацию подмножества ресурсов, набором валидационных правил.
   * Последовательно валидирует набор ресурсов на каждом валидационном правиле.
   * 
   * @param rules набор правил. Если {@code null} или список пуст, метод завершается. Валидация проходит по порядку правил, определенному данным списком
   * @param resourcesForValidation множество ресурсов для валидации. Если {@code null} или список пуст, метод завершается
   * @param validationMessageCollector сборщик сообщений при валидации (или {@code null})
   * @param progressCallback обратная связь для сообщения о прогрессе валидации, или null
   * 
   * Если {@code null}, то подставляется сборщик по умолчанию {@link MessageCollector#DUMMY}
   * <br><br>
   * <i>
   * Трансформация ресурсов невозможна при запуске валидации на множестве правил. Для трансформации см. {@link #validateUnderRule}
   * </i> 
   */
  public void validateInContext(
      List<ValidatorRule> rules,
      List<Resource> resourcesForValidation, 
      MessageCollector validationMessageCollector,
      ProgressCallback progressCallback) {
    
    if (rules == null || rules.isEmpty() || 
        resourcesForValidation == null || resourcesForValidation.isEmpty()) {
      return;
    }
    
    if (validationMessageCollector == null) {
      validationMessageCollector = MessageCollector.DUMMY;
    }
    
    for (ValidatorRule rule: rules) {
      validateUnderRule(rule, resourcesForValidation, validationMessageCollector, false, null, progressCallback);
    }
  }
  
  /**
   * Обработчик валидационных сообщений, перенаправляющий их в {@link MessageCollector}
   */
  private static class MessageHandlerToCollector implements MessageHandler {

    private final MessageCollector collector;
    private final ValidatorRule rule;
    
    /**
     * @param collector сборщик сообщений, в который они перенаправлются 
     * @param rule валидатор, в котором используется данный обработчик
     */
    public MessageHandlerToCollector(MessageCollector collector, ValidatorRule rule) {
      this.collector = collector;
      this.rule = rule;
    }
    
    private Resource resource;
    
    /**
     * Задаёт обработчику текущий валидируемый ресурс
     */
    public void setResource(Resource resource) {
      this.resource = resource;
    }

    @Override
    public void handleMessage(Message message) {
      if (collector != null) {
        collector.collect(rule, resource, message);
      }
    }

    @Override
    public void handleThrowable(Throwable e) {
      if (collector != null) {
        collector.collect(rule, resource, e);
      }
    }
    
  }
  
  /**
   * Класс, реализующий обратную связь для сообщения о прогрессе.
   */
  public static abstract class ProgressCallback {
    
    /**
     * Обратный вызов, сообщает об очередном завершенном этапе прогресса
     * @param completed количество завершенных этапов
     * @param total общее количество этапов
     */
    protected abstract void onProgress(int completed, int total);
    
    protected void onInitialized() {};
    
    public void initialize() {
      onInitialized();
    }
    
    private int total;
    
    /**
     * Выставляет общее количество этапов прогресса
     * @param total
     */
    public void setTotal(int total) {
      this.total = total;
    }
    
    private int completed = 0;
    
    /**
     * Сообщает об очередном завершенном этапе прогресса
     */
    public void onNextCompleted() {
      completed++;
      onProgress(completed, total);
    };
  }
  
  /**
   * Последовательно валидирует набор ресурсов по валидационному правилу
   * 
   * @param rule валидационное правило. Если {@code null}, метод завершается
   * @param resourcesForValidation множество ресурсов для валидации. Если {@code null} или список пуст, метод завершается. 
   * @param validationMessageCollector сборщик сообщений при валидации (или {@code null})
   * @param transform {@code true} если требуется рефакторинг переданных ресурсов, {@code false} если только валидация
   * @param transformMessageCollector сборщик сообщений при рефакторинге (или {@code null}). Используется только если {@code transform true}
   * @param progressCallback
   * <br><br>
   * <i>
   * <b>Конвенция:</b> Ресурсы из множества должны валидироваться одним и тем же правилом независимо друг от друга,
   * поэтому нельзя основывать реализацию валидационного правила на порядок ресурсов в списке.
   * </i>
   */
  public void validateUnderRule(
      ValidatorRule rule,
      List<Resource> resourcesForValidation, 
      MessageCollector validationMessageCollector,
      boolean transform,
      MessageCollector transformMessageCollector,
      ProgressCallback progressCallback) {
    
    if (rule == null || 
        resourcesForValidation == null || resourcesForValidation.isEmpty()) {
      return;
    }
    
    if (validationMessageCollector == null) {
      validationMessageCollector = MessageCollector.DUMMY;
    }
    
    if (transformMessageCollector == null) {
      transformMessageCollector = MessageCollector.DUMMY;
    }
    
    final MessageHandlerToCollector validationMessageHandlerToCollector = new MessageHandlerToCollector(validationMessageCollector, rule);
    final MessageHandlerToCollector transformMessageHandlerToCollector = new MessageHandlerToCollector(transformMessageCollector, rule);
    
    rule.setContextReadAdapter(contextReadAdapter);

    for (Resource resource: resourcesForValidation) {
      try {
        validationMessageHandlerToCollector.setResource(resource);
        rule.setMessageHandler(validationMessageHandlerToCollector);
        
        Transformation transformation = validate(rule, resource);
        
        if (transform && transformation != null) {
          
          // произведём рефакторинг содержимого ресурса, если нужно
          Action<ContentRefactorer> contentRefactoringAction = transformation.getContentRefactorer();
          if (contentRefactoringAction != null) {
          
            if (resource instanceof PlainResource) {
              PlainResource resourceWithContent = (PlainResource)resource;
              
              transformMessageHandlerToCollector.setResource(resource);
              rule.setMessageHandler(transformMessageHandlerToCollector);
              
              
              // В случае если реально рефакторится java-файл, проверим его на JSNI методы (обход бага JavaPasrer)
              // TODO удалить эту строку когда баг пофиксят
              if (resource.getName().endsWith(".java")) {checkJsniMethods(resourceWithContent);}
              
              
              DisposableContentRefactoringHelper contentRefactoringHelper = new DisposableContentRefactoringHelper(resourceWithContent);
              contentRefactoringAction.perform(contentRefactoringHelper);
              contentRefactoringHelper.dispose();
            } else {
              throw new TransformationException("Content refactoring is not applicable for this type of resource");
            }
          }
          
          // произведём рефакторинг ресурса, если нужно
          Action<ResourceRefactorer> resourceRefactoringAction = transformation.getResourceRefactorer();
          if (resourceRefactoringAction != null) {
          
            transformMessageHandlerToCollector.setResource(resource);
            rule.setMessageHandler(transformMessageHandlerToCollector);
            
            DisposableResourceRefactorer resourceRefactoringHelper = new DisposableResourceRefactorer(resource);
            resourceRefactoringAction.perform(resourceRefactoringHelper);
            resourceRefactoringHelper.dispose();
          }
          
          // произведём рефакторинг контекста, если нужно
          Action<ContextRefactorer> contextRefactoringAction = transformation.getContextRefactorer();
          if (contextRefactoringAction != null) {
          
            transformMessageHandlerToCollector.setResource(resource);
            rule.setMessageHandler(transformMessageHandlerToCollector);
            
            DisposableContextRefactorer contextRefactoringHelper = new DisposableContextRefactorer();
            contextRefactoringAction.perform(contextRefactoringHelper);
            contextRefactoringHelper.dispose();
          }
        }
      } catch (Throwable e) {// catch everything
        validationMessageHandlerToCollector.handleThrowable(e);
        if (transform) {// в режиме рефакторинга дублируем исключение в сборщик рефакторинга
          transformMessageHandlerToCollector.handleThrowable(e);
        }
      } finally {
        rule.setMessageHandler(null);
      }
      
      if (progressCallback != null) {
        progressCallback.onNextCompleted();
      }
    }
  }
  
  /**
   * Обход бага: JavaParser удаляет имплементации JSNI-методов, заключенных в последовательности / * - { ... } - * /
   * Данный метод сканирует файл в поисках таких имплементаций и выводит предупреждение в System.out.
   * 
   * @deprecated remove the method with all use cases when the bug gets fixed. 
   */
  @Deprecated
  private static void checkJsniMethods(PlainResource javaResource) {
    Pattern pattern = Pattern.compile("/\\*\\-\\{");
    try (Scanner sc = new Scanner(javaResource.newInputStream(), "UTF-8")) {
      int lineNum = 0;
      while (sc.hasNextLine()) {
        lineNum++;
        String line = sc.nextLine();
        Matcher m = pattern.matcher(line);
        int col = 0;
        while(m.find(col)) {
          col = m.start() + 1;
          System.out.println("/WARNING: Found JSNI method implementation at " + javaResource.getPathName() + ":" + lineNum + ":" + col + ". "
              + "This will be erased by JavaParser (because it is not supported), restore manually after validation.");
        }
      }
    }
  }
  
  // internal class
  private class Disposable {
    /**
     * Признак непригодности для использования объекта класса
     */
    private boolean disposed = false;
    
    /**
     * Пометить объект как непригодный для дальнейшего использования
     */
    public void dispose() {
      disposed = true;
    }
    
    /**
     * @throws IllegalStateException если объект был помечен как непригодный для дальнейшего использования
     */
    // protected
    protected void throwIfDisposed() {
      if (disposed) {
        throw new IllegalStateException("The method is invoked on an object that has already been disposed");
      }
    }
  }
  
  // internal class
  private class DisposableContentRefactoringHelper extends Disposable implements ContentRefactorer {
    
    private final PlainResource resource;
    
    public DisposableContentRefactoringHelper(PlainResource resource) {
      this.resource = resource;
    }
    
    /**
     * Открытый поток записи в отрефакторенный файл, для последующего закрытия
     */
    private OutputStream refactoringStream = null;
    
    @Override
    public OutputStream getRefactoringStream() {
      throwIfDisposed();
      if (refactoringStream == null) {
        refactoringStream = contextWriteAdapter.newRefactoringStream(resource);
      }
      return refactoringStream;
    }
    
    @Override
    public void dispose() {
      super.dispose();
      
      // Закроем открытый поток
      try {
        if (refactoringStream != null) {
          refactoringStream.close();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  // internal class
  private class DisposableResourceRefactorer extends Disposable implements ResourceRefactorer {
    
    private final Resource resource;
    
    public DisposableResourceRefactorer(Resource resource) {
      this.resource = resource;
    } 
    
    @Override
    public void deleteThisResource() {
      throwIfDisposed();
      contextWriteAdapter.deleteResource(resource);
    }
    @Override
    public void renameThisResource(String newPathName) {
      throwIfDisposed();
      contextWriteAdapter.renameResource(resource, newPathName);
    }
  }
  
  // internal class
  private class DisposableContextRefactorer extends Disposable implements ContextRefactorer {
    
    /**
     * Список открытых потоков записи в созданные ресурсы, для последующего закрытия
     */
    private final List<OutputStream> newResourceStreams = new ArrayList<>();
    
    @Override
    public OutputStream writeNewResource(String pathName) {
      throwIfDisposed();
      OutputStream newResourceStream = contextWriteAdapter.writeNewResource(pathName);
      newResourceStreams.add(newResourceStream);
      return newResourceStream;
    }
    
    @Override
    public void createNewDirectory(String pathName) {
      throwIfDisposed();
      contextWriteAdapter.createNewDirectory(pathName);
    }
    
    @Override
    public void dispose() {
      super.dispose();
      
      // Закроем открытые потоки
      try {
        for (OutputStream newResourceStream: newResourceStreams) {
          if (newResourceStream != null) {
            newResourceStream.close();
          }
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  /**
   * В зависимости от ресурса, валидирует его подходящим методом интерфейса {@link IValidatorRule}
   * @param rule
   * @param resource
   * @return
   * 
   * @throws IllegalArgumentException если resource {@code null} или неопознанного типа
   */
  protected Transformation validate(IValidatorRule rule, Resource resource) {
    Transformation transformation = null;
    
    if (resource instanceof PlainResource) {
      PlainResource plainResource = (PlainResource)resource;
      
      if (resource.getName().toLowerCase().endsWith(".java")) {
        transformation = rule.forJavaResource(new JavaResource(plainResource));
        
      } else if (resource.getName().toLowerCase().endsWith(".xml")) {
        transformation = rule.forXmlResource(new XmlResource(plainResource));
        
      } else {
        transformation = rule.forPlainResource(plainResource);
      }
    } else if (resource instanceof Directory) {
      Directory directory = (Directory)resource;
      
      transformation = rule.forDirectory(directory);
      
    } else {
      if (resource == null) {
        throw new IllegalArgumentException("The resource must not be null");
      } else {
        throw new IllegalArgumentException("Unknown resource type: " + resource.getClass().getCanonicalName());
      }
    }
    
    return transformation;
  }
}
