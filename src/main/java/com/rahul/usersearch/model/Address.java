package com.rahul.usersearch.model;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "uid",
        "city",
        "street_name",
        "street_address",
        "secondary_address",
        "building_number",
        "mail_box",
        "community",
        "zip_code",
        "zip",
        "postcode",
        "time_zone",
        "street_suffix",
        "city_suffix",
        "city_prefix",
        "state",
        "state_abbr",
        "country",
        "country_code",
        "latitude",
        "longitude",
        "full_address"
})
@Generated("jsonschema2pojo")
public class Address {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("uid")
    public String uid;
    @JsonProperty("city")
    public String city;
    @JsonProperty("street_name")
    public String streetName;
    @JsonProperty("street_address")
    public String streetAddress;
    @JsonProperty("secondary_address")
    public String secondaryAddress;
    @JsonProperty("building_number")
    public String buildingNumber;
    @JsonProperty("mail_box")
    public String mailBox;
    @JsonProperty("community")
    public String community;
    @JsonProperty("zip_code")
    public String zipCode;
    @JsonProperty("zip")
    public String zip;
    @JsonProperty("postcode")
    public String postcode;
    @JsonProperty("time_zone")
    public String timeZone;
    @JsonProperty("street_suffix")
    public String streetSuffix;
    @JsonProperty("city_suffix")
    public String citySuffix;
    @JsonProperty("city_prefix")
    public String cityPrefix;
    @JsonProperty("state")
    public String state;
    @JsonProperty("state_abbr")
    public String stateAbbr;
    @JsonProperty("country")
    public String country;
    @JsonProperty("country_code")
    public String countryCode;
    @JsonProperty("latitude")
    public Float latitude;
    @JsonProperty("longitude")
    public Float longitude;
    @JsonProperty("full_address")
    public String fullAddress;

}