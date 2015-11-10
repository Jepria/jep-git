package com.technology.jep.jepriashowcase.custom.client.ui.plain;

import static com.technology.jep.jepria.client.util.JepClientUtil.goToUrl;
import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_NAME_FIELD_NAME;
import static com.technology.jep.jepria.shared.JepRiaConstant.JEP_USER_ROLES_FIELD_NAME;
import static com.technology.jep.jepria.shared.field.JepFieldNames.OPERATOR_ID;
import static com.technology.jep.jepriashowcase.custom.client.CustomClientConstant.customText;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.OAS_SSO_MODULE_URL;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.URL_FULL_SCREEN;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.URL_SEARCH_MODULE;
import static com.technology.jep.jepriashowcase.main.client.JepRiaShowcaseClientConstant.WL_SSO_MODULE_URL;
import static com.technology.jep.jepriashowcase.main.shared.JepRiaShowcaseConstant.URL_EMBEDDED;

import java.util.Date;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.technology.jep.jepria.client.async.JepAsyncCallback;
import com.technology.jep.jepria.client.history.scope.JepScopeStack;
import com.technology.jep.jepria.client.message.ErrorDialog;
import com.technology.jep.jepria.client.security.ClientSecurity;
import com.technology.jep.jepria.client.ui.eventbus.plain.PlainEventBus;
import com.technology.jep.jepria.client.ui.plain.PlainClientFactory;
import com.technology.jep.jepria.client.ui.plain.PlainModulePresenter;
import com.technology.jep.jepria.client.util.JepClientUtil;
import com.technology.jep.jepria.shared.dto.JepDto;
import com.technology.jep.jepria.shared.service.JepMainServiceAsync;
import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepriashowcase.custom.shared.service.CustomServiceAsync;
import com.technology.jep.jepriashowcase.main.shared.JepRiaShowcaseConstant;

