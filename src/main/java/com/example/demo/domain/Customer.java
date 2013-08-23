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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.Assert;

/**
 * A customer.
 */
@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer extends AbstractEntity {

  /**
   * name
   */
  @Column(nullable = false)
  private String name;
  
  /**
   * telephone nunumber
   */
  private String telephoneNumber;
  
  /**
   * fax number
   */
  private String faxNumber;

  /**
   * List of addresses. No duplicate.
   */
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "customer_id")
  @XmlElementWrapper(name="addresses")
  @XmlElement(name="address")
  private Set<Address> addresses = new HashSet<Address>();
  
  public void setName(String name) {
    Assert.hasText(name, "Name must not be null or empty!");

    this.name = name;
  }

  public String getName() {
    return name;
  }

  /**
   * Adds the given {@link Address} to the {@link Customer}.
   * 
   * @param address
   *          must not be {@literal null}.
   */
  public void add(Address address) {

    Assert.notNull(address);
    this.addresses.add(address);
  }

  /**
   * Return the {@link Customer}'s addresses.
   * 
   * @return
   */
  public Set<Address> getAddresses() {
    return Collections.unmodifiableSet(addresses);
  }

  public String getFaxNumber() {
    return faxNumber;
  }

  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  public void setFaxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
  }
  
  public void update(Customer updated) {
    this.name = updated.name;
    this.faxNumber = updated.faxNumber;
    this.telephoneNumber = updated.telephoneNumber;
    this.addresses.clear();
    this.addresses.addAll(updated.addresses);
  }

}
