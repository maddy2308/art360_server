package com.art360.controllers;

import com.art360.dao.ArtistRepository;
import com.art360.models.Artist;
import com.art360.models.ArtistProjection;
import com.art360.models.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import java.util.Date;


@ControllerAdvice
@Controller
public class RegistrationController extends ResponseEntityExceptionHandler {
  private ArtistRepository artistRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public RegistrationController(ArtistRepository artistRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.artistRepository = artistRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @PostMapping("/sign-up")
  @ResponseStatus(HttpStatus.CREATED)
  public @ResponseBody ArtistProjection signUp(@Valid @RequestBody Artist artist) {
    artist.setPassword(bCryptPasswordEncoder.encode(artist.getPassword()));
    artist.setRole("USER");
    return artistRepository.save(artist);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status,
                                                                WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
        request.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status.value()));
  }
}