public class CustomModulePresenter<V extends CustomModuleView, E extends PlainEventBus, S extends CustomServiceAsync, 
		F extends PlainClientFactory<E, S>> 
	extends PlainModulePresenter<V, E, S, F> {
	
	private JepMainServiceAsync mainService = clientFactory.getMainClientFactory().getMainService();
	
	public CustomModulePresenter(String moduleId, Place place, F clientFactory) {
		super(moduleId, place, clientFactory);
	}
	
	protected void bind() {
		super.bind();
				
		view.addFullScreenButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goToUrl(URL_FULL_SCREEN);
			}
		});
		
		view.addEmbeddedButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goToUrl(URL_EMBEDDED);
			}
		});
		
		view.addSearchButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				goToUrl(URL_SEARCH_MODULE);
			}
		});
		
		view.addErrorButtonClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ErrorDialog errorDialog = new ErrorDialog(customText.custom_errorDialog_title(), new Throwable(customText.custom_errorDialog_throwableMessage()), 
						customText.custom_errorDialog_message());
				errorDialog.show();
			}
		});
		
		view.addCurrentUserClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mainService.getUserData(new JepAsyncCallback<JepDto>() {
					@Override
					public void onSuccess(JepDto result) {
						Integer currentOperatorId = (Integer) result.get(OPERATOR_ID);
						service.getOperatorName(currentOperatorId, new JepAsyncCallback<String>() {
							public void onSuccess(String operatorName) {
								messageBox.alert(customText.custom_currentUser() + " : " + operatorName);
							}
						});
					}
				});
			}
		});
		
		view.addAuthorizationButtonClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final Credential userCredential = view.getUserCredential();
				view.showOrClearError(null);
				if (userCredential == null){
					view.showOrClearError(customText.custom_authorizationError_loginOrPasswordEmpty());
				}
				else {
					view.showLoadingIndicator();
					//осуществляем запрос авторизации к SSO-модулю сервера Oracle AS
					sendRequest(
						JepClientUtil.substitute(OAS_SSO_MODULE_URL, userCredential.getLogin(), userCredential.getPassword()) // url
						, RequestBuilder.POST // method
						, new RequestCallback() {
							@Override
							public void onResponseReceived(Request request, Response response) {
								if (Response.SC_OK == response.getStatusCode()){
									boolean isSuccessfulAuthorized = JepRiaShowcaseConstant.SUCCESS.equalsIgnoreCase(response.getText().trim());
									
									if (isSuccessfulAuthorized){
										//после успешного логона также осуществляем запрос авторизации к SSO-модулю сервера WebLogic 
										//TODO: от 27.03.2015 ожидается подключение к WL (находится в разработке)
										sendRequest(
											JepClientUtil.substitute(WL_SSO_MODULE_URL, userCredential.getLogin(), userCredential.getPassword())
											, RequestBuilder.GET
											, new RequestCallback() {
												@Override
												public void onResponseReceived(Request request, Response response) {
													String cookietoSet = response.getHeader("Set-Cookie");
													if(cookietoSet != null) {
											    	    String domain = ""; // По умолчанию, устанавливаем для текущего домена
											    	    String path = "/"; // и для всех модулей
											    	    Integer expires = 7200; // срок действия cookie (в годах)
											    	    boolean secure = false; // признак безопасности cookie											    	    
											    	    
											    	    expires = expires * 1000 * 60 * 60 * 24;
											    		Date expireDate = new Date( new Date().getTime() + expires );
											    	    
											    	    Cookies.setCookie(cookietoSet.split("=")[0], cookietoSet.split("=")[1], expireDate, domain, path, secure);
													}
												}
												@Override
												public void onError(Request request, Throwable exception) {
													view.hideLoadingIndicator();
													view.showOrClearError(exception.getMessage());
												}
											}
										);
										
										mainService.getUserData(new JepAsyncCallback<JepDto>(){
											@Override
											@SuppressWarnings("unchecked")
											public void onSuccess(JepDto userData) {
												String username = (String)userData.get(JEP_USER_NAME_FIELD_NAME);
												
												ClientSecurity.instance.setOperatorId((Integer)userData.get(OPERATOR_ID));
												ClientSecurity.instance.setRoles((List<String>) userData.get(JEP_USER_ROLES_FIELD_NAME));
												ClientSecurity.instance.setUsername(username);
												
												JepScopeStack.instance.setUserEntered();
												
												view.setUsername(username);
												view.hideLoadingIndicator();
												view.toggleAuthorizationPanel();
											}
										});
									}
									else {
										view.hideLoadingIndicator();
										view.showOrClearError(customText.custom_authorizationError_wrongCredential());
									}
			                    }
							}
							
							@Override
							public void onError(Request request, Throwable exception) {
								view.hideLoadingIndicator();
								view.showOrClearError(exception.getMessage());
							}
					});
				}
			}
		});
		
		view.addExitButtonClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				view.clearCredential();
				// Самостоятельный выход.
				mainService.logout(new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						view.toggleAuthorizationPanel();
					}
		
					public void onSuccess(String result) {
						view.toggleAuthorizationPanel();
					}
				});
			}
		});
	}
	
	private void sendRequest(String requestUrl, RequestBuilder.Method method, RequestCallback callback){
		RequestBuilder rBuilder = new RequestBuilder(method, URL.encode(requestUrl));
		try {
			rBuilder.sendRequest(null, callback);
		}
		catch(RequestException exception){
			view.hideLoadingIndicator();
			view.showOrClearError(exception.getMessage());
		}
	}

	/**
	 * Установка главного виджета(-контейнера) приложения.<br/>
	 * В методе используется вызов вида : <code>mainEventBus.setMainView(clientFactory.getMainClientFactory().getMainView());</code> <br/>
	 * При этом, при передаче <code>null</code> в качестве главного виджета приложения, текущий главный виджет удаляется с RootPanel'и.<br/>
	 * Т.о., перегрузкой данного метода можно установить, при заходе на модуль приложения, любой главный виджет приложения или скрыть текущий.
	 */
	protected void setMainView() {
		Log.trace(this.getClass() + ".setMainView()");
	
		mainEventBus.setMainView(null);
		
		String username = ClientSecurity.instance.getUsername();
		boolean isGuest = JepRiaUtil.isEmpty(username);
		if (!isGuest){
			view.setUsername(username);
			view.toggleAuthorizationPanel();
		}
	}
}
