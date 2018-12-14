package com.art360.Utilities;

/**
 * @author madhur.mehta
 */
public class ExtraAuthSecurity {

  public static boolean isMe(String token, String id) {
    JWTPayload payload = JWTPayload.decode(token);
    return payload.getUser().equals(id);
  }
}
