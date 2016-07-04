package com.technology.jep.jepria.client.ui.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

import com.google.gwt.activity.shared.Activity;
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
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.log.JepLogger;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.text.JepRiaText;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Document.class, GWT.class, JepClientUtil.class, MainModulePresenter.class})
public class MainModulePresenterTest {
	
	@Test
	public void checkAccessTestAvailableModule() throws Exception {
		MainClientFactory<?, ?> cf = createMainClientFactoryMock(null);		
		MainModulePresenter<?, ?, ?, ?> p = PowerMockito.spy(new MainModulePresenter(cf){});
		Set<String> modules = new HashSet<String>();
		modules.add("Module1");
		modules.add("Module2");
		modules.add("Module3");	
		PowerMockito.doReturn(modules).when(p, "getAccessibleModules");
		boolean result = WhiteboxImpl.<Boolean>invokeMethod(p, "checkAccess", "Module1");
		assertTrue(result);
	}
	
	@Test
	public void checkAccessTestUnavailableModule() throws Exception {
		mockDocumentGetBody();
		mockJepClientUtil();		
		mockJepTexts();	

		final JepMessageBox messageBoxMock = Mockito.mock(JepMessageBox.class);
		Mockito.when(messageBoxMock.showError(Mockito.anyString())).thenReturn(null);		
		MainClientFactory<?, ?> cf = createMainClientFactoryMock(messageBoxMock);
		
		MainModulePresenter<?, ?, ?, ?> p = PowerMockito.spy(new MainModulePresenter(cf){});		
		Set<String> modules = new HashSet<String>();
		modules.add("Module1");
		modules.add("Module2");
		modules.add("Module3");	
		PowerMockito.doReturn(modules).when(p, "getAccessibleModules");
		
		PowerMockito.doReturn("").when(GWT.class);
		GWT.getPermutationStrongName();
		
		boolean result = WhiteboxImpl.<Boolean>invokeMethod(p, "checkAccess", "Module4");
		assertFalse(result);
		PowerMockito.verifyStatic(Mockito.times(1));
		JepClientUtil.hideLoadingPanel();
		Mockito.verify(messageBoxMock, Mockito.times(1)).showError(Mockito.anyString());
	}

	/**
	 * Создание mock для клиентской фабрики.<br/>
	 * @param messageBoxMock
	 * @return
	 */
	private static MainClientFactory<?, ?> createMainClientFactoryMock(final JepMessageBox messageBoxMock) {
		MainClientFactory<?, ?> cf = new MainClientFactory(){
			@Override
			public Activity createMainModulePresenter() {
				return null;
			}

			@Override
			public void getPlainClientFactory(String moduleId,
					LoadAsyncCallback callback) {				
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
			public void setModuleIds(String[] moduleIds) {				
			}

			@Override
			public String[] getModuleIds() {
				return null;
			}

			@Override
			public boolean contains(String moduleId) {
				return false;
			}

			@Override
			public String[] getModuleItemTitles() {
				return null;
			}};
		return cf;
	}

	/**
	 * Служебный метод, который необходимо вызвать перед тестированием класса
	 * {@link JepClientUtil}, чтобы избежать проблем, вызываемых строкой
	 * <code>public static final BodyElement BODY = Document.get().getBody();</code>
	 */
	private static void mockDocumentGetBody() {
		PowerMockito.mockStatic(Document.class);
		Document documentMock = PowerMockito.mock(Document.class);
		PowerMockito.when(documentMock.getBody()).thenReturn(null);
		PowerMockito.doReturn(documentMock).when(Document.class);
		Document.get();
	}

	private static void mockJepClientUtil() {
		PowerMockito.mockStatic(JepClientUtil.class);
		PowerMockito.doNothing().when(JepClientUtil.class);
		JepClientUtil.hideLoadingPanel();
	}

	private static void mockJepTexts() {
		JepRiaText jepTextsMock = Mockito.mock(JepRiaText.class);
		Mockito.when(jepTextsMock.field_blankText()).thenReturn("");		
		PowerMockito.mockStatic(GWT.class);
		PowerMockito.doReturn(jepTextsMock).when(GWT.class);
		GWT.create(JepRiaText.class);
	}
}
