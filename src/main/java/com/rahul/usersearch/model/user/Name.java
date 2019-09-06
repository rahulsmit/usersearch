
package com.rahul.usersearch.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "first",
    "last"
})
public class Name {

    @JsonProperty("title")
    public String title;
    @JsonProperty("first")
    public String first;
    @JsonProperty("last")
    public String last;

}
