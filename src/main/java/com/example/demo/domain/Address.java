/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.Assert;

/**
 * An address.
 * 
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Address extends AbstractEntity {

  @Column(nullable = false)
  private String street;
  
  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String country;

  /**
   * Creates a new {@link Address} from the given street, city and country.
   * 
   * @param street
   *          must not be {@literal null} or empty.
   * @param city
   *          must not be {@literal null} or empty.
   * @param country
   *          must not be {@literal null} or empty.
   */
  public Address(String street, String city, String country) {

    Assert.hasText(street, "Street must not be null or empty!");
    Assert.hasText(city, "City must not be null or empty!");
    Assert.hasText(country, "Country must not be null or empty!");

    this.street = street;
    this.city = city;
    this.country = country;
  }

  protected Address() {

  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }
}
