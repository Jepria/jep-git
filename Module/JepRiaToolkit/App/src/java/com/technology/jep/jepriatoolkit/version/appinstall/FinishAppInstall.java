package com.technology.jep.jepriatoolkit.version.appinstall;

import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.checkParameter;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;

import org.apache.tools.ant.BuildException;


public class FinishAppInstall extends AppInstall {

  // атрибуты таска
  private String statusCode, errorMessage;

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws BuildException {
    callVersionServlet();
  }
  
  /**
   * {@inheritDoc}
   * @throws UnsupportedEncodingException 
   */
  @Override
  public URL prepareUrl() throws MalformedURLException, UnsupportedEncodingException {

    checkParameter(appInstallResultId, "Incorrect argument: appInstallResultId!");
    
    return new URL(MessageFormat.format(
        "{0}?action=finishAppInstall&appInstallResultId={1}&statusCode={2}&errorMessage={3}&login={4}&password={5}",
        getVersionServletUrl(),
        appInstallResultId,
        statusCode,
        URLEncoder.encode(errorMessage, "UTF-8"),
        getEncodedLogin(),
        getEncodedPassword()));
  }
  
  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}