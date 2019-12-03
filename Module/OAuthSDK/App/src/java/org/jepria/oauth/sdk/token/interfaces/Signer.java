package org.jepria.oauth.sdk.token.interfaces;

import com.nimbusds.jose.util.Base64URL;

/**
 * Token Signer interface. Provides methods for generation of Base64 signature for Token claims.
 */
public interface Signer {
  /**
   * Sign provided token {@link org.jepria.oauth.sdk.token.interfaces.Token}.</br>
   * <b>Method MAY mutate incoming token entity.</b>
   *
   * @param token token entity {@link org.jepria.oauth.sdk.token.interfaces.Token}
   * @return Base64 representation of signature
   */
  Base64URL sign(Token token);

  /**
   * Get name of Hash Algorithm for signature.
   * @return algorithm
   */
  String getAlgorithm();
}
