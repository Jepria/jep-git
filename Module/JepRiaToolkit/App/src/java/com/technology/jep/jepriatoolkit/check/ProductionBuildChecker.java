package com.technology.jep.jepriatoolkit.check;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.*;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.EXTEND_PROPERTY_MAIN_GWT_XML_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.INHERITS_MAIN_GWT_XML_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.LOG4J_LOGGER_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAME_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PREFIX_DESTINATION_JAVA_CODE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PRODUCTION_BUILD_CHECKER_ERROR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PRODUCTION_BUILD_CONFIG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SET_PROPERTY_FALLBACK_MAIN_GWT_XML_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SET_PROPERTY_MAIN_GWT_XML_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.VALUES_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.VALUE_ATTRIBUTE;
import static org.w3c.dom.Node.ELEMENT_NODE;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.technology.jep.jepriatoolkit.switcher.BuildConfigSwitcher;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class ProductionBuildChecker extends Task {
  
  //атрибуты таска
  private String packageName, moduleName, packagePrefixAsPath;
  //выходной атрибут, куда помещается результат выполненных проверок
  private String property = "";

  PropertyHelper ph;
  /**
   * Основной метод, который выполняет данный Task 
   */
  @Override
  public void execute() throws BuildException {
    String currentConfig = BuildConfigSwitcher.getCurrentConfigName();
    boolean isProductionBuild = isProductionBuild();
    
    if (PRODUCTION_BUILD_CONFIG_NAME.equalsIgnoreCase(currentConfig) && !isProductionBuild) {
      throw new BuildException("PRODUCTION build not valid: " + ph.getProperty(PRODUCTION_BUILD_CHECKER_ERROR).toString());
    }
  }
  
  /**
   * Метод проверки приложения на продукционность сборки
   * 
   * @return  флаг продукционности текущей сборки
   */
  public boolean isProductionBuild(){
    if (packagePrefixAsPath == null) {
      packagePrefixAsPath = PACKAGE_PREFIX_AS_PATH_DEFAULT;
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(WARNING_PREFIX, "The 'packagePrefixAsPath' attribute was not specified when calling ant, will set default: ", packagePrefixAsPath));
    }
    
    ph = PropertyHelper.getPropertyHelper(getProject());
    String mainGwtXml = JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_JAVA_CODE, "/", packagePrefixAsPath.toLowerCase(), "/", packageName.toLowerCase() , "/" , moduleName.toLowerCase() , "/main/" , moduleName , ".gwt.xml");
    try {
      //--проверка <Application>.gwt.xml
      Document mainGwtXmlDocument = JepRiaToolkitUtil.getDOM(mainGwtXml);
      NodeList nodes = mainGwtXmlDocument.getElementsByTagName(INHERITS_MAIN_GWT_XML_TAG_NAME);
      if (!JepRiaToolkitUtil.isEmpty(nodes)) {
        for (int counter = 0; counter < nodes.getLength(); counter++){
          Node tempNode = nodes.item(counter);
          if (tempNode.getNodeType() == ELEMENT_NODE) {
            Element node = (Element) tempNode;
            if (node.getAttribute(NAME_ATTRIBUTE).equals("com.allen_sauer.gwt.log.gwt-log-RemoteLogger")) {
              ph.setProperty(property, false, true);
              ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "There exists XML-node 'inherits' with attribute 'name' which has value 'com.allen_sauer.gwt.log.gwt-log-RemoteLogger'! For production build you should switch log off!");
              return false;
            }
          }
        }
      }
      nodes = mainGwtXmlDocument.getElementsByTagName(EXTEND_PROPERTY_MAIN_GWT_XML_TAG_NAME);
      if (!JepRiaToolkitUtil.isEmpty(nodes)) {
        boolean hasRuLocale = false;
        boolean hasEnLocale = false;
        for (int counter = 0; counter < nodes.getLength(); counter++){
          Node tempNode = nodes.item(counter);
          if (tempNode.getNodeType() == ELEMENT_NODE) {
            Element node = (Element) tempNode;
            String nameAttribute = node.getAttribute(NAME_ATTRIBUTE);
            String valuesAttribute = node.getAttribute(VALUES_ATTRIBUTE);
            if (nameAttribute.equals("log_level")) {
              ph.setProperty(property, false, true);
              ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "There exists XML-node 'extend-property' with attribute 'name' which has value 'log_level'! For production build you should switch log off!");
              return false;
            }
            else if (nameAttribute.equals("locale")){
              if (valuesAttribute.equals("ru")) {
                hasRuLocale = true;
              }
              else if (valuesAttribute.equals("en")) {
                hasEnLocale = true;
              }
            }
          }
        }
        if (!hasRuLocale && !hasEnLocale){
          ph.setProperty(property, false, true);
          ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "For production build you have to define locales (Russian and English)!");
          return false;
        }
      }
      nodes = mainGwtXmlDocument.getElementsByTagName(SET_PROPERTY_MAIN_GWT_XML_TAG_NAME);
      if (!JepRiaToolkitUtil.isEmpty(nodes)) {
        for (int counter = 0; counter < nodes.getLength(); counter++){
          Node tempNode = nodes.item(counter);
          if (tempNode.getNodeType() == ELEMENT_NODE) {
            Element node = (Element) tempNode;
            String nameAttribute = node.getAttribute(NAME_ATTRIBUTE); 
            if (nameAttribute.equals("log_DivLogger") || 
                nameAttribute.equals("log_RemoteLogger") ||
                  nameAttribute.equals("user.agent")) {
              ph.setProperty(property, false, true);
              ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "There exists XML-node 'set-property' with attribute 'name' which has value 'log_DivLogger' or 'log_RemoteLogger' or 'user.agent'! For production build you should switch log off and compile module for all browsers!");
              return false;
            }
          }
        }
      }
      nodes = mainGwtXmlDocument.getElementsByTagName(SET_PROPERTY_FALLBACK_MAIN_GWT_XML_TAG_NAME);
      if (!JepRiaToolkitUtil.isEmpty(nodes)) {
        boolean hasLocale = false;
        for (int counter = 0; counter < nodes.getLength(); counter++){
          Node tempNode = nodes.item(counter);
          if (tempNode.getNodeType() == ELEMENT_NODE) {
            Element node = (Element) tempNode;
            String nameAttribute = node.getAttribute(NAME_ATTRIBUTE); 
            if (nameAttribute.equals("locale") && node.getAttribute(VALUE_ATTRIBUTE).equals("ru")) {
              hasLocale = true;
              break;
            }
          }
        }
        if (!hasLocale){
          ph.setProperty(property, false, true);
          ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "There doesn't exist XML-node 'set-property-fallback' with attribute 'name' which has value 'locale' and attribute 'value' which has value 'ru'! For production build you have to define locales (Russian and English)!");
          return false;
        }
      }
      else {
        ph.setProperty(property, false, true);
        ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "There doesn't exist XML-node 'set-property-fallback' with attribute 'name' which has value 'locale' and attribute 'value' which has value 'ru'! For production build you have to define locales (Russian and English)!");
        return false;
      }
      //--- проверка log4j.properties на вхождение DEBUG или TRACE в параметры, начинающиеся "с log4j.logger."  
      Properties log4jProperties = new Properties();
      
      log4jProperties.load(new FileInputStream(JepRiaToolkitUtil.multipleConcat(PREFIX_DESTINATION_JAVA_CODE, "/log4j.properties")));
      
      String propVal;
      for (String propName : log4jProperties.stringPropertyNames()) {
        if (propName.startsWith(LOG4J_LOGGER_PREFIX)) {
          propVal = log4jProperties.getProperty(propName);
          
          if (propVal != null &&
              (propVal.toLowerCase().contains("debug") 
              || propVal.toLowerCase().contains("trace"))) {
            
            ph.setProperty(property, false, true);
            ph.setNewProperty(PRODUCTION_BUILD_CHECKER_ERROR, "For production build you have to define 'INFO' level for server logs!");
            return false; 
          } 
        }
      }
      
      ph.setProperty(property, true, true);
      return true;
    }
    catch (ParserConfigurationException e) {
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", mainGwtXml, " can't be parsed!"));
      ph.setProperty(property, false, true);
    }
    catch (IOException e) {
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", e.getLocalizedMessage()));
      ph.setProperty(property, false, true);
    } 
    catch (SAXException e) {
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "File ", mainGwtXml, " is not valid!! Check and correct it!"));
      ph.setProperty(property, false, true);
    }
    catch (NullPointerException e){
      ph.setProperty(property, false, true);
    }
    return false;
  }
  
  public void setPackagePrefixAsPath(String packagePrefixAsPath) {
    this.packagePrefixAsPath = packagePrefixAsPath;
  }
  
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }
  
  /**
     * Set the name of the property which will be set if JepRia-application meets all requirements
     * for production build.
     *
     * @param property the name of the property to set.
     */
    public void setProperty(String property) {
        this.property = property;
    }
}
