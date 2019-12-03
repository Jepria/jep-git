package org.jepria.oauth.sdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * Random, unguessable, CSRF token string, for Replay-attacks prevention.
 */
public class State {

  private final String state;

  public State() {
    String state = null;
    try {
      MessageDigest cryptoProvider = MessageDigest.getInstance("SHA-256");
      UUID randomUuid = UUID.randomUUID();
      cryptoProvider.update(randomUuid.toString().getBytes());
      state = Base64.getUrlEncoder().withoutPadding().encodeToString(cryptoProvider.digest());
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      state = null;
    } finally {
      this.state = state;
    }
  }

  @Override
  public String toString() {
    return state;
  }
}
