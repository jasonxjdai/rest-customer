package com.example.demo.test.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.Charset;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.http.MediaType;

import com.example.demo.domain.Address;
import com.example.demo.domain.Customer;

/**
 * Utilities for Test.
 */
public class TestUtil {
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
      MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  /**
   * customer id which can be found.
   */
  public static final Long CUSTOMER_ID = Long.valueOf(2);
  
  /**
   * customer id which cannot be found
   */
  public static final Long CUSTOMER_ID_NONEXIST = Long.valueOf(10);


  public static Address createMockAddress() {
    return new AddressBuilder().city("fooCity").country("fooCountry")
        .street("fooStreet").build();
  }

  public static Customer createObject() {
    Customer customer = new CustomerBuilder().name("foo").faxNumber("123")
        .telephoneNumber("456").build();
    customer.add(createMockAddress());

    return customer;
  }

  public static Customer createUpdatedObject() {
    Customer customer = new CustomerBuilder().id(1L).name("foo_updated")
        .faxNumber("123_updated").telephoneNumber("456_updated").build();
    customer.add(createMockAddress());
    return customer;
  }

  /**
   * create object with id.
   * 
   * @return customer with id
   */
  public static Customer createModelObject() {
    Customer customer = new CustomerBuilder().id(1L).name("foo")
        .faxNumber("123").telephoneNumber("456").build();
    return customer;
  }

  public static void assertCustomer(Customer expected, Customer actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getFaxNumber(), expected.getFaxNumber());
    assertEquals(expected.getTelephoneNumber(), expected.getTelephoneNumber());
    assertEquals(expected.getAddresses(), expected.getAddresses());
  }

  public static void assertCustomerNoId(Customer actual, Customer expected) {
    assertTrue(actual.getId() > 0);
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getFaxNumber(), expected.getFaxNumber());
    assertEquals(expected.getTelephoneNumber(), expected.getTelephoneNumber());
    assertEquals(expected.getAddresses(), expected.getAddresses());
  }

  public static byte[] convertObjectToJsonBytes(Object object)
      throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    return mapper.writeValueAsBytes(object);
  }

}
