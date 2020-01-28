package org.jepria.oauth.sdk.token.interfaces;

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
   * Get token expiry date.
   *
   * @return expiry date
   */
  Date getExpiryDate();

  /**
   * Check if token was previously signed.
   *
   * @return signed flag
   */
  boolean isSigned();

  /**
   * Sign token token with provided Signer.
   *
   * @param signer
   */
  void sign(Signer signer);

  /**
   * Get token in Base64 string representation
   * @return
   */
  String asString();
}
