
package com.rahul.usersearch.model.user;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cc_number"
})
@Generated("jsonschema2pojo")
public class CreditCard {

    @JsonProperty("cc_number")
    private String ccNumber;

    @JsonProperty("cc_number")
    public String getCcNumber() {
        return ccNumber;
    }

    @JsonProperty("cc_number")
    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

}
