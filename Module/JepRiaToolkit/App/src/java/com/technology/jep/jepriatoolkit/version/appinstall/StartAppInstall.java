package com.technology.jep.jepriatoolkit.version.appinstall;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.END_OF_LINE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.TAB;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.checkParameter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.technology.jep.jepria.shared.util.JepRiaUtil;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;
import com.technology.jep.jepriatoolkit.version.MapXml;


public class StartAppInstall extends AppInstall {

  // атрибуты таска
  private String version, svnPath, svnVersionInfo, mapXmlPath;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws BuildException {
    callVersionServlet();
  }

  /**
   * {@inheritDoc}
   * @throws MalformedURLException
   */
  @Override
  public URL prepareUrl() throws MalformedURLException {

    checkParameter(version, "Incorrect parameter: VERSION!");
    checkParameter(svnPath, "Incorrect argument: svnPath!");
    checkParameter(svnVersionInfo, "Incorrect argument: svnVersionInfo!");

    String errorMsg = "Error parsing map.xml!";

    MapXml mapXml = null;
    String installVersion;

    try { //TODO: use Multiple Exception Types in catch after jdk > 6
      mapXml = parseMapXml();
      installVersion = getInstallVersion(version); //устанавливаемая версия модуля, взятая из пути модуля
    } catch (IOException e) {
      throw new BuildException(errorMsg, e);
    } catch (ParserConfigurationException e) {
      throw new BuildException(errorMsg, e);
    } catch (SAXException e) {
      throw new BuildException(errorMsg, e);
    } catch (IllegalArgumentException e) {
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Invalid parameter 'Version' = ", version, "!"));
      throw new BuildException("Error parsing installVersion.", e);
    }

    return new URL(MessageFormat.format("{0}?action=startAppInstall&svnRoot={1}&initPath={2}"
        + "&modVersion={3}&instVersion={4}&deployPath={5}&svnPath={6}&svnVersionInfo={7}"
        + "&login={8}&password={9}",
        getVersionServletUrl(),
        mapXml.getSvnRoot(),
        mapXml.getInitialPath(),
        mapXml.getModuleVersion(),
        installVersion,
        deploymentPath,
        svnPath,
        svnVersionInfo,
        getEncodedLogin(),
        getEncodedPassword()));
  }

  /**
   * Получение служебной информации из map.xml <br/>
   * TODO: Необходим рефакторинг, - обработка исключений (переменная stage), перенести функцию в класс MapXml.
   * @return Описание map.xml
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  private MapXml parseMapXml() throws ParserConfigurationException, SAXException, IOException {

    Document mapXmlDocument = null;
    try{
      mapXmlDocument = JepRiaToolkitUtil.getDOM(JepRiaUtil.isEmpty(mapXmlPath) ? "../Doc/map.xml" : mapXmlPath);
    } catch(IOException io) {
      JepRiaToolkitUtil.echoMessage(
         JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Error parsing map.xml : ", END_OF_LINE, TAB, io.getMessage()));
      throw io;
    }

    Integer stage = 1;
    String svnRoot = null;
    String initPath = null;
    String moduleVersion = null;
    MapXml mapXml = null;
    try {
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();

      XPathExpression expr = xpath.compile("//map/version/text()");
      //версия модуля, взятая из map.xml
      moduleVersion = (String) expr.evaluate(mapXmlDocument, XPathConstants.STRING);
      stage++;

      xpath.reset();
      expr = xpath.compile("//map/path/text()");
      //корень модуля, взятый из map.xml
      svnRoot = ((Node) expr.evaluate(mapXmlDocument, XPathConstants.NODE)).getTextContent();
      stage++;

      xpath.reset();
      expr = xpath.compile("//map/initialPath/text()");
      //начальный путь модуля, взятый из map.xml
      initPath = ((Node) expr.evaluate(mapXmlDocument, XPathConstants.NODE)).getTextContent();
      stage++;

      boolean isFault = false;

      if (JepRiaToolkitUtil.isEmpty(moduleVersion)){
        JepRiaToolkitUtil.echoMessage(
            JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Invalid parameter 'Module Version', check map.xml!"));
        isFault = true;
      }

      if (!isFault) mapXml = new MapXml(svnRoot, initPath, moduleVersion);

    } catch(Exception e) {
      String errorMessageName = new String();
      switch(stage) {
        case 2 : errorMessageName = JepRiaToolkitUtil.multipleConcat("Error getting attribute 'svnRoot' (value - ", svnRoot, ") during parsing map.xml. Please check this file!"); break;
        case 3 : errorMessageName = JepRiaToolkitUtil.multipleConcat("Error getting attribute 'initialPath' (value - ", initPath, ") during parsing map.xml. Please check this file!"); break;
        case 1 :
        default : errorMessageName = JepRiaToolkitUtil.multipleConcat("Error getting attribute 'moduleVersion' (value - ", moduleVersion, ") during parsing map.xml. Please check this file!"); break;
      }
      JepRiaToolkitUtil.echoMessage(JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, errorMessageName));
    }

    return mapXml;
  }

  /**
   * Валидация версии
   *
   * @param version  проверяемая версия
   * @return флаг валидности
   * @throws IllegalArgumentException
   */
  private boolean checkVersion(String version) {
    Pattern pattern = Pattern.compile("^[\\d]+([\\.][\\d]+)*(_[\\w]*)?$");
    boolean flag = !JepRiaUtil.isEmpty(version) && pattern.matcher(version).matches();
    if (!flag) {
      throw new IllegalArgumentException("Not correct version (value="+version+")");
    }
    return flag;
  }

  /**
   * Получение версии из строки.
   *
   * @param version Строка.
   * @return Версия в формате цифр, разделенных точками.
   * @throws IllegalArgumentException
   */
  private String getInstallVersion(String version) {
    if (JepRiaUtil.isEmpty(version)) {
      return null;
    } else {
      if (version.contains("\\"))
        version = version.split("\\")[1];
      if (checkVersion(version))
        return version;
    }
    return null;
  }

  public void setVersion(String version){
    this.version = version;
  }

  public void setSvnVersionInfo(String svnVersionInfo) {
    this.svnVersionInfo = svnVersionInfo;
  }

  public void setSvnPath(String svnPath) {
    this.svnPath = svnPath;
  }
  
  public void setMapXmlPath(String mapXmlPath) {
    this.mapXmlPath = mapXmlPath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void afterVersionServletCall(HttpURLConnection versionServletConnection) {
    String resultId = versionServletConnection.getHeaderField("appInstallResultId");
    PropertyHelper ph = PropertyHelper.getPropertyHelper(getProject());
    ph.setProperty(appInstallResultId, resultId, false);
  }
}