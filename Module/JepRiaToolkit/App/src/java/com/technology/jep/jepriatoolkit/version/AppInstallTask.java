package com.technology.jep.jepriatoolkit.version;

import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.checkParameter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.technology.jep.jepriatoolkit.JepRiaToolkitConstant;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class AppInstallTask extends Task implements JepRiaToolkitConstant {

	// атрибуты таска
	private String action, port, deploymentPath, loadOperatorId, 
			javaReturnCode, errorMessage, skipSaveInstallInfo, version, 
			packageName, moduleName, svnPath, svnVersionInfo;
	// служебные поля
	private String responseError, serverName;

	private String appInstallResultId = "";
	
	private ApplicationSetting appSetting;
	
	PropertyHelper ph;

	/**
	 * Основной метод, который выполняет Task
	 */
	@Override
	public void execute() throws BuildException {
		
		int responseCode;
		ph = PropertyHelper.getPropertyHelper(getProject());
		URL url = null;
		
		if (!"startAppInstall".equals(action) && !"finishAppInstall".equals(action)) {
			throw new BuildException("Incorrect argument: action.");
		}

		// если параметр skipSaveInstallInfo принимает значение 1, то версию не пишем
		if ("1".equals(skipSaveInstallInfo)
				&& !(ph.getProperty("sun.java.command") != null 
					&& ((String) ph.getProperty("sun.java.command")).contains("-DLOAD_OPERATORID")
					)
			) 
		{
			JepRiaToolkitUtil.echoMessage("Skipped");
			return;
		}
		
		try {
			checkParameter(deploymentPath, "Incorrect parameter: DEPLOYMENT_PATH!");
			checkParameter(loadOperatorId, "Incorrect parameter: LOAD_OPERATORID!");

			serverName = JepRiaToolkitUtil.getServerName(deploymentPath.replace("\\", PATH_SEPARATOR));

			if ("startAppInstall".equals(action)) {
				if (!parseMapXML() || JepRiaToolkitUtil.isEmpty(appSetting)) { 
					throw new BuildException("Error parsing map.xml!");
				}
				
				checkParameter(version, "Incorrect parameter: VERSION!");
				checkParameter(moduleName, "Incorrect parameter: MODULE_NAME!");
				checkParameter(packageName, "Incorrect parameter: PACKAGE_NAME!");
				checkParameter(svnPath, "Incorrect argument: svnPath!");
				checkParameter(svnVersionInfo, "Incorrect argument: svnVersionInfo!");
				
				url = new URL(
					MessageFormat.format("{0}?action=startAppInstall&svnRoot={1}&initPath={2}"
							+ "&modVersion={3}&instVersion={4}&deployPath={5}&svnPath={6}&svnVersionInfo={7}"
							+ "&login={8}&password={9}",
						JepRiaToolkitUtil.prepareServerUrl(HTTP_PROTOCOL, serverName, port) + "/ModuleInfo/versionServlet",
						appSetting.getSvnRoot(),
						appSetting.getInitialPath(),
						appSetting.getModuleVersion(),
						appSetting.getInstallVersion(),
						deploymentPath,
						svnPath,
						svnVersionInfo,
						appSetting.getLogin(),
						appSetting.getPassword()));
			}
			
			if ("finishAppInstall".equals(action)) {
				checkParameter(appInstallResultId, "Incorrect argument: appInstallResultId!");
				
				loadOperatorId = loadOperatorId.replaceAll("\\\\", PATH_SEPARATOR);
				String[] loginAndPass = loadOperatorId.split(PATH_SEPARATOR);
				String login = loginAndPass[0];
				// если пароль не указан через / используем в качестве него - логин
				String password = loginAndPass.length > 1 ? loadOperatorId
						.substring(loadOperatorId.indexOf(PATH_SEPARATOR) + 1)
						: loginAndPass[0];
				serverName = JepRiaToolkitUtil.getServerName(deploymentPath.replace("\\", PATH_SEPARATOR));
				
				url = new URL(MessageFormat.format("{0}?action=finishAppInstall&appInstallResultId={1}&javaReturnCode={2}&errorMessage={3}"
							+ "&login={4}&password={5}",
						JepRiaToolkitUtil.prepareServerUrl(HTTP_PROTOCOL, serverName, port) + "/ModuleInfo/versionServlet",
						appInstallResultId,
						javaReturnCode,
						URLEncoder.encode(errorMessage, "UTF-8"),
						JepRiaToolkitUtil.encode(login),
						JepRiaToolkitUtil.encode(password)
						)
					);
			}
			
			HttpURLConnection versionServletConnection = getVersionServletConnection(url);
			responseCode = versionServletConnection.getResponseCode();
			
			if (responseCode != HttpURLConnection.HTTP_OK)
			{
				responseError = "HTTP Status-Code " + new Integer(responseCode).toString() + ": "; 
				
				if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
					responseError += "Bad request. Check DEPLOYMENT_PATH & PORT parameters!";
				}
				if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
					responseError += "Unauthorized. Check LOGIN & PASSWORD parameters!";
				}
				if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
					responseError += "Not found. Check DEPLOYMENT_PATH & PORT parameters!";				
				}
				if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
					responseError += "Internal server error.";
				}
				if (responseCode == HttpURLConnection.HTTP_BAD_GATEWAY) {
					responseError += "Bad Gateway. Check DEPLOYMENT_PATH & PORT parameters!";
				}
				
				throw new BuildException(responseError);
			}

			if ("startAppInstall".equals(action)) {
				String resultId = versionServletConnection.getHeaderField("appInstallResultId");
				ph.setProperty(appInstallResultId, resultId, false);
				
				versionServletConnection.disconnect();
			}

			if ("finishAppInstall".equals(action)) {
				if (!"0".equals(javaReturnCode)) {
					throw new BuildException(errorMessage);
				}
			}
		} 
		catch (IOException e) {
			//e.printStackTrace();
			throw new BuildException("Connection refused. Check DEPLOYMENT_PATH & PORT parameters! Details: " + e.getMessage(), e);
		} 
		catch (Exception e) {
		//e.printStackTrace();
		throw new BuildException(e.getMessage(), e);
	} 

	}

	
	/**
	 * Обращение к сервлету версионности
	 * 
	 * @return соединение с сервлетом
	 * @throws Exception
	 */
	private HttpURLConnection getVersionServletConnection(URL url) throws IOException {
		if (url == null) 
			return null;
		
		System.setProperty("java.net.useSystemProxies", "true");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		boolean isAllRight = Boolean.FALSE;
		int responseCode = 0;
		
		urlConn.connect();
		responseCode = urlConn.getResponseCode();
		isAllRight = new Integer(HttpURLConnection.HTTP_OK).equals(responseCode);
		
		return urlConn;
	}

	/**
     * Получение служебной информации 
     * 
     * @param mapXML				документ
     * @return флаг успешности парсинга
     */
    private boolean parseMapXML()
    	throws ParserConfigurationException, SAXException{
    	Integer stage = 1;
    	Document mapXML = null;
    	String instVersion = null;
    	String moduleVersion = null;
    	String svnRoot = null;
    	String initPath = null;
    	try{
    		mapXML = JepRiaToolkitUtil.getDOM("../Doc/map.xml");
    	}
    	catch(IOException io){
    		JepRiaToolkitUtil.echoMessage(
   				JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Error parsing map.xml : ", END_OF_LINE, TAB, io.getMessage()));
    		return false;
    	}
    	
    	try{
    		XPathFactory factory = XPathFactory.newInstance();
    		XPath xpath = factory.newXPath();
    		
    		XPathExpression expr = xpath.compile("//map/version/text()");
    		//версия модуля, взятая из map.xml
    		moduleVersion = (String) expr.evaluate(mapXML, XPathConstants.STRING);
    		stage++;
    		
    		xpath.reset();
    		expr = xpath.compile("//map/path/text()");
    		//корень модуля, взятый из map.xml
    		svnRoot = ((Node) expr.evaluate(mapXML, XPathConstants.NODE)).getTextContent();
    		stage++;
    		
    		xpath.reset();
    		expr = xpath.compile("//map/initialPath/text()");
    		//начальный путь модуля, взятый из map.xml
    		initPath = ((Node) expr.evaluate(mapXML, XPathConstants.NODE)).getTextContent();
    		stage++;
    		
    		boolean isFault = false;
    		try {
    		//устанавливаемая версия модуля, взятая из пути модуля
    			instVersion = JepRiaToolkitUtil.getInstallVersion(version);
    		}
    		catch(Exception e){
    			JepRiaToolkitUtil.echoMessage(
    					JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Invalid parameter 'Version' = ", version, "!"));
    			isFault = true;
    		}
    		    		
    		if (JepRiaToolkitUtil.isEmpty(moduleVersion)){
    			JepRiaToolkitUtil.echoMessage(
    					JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Invalid parameter 'Module Version', check map.xml!"));
    			isFault = true;
    		}   
    		
    		loadOperatorId = loadOperatorId.replaceAll("\\\\", PATH_SEPARATOR);
    		String[] loginAndPass = loadOperatorId.split(PATH_SEPARATOR);
    		String login = loginAndPass[0];
    		//если пароль не указан через / используем в качестве него - логин
    		String password = loginAndPass.length > 1 ? loadOperatorId.substring(loadOperatorId.indexOf(PATH_SEPARATOR) + 1) : loginAndPass[0];
    		if (!isFault){
    			appSetting = new ApplicationSetting(
    					svnRoot
    					, initPath
    					, moduleVersion
    					, instVersion
    					, deploymentPath
    					, svnPath
    					, svnVersionInfo
    					, JepRiaToolkitUtil.encode(login)
    					, JepRiaToolkitUtil.encode(password));
    		} else {
    			return false;
    		}
    	}
    	catch(Exception e){
    		String errorMessageName = new String();
    		switch(stage){
    			case 2 : errorMessageName = JepRiaToolkitUtil.multipleConcat("Error getting attribute 'svnRoot' (value - ", svnRoot, ") during parsing map.xml. Please check this file!"); break;
    			case 3 : errorMessageName = JepRiaToolkitUtil.multipleConcat("Error getting attribute 'initialPath' (value - ", initPath, ") during parsing map.xml. Please check this file!"); break;
    			case 4 : errorMessageName = JepRiaToolkitUtil.multipleConcat("Not correct value for attribute 'instVersion' - ", instVersion, ". Please check this attribute in ", BUILD_FILE, "!"); break;
    			case 1 :
    			default : errorMessageName = JepRiaToolkitUtil.multipleConcat("Error getting attribute 'moduleVersion' (value - ", moduleVersion, ") during parsing map.xml. Please check this file!"); break;
    		}
    		JepRiaToolkitUtil.echoMessage(
    				JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, errorMessageName));
    		return false;
    	}
    	return true;
    }
    
	public void setAction(String action){
		this.action = action;
	}

	public void setJavaReturnCode(String javaReturnCode) {
		this.javaReturnCode = javaReturnCode;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setPort(String port){
		this.port = port;
	}
	
	public void setVersion(String version){
		this.version = version;
	}

	public void setDeploymentPath(String deploymentPath) {
		this.deploymentPath = deploymentPath;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setLoadOperatorId(String loadOperatorId) {
		this.loadOperatorId = loadOperatorId;
	}
	
	public void setSkipSaveInstallInfo(String skipSaveInstallInfo) {
		this.skipSaveInstallInfo = skipSaveInstallInfo;
	}
	
	public void setSvnVersionInfo(String svnVersionInfo) {
		this.svnVersionInfo = svnVersionInfo;
	}
	
	public void setSvnPath(String svnPath) {
		this.svnPath = svnPath;
	}
	
	/**
     * Set the name of the property which will be set if JepRia-application meets all requirements
     * for deployment.
     *
     * @param property the name of the property to set.
     */
	
    public void setAppInstallResultId(String appInstallResultId) {
        this.appInstallResultId = appInstallResultId;
    }

}