package com.art360.controller;

import com.art360.exceptions.NotValidActionException;
import com.art360.models.Art;
import com.art360.service.ArtService;
import com.art360.utility.ExtraAuthSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;

/**
 * @author madhur.mehta
 */

@RestController
public class ArtworkController {

  private final ArtService artService;

  @Autowired
  public ArtworkController(final ArtService artService) {
    this.artService = artService;
  }

  @RequestMapping("/artwork")
  public Art saveArt(@RequestHeader(value = "Authorization") String authorization,
                     @RequestBody Art art) throws NotValidActionException {
    if (ExtraAuthSecurity.isMe(authorization, art.getArtist())) {
      return artService.save(art);
    } else {
      throw new NotValidActionException(
          "This art doesn't belong to you and so you cannot perform this action");
    }
  }

  @RequestMapping(value = "/artist/{artistId}/artwork", method = RequestMethod.GET,
      produces = "application/json")
  public List<Art> getAllArtsOfAnArtist(@PathVariable("artistId") String artistId) {
    return artService.findByArtist(artistId);
  }

  @RequestMapping(value = "/artwork/all", method = RequestMethod.GET, produces = "application/json")
  public List<Art> getAllPublicArt() {
    return artService.findAllPublicArt(true);
  }

  @RequestMapping(value = "/artwork/{id}", method = RequestMethod.DELETE,
      produces = "application/json")
  public ResponseEntity deleteArt(@PathVariable("id") String artId,
                                  @RequestHeader(value = "Authorization") String authorization) {
    return artService.deleteArt(authorization, artId);
  }

  @RequestMapping(value = "/artwork/{id}", method = RequestMethod.PUT,
      produces = "application/json")
  private Art updateArt(@PathVariable("id") String id,
                        @RequestHeader(value = "Authorization") String authorization,
                        @Valid @RequestBody Art art) throws NotValidActionException {
    if (isArtMine(authorization, art.getArtist())) {
      return artService.patchArt(id, art);
    } else {
      throw new NotValidActionException(
          "This art doesn't belong to you and so you cannot perform this action");
    }
  }

  private boolean isArtMine(String authorization, String artistId) {
    return ExtraAuthSecurity.isMe(authorization, artistId);
  }

}
