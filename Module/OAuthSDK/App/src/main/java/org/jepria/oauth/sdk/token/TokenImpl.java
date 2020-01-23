package org.jepria.oauth.sdk.token;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.SignedJWT;

import org.jepria.oauth.sdk.token.interfaces.Signer;
import org.jepria.oauth.sdk.token.interfaces.Token;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class TokenImpl implements Token {

  JWT token;
  Algorithm algorithm;
  protected String tokenString;
  protected String type;
  protected boolean isSigned = false;
  protected boolean isEncrypted = false;

  private TokenImpl() {}

  public TokenImpl(String tokenId, List<String> aud, String sub, String iss, Date expiryDate) {
    if (tokenId == null || aud == null || aud.size() == 0 || sub == null || iss == null || expiryDate == null) {
      throw new IllegalArgumentException("All token claims must be not null and not empty");
    }
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
      .jwtID(tokenId)
      .subject(sub)
      .issuer(iss)
      .audience(aud)
      .expirationTime(expiryDate)
      .build();
    token = new PlainJWT(claimsSet);
    algorithm = Algorithm.NONE;
    type = "JWT";
  }

  TokenImpl(JWT token, String tokenString) {
    if (token instanceof SignedJWT) {
      type = "JWS";
    } else {
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
    try {
      return token.getJWTClaimsSet().getJWTID();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public List<String> getAudience() {
    try {
      return token.getJWTClaimsSet().getAudience();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getSubject() {
    try {
      return token.getJWTClaimsSet().getSubject();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String getIssuer() {
    try {
      return token.getJWTClaimsSet().getIssuer();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Date getExpiryDate() {
    try {
      return token.getJWTClaimsSet().getExpirationTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public boolean isSigned() {
    return isSigned;
  }

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
    result = 37 * result + getExpiryDate().hashCode();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TokenImpl)) return false;
    TokenImpl tokenObject = (TokenImpl) o;
    if (!this.token.equals(tokenObject.token)) return false;
    if (!this.getAudience().equals(tokenObject.getAudience())) return false;
    if (!this.getSubject().equals(tokenObject.getSubject())) return false;
    if (!this.getIssuer().equals(tokenObject.getIssuer())) return false;
    if (!this.getExpiryDate().equals(tokenObject.getExpiryDate())) return false;
    return true;
  }

  @Override
  public void sign(Signer signer) {
    if (isSigned) {
      return;
    }
    try {
      token = new SignedJWT(
        new JWSHeader.Builder(JWSAlgorithm.parse(signer.getAlgorithm())).build(),
        token.getJWTClaimsSet());
      signer.sign(this);
      tokenString = token.serialize();
      isSigned = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Token parseFromString(String tokenString) throws ParseException, IllegalArgumentException {
    int partsCount = 0;
    try {
      partsCount = tokenString.split("\\.").length;
    } catch (Throwable th) {
      th.printStackTrace();
    } finally {
      if (partsCount == 0 || tokenString == null) {
        throw new IllegalArgumentException("\\'" + tokenString + "\\'" + "\nThe string does not match the expected format.");
      }
    }
    switch (partsCount) {
      case 2 : return new TokenImpl(PlainJWT.parse(tokenString), tokenString);
      case 3 : return new TokenImpl(SignedJWT.parse(tokenString), tokenString);
      default : return null;
    }
  }

}
