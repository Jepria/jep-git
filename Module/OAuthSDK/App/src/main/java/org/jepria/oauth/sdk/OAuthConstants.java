package org.jepria.oauth.sdk;

/**
 * OAuth 2.0 Common Constants
 */
public class OAuthConstants {

  /**
   * Cookie names
   */
  public static final String RFI_OAUTH_TOKEN = "RFI_OAUTH_TOKEN";
  public static final String RFI_OAUTH_PARAMS = "RFI_OAUTH_PARAMS";
  public static final String RFI_OAUTH_CSRF_TOKEN = "RFI_OAUTH_STATE";
  public static final String SESSION_ID = "SID";

  /**
   * Endpoint entrance URI's
   */
  public static final String OAUTH_AUTHORIZATION_CONTEXT_PATH = "/oauth/api/authorize";
  public static final String OAUTH_TOKEN_CONTEXT_PATH = "/oauth/api/token";
  public static final String OAUTH_TOKENINFO_CONTEXT_PATH = "/oauth/api/tokeninfo";
  public static final String OAUTH_TOKENREVOKE_CONTEXT_PATH = "/oauth/api/token/revoke";
  public static final String OAUTH_LOGOUT_CONTEXT_PATH = "/oauth/api/logout";

  /**
   * Query parameters names
   */
  public static final String RESPONSE_TYPE = "response_type";
  public static final String CLIENT_ID = "client_id";
  public static final String CLIENT_NAME = "client_name";
  public static final String CLIENT_SECRET = "client_secret";
  public static final String REDIRECT_URI = "redirect_uri";
  public static final String GRANT_TYPE = "grant_type";
  public static final String STATE = "state";
  public static final String CODE = "code";
  public static final String TOKEN = "token";

  /**
   * Environment properties
   */
  public static final String CLIENT_ID_PROPERTY = "org.jepria.oauth.clientCode";
  public static final String CLIENT_SECRET_PROPERTY = "org.jepria.oauth.clientSecret";

  /**
   * Response constants
   */
  public static final String INVALID_REQUEST = "invalid_request";
  public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
  public static final String UNSUPPORTED_RESPONSE_TYPE = "unsupported_response_type";
  public static final String SERVER_ERROR = "server_error";
  public static final String ACCESS_DENIED = "access_denied";

  /**
   * Query params
   */
  public static final String ERROR_QUERY_PARAM = "error=";
  public static final String ERROR_ID_QUERY_PARAM = "error_id=";
  public static final String ERROR_DESCRIPTION_QUERY_PARAM = "error_description=";
  public static final String ACCESS_TOKEN_QUERY_PARAM = "access_token=";
  public static final String TOKEN_TYPE_QUERY_PARAM = "token_type=";
  public static final String EXPIRES_IN_QUERY_PARAM = "expires_in=";

}
