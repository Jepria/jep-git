package org.jepria.oauth.sdk.token;

import java.text.ParseException;

/**
 * Token verifier interface
 */
public interface Verifier {

  /**
   * Verify provided token {@link Token}.</br>
   * <b>Method MAY mutate incoming token entity.</b>
   *
   * @param token token entity {@link Token}
   * @return verification result
   */
  boolean verify(Token token) throws ParseException;
}
