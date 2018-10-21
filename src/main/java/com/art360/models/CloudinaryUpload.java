package com.art360.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
class CloudinaryUpload {

  @Field("original_filename")
  private String originalFilename;

  @Field("secure_url")
  private String secureURL;

  private String url;

  private boolean placeholder;

  private String etag;

  private String type;

  private long bytes;

  private List<String> tags;

  @Field("created_at")
  private String createdAt;

  @Field("resource_type")
  private String resourceType;

  private String format;

  private int height;
  private int width;
  private String signature;
  private long version;

  @Field("public_id")
  private String publicId;

}
