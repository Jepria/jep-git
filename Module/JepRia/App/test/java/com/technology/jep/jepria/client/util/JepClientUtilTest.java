package com.technology.jep.jepria.client.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Document.class, GWT.class, Window.Location.class})
public class JepClientUtilTest {

	@SuppressWarnings("serial")
	public class StubException extends RuntimeException{};

	@Rule
	public ExpectedException thrown = ExpectedException.none();
			
	@Test
	public void goToUrlTestRelativeUrlWithoutLeadingSlash(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(GWT.class, Window.Location.class);
		
		PowerMockito.doReturn("https://example.com/somepage/").when(GWT.class);
		GWT.getHostPageBaseURL();
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("https://example.com/somepage/myurl");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("myurl");
	}
	
	@Test
	public void goToUrlTestRelativeUrlWithLeadingSlash(){
		mockDocumentGetBody();		
		PowerMockito.mockStatic(GWT.class, Window.Location.class);
		
		PowerMockito.doReturn("https://example.com/somepage/").when(GWT.class);
		GWT.getHostPageBaseURL();
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("https://example.com/somepage/myurl");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("/myurl");
	}
	
	@Test
	public void goToUrlTestAbsoluteUrlWithoutProtocol(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(Window.Location.class);
		
		PowerMockito.doReturn("protocol:").when(Window.Location.class); 
		Window.Location.getProtocol();
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("protocol://myurl");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("//myurl");
	}
	
	@Test
	public void goToUrlTestAbsoluteUrlHttp(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(GWT.class, Window.Location.class);
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("http://example.com/somepage/");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("http://example.com/somepage/");
	}
	
	@Test
	public void goToUrlTestAbsoluteUrlHttps(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(GWT.class, Window.Location.class);
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("https://secure.example.com/somepage/");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("https://secure.example.com/somepage/");
	}
	
	@Test
	public void goToUrlTestAbsoluteUrlFtp(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(GWT.class, Window.Location.class);
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("ftp://ftp.example.com/");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("ftp://ftp.example.com/");
	}
	
	@Test
	public void goToUrlTestAbsoluteUrlEmail(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(GWT.class, Window.Location.class);
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("mailto:test@example.com");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("mailto:test@example.com");
	}
	
	@Test
	public void goToUrlTestCyrillicUrl(){
		mockDocumentGetBody();
		PowerMockito.mockStatic(Window.Location.class);
		
		PowerMockito.doReturn("protocol:").when(Window.Location.class); 
		Window.Location.getProtocol();
		
		PowerMockito.doThrow(new StubException()).when(Window.Location.class);
		Window.Location.assign("protocol://русскоязычный-урл.рф");
		
		thrown.expect(StubException.class);
		JepClientUtil.goToUrl("//русскоязычный-урл.рф");
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

}
