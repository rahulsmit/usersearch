
package com.rahul.usersearch.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "large",
    "medium",
    "thumbnail"
})
public class Picture {

    @JsonProperty("large")
    public String large;
    @JsonProperty("medium")
    public String medium;
    @JsonProperty("thumbnail")
    public String thumbnail;

}
