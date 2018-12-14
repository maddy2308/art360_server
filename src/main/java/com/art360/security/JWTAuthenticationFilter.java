package com.art360.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.art360.security.SecurityConstants.*;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;
  private UserDetailsServiceImpl userDetailsService;

  JWTAuthenticationFilter(
      final AuthenticationManager authenticationManager,
      final UserDetailsServiceImpl userDetailsService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
                                              HttpServletResponse res) throws AuthenticationException {
    try {
      CustomUser user = new ObjectMapper().readValue(req.getInputStream(), CustomUser.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              user.getUsername(),
              user.getPassword(),
              new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
                                          HttpServletResponse res,
                                          FilterChain chain,
                                          Authentication auth) {

    String id = userDetailsService.getUserId(((User) auth.getPrincipal()).getUsername());
    String token = JWT.create()
        .withSubject(id)
        .withClaim("role", auth.getAuthorities().toArray()[0].toString())
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        .sign(HMAC512(SECRET.getBytes()));
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }

  @Getter
  @Setter
  private static class CustomUser {
    private String username;
    private String password;

    public CustomUser() {
    }
  }
}