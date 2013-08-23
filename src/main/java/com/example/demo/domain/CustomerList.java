package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class for returning list of customers. 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerList {

  @XmlElement(name = "customer")
  private List<Customer> customers = new ArrayList<Customer>();

  public CustomerList() {
  }

  public CustomerList(List<Customer> customers) {
    this.customers = customers;
  }

  public List<Customer> getCustomers() {
    return customers;
  }

  public void setCustomers(List<Customer> customers) {
    this.customers = customers;
  }
}