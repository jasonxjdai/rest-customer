package com.example.demo.test.helper;

import com.example.demo.domain.Address;

/**
 * Builder Class for Address.
 */
public class AddressBuilder {
  private String street;
  private String city;
  private String country;

  public AddressBuilder street(String street) {
    this.street = street;
    return this;
  }

  public AddressBuilder city(String city) {
    this.city = city;
    return this;
  }

  public AddressBuilder country(String country) {
    this.country = country;
    return this;
  }

  public Address build() {
    return new Address(street, city, country);
  }
}
