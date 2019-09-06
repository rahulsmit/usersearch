
package com.rahul.usersearch.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "street",
    "city",
    "state",
    "postcode",
    "coordinates",
    "timezone"
})
public class Location {

    @JsonProperty("street")
    public String street;
    @JsonProperty("city")
    public String city;
    @JsonProperty("state")
    public String state;
    @JsonProperty("postcode")
    public String postcode;
    @JsonProperty("coordinates")
    public Coordinates coordinates;
    @JsonProperty("timezone")
    public Timezone timezone;

}