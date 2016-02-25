package org.jasig.cas.client.util;

import java.util.Properties;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Abstract filter that contains code that is common to all CAS filters.
 *  <p>
 * The following filter options can be configured (either at the context-level or filter-level).
 * <ul>
 * <li><code>serverName</code> - the name of the CAS client server, in the format: localhost:8080 or localhost:8443 or localhost or https://localhost:8443</li>
 * <li><code>service</code> - the completely qualified service url, i.e. https://localhost/cas-client/app</li>
 * </ul>
 * <p>Please note that one of the two above parameters must be set.</p>
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public abstract class AbstractCasFilter extends AbstractConfigurationFilter {
    /** Имя внешней переменной - адреса CAS-сервера */
    public static final String CAS_SERVER_ADDRESS = "CAS_SERVER_ADDRESS";
    
//	/**
//	 * Имя cookie-признака (устанавливаемого в login.jsp приложения), говорящего о том, что требуется логин через CAS 
//	 */
//	public static final String LOGIN_REQUEST_FLAG_COOKIE_NAME = "LOGIN_REQUEST_FLAG_COOKIE_NAME";
    
	/**
	 * Имя cookie, отражающей фазу авторизации WebLogic
	 */
	public static final String WLA_STATE_COOKIE_NAME = "WLA_STATE";
	
//	/**
//	 * Значение appState cookie - авторизация WebLogic не используется 
//	 */
//	public static final String OFF_WLA_STATE_COOKIE_VALUE = "Off";
	
	/**
	 * Значение appState cookie - запрос login-а на CAS 
	 */
	public static final String INITIAL_WLA_STATE_COOKIE_VALUE = "Initial";
	
	/**
	 * Значение appState cookie - запрос login-а на CAS 
	 */
	public static final String LOGIN_REQUEST_WLA_STATE_COOKIE_VALUE = "LoginRequest";
	
	/**
	 * Значение appState cookie - автологон в WebLogic 
	 */
	public static final String WL_AUTOLOGON_REQUEST_WLA_STATE_COOKIE_VALUE = "AutoLogonRequest";
	
	/**
	 * Значение appState cookie - выполнялась попытка автологона в WebLogic 
	 */
	public static final String WL_AUTOLOGON_WAS_ATTEMPTED_WLA_STATE_COOKIE_VALUE = "AutoLogonWasAttempted";
	
	/**
	 * Значение appState cookie - автологон в WebLogic прошёл успешно 
	 */
	public static final String WL_AUTOLOGON_SUCCEEDED_WLA_STATE_COOKIE_VALUE = "AutoLogonSucceeded";

	/**
	 * Целевой Url при WLA-авторизации
	 */
	public static final String WLA_TARGET_URL = "targetUrl";
		
	
	/**
	 * Имя cas credentials cookie 
	 */
	public static final String CAS_CREDENTIALS_COOKIE_NAME = "CasCredentialsCookieName";

    /** Имя параметра, содержащего контекстную часть URL CAS-сервера */
    public static final String CAS_SERVER_NAME_CONTEXT_PARAMETER = "casServerName";
    
    /** Represents the constant for where the assertion will be located in memory. */
    public static final String CONST_CAS_ASSERTION = "_const_cas_assertion_";

    /** Instance of commons logging for logging purposes. */
    private final Log logger = LogFactory.getLog(getClass());

    /** Defines the parameter to look for for the artifact. */
    private String artifactParameterName = "ticket";

    /** Defines the parameter to look for for the service. */
    private String serviceParameterName = "service";
    
    /** Sets where response.encodeUrl should be called on service urls when constructed. */
    private boolean encodeServiceUrl = true;

    /**
     * The name of the server.  Should be in the following format: {protocol}:{hostName}:{port}.
     * Standard ports can be excluded. */
    private String serverName;	// TODO Сейчас не используется, поскольку serverName определяется динамически, убрать ?

    /** The exact url of the service. */
    private String service;

    public final void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
// TODO Сейчас не используется, поскольку serverName определяется динамически, убрать ?
//            setServerName(getPropertyFromInitParams(filterConfig, "serverName", null));
            
//            logger.trace("Loading serverName property: " + this.serverName);
            setService(getPropertyFromInitParams(filterConfig, "service", null));
            logger.trace("Loading service property: " + this.service);
            setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            logger.trace("Loading artifact parameter name property: " + this.artifactParameterName);
            setServiceParameterName(getPropertyFromInitParams(filterConfig, "serviceParameterName", "service"));
            logger.trace("Loading serviceParameterName property: " + this.serviceParameterName);
            setEncodeServiceUrl(parseBoolean(getPropertyFromInitParams(filterConfig, "encodeServiceUrl", "true")));
            logger.trace("Loading encodeServiceUrl property: " + this.encodeServiceUrl);

            initInternal(filterConfig);
        }
        init();
    }

	/**
	 * Controls the ordering of filter initialization and checking by defining a method that runs before the init.
     * @param filterConfig the original filter configuration.
     * @throws ServletException if there is a problem.
     */
    protected void initInternal(final FilterConfig filterConfig) throws ServletException {
        // template method
    }

    /**
     * Initialization method.  Called by Filter's init method or by Spring.  Similar in concept to the InitializingBean interface's
     * afterPropertiesSet();
     */
    public void init() {
        CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
        CommonUtils.assertNotNull(this.serviceParameterName, "serviceParameterName cannot be null.");
//        CommonUtils.assertTrue(CommonUtils.isNotEmpty(this.serverName) || CommonUtils.isNotEmpty(this.service), "serverName or service must be set.");
    }

    // empty implementation as most filters won't need this.
    public void destroy() {
        // nothing to do
    }

    protected final String constructServiceUrl(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
//        return CommonUtils.constructServiceUrl(request, response, this.service, this.serverName, this.artifactParameterName, this.encodeServiceUrl);
    	
        return CommonUtils.constructServiceUrl(request, response, this.service, CommonUtils.obtainServerName(request), this.artifactParameterName, this.encodeServiceUrl);
    }

    // TODO Сейчас не используется, поскольку serverName определяется динамически, убрать ?
    public final void setServerName(final String serverName) {
        this.serverName = serverName;
    }

    public final void setService(final String service) {
        this.service = service;
    }

    public final void setArtifactParameterName(final String artifactParameterName) {
        this.artifactParameterName = artifactParameterName;
    }

    public final void setServiceParameterName(final String serviceParameterName) {
        this.serviceParameterName = serviceParameterName;
    }
    
    public final void setEncodeServiceUrl(final boolean encodeServiceUrl) {
    	this.encodeServiceUrl = encodeServiceUrl;
    }

    public final String getArtifactParameterName() {
        return this.artifactParameterName;
    }

    public final String getServiceParameterName() {
        return this.serviceParameterName;
    }

    protected String getCasServerAddress() {
        String casServerAddress = System.getProperty(CAS_SERVER_ADDRESS);
        if(casServerAddress == null) {
        	String message = this.getClass() + ".getCasServerAddress(): " + CAS_SERVER_ADDRESS + " environment variable should be set.";
            logger.error(message);
        	printSystemProperties();
            throw new IllegalArgumentException(message);
        }
        
        return casServerAddress.replace('\\', '/');
	}

	private void printSystemProperties() {
		Properties props = System.getProperties();
		props.list(System.out);
	}
}
