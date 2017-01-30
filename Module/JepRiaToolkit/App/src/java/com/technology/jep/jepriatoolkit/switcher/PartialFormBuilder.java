package com.technology.jep.jepriatoolkit.switcher;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_CONFIG_FILE_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.BUILD_CONFIG_PATH_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.DOT;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FILE_STRUCTURE_PATTERN;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.FORMS_TASK_ATTRIBUTE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.INFO_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.INHERITS_MAIN_GWT_XML_TAG_NAME;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PATH_SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.PREFIX_DESTINATION_SOURCE_CODE;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.REGEXP_FOR_BLANK;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.SEPARATOR;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WARNING_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.WHITE_SPACE;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getApplicationBySourceCode;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.getModuleDeclarationSuppressException;
import static com.technology.jep.jepriatoolkit.parser.ApplicationStructureParserUtil.saveToFile;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.antClean;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.buildAndDeployWithCustomConfigName;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.convertPatternInRealPath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.currentSourceDirectory;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDOM;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.getDefinitionProperty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmpty;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.isEmptyOrNotInitializedParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.normalizePath;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.prettyPrint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.technology.jep.jepriatoolkit.creator.module.Application;
import com.technology.jep.jepriatoolkit.creator.module.ModuleDeclaration;
import com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil;
import com.technology.jep.jepriatoolkit.util.Pair;

public class PartialFormBuilder extends Task {

  private static final String GET_PLAIN_CLIENT_FACTORY_METHOD_NAME = "getPlainClientFactory";
  private static final String SUFFIX_MODULE_IDENTIFIER = "_module_id";
  private String forms;
  private String jepRiaVersion;
  private String targetConfig;
  
