package com.art360.Utilities;

import static com.art360.security.SecurityConstants.TOKEN_PREFIX;

import com.art360.security.SecurityConstants;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;

@Getter
public class JWTPayload {

  String user, role;

  private JWTPayload(final String user, final String role) {
    this.user = user;
    this.role = role;
  }

  public static JWTPayload decode(String token) {
    DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
        .build()
        .verify(token.replace(TOKEN_PREFIX, ""));
    return new JWTPayload(decodedJWT.getSubject(), "ROLE_" + decodedJWT.getClaim("role").asString());
  }
}
