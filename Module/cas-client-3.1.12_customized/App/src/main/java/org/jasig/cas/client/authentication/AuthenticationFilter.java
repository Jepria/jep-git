/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/index.html
 */
package org.jasig.cas.client.authentication;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;

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
		final HttpSession session = request.getSession(false);
		final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;

        logger.debug(this.getClass() + ".doFilter() request.getAuthType() = " + request.getAuthType());
		
		response = new DebugResponseWrapper(response);
		if (assertion != null) {
			filterChain.doFilter(request, response);
			return;
		}

		final String serviceUrl = constructServiceUrl(request, response);
		final String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
		final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);

		if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {
			filterChain.doFilter(request, response);
			return;
		}

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

        response.sendRedirect(urlToRedirectTo);
    }

	public final void setRenew(final boolean renew) {
        this.renew = renew;
    }

    public final void setGateway(final boolean gateway) {
        this.gateway = gateway;
    }

    public final void setCasServerLoginUrl(final String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }
    
    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {
    	this.gatewayStorage = gatewayStorage;
    }

//    private void printRequestAndResponse(HttpServletRequest request, DebugResponseWrapper debugResponseWrapper, String message) throws IOException {
    private void printRequestAndResponse(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
    	DebugResponseWrapper debugResponseWrapper = null;
    	if(!(response instanceof DebugResponseWrapper)) {
    			return;
    		} else {
    			debugResponseWrapper = (DebugResponseWrapper) response;
    		}
    		
    	  Map<String, String> requestMap = this.getTypesafeRequestMap(request);
    	  BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(request);
    	  
    	  final StringBuilder requestLogMessage = new StringBuilder("Request - ")
    		.append("[HTTP METHOD:")
            .append(request.getMethod())                                      
            .append("] [PATH INFO:")
            .append(request.getPathInfo())                              
            .append("] [REQUEST PARAMETERS:")
            .append(requestMap)
            .append("] [REQUEST BODY:")
            .append(bufferedRequest.getRequestBody())                                        
            .append("] [REMOTE ADDRESS:")
            .append(request.getRemoteAddr())
            .append("]");
    	  
    	  final StringBuilder responseLogMessage = new StringBuilder("Response - ")
    		.append("] [CHARACTER_ENCODING:")
            .append(debugResponseWrapper.getCharacterEncoding())                                      
    		.append("] [BUFFER_SIZE:")
            .append(debugResponseWrapper.getBufferSize())                                      
    		.append("] [LOCALE:")
            .append(debugResponseWrapper.getLocale())                                      
    		.append("] [HEADERS:")
            .append(debugResponseWrapper.getHeaders())                                      
    		.append("] [COOKIES:")
            .append(debugResponseWrapper.getCookies())                                      
            .append("]");
    	  
          logger.debug(message + ":\n" + requestLogMessage + "\n" + responseLogMessage);
          
          logger.debug("request.isUserInRole(\"\")\n" + requestLogMessage + "\n" + responseLogMessage);
    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String)requestParamNames.nextElement();
			String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}	

	private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private ByteArrayInputStream bais = null;
        private ByteArrayOutputStream baos = null;
        private BufferedServletInputStream bsis = null;
        private byte[] buffer = null;
 

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            // Read InputStream and store its content in a buffer.
            InputStream is = req.getInputStream();
            this.baos = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int letti;
            while ((letti = is.read(buf)) > 0) {
                this.baos.write(buf, 0, letti);
            }
            this.buffer = this.baos.toByteArray();
        }

        
        @Override
        public ServletInputStream getInputStream() {
            this.bais = new ByteArrayInputStream(this.buffer);
            this.bsis = new BufferedServletInputStream(this.bais);
            return this.bsis;
        }

        

        String getRequestBody() throws IOException  {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
            String line = null;
            StringBuilder inputBuffer = new StringBuilder();
            do {
            	line = reader.readLine();
            	if (null != line) {
            		inputBuffer.append(line.trim());
            	}
            } while (line != null);
            reader.close();
            return inputBuffer.toString().trim();
        }

    }

    private static final class BufferedServletInputStream extends ServletInputStream {

        private ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }
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
}
