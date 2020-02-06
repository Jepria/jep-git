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
import java.util.Objects;

/**
 * Verifier for RSA signed JWS tokens.
 * Checks signature validity by public key.
 */
public class VerifierRSA extends VerifierBase {

  private final SignatureVerifierRSA signatureVerifier;

  public VerifierRSA(List<String> aud, String iss, Date expiryDate, String publicKey) {
    super(aud, iss, expiryDate);
    signatureVerifier = new SignatureVerifierRSA(publicKey);
  }

  public VerifierRSA(List<String> aud, String iss, Date expiryDate, RSAPublicKey publicKey) {
    super(aud, iss, expiryDate);
    signatureVerifier = new SignatureVerifierRSA(publicKey);
  }

  @Override
  public boolean verify(Token token) {
    Objects.requireNonNull(token);
    return super.verify(token)
      && signatureVerifier.verify(token);
  }
}
