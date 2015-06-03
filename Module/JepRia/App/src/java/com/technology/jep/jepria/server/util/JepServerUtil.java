package com.technology.jep.jepria.server.util;

import static com.technology.jep.jepria.server.JepRiaServerConstant.HTTP_REQUEST_PARAMETER_LANG;
import static com.technology.jep.jepria.server.JepRiaServerConstant.LOCALE_KEY;
import static com.technology.jep.jepria.shared.JepRiaConstant.DEFAULT_DATE_FORMAT;
import static com.technology.jep.jepria.shared.JepRiaConstant.LOCAL_LANG;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.technology.jep.jepria.shared.exceptions.SystemException;

/**
 * Класс содержащий вспомогательные/полезные функции.
 * 
 * @version 1.0
 */
public class JepServerUtil {
	/**
	 * Стандартный форматировщик дат.<br/>
	 * Преобразует строки в даты и даты в строки формата {@link com.technology.jep.jepria.shared.JepRiaConstant#DEFAULT_DATE_FORMAT}.
	 */
	public static final SimpleDateFormat defaultDateFormatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	static { defaultDateFormatter.setLenient(false); }

	/**
	 * Карта соответствия mime-типов наиболее используемым расширениям файлов.
	 */
	static private Map<String, String> fileExtensionMimeTypeMap = new HashMap<String, String>();
	static {
		fileExtensionMimeTypeMap.put("au", "audio/basic");
		fileExtensionMimeTypeMap.put("avi", "video/x-msvideo");
		fileExtensionMimeTypeMap.put("bin", "application/octet-stream");
		fileExtensionMimeTypeMap.put("bmp", "image/bmp");
		fileExtensionMimeTypeMap.put("crt", "application/x-x509-ca-cert");
		fileExtensionMimeTypeMap.put("css", "text/css");
		fileExtensionMimeTypeMap.put("dll", "application/x-msdownload");
		fileExtensionMimeTypeMap.put("doc", "application/msword");
		fileExtensionMimeTypeMap.put("dot", "application/msword");
		fileExtensionMimeTypeMap.put("dvi", "application/x-dvi");
		fileExtensionMimeTypeMap.put("exe", "application/octet-stream");
		fileExtensionMimeTypeMap.put("gif", "image/gif");
		fileExtensionMimeTypeMap.put("gz", "application/x-gzip");
		fileExtensionMimeTypeMap.put("gzip", "application/x-gzip");
		fileExtensionMimeTypeMap.put("hlp", "application/winhlp");
		fileExtensionMimeTypeMap.put("htm", "text/html");
		fileExtensionMimeTypeMap.put("html", "text/html");
		fileExtensionMimeTypeMap.put("htmls", "text/html");
		fileExtensionMimeTypeMap.put("htt", "text/webviewhtml");
		fileExtensionMimeTypeMap.put("htx", "text/html");
		fileExtensionMimeTypeMap.put("ico", "image/x-icon");
		fileExtensionMimeTypeMap.put("jpe", "image/jpeg");
		fileExtensionMimeTypeMap.put("jpeg", "image/jpeg");
		fileExtensionMimeTypeMap.put("jpg", "image/jpeg");
		fileExtensionMimeTypeMap.put("java", "text/plain");
		fileExtensionMimeTypeMap.put("js", "application/x-javascript");
		fileExtensionMimeTypeMap.put("mov", "video/quicktime");
		fileExtensionMimeTypeMap.put("mp2", "video/mpeg");
		fileExtensionMimeTypeMap.put("mp3", "audio/mpeg");
		fileExtensionMimeTypeMap.put("mpeg", "video/mpeg");
		fileExtensionMimeTypeMap.put("mpg", "video/mpeg");
		fileExtensionMimeTypeMap.put("mpp", "application/vnd.ms-project");
		fileExtensionMimeTypeMap.put("pdf", "application/pdf");
		fileExtensionMimeTypeMap.put("png", "image/png");
		fileExtensionMimeTypeMap.put("ppt", "application/vnd.ms-powerpoint");
		fileExtensionMimeTypeMap.put("ps", "application/postscript");
		fileExtensionMimeTypeMap.put("qt", "video/quicktime");
		fileExtensionMimeTypeMap.put("rtf", "application/rtf");
		fileExtensionMimeTypeMap.put("sh", "application/x-sh");
		fileExtensionMimeTypeMap.put("shtml", "text/html");
		fileExtensionMimeTypeMap.put("tar", "application/x-tar");
		fileExtensionMimeTypeMap.put("tex", "application/x-tex");
		fileExtensionMimeTypeMap.put("tif", "image/tiff");
		fileExtensionMimeTypeMap.put("tiff", "image/tiff");
		fileExtensionMimeTypeMap.put("txt", "text/plain");
		fileExtensionMimeTypeMap.put("vrml", "x-world/x-vrml");
		fileExtensionMimeTypeMap.put("wav", "audio/x-wav");
		fileExtensionMimeTypeMap.put("xla", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("xlc", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("xlm", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("xls", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("xlt", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("xlw", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("csv", "application/vnd.ms-excel");
		fileExtensionMimeTypeMap.put("xml", "text/xml");
		fileExtensionMimeTypeMap.put("zip", "application/zip");
	};

	/**
	 * Параметр / JNDI-имя, в котором передается URL контекст для формирования
	 * полного URL для вызова EJB модуля. Например, для полного URL вызова EJB
	 * вида "ormi://localhost:23791/Ejb3Template" в данном параметре будет
	 * содержаться "ormi://localhost:23791". Данная константа используется для
	 * вызова EJB расположенного на том же инстансе OC4J, что и вызывающий
	 * модуль.<br/>
	 * Если необходимо вызвать EJB расположенный на другом инстансе и (или)
	 * Application сервере, то в константах модуля создается дополнительная
	 * (-ые) константы вида: <br/>
	 * <code>
	 * static final String ENVIRONMENT_EJB_URL_CONTEXT_XXXX = "ejbUrlContextXxxx";
	 * </code> <br/>
	 * , где Xxxx - "логическое" (осмысленное) имя другого инстанса и (или)
	 * Application сервера.
	 */
	public static final String ENVIRONMENT_EJB_URL_CONTEXT = "ejbUrlContext";

	/**
	 * Получение Remote-интерфейса EJB.
	 * 
	 * @param appLogin пользователь Application Сервера, под которым происходит
	 *            подключение к EJB
	 * @param appPassword пароль пользователя Application Сервера, под которым
	 *            происходит подключение к EJB
	 * @param ejbUrlContext URL контекст для формирования полного URL для вызова
	 *            EJB модуля. Например, для полного URL вызова EJB вида
	 *            "ormi://localhost:23791/ejb3Template" в данном параметре будет
	 *            содержаться "ormi://localhost:23791".
	 * @param ejbName имя интерфейса. Например, для
	 *            "ormi://localhost:23791/ejb3Template" в данном параметре будет
	 *            содержаться "ejb3Template".
	 * @param beanName имя бина. Например, для
	 *            "ormi://localhost:23791/ejb3Template" в данном параметре будет
	 *            содержаться "Ejb3TemplateBean".
	 * 
	 * @return Remote-интерфейс EJB
	 * @throws NamingException
	 */
	public static Object ejbLookup(String appLogin, String appPassword, String ejbUrlContext, String ejbName, String beanName) throws NamingException {

		Context initial = null;
		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put("java.naming.factory.initial", "oracle.j2ee.rmi.RMIInitialContextFactory");
		env.put("java.naming.provider.url", ejbUrlContext + "/" + ejbName);
		env.put("java.naming.security.principal", appLogin);
		env.put("java.naming.security.credentials", appPassword);

		initial = new InitialContext(env);

		return initial.lookup(beanName);
	}

	/**
	 * Получение Local-интерфейса EJB.
	 * 
	 * @param beanName имя бина. Например, для
	 *            "java:comp/env/ejb/Ejb3TemplateBean" в данном параметре будет
	 *            содержаться "Ejb3TemplateBean".
	 * 
	 * @return Local-интерфейс EJB
	 * @throws NamingException
	 */
	public static Object ejbLookup(String beanName) throws NamingException {
		return new InitialContext().lookup("java:comp/env/ejb/" + beanName);
	}

	/**
	 * Считывание объекта из JNDI по его полному имени.
	 * 
	 * @param username имя пользователя, имеющего доступ на чтение JNDI
	 * @param password пароль
	 * @param path полное JNDI-имя объекта
	 * @return значение объекта
	 */
	public static Object jndiLookup(String username, String password, String path) {
		Object result = null;
		String ejbUrlContext = JepServerUtil.getEnvironmentValue(ENVIRONMENT_EJB_URL_CONTEXT);

		if (ejbUrlContext != null) {
			ejbUrlContext = ejbUrlContext.replace('\\', '/'); // Обход багофичи Oracle AS (там в Environment Variables '//' заменяет на '\\').

			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "oracle.j2ee.rmi.RMIInitialContextFactory");
			env.put(Context.PROVIDER_URL, ejbUrlContext);
			env.put(Context.SECURITY_PRINCIPAL, username);
			env.put(Context.SECURITY_CREDENTIALS, password);
			try {
				InitialContext ic = new InitialContext(env);
				result = ic.lookup(path);
			} catch (NamingException ex) {
				throw new SystemException("JNDI object having path '" + path + "' not found", ex);
			}
		} else {
			throw new SystemException("'ejbUrlContext' environment variable should be specified", null);
		}

		return result;
	}

	/**
	 * Получение значения переменной окружения.
	 * 
	 * @param name имя переменной
	 * 
	 * @return строковое значение параметра
	 */
	public static String getEnvironmentValue(String name) {
		return System.getenv(name);
	}

	/**
	 * Функция определяет: является ли текущий язык основным языком для пользователей.
	 * 
	 * @param request запрос, используя который из сессии получим текущий язык
	 * 
	 * @return возвращает true, если текущий язык является основным языком для
	 *         пользователей. В противном случае возвращает false.
	 */
	public static boolean isLocalLang(HttpServletRequest request) {
		return getLocale(request).getLanguage().equals((new Locale(LOCAL_LANG, "")).getLanguage());
	}

	/**
	 * Получение локали.
	 * 
	 * @param request HTTP-запрос
	 * @return локаль
	 */
	public static Locale getLocale(HttpServletRequest request) {
		Locale locale = (Locale) request.getSession().getAttribute(LOCALE_KEY);
		if (locale == null) {
			String lang = (String) request.getAttribute(HTTP_REQUEST_PARAMETER_LANG);
			if (lang == null) {
				lang = LOCAL_LANG;
			}
			locale = new Locale(lang);
			request.getSession().setAttribute(LOCALE_KEY, locale);
		}
		return locale;
	}
	
	public static String detectMimeType(String fileExtension) {
		return fileExtension != null ? fileExtensionMimeTypeMap.get(fileExtension.toLowerCase()) : null;
	}

	public static boolean isOC4JEnvironment(ServletContext servletContext) {
		String serverInfo = servletContext.getServerInfo();
		return serverInfo.indexOf("10.1.3") >= 0;
	}
	
	public static boolean isOC4JEnvironment(HttpServletRequest request) {
		return isOC4JEnvironment(request.getSession().getServletContext());
	}
	
	public static String getServerUrl(HttpServletRequest request) {
		return request.getProtocol().substring(0,
				request.getProtocol().indexOf("/")).toLowerCase()
				+ "://"
				+ request.getServerName()
				+ ":"
				+ request.getServerPort();
	}
}
