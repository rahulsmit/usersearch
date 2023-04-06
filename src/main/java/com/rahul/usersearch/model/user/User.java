
package com.rahul.usersearch.model.user;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "id",
    "uid",
    "password",
    "first_name",
    "last_name",
    "username",
    "email",
    "avatar",
    "gender",
    "phone_number",
    "social_insurance_number",
    "date_of_birth",
    "employment",
    "address",
    "credit_card",
    "subscription"
})
@Generated("jsonschema2pojo")
public class User {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("password")
    private String password;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("social_insurance_number")
    private String socialInsuranceNumber;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    @JsonProperty("employment")
    private Employment employment;
    @JsonProperty("address")
    private Address address;
    @JsonProperty("credit_card")
    private CreditCard creditCard;
    @JsonProperty("subscription")
    private Subscription subscription;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("uid")
    public String getUid() {
        return uid;
    }

    @JsonProperty("uid")
    public void setUid(String uid) {
        this.uid = uid;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("first_name")
    public String getFirstName() {
        return firstName;
    }

    @JsonProperty("first_name")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("social_insurance_number")
    public String getSocialInsuranceNumber() {
        return socialInsuranceNumber;
    }

    @JsonProperty("social_insurance_number")
    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    @JsonProperty("date_of_birth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("date_of_birth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("employment")
    public Employment getEmployment() {
        return employment;
    }

    @JsonProperty("employment")
    public void setEmployment(Employment employment) {
        this.employment = employment;
    }

    @JsonProperty("address")
    public Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonProperty("credit_card")
    public CreditCard getCreditCard() {
        return creditCard;
    }

    @JsonProperty("credit_card")
    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @JsonProperty("subscription")
    public Subscription getSubscription() {
        return subscription;
    }

    @JsonProperty("subscription")
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

}
