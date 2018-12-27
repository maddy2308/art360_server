package com.art360.dao;

import com.art360.models.Art;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ArtRepository extends MongoRepository<Art, ObjectId> {

  List<Art> findByArtist(String artistId);

  List<Art> findAllByIsPublicOrIsPublicNull(boolean isPublic);
}
