package com.art360.controllers;

import com.art360.Utilities.ExtraAuthSecurity;
import com.art360.dao.ArtRepository;
import com.art360.exceptions.NotValidActionException;
import com.art360.models.Art;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class ArtController {

  private final ArtRepository artRepository;

  public ArtController(ArtRepository artRepository) {
    this.artRepository = artRepository;
  }

  @RequestMapping(value = "artist/{artistId}/art", method = RequestMethod.GET,
      produces = "application/json")
  public List<Art> getAllArts(@PathVariable("artistId") String artistId) {
    return this.artRepository.findByArtist(artistId);
  }

  @RequestMapping(value = "/art", method = RequestMethod.POST, produces = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  private Art saveNewArt(@Valid @RequestBody Art art) {
    return this.artRepository.save(art);
  }

  @RequestMapping(value = "/art/{id}", method = RequestMethod.PATCH, produces = "application/json")
  private Art updateArt(@RequestHeader(value = "authorization") String authorization,
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
}
