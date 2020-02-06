package org.jepria.oauth.sdk.token.rsa;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.jepria.oauth.sdk.token.Token;
import org.jepria.oauth.sdk.token.Verifier;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Objects;

public class SignatureVerifierRSA implements Verifier {

  public static final String ALGORITHM = "RSA";
  protected final RSAPublicKey key;
  protected final JWSVerifier verifier;

  public SignatureVerifierRSA(String publicKey) {
    byte[] byteKey = Base64.getDecoder().decode(publicKey);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
    try {
      KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
      this.key = (RSAPublicKey) kf.generatePublic(spec);
      verifier = new RSASSAVerifier(this.key);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public SignatureVerifierRSA(RSAPublicKey publicKey) {
    this.key = publicKey;
    verifier = new RSASSAVerifier(this.key);
  }

  @Override
  public boolean verify(Token token) throws ParseException {
    Objects.requireNonNull(token);
    if (!token.isSigned()) {
      throw new IllegalArgumentException("Token is not signed");
    }
    SignedJWT tokenObject = SignedJWT.parse(token.asString());
    try {
      return tokenObject.verify(verifier);
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

}
