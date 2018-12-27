package com.art360.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ArtInformation")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Art {

  @Id
  private ObjectId id;

  @NotNull(message = "The artist field can not be left empty")
  @NotEmpty(message = "The artist field can not be left empty")
  @Indexed
  @Field("_artist")
  private String artist;

  private String artistName;

  private String artName;

  private String artType;

  private String description;

  private Double rating;

  private List<Heart> hearts;

  private List<Comment> comments;

  private List<String> tags;

  private Boolean isPublic;

  private String availableFrom;

  private String startDate;

  private String created;

  private String updated;

  private List<CloudinaryUpload> uploadedImages;

}
