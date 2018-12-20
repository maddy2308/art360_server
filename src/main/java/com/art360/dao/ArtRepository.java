package com.art360.dao;

import com.art360.models.Art;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArtRepository extends MongoRepository<Art, ObjectId> {

    List<Art> findByArtist(String artistId);
}
