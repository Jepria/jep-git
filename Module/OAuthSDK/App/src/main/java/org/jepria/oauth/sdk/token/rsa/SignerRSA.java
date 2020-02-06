package org.jepria.oauth.sdk.token.rsa;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64URL;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.jepria.oauth.sdk.token.TokenImpl;
import org.jepria.oauth.sdk.token.Signer;
import org.jepria.oauth.sdk.token.Token;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

/**
 * Signer for token based on RSA crypto algorithm with public/private key pair.
 */
public class SignerRSA implements Signer {

  private static final String ALGORITHM = "RSA";
  final RSASSASigner signer;

  public SignerRSA(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] byteKey = Base64.getDecoder().decode(privateKeyString);
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(byteKey);
    KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
    RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(spec);
    signer = new RSASSASigner(privateKey);
  }

  public SignerRSA(RSAPrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    signer = new RSASSASigner(privateKey);
  }

  public String[] sign(String payload) {
    String[] result = new String[3];
    try {
      JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256).build();
      byte[] signingInput = (header.toBase64URL().toString() + '.' + Base64URL.from(payload).toString()).getBytes(Charset.forName("UTF-8"));
      result[0] = header.toBase64URL().toString();
      result[1] = payload;
      result[2] = signer.sign(header, signingInput).toString();
    } catch (Throwable e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    return result;
  }

  @Override
  public Token sign(Token token) throws ParseException {
    if (token == null) {
      throw new NullPointerException();
    }
    if (token.isSigned()) {
      throw new IllegalStateException("Token is already signed");
    }
    if (token.isEncrypted()) {
      throw new IllegalStateException("Encrypted tokens are not available dor signing");
    }
    SignedJWT jws = new SignedJWT(
      new JWSHeader.Builder(JWSAlgorithm.RS256).build(),
      JWTClaimsSet.parse(new Payload(Base64URL.from(token.asString().split("\\.")[1])).toJSONObject()));
    try {
      jws.sign(signer);
      return TokenImpl.parseFromString(jws.serialize());
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getAlgorithm() {
    return JWSAlgorithm.RS256.getName();
  }
}
