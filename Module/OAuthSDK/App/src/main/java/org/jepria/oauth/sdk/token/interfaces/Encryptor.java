package org.jepria.oauth.sdk.token.interfaces;

public interface Encryptor {

  /**
   * Encrypt token object
   * @param header Base64 URL encoded token header
   * @param payload Base64 URL encoded token payload
   * @return String array of Base64 URL encoded strings. Size of array is 5. [header, encryptedKey, initialization_vector, cipher_text, authentication_tag]
   */
  String[] encrypt(String header, String payload);

  String getAlgorithm();

  String getEncryptionMethod();
}
