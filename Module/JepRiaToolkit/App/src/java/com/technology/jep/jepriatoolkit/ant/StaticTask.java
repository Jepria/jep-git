package com.technology.jep.jepriatoolkit.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;

/**
 * Ant-таск, который вызывается <b>статически</b> в сценарии <code>build.xml</code>,
 * и выполнение которого гарантируется единственным в рамках одного запуска ant.
 * <br><br>
 * Пример статического вызова таска в <code>build.xml</code>:
 * <pre>
&lt;project&gt;
  ...
  &lt;taskdef name="static-task" classname="org.jepria.toolkit.StaticTaskImpl"&gt;
    &lt;classpath&gt;
      &lt;pathelement location="${JEPRIA-TOOLKIT_LIB}"/&gt;
    &lt;/classpath&gt;
  &lt;/taskdef&gt;
  &lt;static-task/&gt;
  ...
&lt;/project&gt;
</pre>
 *
 */
public abstract class StaticTask extends Task {

  private String executionProperty;
  
  protected StaticTask () {
    super();
  }
  
  @Override
  public final void execute() throws BuildException {
    this.executionProperty = "executed-" + getClass().getName() + "-" + getExecutionId();
    if (!isExecuted()) {
      executeStatic();
      setExecuted();
    }
  }

  /**
   * Определение уникального ID выполнения таска.
   * Гарантируется единственность выполнения таска, вызванного статически, по конкретному ID.
   * Имеет смысл возвращать значения некоторых атрибутов, передаваемых при вызове <code>taskdef</code> в <code>build.xml</code>.
   * <br><br>
   * Пример: пусть таск <code>org.jepria.toolkit.StaticEchoTask</code> имеет атрибут <code>message</code>
   * <pre>
&lt;project&gt;
  ...
  &lt;taskdef name="static-echo-task" classname="org.jepria.toolkit.StaticEchoTask"&gt;
    &lt;classpath&gt;
      &lt;pathelement location="${JEPRIA-TOOLKIT_LIB}"/&gt;
    &lt;/classpath&gt;
  &lt;/taskdef&gt;
  &lt;static-echo-task message="hello"/&gt;
  &lt;static-echo-task message="world"/&gt;
  &lt;static-echo-task message="world"/&gt;
  ...
&lt;/project&gt;
</pre>
   * 
   * В случае если <code>getExecutionId()</code> возвращает значение атрибута <code>message</code>,
   * вызов произойдет два раза (<code>hello</code> и <code>world</code>).
   * В случае если <code>getExecutionId()</code> возвращает константу,
   * вызов произойдет один раз (только <code>hello</code>).
   * В случае если <code>getExecutionId()</code> возвращает всегда разное значение,
   * произойдут все три вызова.
   */
  protected abstract String getExecutionId();
  
  /**
   * Реализация тела таска, аналог {@link Task#execute()} 
   * @throws BuildException
   */
  protected abstract void executeStatic() throws BuildException;
  
  /**
   * Проверяет свойство-флаг, отвечающее за однократное выполнение таска
   * @return true если таск с конкретным <code>executionId</code> уже запускался в рамках вызова ant, иначе false
   */
  public boolean isExecuted() {
    return PropertyHelper.getPropertyHelper(getProject()).getProperty(executionProperty) != null;
  }
  
  /**
   * Устанавливает свойство-флаг, отвечающее за однократное выполнение таска
   * @return false если таск вызывается из ant впервые, иначе true
   */
  private void setExecuted() {
    PropertyHelper.getPropertyHelper(getProject()).setProperty(executionProperty, "true", true);
  }
}
