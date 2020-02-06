package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.*;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.*;

import org.jepria.oauth.sdk.token.interfaces.Decryptor;
import org.jepria.oauth.sdk.token.interfaces.Encryptor;
import org.jepria.oauth.sdk.token.interfaces.Signer;
import org.jepria.oauth.sdk.token.interfaces.Token;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TokenImpl implements Token {

  protected JWT token;
  protected Algorithm algorithm;
  protected Header header;
  protected JWTClaimsSet claimsSet;
  protected String tokenString;
  protected String type;
  protected boolean isSigned = false;
  protected boolean isEncrypted = false;

  private TokenImpl() {
  }

  public TokenImpl(String tokenId, List<String> aud, String sub, String iss, Date expiryDate, Date issuedAt) {
    if (tokenId == null || sub == null || iss == null || expiryDate == null) {
      throw new IllegalArgumentException("All token claims must be not null and not empty");
    }
    claimsSet = new JWTClaimsSet.Builder()
      .jwtID(tokenId)
      .subject(sub)
      .issuer(iss)
      .audience(aud)
      .expirationTime(expiryDate)
      .issueTime(issuedAt)
      .build();
    token = new PlainJWT(claimsSet);
    algorithm = Algorithm.NONE;
    type = "JWT";
  }

  TokenImpl(JWT token, String tokenString) throws ParseException {
    if (token instanceof SignedJWT) {
      header = token.getHeader();
      algorithm = header.getAlgorithm();
      claimsSet = token.getJWTClaimsSet();
      isSigned = ((SignedJWT) token).getState().equals(JWSObject.State.SIGNED);
      type = "JWS";
    }
    if (token instanceof EncryptedJWT) {
      EncryptedJWT jwt = (EncryptedJWT) token;
      header = jwt.getHeader();
      algorithm = header.getAlgorithm();
      if (jwt.getState().equals(JWEObject.State.DECRYPTED) || jwt.getState().equals(JWEObject.State.UNENCRYPTED)) {
        claimsSet = jwt.getJWTClaimsSet();
        isEncrypted = false;
      } else {
        isEncrypted = true;
      }
      type = "JWE";
    } else {
      claimsSet = token.getJWTClaimsSet();
      type = "JWT";
    }
    this.token = token;
    this.tokenString = tokenString;
  }

  @Override
  public String getType() {
    return type;
  }

  @Override
  public String getAlgorithm() {
    return algorithm.getName();
  }

  @Override
  public String getJti() {
    if (claimsSet == null && isEncrypted) {
      throw new IllegalStateException("Token is encrypted, decrypt token before getting claims");
    }
    return claimsSet.getJWTID();
  }

  @Override
  public List<String> getAudience() {
    if (claimsSet == null && isEncrypted) {
      throw new IllegalStateException("Token is encrypted, decrypt token before getting claims");
    }
    return claimsSet.getAudience();
  }

  @Override
  public String getSubject() {
    if (claimsSet == null && isEncrypted) {
      throw new IllegalStateException("Token is encrypted, decrypt token before getting claims");
    }
    return claimsSet.getSubject();
  }

  @Override
  public String getIssuer() {
    if (claimsSet == null && isEncrypted) {
      throw new IllegalStateException("Token is encrypted, decrypt token before getting claims");
    }
    return claimsSet.getIssuer();
  }

  @Override
  public Date getExpirationTime() {
    if (claimsSet == null && isEncrypted) {
      throw new IllegalStateException("Token is encrypted, decrypt token before getting claims");
    }
    return claimsSet.getExpirationTime();
  }

  @Override
  public Date getIssueTime() {
    if (claimsSet == null && isEncrypted) {
      throw new IllegalStateException("Token is encrypted, decrypt token before getting claims");
    }
    return claimsSet.getIssueTime();
  }

  @Override
  public boolean isSigned() {
    return isSigned;
  }

  @Override
  public boolean isEncrypted() {
    return isEncrypted;
  }

  @Override
  public String asString() {
    if (tokenString != null) {
      return tokenString;
    } else {
      return token.serialize();
    }
  }

  @Override
  public final String toString() {
    return asString();
  }

  @Override
  public int hashCode() {
    int result = 15;
    result = 37 * result + token.hashCode();
    result = 37 * result + getAudience().hashCode();
    result = 37 * result + getSubject().hashCode();
    result = 37 * result + getIssuer().hashCode();
    result = 37 * result + getExpirationTime().hashCode();
    result = 37 * result + getIssueTime().hashCode();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TokenImpl)) return false;
    TokenImpl tokenObject = (TokenImpl) o;
    if (this.token.equals(tokenObject.token)) return true;
    if (!this.getAudience().equals(tokenObject.getAudience())) return false;
    if (!this.getSubject().equals(tokenObject.getSubject())) return false;
    if (!this.getIssuer().equals(tokenObject.getIssuer())) return false;
    if (!this.getExpirationTime().equals(tokenObject.getExpirationTime())) return false;
    if (!this.getIssueTime().equals(tokenObject.getIssueTime())) return false;
    return true;
  }

  /**
   * Parse JWT token string
   *
   * @param tokenString
   * @return {@link Token} parsed token object
   * @throws NullPointerException     when tokenString is null
   * @throws ParseException           when tokensString has wrong format
   * @throws IllegalArgumentException when tokenString is invalid
   */
  public static Token parseFromString(String tokenString) throws NullPointerException, ParseException, IllegalArgumentException {
    Objects.requireNonNull(tokenString);
    int partsCount = 0;
    try {
      partsCount = tokenString.split("\\.").length;
    } catch (Throwable th) {
      th.printStackTrace();
    } finally {
      if (partsCount == 0) {
        throw new IllegalArgumentException("\\'" + tokenString + "\\'" + "\nThe string is invalid.");
      }
      switch (partsCount) {
        case 2:
          return new TokenImpl(PlainJWT.parse(tokenString), tokenString);
        case 3:
          return new TokenImpl(SignedJWT.parse(tokenString), tokenString);
        case 5:
          return new TokenImpl(EncryptedJWT.parse(tokenString), tokenString);
        default:
          return null;
      }
    }
  }

}
