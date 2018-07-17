<!DOCTYPE html>
 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.technology.jep.jepria.client.JepRiaClientConstant" %>
 
<html style="width: 100%; height: 100%;">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
		
		<title><%ModuleName%> Module</title>
		<script type="text/javascript" language="javascript" src="<%ModuleName%>/<%ModuleName%>.nocache.js"></script>
		<script>
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
		</script>
	</head>
 
	<body style="margin: 0px; padding: 0px; width: 100%; height: 100%;">
		<table style="border: 0px; table-layout: fixed; border-collapse: collapse; margin: 0px; padding: 0px; width: 100%; height: 100%;">
			<tr>
				<td style="width: 100%; height: 100%;">
 
					<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position: absolute; width: 0; height: 0; border: 0;"></iframe>
					
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
							This is test <span id="name-testBuildMessageInfo"></span> build!<br>Deploy at: <span id="time_stamp-testBuildMessageInfo"></span><br>
							SVN: <span id="svn-testBuildMessageInfo"></span><br>
							Library: <span id="library-testBuildMessageInfo"></span><br>
							<span id="developer-testBuildMessageInfo"></span>
							<small><span id="UUID-testBuildMessageInfo"></span></small>
							<script>
								loadJSON("./actuator/version.json", function(response) {
								var version_JSON = JSON.parse(response);
								document.getElementById('name-testBuildMessageInfo').innerHTML=version_JSON.svn.repo_name+'/'+version_JSON.svn.module_name;
								document.getElementById('time_stamp-testBuildMessageInfo').innerHTML=version_JSON.compile.time_stamp;
								document.getElementById('svn-testBuildMessageInfo').innerHTML=version_JSON.svn.tag_version+'/'+version_JSON.svn.revision;
								document.getElementById('UUID-testBuildMessageInfo').innerHTML=version_JSON.compile.UUID;
								document.getElementById('developer-testBuildMessageInfo').innerHTML='Developer: '+version_JSON.compile.user_name+' '+version_JSON.compile.host_name;
								var library='';
								for(var key in version_JSON.library){
									library+=key + ':' + version_JSON.library[key]+' ';
								}
								document.getElementById('library-testBuildMessageInfo').innerHTML=library;
								console.info(version_JSON);
								});
							</script>
						</div> 
			            </div> 
			        </div>
					
					<div id="loadingProgress" class="jepRia-loadingProgress">
						<div class="jepRia-loadingIndicator">
							<img src="images/loading.gif" width="32" height="32" alt="Loading..."/>
								<div>
									<p>
										<span id="loadingHeader"><%ModuleName%></span>
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
				</td>
			</tr>
		</table>
		
		<link type="text/css" rel="stylesheet" href="css/JepRia.css">
		<link type="text/css" rel="stylesheet" href="css/<%ModuleName%>.css">
	</body>
</html>