  /**
   * Основной метод, который выполняет переключение PartialFormBuilder
   */
  @Override
  public void execute() throws BuildException {
    if (!isEmptyOrNotInitializedParameter(forms)) {
      List<String> fms = Arrays.asList(forms.split(multipleConcat(SEPARATOR, REGEXP_FOR_BLANK, "*")));
      if (fms.size() == 0){
        echoMessage(multipleConcat(ERROR_PREFIX,
            "Application is not modified! Please specify at least one form to build using parameter '-D", FORMS_TASK_ATTRIBUTE, "' in command line!"));
        return;
      }
        
      Collections.sort(fms);
      Set<String> buildedForms = new HashSet<String>(fms);
      // The information about modules will be given from main.gwt.xml
      String mainGwtXmlPropertyPath = convertPatternInRealPath(getDefinitionProperty(MAIN_GWT_XML_PATH_TEMPLATE_PROPERTY, 
        multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/{2}.gwt.xml")
      ));
      String mainClientFactoryPath = convertPatternInRealPath(getDefinitionProperty(MAIN_MODULE_FACTORY_PATH_TEMPLATE_PROPERTY, 
        multipleConcat(PREFIX_DESTINATION_SOURCE_CODE, "{0}/{1}/main/client/{2}ClientFactoryImpl.java"))
      );
      
      String targetConfigMainGwtXmlPath = multipleConcat(BUILD_CONFIG_PATH_PREFIX, targetConfig, "/", mainGwtXmlPropertyPath);
      try {
        JepRiaToolkitUtil.copyFile(new File(targetConfigMainGwtXmlPath), new File(mainGwtXmlPropertyPath));
      } catch (IOException e) {
        e.printStackTrace();
      }
      String targetConfigMainClientFactoryPath = multipleConcat(BUILD_CONFIG_PATH_PREFIX, targetConfig, "/", mainClientFactoryPath);
      try {
        JepRiaToolkitUtil.copyFile(new File(targetConfigMainClientFactoryPath), new File(mainClientFactoryPath));
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      Application application = getApplicationBySourceCode(jepRiaVersion);
      List<String> applicationForms = new ArrayList<String>(application.getModuleIds());
      applicationForms.retainAll(buildedForms);
      boolean same = applicationForms.size() == buildedForms.size();
      
      if (same) {
        // создадим сперва нужную конфигурацию для частичной сборки
        String newPartialConfigName = multipleConcat("partial", Long.toString(System.currentTimeMillis()));
        File copyOfMainGwtXml = new File(multipleConcat(BUILD_CONFIG_PATH_PREFIX, newPartialConfigName,"/", mainGwtXmlPropertyPath));
        if (!copyOfMainGwtXml.exists()){
          copyOfMainGwtXml.getParentFile().mkdirs();
        }
        
        File copyOfMainClientFactory = new File(multipleConcat(BUILD_CONFIG_PATH_PREFIX, newPartialConfigName,"/", mainClientFactoryPath));
        if (!copyOfMainClientFactory.exists()){
          copyOfMainClientFactory.getParentFile().mkdirs();
        }
        
        applicationForms = new ArrayList<String>(application.getModuleIds());
        applicationForms.removeAll(buildedForms);
        
        ModuleDeclaration mainClientFactory = getModuleDeclarationSuppressException(mainClientFactoryPath);
        List<ImportDeclaration> newImports = new ArrayList<ImportDeclaration>();
        StringBuilder sb = new StringBuilder();
        
        for (ImportDeclaration importDeclaration : mainClientFactory.getImports()){
          boolean exist = false;
          for (String applicationFormToComment : applicationForms){
            if (importDeclaration.toStringWithoutComments().toLowerCase().contains(
                multipleConcat(".", applicationFormToComment.toLowerCase(), "clientfactoryimpl;"))){
              sb.append(importDeclaration.toStringWithoutComments());
              exist = true;
            }
          }
          if (!exist){
            newImports.add(importDeclaration);
          }
        }
        if (!sb.toString().isEmpty()){
          newImports.get(newImports.size() - 1).setComment(new BlockComment(sb.toString()));
        }
        CompilationUnit compilationUnit = mainClientFactory.getCompilationUnit();
        compilationUnit.setImports(newImports);
        
        //TODO: закомментировать и параметры конструктора 
//        ConstructorDeclaration defaultConstructor = mainClientFactory.getConstructors().iterator().next();
//        ConstructorVisitor constructorVisitor = new ConstructorVisitor(applicationForms);
//        defaultConstructor.accept(constructorVisitor, null);
        
        for (MethodDeclaration methodDeclaration : mainClientFactory.getMethods()){
          if (GET_PLAIN_CLIENT_FACTORY_METHOD_NAME.equalsIgnoreCase(methodDeclaration.getName())){
            Statement statement = methodDeclaration.getBody().getStmts().iterator().next();
            PlainClientFactoryVisitor visitor = new PlainClientFactoryVisitor(applicationForms);
            statement.accept(visitor, null);
            methodDeclaration.getBody().setStmts(visitor.getWholeCondition());
            break;
          }
        }
        echoMessage(multipleConcat(INFO_PREFIX, "File '", normalizePath(mainClientFactoryPath), "' was modified"));
        //после модификаций необходимо содержимое пересохранить в файле MainClientFactory
        saveToFile(compilationUnit, mainClientFactoryPath);
        try {
          JepRiaToolkitUtil.copyFile(new File(mainClientFactoryPath), copyOfMainClientFactory);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
                
        List<Pair<String, Element>> moduleNames = getDomModules(mainGwtXmlPropertyPath);
        Document ownerDocument = null;
        for (Pair<String, Element> module : moduleNames){
          String moduleName = module.getKey();
          boolean exist = false;
          for (String form : applicationForms){
            if (form.toLowerCase().equalsIgnoreCase(moduleName)){
              exist = true;
            }
          }
          if (exist){
            if (ownerDocument == null) {
              ownerDocument = module.getValue().getOwnerDocument();
            }
            module.getValue().getParentNode().replaceChild(
              ownerDocument.createComment(
                multipleConcat("<inherits name=\"com.technology.", application.getProjectPackage().toLowerCase(), ".", application.getName().toLowerCase(), ".", moduleName.toLowerCase(), ".", moduleName, "\" />")
              ), module.getValue());
          }
        }
        if (ownerDocument != null)
          try {
            echoMessage(multipleConcat(INFO_PREFIX, "File '", normalizePath(mainGwtXmlPropertyPath), "' was modified"));
            prettyPrint(ownerDocument, mainGwtXmlPropertyPath);
            JepRiaToolkitUtil.copyFile(new File(mainGwtXmlPropertyPath), copyOfMainGwtXml);
          } catch (TransformerConfigurationException e) {
            e.printStackTrace();
          } catch (TransformerException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          } 
        antClean();
        new File(BUILD_CONFIG_FILE_NAME).delete();
        buildAndDeployWithCustomConfigName(newPartialConfigName);
      }
      else {
        buildedForms.removeAll(applicationForms);
        echoMessage(multipleConcat(WARNING_PREFIX, "The listed form", buildedForms.size() > 1 ? "s" : "", " '", buildedForms.toString(), "' ", buildedForms.size() > 1 ? "are" : "is", "n't presented in existing application structure! Please check the file '", normalizePath(mainGwtXmlPropertyPath), "' and specify correct value of the parameter '-D", FORMS_TASK_ATTRIBUTE, "'"));
      }
    }
    else {
      echoMessage(multipleConcat(WARNING_PREFIX, "You should specify what forms you'd like to compile using the parameter '-D", FORMS_TASK_ATTRIBUTE, "' in command line!"));
    }
  }
  
  public void setForms(String forms){
    this.forms = forms;
  }
  
  public void setJepRiaVersion(String jepRiaVersion) {
    this.jepRiaVersion = jepRiaVersion;
  }
  
  public void setTargetConfig(String targetConfig){
    this.targetConfig = targetConfig; 
  }
  
  private static class ConstructorVisitor extends VoidVisitorAdapter<Void> {
    
    String firstArg = null;
    String secondArg = null;
    
    private List<String> applicationFormsToComment;
    
    public ConstructorVisitor(List<String> applicationForms){
      this.applicationFormsToComment = applicationForms;
    }
    
    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Void a) {
      Pattern pattern = Pattern.compile(FILE_STRUCTURE_PATTERN); // all info between curly brackets in two args
      for (Expression argument : n.getArgs()){
        Matcher m = pattern.matcher(argument.toString());
        while (m.find()){
          String list = m.group(1);
          List<String> modules = Arrays.asList(list.split(multipleConcat(SEPARATOR, REGEXP_FOR_BLANK, "*")));
          StringBuilder arg = new StringBuilder();
          StringBuilder comment = new StringBuilder();
          boolean isFirstArg = isEmpty(firstArg);
          
          for (String module : modules){
            module = module.trim();
            boolean exist = false;
            for (String form : applicationFormsToComment){
              String part = isFirstArg ? multipleConcat(form.toLowerCase(), SUFFIX_MODULE_IDENTIFIER) : multipleConcat("submodule_", form.toLowerCase(), "_title");
              if (isFirstArg ? module.toLowerCase().startsWith(part) : module.toLowerCase().contains(part)){
                exist = true;
                break;
              }
            }            
            
            if (exist){
              comment.append(SEPARATOR).append(WHITE_SPACE).append(module);
            }
            else {
              arg.append(!JepRiaToolkitUtil.isEmpty(arg.toString()) ? SEPARATOR.concat(WHITE_SPACE) : "").append(module);
            }
          }
          
          String result = arg.append("/*").append(WHITE_SPACE).append(comment.toString()).append(WHITE_SPACE).append("*/").toString(); 
          if (isFirstArg){
            firstArg = result;
          }
          else {
            secondArg = result;
          }
          
        }
      }
    }
  }
  
  private static class PlainClientFactoryVisitor extends VoidVisitorAdapter<Void> {
    
    private List<IfStmt> presentedForms = new ArrayList<IfStmt>();
    private List<IfStmt> formsToComment = new ArrayList<IfStmt>();
    private List<String> applicationFormsToComment;
    
    public PlainClientFactoryVisitor(List<String> applicationForms){
      this.applicationFormsToComment = applicationForms;
    }
    
    @Override
    public void visit(IfStmt n, Void arg) {
      visitStatement(n);
    }

    private void visitStatement(IfStmt n) {
      Expression condition = n.getCondition();
      boolean exist = false;
      for (String applicationFormToComment : applicationFormsToComment) {
        if (condition.toString().toLowerCase().startsWith(multipleConcat(applicationFormToComment.toLowerCase(), SUFFIX_MODULE_IDENTIFIER))){
          exist = true;
          break;
        }
      }
      
      IfStmt stmt = new IfStmt();
      stmt.setCondition(condition);
      stmt.setThenStmt(n.getThenStmt());
      if (!exist){
        presentedForms.add(stmt);
      }
      else {
        formsToComment.add(stmt);
      }
        
      Statement elseStmt = n.getElseStmt();
      if (elseStmt != null) {
        if (IfStmt.class.isAssignableFrom(elseStmt.getClass())) {
          visitStatement((IfStmt) elseStmt);
        }
      }
      
    }
    
    public List<Statement> getWholeCondition(){
      final IfStmt st = new IfStmt();
      IfStmt lastStatement = null;
      for (Iterator<IfStmt> iterator = presentedForms.iterator(); iterator.hasNext();){
        IfStmt stmt = iterator.next();
        Expression cond = stmt.getCondition();
        Statement then = stmt.getThenStmt();
        if (lastStatement == null) { // first condition
          st.setCondition(cond);
          st.setThenStmt(then);
          lastStatement = st;
        }
        else {
          IfStmt elseSt = new IfStmt();
          elseSt.setCondition(cond);
          elseSt.setThenStmt(then);
          lastStatement.setElseStmt(elseSt);
          lastStatement = elseSt;
        }
      }
      IfStmt lastStatementToComment = null;
      IfStmt stComment = new IfStmt();
      for (IfStmt conditionToComment : formsToComment){
        Expression cond = conditionToComment.getCondition();
        Statement then = conditionToComment.getThenStmt();
        if (lastStatementToComment == null) { // first condition
          stComment.setCondition(cond);
          stComment.setThenStmt(then);
          lastStatementToComment = stComment;
        }
        else {
          IfStmt elseSt = new IfStmt();
          elseSt.setCondition(cond);
          elseSt.setThenStmt(then);
          lastStatementToComment.setElseStmt(elseSt);
          lastStatementToComment = elseSt;
        }
      }
      if (lastStatementToComment != null) {
        st.setComment(new BlockComment(stComment.toString()));
      }
      
      return Arrays.asList((Statement) st);
    }
  }
  
  /**
   * Получение списка модулей приложения из main.gwt.xml
   * 
   * @param mainGwtXmlPath путь до main.gwt.xml
   * 
   * @return список модулей
   */
  public static List<Pair<String, Element>> getDomModules(String mainGwtXmlPath) {
    Document doc;
    List<Pair<String, Element>> result = new ArrayList<Pair<String, Element>>();
    try {
      doc = getDOM(multipleConcat(currentSourceDirectory(), PATH_SEPARATOR, mainGwtXmlPath));
      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();
      XPathExpression expr = xpath.compile(multipleConcat(PATH_SEPARATOR, PATH_SEPARATOR, INHERITS_MAIN_GWT_XML_TAG_NAME, "[starts-with(@name,'com.technology')]"));
      NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
      if (nodes != null){
        for (int i = 0; i < nodes.getLength(); i++){
          Node node = nodes.item(i); 
          if (node.getNodeType() == Node.ELEMENT_NODE){
            Element el = (Element) node;
            String nameAttr = el.getAttribute("name");
            if (nameAttr.startsWith("com.technology") && 
                !nameAttr.equalsIgnoreCase("com.technology.jep.jepria.JepRia")){
              result.add(new Pair<String, Element>(nameAttr.substring(nameAttr.lastIndexOf(DOT) + 1), el));
            }
          }
        }
      }
    } catch (Exception e) {
      echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
    }
    finally {
      doc = null;
    }
    return result;
  }  
}
