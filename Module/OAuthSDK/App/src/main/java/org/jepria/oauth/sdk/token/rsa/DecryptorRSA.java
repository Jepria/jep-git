package org.jepria.oauth.sdk.token.rsa;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;
import org.jepria.oauth.sdk.token.TokenImpl;
import org.jepria.oauth.sdk.token.Decryptor;
import org.jepria.oauth.sdk.token.Token;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Objects;

public class DecryptorRSA implements Decryptor {

  private static final String ALGORITHM = "RSA";
  private final RSAPrivateKey key;
  private final RSADecrypter decrypter;

  public DecryptorRSA(String privateKey) {
    byte[] byteKey = Base64.getDecoder().decode(privateKey);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKey);
    try {
      KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
      key = (RSAPrivateKey) kf.generatePrivate(spec);
      decrypter = new RSADecrypter(key);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public DecryptorRSA(RSAPrivateKey privateKey) {
    key = privateKey;
    decrypter = new RSADecrypter(key);
  }

  @Override
  public Token decrypt(Token token) throws ParseException {
    Objects.requireNonNull(token);
    if (!token.isEncrypted()) {
      throw new IllegalArgumentException("Token is not encrypted");
    }
    JWEObject jweObject = JWEObject.parse(token.asString());
    if (!jweObject.getHeader().getAlgorithm().equals(JWEAlgorithm.RSA_OAEP_256)
      || !jweObject.getHeader().getEncryptionMethod().equals(EncryptionMethod.A256GCM)) {
      throw new IllegalStateException("Algorithm or Encryption Method mismatch");
    }
    try {
      jweObject.decrypt(decrypter);
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
    if ("JWT".equals(jweObject.getHeader().getContentType())) {
      SignedJWT jws = jweObject.getPayload().toSignedJWT();
      return TokenImpl.parseFromString(jws.serialize());
    } else {
      PlainJWT jwt = new PlainJWT(JWTClaimsSet.parse(jweObject.getPayload().toJSONObject()));
      return TokenImpl.parseFromString(jwt.serialize());
    }
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
