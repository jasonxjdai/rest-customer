package com.example.demo.test.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.demo.domain.Customer;
import com.example.demo.domain.CustomerList;
import com.example.demo.test.helper.CustomerBuilder;
import com.example.demo.test.helper.TestUtil;

public class CustomerSystemTest {
  private static final String REST_SERVICE_URL = "http://localhost:8080/customer/customers";
  private static RestTemplate restTemplate;

  @BeforeClass
  public static void beforeClass() {
    restTemplate = new RestTemplate();
  }

  @Test
  public void create() {
    createAndAssertCustomer();
  }

  @Test
  public void read() {
    Customer createdCustomer = createAndAssertCustomer();
    Customer customer = restTemplate.getForObject(REST_SERVICE_URL
        + "/{customerId}", Customer.class, createdCustomer.getId());
    TestUtil.assertCustomer(customer, createdCustomer);
  }

  @Test
  public void update() {
    Customer customer = createAndAssertCustomer();
    customer.setName("Updated customer name");
    restTemplate.put(REST_SERVICE_URL + "/{customerId}", customer,
        customer.getId());
    Customer updatedCustomer = restTemplate.getForObject(REST_SERVICE_URL
        + "/{customerId}", Customer.class, customer.getId());
    TestUtil.assertCustomer(updatedCustomer, customer);
  }

  @Test
  public void updateIncorrectUrl() {
    Customer customer = createAndAssertCustomer();
    customer.setName("Updated customer name");
    try {
      restTemplate.put(REST_SERVICE_URL + "/{customerId}", customer,
          customer.getId() + 1);
      fail("Expecting HttpClientErrorException: 400 Bad Request");
    }
    catch (HttpClientErrorException e) {
      assertEquals(e.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
  }

  @Test
  public void delete() {
    Customer createdCustomer = createAndAssertCustomer();
    restTemplate.delete(REST_SERVICE_URL + "/{customerId}",
        createdCustomer.getId());
    try {
      restTemplate.getForObject(REST_SERVICE_URL + "/{customerId}",
          Customer.class, createdCustomer.getId());
      fail("Expecting HttpClientErrorException: 400 Bad Request");
    }
    catch (HttpClientErrorException e) {
      assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
    }
  }

  @Test
  public void list() {
    CustomerList initialCustomers = restTemplate.getForObject(REST_SERVICE_URL,
        CustomerList.class);
    Customer createdCustomer = createAndAssertCustomer();
    CustomerList customers = restTemplate.getForObject(REST_SERVICE_URL,
        CustomerList.class);
    List<Customer> createdCustomers = new ArrayList<Customer>(
        customers.getCustomers());
    createdCustomers.removeAll(initialCustomers.getCustomers());
    assertEquals(createdCustomers.size(), 1);
    TestUtil.assertCustomer(createdCustomers.get(0), createdCustomer);
  }

  private Customer createAndAssertCustomer(Customer customer) {
    Customer createdCustomer = restTemplate.postForObject(REST_SERVICE_URL,
        customer, Customer.class);
    TestUtil.assertCustomerNoId(createdCustomer, customer);
    return createdCustomer;
  }

  private Customer createAndAssertCustomer() {
    Customer customer = new CustomerBuilder().name("Customer name").build();
    return createAndAssertCustomer(customer);
  }

}
