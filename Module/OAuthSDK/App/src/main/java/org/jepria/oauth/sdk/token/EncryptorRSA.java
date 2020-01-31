package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWT;
import org.jepria.oauth.sdk.token.interfaces.Encryptor;
import org.jepria.oauth.sdk.token.interfaces.Token;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

public class EncryptorRSA implements Encryptor {

  private static final String ALGORITHM = "RSA";
  private final RSAPublicKey key;
  private final RSAEncrypter encrypter;

  public EncryptorRSA(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] byteKey = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
    KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
    this.key = (RSAPublicKey) kf.generatePublic(spec);
    encrypter = new RSAEncrypter(key);
  }

  public EncryptorRSA(RSAPublicKey publicKey) {
    this.key = publicKey;
    encrypter = new RSAEncrypter(publicKey);
  }

  @Override
  public String[] encrypt(String header, String payload) {
    JWECryptoParts parts;
    try {
      JWEHeader jweHeader = JWEHeader.parse(Base64URL.from(header));
      parts = encrypter.encrypt(jweHeader, new Payload(Base64URL.from(payload)).toBytes());
    } catch (Throwable th) {
      th.printStackTrace();
      throw new RuntimeException(th);
    }
    String[] result = new String[5];
    result[0] = parts.getHeader().toBase64URL().toString();
    result[1] = parts.getEncryptedKey().toString();
    result[2] = parts.getInitializationVector().toString();
    result[3] = parts.getCipherText().toString();
    result[4] = parts.getAuthenticationTag().toString();
    return result;
  }

  @Override
  public String getAlgorithm() {
    return JWEAlgorithm.RSA_OAEP_256.getName();
  }

  @Override
  public String getEncryptionMethod() {
    return EncryptionMethod.A256GCM.getName();
  }
}
