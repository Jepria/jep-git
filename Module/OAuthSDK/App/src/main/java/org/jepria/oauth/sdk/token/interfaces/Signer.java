package org.jepria.oauth.sdk.token.interfaces;


/**
 * Token Signer interface. Provides methods for generation of Base64 signature for Token claims.
 */
public interface Signer {

  /**
   * Sign provided token payload.
   *
   * @param payload Base64 URL encoded token payload
   * @return Base64 URL encoded string array. Size is 3. [Header, Payload, Signature].
   */
  String[] sign(String payload);

  /**
   * Get name of Hash Algorithm for signature.
   * @return algorithm
   */
  String getAlgorithm();
}
