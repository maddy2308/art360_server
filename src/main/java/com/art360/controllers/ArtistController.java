package com.art360.controllers;

import com.art360.dao.ArtistRepository;
import com.art360.models.Artist;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@ControllerAdvice
@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class ArtistController {

  private ArtistRepository artistRepository;

  public ArtistController(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @RequestMapping(value = "/artist", method = RequestMethod.GET, produces = "application/json")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public @ResponseBody List<Artist> getAllArtists() {
    return this.artistRepository.findAll();
  }

  @RequestMapping(value = "/artist/{id}", method = RequestMethod.GET, produces = "application/json")
  public Optional<Artist> getArtistById(@PathVariable("id") ObjectId id) {
    return this.artistRepository.findById(id);
  }


}
