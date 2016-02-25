/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/index.html
 */
package org.jasig.cas.client.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

import weblogic.servlet.security.ServletAuthentication;

/**
 * Filter implementation to intercept all requests and attempt to authenticate
 * the user by redirecting them to CAS (unless the user has a ticket).
 * <p>
 * This filter allows you to specify the following parameters (at either the context-level or the filter-level):
 * <ul>
 * <li><code>casServerLoginUrl</code> - the url to log into CAS, i.e. https://cas.rutgers.edu/login</li>
 * <li><code>renew</code> - true/false on whether to use renew or not.</li>
 * <li><code>gateway</code> - true/false on whether to use gateway or not.</li>
 * </ul>
 *
 * <p>Please see AbstractCasFilter for additional properties.</p>
 *
 * @author Scott Battaglia
 * @version $Revision: 11768 $ $Date: 2007-02-07 15:44:16 -0500 (Wed, 07 Feb 2007) $
 * @since 3.0
 */
public class AuthenticationFilter extends AbstractCasFilter {
	
	/**
     * The URL to the CAS Server login.
     */
    private String casServerLoginUrl;

    /**
     * Whether to send the renew request or not.
     */
    private boolean renew = false;

    /**
     * Whether to send the gateway request or not.
     */
    private boolean gateway = false;

    /**
     * Whether webLogicAuthorization required on not.
     */
    private boolean webLogicAuthorization = true; 
    
    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();
    
    protected static Log logger = LogFactory.getLog(AuthenticationFilter.class);

    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        logger.trace(this.getClass() + ".initInternal() BEGIN");
        
