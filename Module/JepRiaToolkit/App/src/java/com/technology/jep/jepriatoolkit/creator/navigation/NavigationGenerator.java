package com.technology.jep.jepriatoolkit.creator.navigation;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.END_OF_LINE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAVIGATION_MENU_DIR_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.NAVIGATION_TEXT_DIR_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.Module;
import com.technology.jep.jepriatoolkit.creator.module.ModuleField;
import com.technology.jep.jepriatoolkit.parser.ApplicationSettingParser;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;

public class NavigationGenerator extends Task {
  
  // объект, хранящий характеристики приложения, извлекаемые из конфигурационного файла приложения  
  private Application application;
  private List<String> forms = new ArrayList<String>();
  // Парсер настроек приложения
  private ApplicationSettingParser applicationParser;
    
  /**
   * Основной метод, который выполняет Task 
   */
  @Override
  public void execute() throws BuildException {
    try{
      // Разбор и проверка <ApplicationName>Definition.xml
      this.applicationParser = ApplicationSettingParser.getInstance();
      
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat("Create Navigation Structure for '", application.getProjectPackage().toLowerCase(), DOT, application.getName().toLowerCase(), "' module"));
          createNavigationFileStructure();
          
      JepRiaToolkitUtil.echoMessage("Create necessary XML file structure");
          createXMLStructure();
          
          JepRiaToolkitUtil.echoMessage("Create Text Resource");
          createTextResource();
    }
    catch(ParserConfigurationException ignore){
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Problem with parsing JepApplication.xml:"
              , END_OF_LINE, ignore.getLocalizedMessage()));
    }
    catch(IOException io){
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, "Problem with storing text resource:"
              , END_OF_LINE, io.getLocalizedMessage()));
    }
  }
  
  /**
   * Создание файловой структуры для навигации
   */
  public void createNavigationFileStructure(){       
      JepRiaToolkitUtil.makeDir(NAVIGATION_MENU_DIR_NAME);
      JepRiaToolkitUtil.makeDir(NAVIGATION_TEXT_DIR_NAME);
    }

  /**
   * Создание xml-документа, хранящего навигационную структуру модуля
   */
  public void createXMLStructure(){
    try{          
        Document doc = JepRiaToolkitUtil.createDOM();
        //root
        Element moduleElement = doc.createElement("menu-definition");
        
        Element catalogElement = doc.createElement("menu");
        catalogElement.setAttribute("name", 
            JepRiaToolkitUtil.multipleConcat("navigation.", application.getProjectPackage().toLowerCase(), DOT, application.getName().toLowerCase()));
        
        for(int i = 0; i < forms.size(); i++){
          String formName = (String)forms.get(i);
          if (!JepRiaToolkitUtil.isEmpty(applicationParser.getMainFormNameIfExist(formName))) continue;
          
          Element menuElement = doc.createElement("menu");
          menuElement.setAttribute("name", 
              JepRiaToolkitUtil.multipleConcat("navigation.", application.getProjectPackage().toLowerCase(), DOT, application.getName().toLowerCase(), DOT, formName.toLowerCase()));
          menuElement.setAttribute("frame", "content");
          
          Map<Module, List<ModuleField>> hm = applicationParser.getModuleWithFieldsById(formName);
        Module module = hm.keySet().iterator().next();
        
        Element rolesElement = null;
        List<String> moduleRoles = module.getModuleRoleNames();
        if (moduleRoles.size() > 0){
          rolesElement = doc.createElement("roles");
          for (String role : moduleRoles){
            Element roleElement = doc.createElement("role");
            roleElement.setTextContent(role);
            rolesElement.appendChild(roleElement);
          }
        }
        
        Element requestElement = doc.createElement("request");
        requestElement.setAttribute("em", formName);
        requestElement.setTextContent(JepRiaToolkitUtil.multipleConcat("../", application.getName()));
        
        menuElement.appendChild(rolesElement);
        menuElement.appendChild(requestElement);
        
        catalogElement.appendChild(menuElement);
        }
        
        moduleElement.appendChild(catalogElement);        
        doc.appendChild(moduleElement);
        
        JepRiaToolkitUtil.prettyPrint(doc, JepRiaToolkitUtil.multipleConcat( 
            NAVIGATION_MENU_DIR_NAME , "/navigation.xml"));
                
      }
      catch(Exception e){
        //e.printStackTrace();
      JepRiaToolkitUtil.echoMessage(
          JepRiaToolkitUtil.multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
      }
  }
  
  /**
   * Создание текстовых ресурсов с наименованиями соответствующих пунктов меню
   * @throws IOException
   */
  public void createTextResource()
    throws IOException {
    
    String content = JepRiaToolkitUtil.multipleConcat("navigation.", application.getProjectPackage().toLowerCase(), DOT,  application.getName().toLowerCase(), "=", application.getName());
    String contentEn = JepRiaToolkitUtil.multipleConcat("navigation.", application.getProjectPackage().toLowerCase(), DOT,  application.getName().toLowerCase(), "=", application.getName());
    
      for(int i = 0; i < forms.size(); i++){
        String formName = (String)forms.get(i);
        if (!JepRiaToolkitUtil.isEmpty(applicationParser.getMainFormNameIfExist(formName))) continue;
        Map<Module, List<ModuleField>> hm = applicationParser.getModuleWithFieldsById(formName);
      Module module = hm.keySet().iterator().next();
        content += JepRiaToolkitUtil.multipleConcat(END_OF_LINE, "navigation.", application.getProjectPackage().toLowerCase(), DOT,  application.getName().toLowerCase(), DOT, formName.toLowerCase(), "=", module.getModuleName());
        contentEn += JepRiaToolkitUtil.multipleConcat(END_OF_LINE, "navigation.", application.getProjectPackage().toLowerCase(), DOT,  application.getName().toLowerCase(), DOT, formName.toLowerCase(), "=" , module.getModuleNameEn());
      }
      
    JepRiaToolkitUtil.writeToFile(content, JepRiaToolkitUtil.multipleConcat(NAVIGATION_TEXT_DIR_NAME, "/Navigation_Source.properties"));
    JepRiaToolkitUtil.writeToFile(contentEn, JepRiaToolkitUtil.multipleConcat(NAVIGATION_TEXT_DIR_NAME, "/Navigation_en.properties"));
  }
}
