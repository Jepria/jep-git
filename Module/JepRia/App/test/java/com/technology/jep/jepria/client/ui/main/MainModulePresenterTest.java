package com.technology.jep.jepria.client.ui.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashSet;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.technology.jep.jepria.client.async.LoadAsyncCallback;
import com.technology.jep.jepria.client.exception.ExceptionManager;
import com.technology.jep.jepria.client.history.place.MainPlaceController;
import com.technology.jep.jepria.client.message.JepMessageBox;
import com.technology.jep.jepria.client.ui.UiSecurity;
import com.technology.jep.jepria.client.ui.eventbus.EventFilter;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.log.JepLogger;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.text.JepRiaText;

/**
 * Исходный код данного класса содержит пример mock-тестирования клиентского кода.<br/>
 * Unit-тестирование в ряде случаев оказывается затруднено или вовсе невозможно
 * в связи с одним или несколькими из следующих обстоятельств:
 * <ul>
 *   <li>Тестируемые методы имеют &quot;побочные эффекты&quot;, т.е. изменяют состояние своего
 *   объекта. При этом интерфейс объекта не всегда позволяет убедиться в корректности выполненных
 *   изменений.</li>
 *   <li>Может возникнуть необходимость протестировать приватные методы. Менять уровень
 *   доступа с private на package private исключительно для нужд тестирования может быть
 *   нецелесообразно.</li>
 *   <li>В конфигурировании тестируемого класса могут принимать участие объекты сторонних классов,
 *   которые не представляется возможным модифицировать.</li>
 *   <li>Код может быть невозможно протестировать без специфических условий: характерный пример
 *   &mdash; клиентский код в GWT; вне браузера невозможно вызвать <code>GWT.create()</code>.</li>
 *   <li>Тестируемый код может активно использовать статические методы. Одним из выходов может,
 *   безусловно, быть модификация кода для повышения его тестируемости, однако это может потребовать
 *   архитектурных решений и сделать его менее понятным и т.д.</li>  
 * </ul>
 * <p>Снять ограничение позволяет mock-тестирование. Идея mock-тестирования заключается в том, что
 * реальные объекты подменяются фиктивными. Какие-то методы mock-объекта могут вести себя как
 * методы реального объекта, а какие-то &mdash; ничего не делать, либо выбрасывать исключение,
 * либо возвращать заданное извне значение в тех или иных условиях. Дополнительно возможно
 * посчитывать количество раз, которое вызывается метод, записывать значения переданных параметров.</p>
 * 
 * <p>Наиболее популярным фреймворком для mock-тестирования является <a href="http://mockito.org/" target="_blank">Mockito</a>,
 * который использует для формирования mock-объектов Reflection API. С последним связано существенное
 * ограничение Mockito: невозможность модифицировать поведение статических методов. Кроме того,
 * Mockito не позволяет модифицировать <code>final</code>-методы. Для снятия этих ограничений 
 * используется <a href="https://github.com/jayway/powermock" target="_blank">PowerMock</a>, работающий на уровне Classloader.
 * Использование PowerMock требует использования собственного runner'а, что обуславливает использование
 * аннотации <code>{@literal @}RunWith(PowerMockRunner.class)</code>. С помощью аннотации 
 * <code>{@literal @}PrepareForTest</code> перечисляются классы, поведение которых подлежит
 * модификации с помощью PowerMock.</p>
 * 
 * <p>В данном примере демонстрируется тестирование метода {@link MainModulePresenter#checkAccess(String)}.
 * Метод проверяет, доступен ли модуль с заданным идентификатором. Если модуль доступен, функция
 * должна возвращать <code>true</code>, в противном случае &mdash; <code>false</code>. Кроме того,
 * в последнем случае функция должна выводить сообщение об ошибке и скрывать индикатор загрузки
 * (побочные эффекты).</p>
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Document.class, GWT.class, JepClientUtil.class, MainModulePresenter.class})
public class MainModulePresenterTest {
  
  /**
   * Проверяет поведение метода {@link MainModulePresenter#checkAccess(String)}, если
   * модуль доступен.
   * @throws Exception
   */
  @Test // Каждый тестовый метод должен сопровождаться данной аннотацией, чтобы JUnit его вызвал.
  public void checkAccessTestAvailableModule() throws Exception {
    /*
     * Создадим фиктивную клиентскую фабрику. Поскольку в данном тесте сообщение
     * об ошибке не должно быть показано, нет необходимости заглушать JepMessageBox.
     */
    MainClientFactory<?, ?> cf = createMainClientFactoryMock(null);
    /*
     * Создадим презентер и с помощью PowerMockito.spy() "внедряемся" в него.
     * Существует два подхода: mock() создаёт фиктивный объект по переданному классу,
     * spy() позволяет внедриться в существующий объект для модификации его поведения
     * и слежения за ним. Отметим необходимость использование PowerMockito.spy() вместо
     * Mockito.spy() в данной ситуации, т.к. мы модифицируем поведение приватного
     * метода getAccessibleModules().
     */
    MainModulePresenter<?, ?, ?, ?> p = PowerMockito.spy(new MainModulePresenter(cf){});
    /*
     * Для упрощения процесса тестирования добьёмся того, чтобы getAccessibleModules()
     * возвращал заданный список модулей.
     */
    Set<String> modules = new HashSet<String>();
    modules.add("Module1");
    modules.add("Module2");
    modules.add("Module3");  
    PowerMockito.doReturn(modules).when(p, "getAccessibleModules");
    /*
     * Тестируемый метод checkAccess() является приватным. Для его вызова нужно либо
     * использовать Reflection API, либо обратиться к входящему в PowerMock классу
     * WhiteboxImpl.
     */
    boolean result = WhiteboxImpl.<Boolean>invokeMethod(p, "checkAccess", "Module1");
    /*
     * Предполагаем, что в данной ситуации метод checkAccess() вернёт true.
     */
    assertTrue(result);
  }
  
  /**
   * Проверяет поведение метода {@link MainModulePresenter#checkAccess(String)}, если
   * модуль недоступен.
   * @throws Exception
   */
  @Test
  public void checkAccessTestUnavailableModule() throws Exception {
    /*
     * Данный тест требует существенно большее количество заглушек.
     */
    mockDocumentGetBody();
    mockJepClientUtil();    
    mockJepTexts();      
    mockJepRiaClientConstant();

    final JepMessageBox messageBoxMock = Mockito.mock(JepMessageBox.class);
    Mockito.when(messageBoxMock.showError(Mockito.anyString())).thenReturn(null);    
    MainClientFactory<?, ?> cf = createMainClientFactoryMock(messageBoxMock);
    
    MainModulePresenter<?, ?, ?, ?> p = PowerMockito.spy(new MainModulePresenter(cf){});    
    Set<String> modules = new HashSet<String>();
    modules.add("Module1");
    modules.add("Module2");
    modules.add("Module3");  
    PowerMockito.doReturn(modules).when(p, "getAccessibleModules");
    
    boolean result = WhiteboxImpl.<Boolean>invokeMethod(p, "checkAccess", "Module4");
    // Проверим, что метод возвращает false.
    assertFalse(result);
    /*
     * Убедимся, что метод JepClientUtil.hideLoadingPanel(), скрывающий индикатор загрузки,
     * вызывается ровно 1 раз. Следует отметить характерный для PowerMock двухстрочный
     * синтаксис, используемый для статических методов.
     */
    PowerMockito.verifyStatic(Mockito.times(1));
    JepClientUtil.hideLoadingPanel();
    /*
     * Убедимся, что messageBox.showError() вызывается ровно один раз. Для простоты
     * не будем проверять переданный методу текст. Следует заметить, что этот вызов
     * не является статическим либо приватным, поэтому прибегать к PowerMock не требуется.
     */
    Mockito.verify(messageBoxMock, Mockito.times(1)).showError(Mockito.anyString());
  }

  /**
   * Создание mock для клиентской фабрики.<br/>
   * @param messageBoxMock фиктивный объект для вывода сообщений
   * @return фиктивная клиентская фабрика главного модуля
   */
  private static MainClientFactory<?, ?> createMainClientFactoryMock(final JepMessageBox messageBoxMock) {
    MainClientFactory<?, ?> cf = new MainClientFactory(){

      @Override
      public void getPlainClientFactory(String moduleId, LoadAsyncCallback callback) {        
      }

      @Override
      public EventBus getEventBus() {
        return null;
      }

      @Override
      public EventFilter getEventFilter() {
        return null;
      }

      @Override
      public UiSecurity getUiSecurity() {
        UiSecurity uiSecurityMock = Mockito.mock(UiSecurity.class);
        Mockito.when(uiSecurityMock.getFirstRequiredRole(Mockito.any())).thenReturn("TestRole");
        return uiSecurityMock;
      }

      @Override
      public JepLogger getLogger() {
        return null;
      }

      @Override
      public JepMessageBox getMessageBox() {
        return messageBoxMock;
      }

      @Override
      public JepRiaText getTexts() {
        return null;
      }

      @Override
      public ExceptionManager getExceptionManager() {
        return null;
      }

      @Override
      public MainPlaceController getPlaceController() {
        return null;
      }

      @Override
      public Place getDefaultPlace() {
        return null;
      }

      @Override
      public IsWidget getMainView() {
        return null;
      }

      @Override
      public JepMainServiceAsync getMainService() {
        return null;
      }

      @Override
      public MainModulePresenter createMainModulePresenter() {
        return null;
      }


      @Override
      public List<String> getModuleIds() {
        return null;
      }
    };
    return cf;
  }

  /**
   * Служебный метод, который необходимо вызвать перед тестированием класса
   * {@link JepClientUtil}, чтобы избежать проблем, вызываемых строкой
   * <code>public static final BodyElement BODY = Document.get().getBody();</code>
   */
  private static void mockDocumentGetBody() {
    /*
     * Включим возможность модифицировать поведение всех статических методов класса Document.
     * Это необходимо для модификации поведения статического метода get().
     */
    PowerMockito.mockStatic(Document.class);
    /*
     * Создадим mock-объект. Использование метода mock() из класса PowerMockito (не Mockito)
     * обусловлено тем, что необходимо переопределить native-метод getBody().
     */
    Document documentMock = PowerMockito.mock(Document.class);
    /*
     * Нам неважно, что именно вернёт getBody(), важно его заглушить. Достаточно потребовать,
     * чтобы он возвращал null.
     */
    PowerMockito.when(documentMock.getBody()).thenReturn(null);
    /*
     * Метод get() возвращает mock вместо реального объекта класса Document.
     * Поскольку модифицируется поведение статического метода, необходимо использовать
     * "обратный" синтаксис doReturn().when(). Кроме того, для указания на то, какой метод
     * "заглушается", его вызов записывается в коде следующей строкой (двустрочный синтаксис).
     */
    PowerMockito.doReturn(documentMock).when(Document.class);
    Document.get();
  }

  /**
   * Служебный метод, заглушаюший функции класса {@link JepClientUtil}.
   */
  private static void mockJepClientUtil() {
    // Включим возможность модифицировать статические методы класса JepClientUtil.
    PowerMockito.mockStatic(JepClientUtil.class);
    /*
     * При вызове hideLoadingPanel() ничего не должно происходить. При этом Powermock
     * ведёт учёт всех вызовов и позволяет подсчитать, сколько раз и с какими параметрами
     * метод был вызван.
     */
    PowerMockito.doNothing().when(JepClientUtil.class);
    JepClientUtil.hideLoadingPanel();
  }

  /**
   * Служебный метод, заглушающий {@link JepRiaText} и его инстанцирование.
   */
  private static void mockJepTexts() {
    /*
     * Объекты с интерфейсом JepRiaText создаются с помощью GWT.create().
     * Это возможно только на клиентской стороне, при тестировании же 
     * этот процесс необходимо заглушить с помощью mock.
     * 
     * Сначала создадим mock-объект JepRiaText. Для этого достаточно
     * стандартных средств Mockito, без привлечения Powermock.
     */
    JepRiaText jepTextsMock = Mockito.mock(JepRiaText.class);
    /*
     * Достаточно заглушить всего лишь один метод.
     */
    Mockito.when(jepTextsMock.field_blankText()).thenReturn("");
    // Будем заглушать статические методы класса GWT.
    PowerMockito.mockStatic(GWT.class);
    // При вызове GWT.create() будем возвращать созданный mock.
    PowerMockito.doReturn(jepTextsMock).when(GWT.class);
    GWT.create(JepRiaText.class);
  }

  /**
   * Служебный метод, заглушающий GWT-вызовы в JepRiaClientConstant.
   */
  private static void mockJepRiaClientConstant() {
    /*
     * Достаточно заглушить метод GWT.getPermutationStrongName(),
     * используемый в JepRiaClientConstant.
     */
    PowerMockito.doReturn("").when(GWT.class);
    GWT.getPermutationStrongName();
  }
}
