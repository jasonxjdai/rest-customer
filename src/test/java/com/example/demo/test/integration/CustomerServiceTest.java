package com.example.demo.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.demo.domain.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.service.CustomerService;
import com.example.demo.test.helper.CustomerBuilder;
import com.example.demo.test.helper.TestUtil;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DatabaseSetup("customerData.xml")
public class CustomerServiceTest {

  @Resource
  private CustomerService customerService;
  
  @Test
  public void create() {
    Customer created = new CustomerBuilder().name("tom").faxNumber("123")
        .telephoneNumber("456").build();
    Customer persisted = new CustomerBuilder().id(4L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();

    Customer returned = customerService.create(created);
    TestUtil.assertCustomerNoId(persisted, returned);
  }

  @Test
  public void delete() throws CustomerNotFoundException {
    Customer deleted = new CustomerBuilder().id(TestUtil.CUSTOMER_ID).name("bruce").faxNumber("789")
        .telephoneNumber("456").build();

    Customer returned = customerService.delete(TestUtil.CUSTOMER_ID);

    TestUtil.assertCustomer(deleted, returned);
  }

  @Test(expected = CustomerNotFoundException.class)
  public void deleteWhenCustomerIsNotFound() throws CustomerNotFoundException {
    customerService.delete(TestUtil.CUSTOMER_ID_NONEXIST);
  }

  @Test
  public void findAll() {
    List<Customer> customers = new ArrayList<Customer>();
    Customer customer1 = new CustomerBuilder().id(1L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();
    Customer customer2 = new CustomerBuilder().id(2L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();
    Customer customer3 = new CustomerBuilder().id(3L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();
    
    customers.add(customer1);
    customers.add(customer2);
    customers.add(customer3);

    List<Customer> returned = customerService.findAll();

    assertEquals(customers, returned);
  }

  @Test
  public void findById() {
    Customer customer = new CustomerBuilder().id(TestUtil.CUSTOMER_ID).name("tom").faxNumber("123")
        .telephoneNumber("101112").build();

    Customer returned = customerService.findById(TestUtil.CUSTOMER_ID);

    assertEquals(customer, returned);
  }

  @Test
  public void update() throws CustomerNotFoundException {
    Customer updated = new CustomerBuilder().id(2L).name("bruce-update").faxNumber("789-update")
        .telephoneNumber("101112-update").build();

    Customer returned = customerService.update(updated);

    assertCustomer(updated, returned);
  }

  @Test(expected = CustomerNotFoundException.class)
  public void updateWhenCustomerIsNotFound() throws CustomerNotFoundException {
    Customer updated = new CustomerBuilder().id(TestUtil.CUSTOMER_ID_NONEXIST).name("bruce-update").faxNumber("789-update")
        .telephoneNumber("101112-update").build();

    customerService.update(updated);
  }

  private void assertCustomer(Customer expected, Customer actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getFaxNumber(), expected.getFaxNumber());
    assertEquals(expected.getTelephoneNumber(), expected.getTelephoneNumber());
    assertEquals(expected.getAddresses(), expected.getAddresses());
  }
}