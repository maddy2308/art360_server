package com.art360.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class ArtistInformation {

  @Id
  @JsonProperty("_id")
  private String _id;

  @JsonProperty("email")
  @Email(message="Passed value is not a well formed email")
  private String email;

  @JsonProperty("password")
  @NotEmpty(message="Password can not be empty")
  private String password;

  @NotEmpty(message="You need to provide a name")
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
}
