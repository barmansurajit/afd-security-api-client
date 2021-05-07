package com.schneider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
class SniUser {
    @JsonProperty("upn")
    private String userPrincipalName;
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    private String mobileNumber;
    private String image;
    private String liablePartyId;
    private String liablePartyName;
    private String lastSelectedCustomer;
    private String lastSelectedCarrier;
    private String carrierId;
    private String tractorNumber;
    private boolean carrier;
    private boolean internalUser;
    private boolean customer;
    private boolean registered;
    @JsonProperty("addresses")
    private List<Address> address;
    @JsonProperty("phoneNumberDetails")
    private List<Phone> phone;
}

@Data
class Phone {
    String number;
    @JsonProperty("phoneNumberType")
    String type;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Address {
    private String addressLine1;
    private String addressLine2;
    private String addressType;
    private String city;
    private String state;
    @JsonProperty("postalCode")
    private String postalCode;
}
