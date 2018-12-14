package com.art360.security;

import com.art360.dao.ArtistRepository;
import com.art360.models.Artist;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private ArtistRepository artistRepository;

  public UserDetailsServiceImpl(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Artist artist = artistRepository.findByEmail(username.toLowerCase());
    if (artist == null) {
      throw new UsernameNotFoundException(username);
    }
    return new User(artist.getEmail(), artist.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority(artist.getRole())));
  }

  String getUserId(String username) {
    Artist artist = artistRepository.findByEmail(username.toLowerCase());
    if (artist == null) {
      throw new UsernameNotFoundException(username);
    } else {
      return artist.get_id();
    }
  }
}