package com.art360.dao;

import com.art360.models.ArtistInformation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<ArtistInformation, String> {

  ArtistInformation findArtistByEmail(String email);
}
