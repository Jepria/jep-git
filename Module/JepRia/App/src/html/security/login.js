/**
 * LoginHelper provide redirect to SSO module. At the end of file created class instance (loginHelper).
 */
function LoginHelper() {};
LoginHelper.prototype = {
    
  getParameterByName: function(name, url) {
    if (!url) {
      url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
  },
  
  getModuleName: function(url) {
    if(!url) {
      url = window.location.pathname;
    }
    var regex = new RegExp("^\/([^\/]*)"),
      results = regex.exec(url);
    if (!results || !results[1]) return '';
    return results[1];
  },
  
  //TODO: hardcode locale, isError and enterModule (used in SSO) parameters names
  //TODO: hardcode Navigation application name
  redirectToSSO: function(isError) {
    isError = isError || false; //default value for isError is false
    var moduleName = this.getModuleName();
    var locale,
      localeParameterName = 'locale';
    
    //TODO: in Navigation iframe with Welcome.jsp don't have main page locale parameter
    if(moduleName == 'Navigation') {
      locale = this.getParameterByName(localeParameterName, window.parent.location.href);
    } else {
      locale = this.getParameterByName(localeParameterName);
    }
    
    var redirect = "/SSO/SSO.html?em=Login";
    redirect += "&enterModule=" + moduleName;
    
    if(locale !== null) {
      locale = "&" + localeParameterName + "=" + locale;
    } else {
      locale = "";
    }
    redirect += locale;
    
    if(isError) redirect += "&isError=1";
    window.location = redirect;
  },
  
  redirectToSSOError: function() {
    this.redirectToSSO(1);
  }
};

var loginHelper = new LoginHelper();
