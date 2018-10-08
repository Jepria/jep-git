<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.technology.jep.jepria.client.JepRiaClientConstant" %>
<%@ page import="com.technology.jep.jepria.client.widget.LoadingProgress" %>
<%@ page import="com.technology.jep.jepria.client.widget.DisabledLayer" %>
<%@ page import="com.technology.jep.jepria.server.util.JepServerUtil" %>
<%@ page import="com.technology.jep.jepria.server.JepRiaServerConstant" %>
<%@ page import="java.util.ResourceBundle" %>

<% ResourceBundle jepRiaText = ResourceBundle.getBundle(JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME, JepServerUtil.getLocale(request)); %>

<% 
// имя приложения определяет context path и имена js-файлов
String appName = config.getServletContext().getInitParameter("app.name");
if (appName == null) {
  // получим имя приложения по умолчанию из context path
  appName = request.getContextPath().substring(1);
}
//заголовок приложения
String appTitle = config.getServletContext().getInitParameter("app.title");
if (appTitle == null) {
  // если не указан явно, то это название приложения
  appTitle = config.getServletContext().getInitParameter("app.name");
  if (appTitle == null) {
    // заголовок по умолчанию это имя приложения из context path
    appTitle = request.getContextPath().substring(1);
  }
}
%>

<html style="width: 100%; height: 100%;">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title><%= appTitle %></title>
    
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                 -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="<%= appName %>/<%= appName %>.nocache.js"></script>
    <script>
// загрузка JSON с поддержкой старых браузеров
function loadJSON(file, callback) {
  var xhr = new XMLHttpRequest();
  xhr.overrideMimeType("application/json");
  xhr.open('GET', file, true); 
  xhr.onreadystatechange = function () {
    if (xhr.readyState == 4 && xhr.status == "200") {
      callback(xhr.responseText);
    }
  };
  xhr.send();
}
// асинхронно подгружаем JSON с инф о сборке
loadJSON("./actuator/version.json", function(response) {
  var versionJSON = JSON.parse(response);
  // ищем плашку для вывода текста
  if (document.getElementById('testBuildMessage')){
    var myClasses = document.getElementsByClassName("jepRia-testBuildMessageInfo");
    var library='';
    for(var key in versionJSON.library){
        library+=key + ':' + versionJSON.library[key]+' ';
    }
    myClasses[0].innerHTML='This is test '+versionJSON.svn.repo_name+'/'+versionJSON.svn.module_name+' build!<br>Build at: '+versionJSON.compile.time_stamp+'<br>'+
    'Based on SVN: '+versionJSON.svn.tag_version+'/'+versionJSON.svn.revision+'<br>'+
    'Library: '+library+'<br>'+
    'Developer: '+versionJSON.compile.user_name+' '+versionJSON.compile.host_name+
    '<br><small>'+versionJSON.compile.UUID+'</small>';
  }
  console.info(versionJSON);
});
    </script>
  </head>

  <body style="margin: 0px; padding: 0px; width: 100%; height: 100%;">
    
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position: absolute; width: 0; height: 0; border: 0;"></iframe>
    
    <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
    <noscript>
      <div class="jepRia-noJavaScriptEnabledMessage"><%= jepRiaText.getString("noJavaScriptEnabledMessage") %></div>
    </noscript>
    
    <div id="testBuildMessage" class="<%= JepRiaClientConstant.TEST_BUILD_MESSAGE_CLASS %>"> 
      <div class="jepRia-testBuildMessageNotification error"> 
        <div class="jepRia-testBuildMessageClose" onclick="document.getElementById('testBuildMessage').style.display = 'none';">
          ×
        </div> 
        <div class="jepRia-testBuildMessageHeader">
          Attention please!
        </div> 
        <div class="jepRia-testBuildMessageInfo">
          This is test build!
        </div> 
      </div>
    </div>

    <table style="width: 100%; height: 100%; border: 0px; table-layout: fixed; border-collapse: collapse; margin: 0px; padding: 0px;">
      <tr>
        <td style="width: 100%; height: 100%;">
          <div id="<%= JepRiaClientConstant.APPLICATION_SLOT %>" style="width: 100%; height: 100%; position: relative;"></div>
        </td>
      </tr>
    </table>
    
    <div id="<%= LoadingProgress.LOADING_PROGRESS_ID %>" class="jepRia-fullScreenContainer">
      <div class="jepRia-fullScreenContainerCenter jepRia-loadingProgress">
          <img class="jepRia-loadingProgressImg" src="images/loading.gif" width="32" height="32" alt="Loading..."/>
            <div>
              <p>
                <span id="<%= LoadingProgress.LOADING_HEADER_ID %>"><%= appTitle %></span>
              </p>
            <span id="<%= LoadingProgress.LOADING_MESSAGE_ID %>" class="jepRia-loadingMessage"><%= jepRiaText.getString("loadingMessage") %></span>
          </div>
      </div>
    </div>
    <div class="<%= DisabledLayer.DISABLED_LAYER_STYLE %>" id="<%= DisabledLayer.DISABLED_LAYER_ID %>"></div>
    
    <!-- According to HTML5 Specification we can place link and style tags in any place inside <BODY> -->
    <!-- For that purpose we should use attribute 'property' -->
    <!-- It allows us to guarantee that all our styles will be applied in correct order without replacing GWT styles-->
    <link type="text/css" rel="stylesheet" property='stylesheet' href="css/JepRia.css" />
    <link type="text/css" rel="stylesheet" property='stylesheet' href="css/<%= appName %>.css" />
  </body>
</html>
