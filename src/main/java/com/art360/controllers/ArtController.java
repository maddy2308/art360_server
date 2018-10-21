package com.art360.controllers;

import com.art360.dao.ArtRepository;
import com.art360.models.Art;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ArtController {

  private ArtRepository artRepository;

  public ArtController(ArtRepository artRepository) {
    this.artRepository = artRepository;
  }

  @RequestMapping(value = "/art/{artistId}", method = RequestMethod.GET, produces = "application/json")
  public List<Art> getAllArts(@PathVariable("artistId") ObjectId artistId) {
    return this.artRepository.findByArtist(artistId);
  }

  @RequestMapping(value = "/art", method = RequestMethod.POST, produces = "application/json")
  private Art saveNewArt(@Valid @RequestBody Art art) {
    return this.artRepository.save(art);
  }
}
