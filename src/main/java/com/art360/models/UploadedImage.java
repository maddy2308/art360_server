package com.art360.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class UploadedImage {

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

  @PersistenceConstructor
  public UploadedImage(String originalFilename, String secureURL, String url, boolean placeholder, String etag, String type, long bytes, List<String> tags, String createdAt, String resourceType, String format, int height, int width, String signature, long version, String publicId) {
    this.originalFilename = originalFilename;
    this.secureURL = secureURL;
    this.url = url;
    this.placeholder = placeholder;
    this.etag = etag;
    this.type = type;
    this.bytes = bytes;
    this.tags = tags;
    this.createdAt = createdAt;
    this.resourceType = resourceType;
    this.format = format;
    this.height = height;
    this.width = width;
    this.signature = signature;
    this.version = version;
    this.publicId = publicId;
  }

}