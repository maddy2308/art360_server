package com.art360.controllers;

import com.art360.dao.ArtistRepository;
import com.art360.models.Artist;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@ControllerAdvice
@Controller
public class ArtistController {

  private ArtistRepository artistRepository;

  public ArtistController(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  @RequestMapping(value = "/artist", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody List<Artist> getAllArtists() {
    return this.artistRepository.findAll();
  }

  @RequestMapping(value = "/artist/{id}", method = RequestMethod.GET, produces = "application/json")
  public Optional<Artist> getArtistById(@PathVariable("id") ObjectId id) {
    return this.artistRepository.findById(id);
  }


}
