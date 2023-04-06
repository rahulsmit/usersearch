
package com.rahul.usersearch.model.user;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "title",
    "key_skill"
})
@Generated("jsonschema2pojo")
public class Employment {

    @JsonProperty("title")
    private String title;
    @JsonProperty("key_skill")
    private String keySkill;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("key_skill")
    public String getKeySkill() {
        return keySkill;
    }

    @JsonProperty("key_skill")
    public void setKeySkill(String keySkill) {
        this.keySkill = keySkill;
    }

}
