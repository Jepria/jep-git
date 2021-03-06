package org.jepria.oauth.sdk.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jepria.oauth.sdk.token.rsa.DecryptorRSA;
import org.jepria.oauth.sdk.token.rsa.EncryptorRSA;
import org.jepria.oauth.sdk.token.rsa.SignerRSA;
import org.jepria.oauth.sdk.token.rsa.VerifierRSA;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.*;

public class TokenImplTest {

  static String publicKeyStr;
  static String privateKeyStr;

  static {
    try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");kpg.initialize(2048);
      KeyPair kp = kpg.generateKeyPair();
      Base64.Encoder encoder = Base64.getEncoder();
      publicKeyStr = encoder.encodeToString(kp.getPublic().getEncoded());
      privateKeyStr = encoder.encodeToString(kp.getPrivate().getEncoded());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  @Test
  void plainToken() throws ParseException, NoSuchAlgorithmException {

    /**
     * Generate uuid for token ID
     */
    MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
    UUID randomUuid = UUID.randomUUID();
    cryptoProvider.update(randomUuid.toString().getBytes());
    String tokenId = Base64.getUrlEncoder().encodeToString(cryptoProvider.digest());

    String subject = "subject";
    String issuer = "issuer";
    Date expiryDate = addHours(new Date(), 8);
    Date issueDate = new Date();

    Token token = new TokenImpl(tokenId, Collections.EMPTY_LIST, subject,
      issuer, expiryDate, issueDate, null, null);

    String tokenString = token.asString();

    Token token2 = TokenImpl.parseFromString(tokenString);

    Verifier verifier = new VerifierBase(null, issuer, new Date());
    assertTrue(verifier.verify(token2));
  }

  @Test
  void signedToken() throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException {
    /**
     * Generate uuid for token ID
     */
    MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
    UUID randomUuid = UUID.randomUUID();
    cryptoProvider.update(randomUuid.toString().getBytes());
    String tokenId = Base64.getUrlEncoder().encodeToString(cryptoProvider.digest());

    String subject = "subject";
    String issuer = "issuer";
    Date expiryDate = addHours(new Date(), 8);
    Date issueDate = new Date();

    Token token = new TokenImpl(tokenId, Collections.EMPTY_LIST, subject,
      issuer, expiryDate, issueDate, null, null);

    /**
     * Sign token with private key
     */
    Signer signer = new SignerRSA(privateKeyStr);
    token = signer.sign(token);

    String tokenString = token.asString();
    Token token2 = TokenImpl.parseFromString(tokenString);

    Verifier verifier = new VerifierRSA(null, issuer, new Date(), publicKeyStr);
    assertTrue(verifier.verify(token2));
  }

  @Test
  void encryptedPlainToken() throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException {

    /**
     * Generate uuid for token ID
     */
    MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
    UUID randomUuid = UUID.randomUUID();
    cryptoProvider.update(randomUuid.toString().getBytes());
    String tokenId = Base64.getUrlEncoder().encodeToString(cryptoProvider.digest());

    String subject = "subject";
    String issuer = "issuer";
    Date expiryDate = addHours(new Date(), 8);
    Date issueDate = new Date();

    Token token = new TokenImpl(tokenId, Collections.EMPTY_LIST, subject,
      issuer, expiryDate, issueDate, null, null);
    Encryptor encryptor = new EncryptorRSA(publicKeyStr);

    token = encryptor.encrypt(token);

    String tokenString = token.asString();

    Token token2 = TokenImpl.parseFromString(tokenString);

    Decryptor decryptor = new DecryptorRSA(privateKeyStr);
    token2 = decryptor.decrypt(token2);

    Verifier verifier = new VerifierBase(null, issuer, new Date());
    assertTrue(verifier.verify(token2));
  }


  @Test
  void ecnryptedSignedToken() throws InvalidKeySpecException, NoSuchAlgorithmException, ParseException {
    /**
     * Generate uuid for token ID
     */
    MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
    UUID randomUuid = UUID.randomUUID();
    cryptoProvider.update(randomUuid.toString().getBytes());
    String tokenId = Base64.getUrlEncoder().encodeToString(cryptoProvider.digest());

    String subject = "subject";
    String issuer = "issuer";
    Date expiryDate = addHours(new Date(), 8);
    Date issueDate = new Date();

    Token token = new TokenImpl(tokenId, Collections.EMPTY_LIST, subject,
      issuer, expiryDate, issueDate, null, null);

    /**
     * Sign token with private key
     */
    Signer signer = new SignerRSA(privateKeyStr);
    token = signer.sign(token);

    /**
     * Encrypt token with public key
     */
    Encryptor encryptor = new EncryptorRSA(publicKeyStr);
    token = encryptor.encrypt(token);
    String encryptedToken = token.asString();

    Token token2 = TokenImpl.parseFromString(encryptedToken);
    Decryptor decryptor = new DecryptorRSA(privateKeyStr);
    token2 = decryptor.decrypt(token2);
    Verifier verifier = new VerifierRSA(null, issuer, new Date(), publicKeyStr);
    assertTrue(verifier.verify(token2));
  }



  private static Date addHours(Date date, int hours) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR_OF_DAY, hours);
    return calendar.getTime();
  }

}
