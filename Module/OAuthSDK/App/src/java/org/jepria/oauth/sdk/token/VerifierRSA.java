package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

import org.jepria.oauth.sdk.token.interfaces.Token;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * Verifier for RSA signed JWS tokens.
 * Checks signature validity by public key.
 */
public class VerifierRSA extends VerifierBase {

  public static final String ALGORITHM = "RSA";
  protected final RSAPublicKey key;
  protected final JWSVerifier verifier;

  public VerifierRSA(List<String> aud, String iss, Date expiryDate, String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
    super(aud, iss, expiryDate);
    byte[] byteKey = Base64.getDecoder().decode(key);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(byteKey);
    KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
    this.key = (RSAPublicKey) kf.generatePublic(spec);
    verifier = new RSASSAVerifier(this.key);
  }

  @Override
  public boolean verify(Token token) {
    SignedJWT tokenObject = (SignedJWT)  ((TokenImpl)token).token;
    try {
      return super.verify(token) && tokenObject.verify(verifier);
    } catch (JOSEException e) {
      return false;
    }
  }
}
