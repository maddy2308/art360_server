package com.art360.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "ArtistInformation")
@Builder
public class Artist {

  @Id
  @JsonProperty("_id")
  private String _id;

  @JsonProperty("email")
  @Size(min = 10, message = "Email should be atleast 10 characters")
  @Email(message = "Passed value is not a well formed email")
  private String email;

  @JsonProperty("password")
  @NotEmpty(message = "Password can not be empty")
  @Size(min = 8, message = "Password should be atleast 8 characters")
  private String password;

  @NotEmpty(message = "You need to provide a name")
  @JsonProperty("name")
  private String name;

  @JsonProperty("address")
  private String address;

  @JsonProperty("aboutMe")
  private String aboutMe;

  @JsonProperty("dob")
  private Date dob;

  @JsonProperty("lastSeen")
  @Builder.Default
  private Date lastSeen = new Date();

  @JsonProperty("isActive")
  @Builder.Default
  private boolean isActive = true;

  @JsonProperty("displayPic")
  private List<String> displayPic;

  @Builder.Default
  @JsonProperty("role")
  private String role = "USER";

  @PersistenceConstructor
  public Artist(String _id,
                @Size(min = 10, message = "Email should be atleast 10 characters")
                @Email(message = "Passed value is not a well formed email") String email,
                @NotEmpty(message = "Password can not be empty") @Size(min = 8, message = "Password should be atleast 8 characters") String password,
                @NotEmpty(message = "You need to provide a name") String name,
                String address, String aboutMe, Date dob, Date lastSeen,
                Boolean isActive, List<String> displayPic, String role) {
    this._id = _id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.address = address;
    this.aboutMe = aboutMe;
    this.dob = dob == null ? new Date() : dob;
    this.lastSeen = lastSeen == null ? new Date() : lastSeen;
    this.isActive = isActive == null || isActive;
    this.displayPic = displayPic;
    this.role = role;
  }
}
