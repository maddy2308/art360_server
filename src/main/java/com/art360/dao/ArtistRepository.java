package com.art360.dao;

import com.art360.models.Artist;
import com.art360.models.ArtistProjection;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<Artist, ObjectId> {

  Artist findByEmail(String email);

  Artist save(Artist artist);
}
