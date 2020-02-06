package org.jepria.oauth.sdk.token.interfaces;

import java.text.ParseException;

/**
 * Token Object Decryptor
 */
public interface Decryptor {

  /**
   * Decrypt token
   * @param token {@link Token}
   * @return {@link Token}
   * @throws ParseException if token is not valid
   */
  Token decrypt(Token token) throws ParseException;

  String getAlgorithm();

  String getEncryptionMethod();
}
