package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64URL;

import org.jepria.oauth.sdk.token.interfaces.Signer;
import org.jepria.oauth.sdk.token.interfaces.Token;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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

  @Override
  public Base64URL sign(Token token) {
    Base64URL result = null;
    try {
      JWSObject tokenObject = (JWSObject) ((TokenImpl) token).token;
      tokenObject.sign(signer);
      result = tokenObject.getSignature();
    } catch (JOSEException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public String getAlgorithm() {
    return JWSAlgorithm.RS256.getName();
  }
}
