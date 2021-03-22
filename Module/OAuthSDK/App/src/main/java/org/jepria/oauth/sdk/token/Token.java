package org.jepria.oauth.sdk.token;

import java.util.Date;
import java.util.List;

/**
 * Basic token interface for JWT, JWS and JWE tokens.
 * See {@linktourl https://tools.ietf.org/html/rfc7519}
 */
public interface Token {

  /**
   * Get token type.
   *
   * @return token type
   */
  String getType();

  /**
   * Get signature algorithm.
   *
   * @return algorithm name, <b>null</b> if token is not signed
   */
  String getAlgorithm();

  /**
   * Get token ID
   *
   * @return token ID
   */
  String getJti();

  /**
   * Get token audience.
   *
   * @return client audience list
   */
  List<String> getAudience();

  /**
   * Get token subject.
   *
   * @return subject
   */
  String getSubject();

  /**
   * Get token issuer.
   *
   * @return issuer
   */
  String getIssuer();
  
  /**
   * Get token username.
   *
   * @return username
   */
  String getUsername();
  
  /**
   * Get token scope.
   *
   * @return scope with ',' delimiter
   */
  String getScope();

  /**
   * Get token expiration time.
   *
   * @return expiration time
   */
  Date getExpirationTime();

  /**
   * Get token issue time.
   *
   * @return issue time
   */
  Date getIssueTime();

  /**
   * Check if token was previously signed.
   *
   * @return signed flag
   */
  boolean isSigned();

  /**
   * Get token in Base64 string representation
   * @return
   */
  String asString();

  /**
   * Check if token was previously encrypted.
   *
   * @return encrypted flag
   */
  boolean isEncrypted();
}
