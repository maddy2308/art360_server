package com.art360.models;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CloudinaryUpload {

  private String[] tags;

  private String original_extension;

  private long bytes;

  private String etag;

  private boolean placeholder;

  private long width;

  private String public_id;

  private String format;

  private String type;

  private String original_filename;

  private String url;

  private String version;

  private long height;

  private String resource_type;

  private Date created_at;

  private String signature;

  private String secure_url;

}
