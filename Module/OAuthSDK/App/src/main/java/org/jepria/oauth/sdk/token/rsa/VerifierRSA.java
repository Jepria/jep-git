package org.jepria.oauth.sdk.token.rsa;

import org.jepria.oauth.sdk.token.VerifierBase;
import org.jepria.oauth.sdk.token.Token;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
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
  public boolean verify(Token token) throws ParseException {
    Objects.requireNonNull(token);
    return super.verify(token)
      && signatureVerifier.verify(token);
  }
}
