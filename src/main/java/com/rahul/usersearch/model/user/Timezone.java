
package com.rahul.usersearch.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "offset",
    "description"
})
public class Timezone {

    @JsonProperty("offset")
    public String offset;
    @JsonProperty("description")
    public String description;

}