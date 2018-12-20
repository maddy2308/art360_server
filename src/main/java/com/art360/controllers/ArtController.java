package com.art360.controllers;

import com.art360.Utilities.ExtraAuthSecurity;
import com.art360.dao.ArtRepository;
import com.art360.exceptions.NotValidActionException;
import com.art360.models.Art;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import javax.validation.Valid;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
public class ArtController {

  private final ArtRepository artRepository;

  public ArtController(ArtRepository artRepository) {
    this.artRepository = artRepository;
  }

  @RequestMapping(value = "artist/{artistId}/art", method = RequestMethod.GET,
      produces = "application/json")
  public List<Art> getAllArtsOfAnArtist(@PathVariable("artistId") String artistId) {
    return this.artRepository.findByArtist(artistId);
  }

  @RequestMapping(value = "/art", method = RequestMethod.POST, produces = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  private Art saveNewArt(@Valid @RequestBody Art art,
                         @RequestHeader(value = "Authorization") String authorization)
      throws NotValidActionException {
    if (ExtraAuthSecurity.isMe(authorization, art.getArtist())) {
      return this.artRepository.save(art);
    } else {
      throw new NotValidActionException(
          "This art doesn't belong to you and so you cannot perform this action");
    }
  }

  @RequestMapping(value = "/art/{id}", method = RequestMethod.PATCH, produces = "application/json")
  private Art updateArt(@RequestHeader(value = "Authorization") String authorization,
                        @PathVariable("id") String id,
                        @Valid @RequestBody Art art) {

    Optional<Art> artToUpdate = this.artRepository.findById(new ObjectId(id));
    final AtomicReference<Art> updatedArt = new AtomicReference<>();
    artToUpdate.ifPresent(artFound -> {
      try {
        updatedArt.set(patchAndSaveArt(authorization, artFound, art));

      } catch (NotValidActionException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "You are not allowed to perform this action", e);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    });
    return updatedArt.get();
  }

  private Art patchAndSaveArt(final String authorization, final Art artFound, Art updatedArt)
      throws NotValidActionException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException {
    if (ExtraAuthSecurity.isMe(authorization, artFound.getArtist())) {
      artRepository.save(updateArt(artFound, updatedArt));
      return artFound;
    } else {
      throw new NotValidActionException(
          "This art doesn't belong to you and so you cannot perform this action");
    }
  }

  private Art updateArt(Art oldRecord, Art newRecord)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    updatePropertyIfNotEmpty(newRecord, "getArtName", oldRecord, "setArtName",
        String.class);
    updatePropertyIfNotEmpty(newRecord, "getArtistName", oldRecord, "setArtistName",
        String.class);
    updatePropertyIfNotEmpty(newRecord, "getArtType", oldRecord, "setArtType", String.class);
    updatePropertyIfNotEmpty(newRecord, "getDescription", oldRecord, "setDescription",
        String.class);
    updatePropertyIfNotEmpty(newRecord, "getIsPublic", oldRecord, "setIsPublic", Boolean.class);
    updatePropertyIfNotEmpty(newRecord, "getUploadedImages", oldRecord, "setUploadedImages",
        List.class);
    oldRecord.setUpdated(String.valueOf(new Date().getTime()));

    return oldRecord;
  }

  private void updatePropertyIfNotEmpty(Art newRecord, String getterMethod, Art oldRecord,
                                        String setterMethod, Serializable serializable)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Object newValue = newRecord.getClass().getMethod(getterMethod).invoke(newRecord);
    if (newValue != null) {
      oldRecord.getClass().getDeclaredMethod(setterMethod, (Class<?>) serializable)
          .invoke(oldRecord, ((Class<?>) serializable).cast(newValue));
    }
  }

  @RequestMapping(value = "art/{id}", method = RequestMethod.DELETE,
      produces = "application/json")
  public ResponseEntity deleteArt(@PathVariable("id") String artId,
                                  @RequestHeader(value = "Authorization") String authorization) {
    Optional<Art> artToDelete = this.artRepository.findById(new ObjectId(artId));
    final AtomicReference<ResponseEntity<String>> entity = new AtomicReference<>();
    artToDelete.ifPresent(artFound -> {
      if (ExtraAuthSecurity.isMe(authorization, artFound.getArtist())) {
        artRepository.delete(artToDelete.get());
        entity.set(new ResponseEntity<>("Art is deleted", null, HttpStatus.OK));
      } else {
        entity.set(new ResponseEntity<>("This art doesn't belong to you", null, HttpStatus.OK));
      }
    });
    return entity.get();
  }

  @RequestMapping(value = "/public_art/", method = RequestMethod.GET, produces = "application/json")
  public List<Art> getAllPublicArt(@RequestParam("isPublic") boolean isPublic) {
//    return this.artRepository.findAllByIsPublic(true);
    return null;
  }
}
