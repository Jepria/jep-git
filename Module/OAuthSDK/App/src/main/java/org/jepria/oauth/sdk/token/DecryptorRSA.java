package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.util.Base64URL;
import org.jepria.oauth.sdk.token.interfaces.Decryptor;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class DecryptorRSA implements Decryptor {

  private static final String ALGORITHM = "RSA";
  private final RSAPrivateKey key;
  private final RSADecrypter decrypter;

  public DecryptorRSA(String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
    byte[] byteKey = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKey);
    KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
    key = (RSAPrivateKey) kf.generatePrivate(spec);
    decrypter = new RSADecrypter(key);
  }

  public DecryptorRSA(RSAPrivateKey privateKey) {
    key = privateKey;
    decrypter = new RSADecrypter(key);
  }

  @Override
  public byte[] decrypt(String header, String encryptedKey, String initializationVector, String cipherText, String authenticationTag) {
    try {
      return decrypter.decrypt(JWEHeader.parse(Base64URL.from(header)), Base64URL.from(encryptedKey),
        Base64URL.from(initializationVector), Base64URL.from(cipherText), Base64URL.from(authenticationTag));
    } catch (Throwable th) {
      th.printStackTrace();
      throw new RuntimeException(th);
    }
  }
}
