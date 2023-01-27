
package com.rahul.usersearch.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "gender",
    "name",
    "location",
    "email",
    "login",
    "dob",
    "registered",
    "phone",
    "cell",
    "id",
    "picture",
    "nat"
})
public class User {

    @JsonProperty("gender")
    public String gender;
    @JsonProperty("name")
    public Name name;
    @JsonProperty("location")
    public Location location;
    @JsonProperty("email")
    public String email;
    @JsonProperty("login")
    public Login login;
    @JsonProperty("dob")
    public Dob dob;
    @JsonProperty("registered")
    public Registered registered;
    @JsonProperty("phone")
    public String phone;
    @JsonProperty("cell")
    public String cell;
    @JsonProperty("id")
    public Id id;
    @JsonProperty("picture")
    public Picture picture;
    @JsonProperty("nat")
    public String nat;

}
