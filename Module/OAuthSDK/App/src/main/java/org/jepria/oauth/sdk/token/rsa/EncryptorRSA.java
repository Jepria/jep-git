package org.jepria.oauth.sdk.token.rsa;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.jepria.oauth.sdk.token.TokenImpl;
import org.jepria.oauth.sdk.token.Encryptor;
import org.jepria.oauth.sdk.token.Token;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Objects;

public class EncryptorRSA implements Encryptor {

  private static final String ALGORITHM = "RSA";
  private final RSAPublicKey key;
  private final RSAEncrypter encrypter;

  public EncryptorRSA(String publicKey) {
    byte[] byteKey = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
    try {
      KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
      this.key = (RSAPublicKey) kf.generatePublic(spec);
      encrypter = new RSAEncrypter(key);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public EncryptorRSA(RSAPublicKey publicKey) {
    this.key = publicKey;
    encrypter = new RSAEncrypter(publicKey);
  }

  @Override
  public Token encrypt(Token token) throws ParseException {
    Objects.requireNonNull(token);
    if (token.isEncrypted()) {
      throw new IllegalStateException("Token is already encrypted");
    }
    if (token.isSigned()) {
      SignedJWT jws = SignedJWT.parse(token.asString());
      JWEObject jweObject = new JWEObject(
        new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
          .contentType("JWT") // required to indicate nested JWT
          .build(),
        new Payload(jws));
      try {
        jweObject.encrypt(encrypter);
        return TokenImpl.parseFromString(jweObject.serialize());
      } catch (JOSEException e) {
        throw new RuntimeException(e);
      }
    } else {
      JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM)
        .build();
      String[] tokenParts = token.asString().split("\\.");
      JWTClaimsSet claimsSet = JWTClaimsSet.parse(new Payload(Base64URL.from(tokenParts[1])).toJSONObject());
      EncryptedJWT jwe = new EncryptedJWT(jweHeader, claimsSet);
      try {
        jwe.encrypt(encrypter);
        return TokenImpl.parseFromString(jwe.serialize());
      } catch (JOSEException e) {
        throw new RuntimeException(e);
      }
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
