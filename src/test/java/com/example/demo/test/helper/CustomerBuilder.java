package com.example.demo.test.helper;

import java.util.HashSet;
import java.util.Set;

import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.domain.Address;
import com.example.demo.domain.Customer;

/**
 * Builder Class for Customer.
 *
 */
public class CustomerBuilder {
  private Long id;
  private String name;
  private String telephoneNumber;
  private String faxNumber;
  private Set<Address> addresses = new HashSet<Address>();

  public CustomerBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public CustomerBuilder name(String name) {
    this.name = name;
    return this;
  }

  public CustomerBuilder telephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
    return this;
  }

  public CustomerBuilder faxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
    return this;
  }

  public Customer build() {
    Customer customer = new Customer();

    ReflectionTestUtils.setField(customer, "id", id);
    customer.setFaxNumber(faxNumber);
    customer.setName(name);
    customer.setTelephoneNumber(telephoneNumber);
    ReflectionTestUtils.setField(customer, "addresses", addresses);
    return customer;
  }
}
