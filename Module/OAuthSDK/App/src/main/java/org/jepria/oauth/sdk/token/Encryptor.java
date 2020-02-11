package org.jepria.oauth.sdk.token;

import java.text.ParseException;

public interface Encryptor {

  /**
   * Encrypt token object
   * @param token {@link Token}
   * @return {@link Token}
   * @throws ParseException if token is invalid object
   */
  Token encrypt(Token token) throws ParseException;

  String getAlgorithm();

  String getEncryptionMethod();
}
