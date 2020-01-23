package org.jepria.oauth.sdk.token.interfaces;

/**
 * Token verifier interface
 */
public interface Verifier {

  /**
   * Verify provided token {@link org.jepria.oauth.sdk.token.interfaces.Token}.</br>
   * <b>Method MAY mutate incoming token entity.</b>
   *
   * @param token token entity {@link org.jepria.oauth.sdk.token.interfaces.Token}
   * @return verification result
   */
  boolean verify(Token token);
}
