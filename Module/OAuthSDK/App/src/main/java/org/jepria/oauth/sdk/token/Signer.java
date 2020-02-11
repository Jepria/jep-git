package org.jepria.oauth.sdk.token;


import java.text.ParseException;

/**
 * Token Signer interface. Provides methods for generation of Base64 signature for Token claims.
 */
public interface Signer {

  /**
   * Sign token
   * @param token {@link Token}
   * @return {@link Token}
   * @throws ParseException if token is invalid
   */
  Token sign(Token token) throws ParseException;

  /**
   * Get name of Hash Algorithm for signature.
   * @return algorithm
   */
  String getAlgorithm();
}
