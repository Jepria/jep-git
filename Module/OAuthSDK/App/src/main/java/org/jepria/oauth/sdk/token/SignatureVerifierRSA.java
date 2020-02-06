package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.jepria.oauth.sdk.token.interfaces.Token;
import org.jepria.oauth.sdk.token.interfaces.Verifier;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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
  public boolean verify(Token token) {
    SignedJWT tokenObject = (SignedJWT) ((TokenImpl)token).token;
    try {
      return tokenObject.verify(verifier);
    } catch (JOSEException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

}