        if (!isIgnoreInitConfiguration()) {
            super.initInternal(filterConfig);
            
            // TODO Вынести получение переменных в предки
            ServletContext context = filterConfig.getServletContext();
            String casServerName = context.getInitParameter(CAS_SERVER_NAME_CONTEXT_PARAMETER);
            CommonUtils.assertNotNull(casServerName, "casServerContextName cannot be null.");

            String casServerLoginPath = casServerName + "/login";

            String casServerAddress = getCasServerAddress();
            setCasServerLoginUrl(casServerAddress + "/" + casServerLoginPath);
            
            logger.trace("CasServerLoginUrl: " + this.casServerLoginUrl);
            
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
            logger.trace("Loaded renew parameter: " + this.renew);
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));
            logger.trace("Loaded gateway parameter: " + this.gateway);
            setWebLogicAuthorization(parseBoolean(getPropertyFromInitParams(filterConfig, "webLogicAuthorization", "true")));
            logger.trace("Loaded webLogicAuthorization parameter: " + this.renew);
                        
            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);

            if (gatewayStorageClass != null) {
                try {
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();
                } catch (final Exception e) {
                    logger.error(e,e);
                    throw new ServletException(e);
                }
            }
        }
        
        logger.trace(this.getClass() + ".initInternal() END");
    }

	public void init() {
        super.init();
        CommonUtils.assertNotNull(this.casServerLoginUrl, "casServerLoginUrl cannot be null.");
    }

	public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
        
		response = new DebugResponseWrapper(response);
		
		// TODO Выяснить, почему по факту у WebLogic фильтры срабатывают раньше security-constraint (данный код базируется на этой особенности) 
		final String serviceUrl;
		final HttpSession session = request.getSession(false);
		final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;
		
		if(assertion != null) {
			if(this.webLogicAuthorization) {
				Cookie wlaStateCookie = getWlaStateCookie(request);
				if(wlaStateCookie == null) {
					wlaStateCookie = new Cookie(WLA_STATE_COOKIE_NAME, INITIAL_WLA_STATE_COOKIE_VALUE);
					wlaStateCookie.setPath("/");
					response.addCookie(wlaStateCookie);
				}
				
				if (isInitialWLAState(wlaStateCookie)) { // TODO поддержать logout, session expiration c установкой InitialState 
					filterChain.doFilter(request, response);
					return;
				} else if(isAutoLogonRequestWLAState(wlaStateCookie)) { // Вход после CAS-логина (username, password - в cookies)
					filterChain.doFilter(request, response);
					return;
				} else if(wasAutoLogonAttemptedWLAState(wlaStateCookie)) {
					if(isEnteringIntoApplication(request)) {
						setWLAState(WL_AUTOLOGON_SUCCEEDED_WLA_STATE_COOKIE_VALUE, response);
						filterChain.doFilter(request, response);
						return;
					} else {
						// Автологон по какой-то причине не прошёл, возможно по-разному сработали логин-модули на CAS и на AS
				        logger.error("Autologon error"); // TODO Нужно ли выдавать сообщение на Web-странице ?
					}
				} else if(isAutoLogonSucceededWLAState(wlaStateCookie)) { // На случай параллельного входа последующих приложений (уже успевших получить login-страницу CAS)
					if(isEnteringIntoApplication(request)) {
						filterChain.doFilter(request, response);
						return;
					} else {
				        String targetURL = (String) request.getSession().getAttribute(WLA_TARGET_URL);
						request.getSession().removeAttribute(WLA_TARGET_URL);
						
				        response.sendRedirect(targetURL);
					}
				}
			} else {
				filterChain.doFilter(request, response);
				return;
			}
		} else {
			if(this.webLogicAuthorization) {
				Cookie wlaStateCookie = getWlaStateCookie(request);
				if(wlaStateCookie == null) {
					setWLAState(INITIAL_WLA_STATE_COOKIE_VALUE, response);
				}
				if (isInitialWLAState(wlaStateCookie)) {
					filterChain.doFilter(request, response);
					return;
				} else if (isLoginRequestWLAState(wlaStateCookie)) { // Был запрос от WebLogic на CAS-логин ?
					setWLAState(WL_AUTOLOGON_REQUEST_WLA_STATE_COOKIE_VALUE, response);
					
					// Сохраняем targetUrl для последующего перехода (на случай коллизии при параллельной работе)
					String targetUrl = ServletAuthentication.getTargetURLForFormAuthentication(request.getSession());
					request.getSession().setAttribute(WLA_TARGET_URL, targetUrl);
					
					serviceUrl = constructServiceUrl(request, response);	// Для redirect на cas/login
					goToCasLogin(request, response, serviceUrl);
					return;
				}
			}
			
			serviceUrl = constructServiceUrl(request, response);
			
			final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
			final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

			if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
				filterChain.doFilter(request, response);
				return;
			}
			
			goToCasLogin(request, response, serviceUrl);
		}
    }

	private boolean isEnteringIntoApplication(HttpServletRequest request) {
		return request.getRequestURL().indexOf("login.jsp") == -1; // TODO Желательно убрать зависимость от login.jsp 
	}

	private void goToCasLogin(HttpServletRequest request, HttpServletResponse response, String serviceUrl) throws IOException {
		final String modifiedServiceUrl;
        
        logger.debug("no ticket and no assertion found");
        if (this.gateway) {
            logger.debug("setting gateway attribute in session");
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);
        } else {
            modifiedServiceUrl = serviceUrl;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Constructed service url: " + modifiedServiceUrl);
        }

        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(this.casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);

        if (logger.isDebugEnabled()) {
            logger.debug("redirecting to \"" + urlToRedirectTo + "\"");
        }

        response.sendRedirect(urlToRedirectTo); // Redirect на CAS-сервер
        
        logger.trace(this.getClass() + ".doFilter().goToCasLogin() END");
	}

	public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public void setWebLogicAuthorization(boolean webLogicAuthorization) {
        this.webLogicAuthorization = webLogicAuthorization;
	}

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }
    
    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
    	this.gatewayStorage = gatewayStorage;
    }
	
    class DebugResponseWrapper extends HttpServletResponseWrapper {
    	private Map<String, String> cookies = new HashMap<String, String>();
    	private Map<String, String> headers = new HashMap<String, String>();
		private String dateHeaderName;
		private long dateHeaderDate;
		private HttpServletResponse originalResponse;
    	
        public DebugResponseWrapper(HttpServletResponse response) {
            super(response);
            originalResponse = response;
        }
        
    	public String getCookies() {
			StringBuilder result = new StringBuilder();
			Set<String> keySet = cookies.keySet();
			for(String key: keySet) {
				if(result.length() > 0) {
					result.append(",\n");
				}
				result.append("[" + key + ", " + headers.get(key) + "]");
			}
			return result.toString();
		}

		public String getHeaders() {
			StringBuilder result = new StringBuilder();
			Set<String> keySet = headers.keySet();
			for(String key: keySet) {
				if(result.length() > 0) {
					result.append(",\n");
				}
				result.append("[" + key + ", " + headers.get(key) + "]");
			}
			return result.toString();
		}

		public void addHeader(String key, String value) {
    		headers.put(key, value);
    		originalResponse.addHeader(key, value);
    	}
        
        public void addCookie(Cookie cookie) {
        	cookies.put(cookie.getName(), cookie.getValue());
        	originalResponse.addCookie(cookie);
        }
        
    	public void addDateHeader(String name, long date) {
    		this.dateHeaderName = name; 
    		this.dateHeaderDate = date; 
    		originalResponse.addDateHeader(name, date);
    	}
    	
		String getDateHeaderName() {
			return dateHeaderName;
		}

		long getDateHeaderDate() {
			return dateHeaderDate;
		}
    }
    
	private static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie result = null;
		if(cookieName != null) {
			Cookie[] cookies = request.getCookies();
			if(cookies != null) {
				for(int i = 0; i < cookies.length; i++) {
				  String ckName = cookies[i].getName();
				  String path = cookies[i].getPath();
				  if((path == null || "/".equals(path)) && cookieName.equals(ckName)) {
					  result = cookies[i];
					  break;
				  }
				}
			}
		}
		
		return result;
	}

	private void deleteCookie(Cookie cookie, HttpServletResponse response) {
		if(cookie != null) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
	}

	private void setWLAState(String wlaState, HttpServletResponse response) {
		Cookie wlaStateCookie = new Cookie(WLA_STATE_COOKIE_NAME, wlaState);
		wlaStateCookie.setPath("/");
		response.addCookie(wlaStateCookie);
	}

	private Cookie getWlaStateCookie(HttpServletRequest request) {
		return getCookie(request, WLA_STATE_COOKIE_NAME);
	}

	private boolean isInitialWLAState(Cookie wlaStateCookie) {
		return wlaStateCookie == null || INITIAL_WLA_STATE_COOKIE_VALUE.equals(wlaStateCookie.getValue());
	}

	private boolean isLoginRequestWLAState(Cookie wlaStateCookie) {
		return wlaStateCookie != null && LOGIN_REQUEST_WLA_STATE_COOKIE_VALUE.equals(wlaStateCookie.getValue());
	}

	private boolean isAutoLogonRequestWLAState(Cookie wlaStateCookie) {
		return wlaStateCookie != null && WL_AUTOLOGON_REQUEST_WLA_STATE_COOKIE_VALUE.equals(wlaStateCookie.getValue());
	}

	private boolean isAutoLogonSucceededWLAState(Cookie wlaStateCookie) {
		return wlaStateCookie != null && WL_AUTOLOGON_SUCCEEDED_WLA_STATE_COOKIE_VALUE.equals(wlaStateCookie.getValue());
	}

	private boolean wasAutoLogonAttemptedWLAState(Cookie wlaStateCookie) {
		return wlaStateCookie != null && WL_AUTOLOGON_WAS_ATTEMPTED_WLA_STATE_COOKIE_VALUE.equals(wlaStateCookie.getValue());
	}
}
