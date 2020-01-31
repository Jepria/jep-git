package org.jepria.oauth.sdk.token.interfaces;

/**
 * Token Object Decryptor
 */
public interface Decryptor {

  /**
   * Decrypt token parts.
   * @param header JWE Header Base64 Url encoded String
   * @param encryptedKey Base64 Url encoded String
   * @param initializationVector Base64 Url encoded String
   * @param cipherText Base64 Url encoded String
   * @param authenticationTag Base64 Url encoded String
   * @return token payload bytes
   */
  byte[] decrypt(String header, String encryptedKey, String initializationVector, String cipherText, String authenticationTag);
}
