package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64URL;

import com.nimbusds.jwt.JWT;
import org.jepria.oauth.sdk.token.interfaces.Signer;
import org.jepria.oauth.sdk.token.interfaces.Token;

import java.nio.charset.Charset;
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
  public String getAlgorithm() {
    return JWSAlgorithm.RS256.getName();
  }
}
