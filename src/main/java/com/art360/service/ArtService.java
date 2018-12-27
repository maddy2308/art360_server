package com.art360.service;

import com.art360.dao.ArtRepository;
import com.art360.models.Art;
import com.art360.utility.ExtraAuthSecurity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author madhur.mehta
 */

@Service
public class ArtService {

  private final ArtRepository artRepository;

  @Autowired
  public ArtService(final ArtRepository artRepository) {
    this.artRepository = artRepository;
  }

  public List<Art> findByArtist(String artistId) {
    return this.artRepository.findByArtist(artistId);
  }

  public Art save(Art art) {
    return this.artRepository.save(art);
  }

  public List<Art> findAllPublicArt(boolean isPublic) {
    return this.artRepository.findAllByIsPublicOrIsPublicNull(isPublic);
  }

  public ResponseEntity<String> deleteArt(String authorization, String artId) {
    final AtomicReference<ResponseEntity<String>> entity = new AtomicReference<>();
    Optional<Art> artToDelete = this.artRepository.findById(new ObjectId(artId));
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

  public Art patchArt(String artId, Art changedArt) {
    Optional<Art> artToUpdate = this.artRepository.findById(new ObjectId(artId));
    final AtomicReference<Art> updatedArt = new AtomicReference<>();
    artToUpdate.ifPresent(artFound -> {
      try {
        artRepository.save(updateArt(artFound, updatedArt.getAndSet(changedArt)));
        updatedArt.getAndSet(artFound);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    });
    return updatedArt.get();
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
