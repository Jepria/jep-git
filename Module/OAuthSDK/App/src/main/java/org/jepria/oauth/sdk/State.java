package org.jepria.oauth.sdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

/**
 * Random, unguessable, CSRF token string, for Replay-attacks prevention.
 */
public class State {

  private final String state;

  public State() {
    try {
      MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
      UUID randomUuid = UUID.randomUUID();
      cryptoProvider.update(randomUuid.toString().getBytes());
      state = Base64.getUrlEncoder().withoutPadding().encodeToString(cryptoProvider.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param additionalValue string will be added to state like : base64Url(state~additionalValue)
   */
  public State(String additionalValue) {
    try {
      MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
      UUID randomUuid = UUID.randomUUID();
      cryptoProvider.update(randomUuid.toString().getBytes());
      state = Base64
        .getUrlEncoder()
        .withoutPadding()
        .encodeToString((Base64
          .getUrlEncoder()
          .withoutPadding()
          .encodeToString(cryptoProvider.digest()) + "~" + additionalValue).getBytes());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return state;
  }
}
