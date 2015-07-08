<!DOCTYPE html>
 
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.technology.jep.jepria.client.JepRiaClientConstant" %>
 
<html style="width: 100%; height: 100%;">
	<head>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
		<link type="text/css" rel="stylesheet" href="css/JepRia.css">
		<link type="text/css" rel="stylesheet" href="css/<%ModuleName%>.css">
		
		<title><%ModuleName%> Module</title>
		<script type="text/javascript" language="javascript" src="<%ModuleName%>/<%ModuleName%>.nocache.js"></script>
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
					
					<div id="testBuildMessage" class="testBuildMessage"> 
			            <div class="testBuildMessage-notification error"> 
			                <div class="testBuildMessage-close" onclick="document.getElementById('testBuildMessage').style.display = 'none';">
			                    X
			                </div> 
			                <div class="testBuildMessage-header">
			                    Attention please!
			                </div> 
			                <div class="testBuildMessage-message">
			                    This is test build!
			                </div> 
			            </div> 
			        </div>
					
					<div id="loading">
						<div class="loading-indicator">
							<img src="images/loading.gif" width="32" height="32" alt="Loading..."/>
							<div>
								<p style="margin:0px;">
									<span id="loading-header"><%ModuleName%></span>
								</p>
								<span id="loading-msg">Loading&nbsp;Application,&nbsp;please&nbsp;wait...</span>
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
	</body>
</html>
