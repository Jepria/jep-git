<!DOCTYPE html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.technology.jep.jepria.client.JepRiaClientConstant" %>

<html style="width: 100%; height: 100%;">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    
    <!--                                           -->
    <!-- Any title is fine                         -->
    <!--                                           -->
    <title>${moduleName} Module</title>
    
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                 -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="${moduleName}/${moduleName}.nocache.js"></script>
  </head>

  <!--                                           -->
  <!-- The body can have arbitrary html, or       -->
  <!-- you can leave the body empty if you want   -->
  <!-- to create a completely dynamic UI.         -->
  <!--                                           -->
  <body style="margin: 0px; padding: 0px; width: 100%; height: 100%;">

    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position: absolute; width: 0; height: 0; border: 0;"></iframe>
    
    <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>
    <div id="testBuildMessage" class="jepRia-testBuildMessage"> 
            <div class="jepRia-testBuildMessageNotification error"> 
                <div class="jepRia-testBuildMessageClose" onclick="document.getElementById('testBuildMessage').style.display = 'none';">
                    X
                </div> 
                <div class="jepRia-testBuildMessageHeader">
                    Attention please!
                </div> 
                <div class="jepRia-testBuildMessageInfo">
                    This is test build!
                </div> 
            </div> 
        </div>
    <div id="loading" class="jepRia-loadingProgress">
      <div class="jepRia-loadingIndicator">
        <img src="images/loading.gif" width="32" height="32" alt="Loading..."/>
        <div>
          <p>
            <span id="loadingHeader">${moduleName}</span>
          </p>
          <span id="loadingMessage" class="jepRia-loadingMessage">Loading&nbsp;Application,&nbsp;please&nbsp;wait...</span>
        </div>
      </div>
    </div>
  
    <table style="border: 0px; table-layout: fixed; border-collapse: collapse; margin: 0px; padding: 0px; width: 100%; height: 100%;">
      <tr>
        <td style="width: 100%; height: 100%;">
 
          <div id="<%= JepRiaClientConstant.APPLICATION_SLOT %>" style="width: 100%; height: 100%; position: relative;"></div>

        </td>
      </tr>
    </table>
    
    <!-- According to HTML5 Specification we can place link and style tags in any place inside <BODY> -->
    <!-- For that purpose we should use attribute 'property' -->
    <!-- It allows us to guarantee that all our styles will be applied in correct order without replacing GWT styles-->
    <link type="text/css" rel="stylesheet" property='stylesheet' href="css/JepRia.css" />
    <link type="text/css" rel="stylesheet" property='stylesheet' href="css/${moduleName}.css" />
  </body>
</html>
