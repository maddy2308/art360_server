package com.art360.controllers;

import com.art360.dao.ArtistRepository;
import com.art360.models.ArtistInformation;
import com.art360.models.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import javax.validation.Valid;

@ControllerAdvice
@Controller
public class ArtistController extends ResponseEntityExceptionHandler {

  @Autowired
  private ArtistRepository artistRepository;

  @GetMapping("/artist")
  @ResponseBody
  public ArtistInformation getArtistByEmail(@RequestParam(name = "email") String email) {
    return artistRepository.findArtistByEmail(email);
  }

  @PostMapping("/artist")
  @ResponseBody
  public ArtistInformation saveArtist(@Valid @RequestBody ArtistInformation artistInformation) {
    return artistRepository.save(artistInformation);
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
