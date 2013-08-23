package com.example.demo.test.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.domain.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.impl.CustomerServiceImpl;
import com.example.demo.test.helper.CustomerBuilder;
import com.example.demo.test.helper.TestUtil;

public class CustomerServiceTest {

  private CustomerServiceImpl customerService;
  private CustomerRepository customerRepositoryMock;

  @Before
  public void setUp() {
    customerService = new CustomerServiceImpl();
    customerRepositoryMock = mock(CustomerRepository.class);
    ReflectionTestUtils.setField(customerService, "customerRepository",
        customerRepositoryMock);
  }


  @Test
  public void create() {
    Customer created = TestUtil.createObject();
    Customer persisted = TestUtil.createModelObject();

    when(customerRepositoryMock.save(any(Customer.class)))
        .thenReturn(persisted);

    Customer returned = customerService.create(created);

    ArgumentCaptor<Customer> customerArgument = ArgumentCaptor
        .forClass(Customer.class);
    verify(customerRepositoryMock, times(1)).save(customerArgument.capture());
    verifyNoMoreInteractions(customerRepositoryMock);

    TestUtil.assertCustomer(created, customerArgument.getValue());
    assertEquals(persisted, returned);
  }

  @Test
  public void delete() throws CustomerNotFoundException {
    Customer deleted = TestUtil.createModelObject();
    when(customerRepositoryMock.findOne(TestUtil.CUSTOMER_ID)).thenReturn(deleted);

    Customer returned = customerService.delete(TestUtil.CUSTOMER_ID);

    verify(customerRepositoryMock, times(1)).findOne(TestUtil.CUSTOMER_ID);
    verify(customerRepositoryMock, times(1)).delete(deleted);
    verifyNoMoreInteractions(customerRepositoryMock);

    assertEquals(deleted, returned);
  }

  @Test(expected = CustomerNotFoundException.class)
  public void deleteWhenCustomerIsNotFound() throws CustomerNotFoundException {
    when(customerRepositoryMock.findOne(TestUtil.CUSTOMER_ID)).thenReturn(null);

    customerService.delete(TestUtil.CUSTOMER_ID);

    verify(customerRepositoryMock, times(1)).findOne(TestUtil.CUSTOMER_ID);
    verifyNoMoreInteractions(customerRepositoryMock);
  }

  @Test
  public void findAll() {
    List<Customer> customers = new ArrayList<Customer>();
    when(customerRepositoryMock.findAll()).thenReturn(customers);

    List<Customer> returned = customerService.findAll();

    verify(customerRepositoryMock, times(1)).findAll();
    verifyNoMoreInteractions(customerRepositoryMock);

    assertEquals(customers, returned);
  }

  @Test
  public void findById() {
    Customer customer = TestUtil.createModelObject();
    when(customerRepositoryMock.findOne(TestUtil.CUSTOMER_ID)).thenReturn(customer);

    Customer returned = customerService.findById(TestUtil.CUSTOMER_ID);

    verify(customerRepositoryMock, times(1)).findOne(TestUtil.CUSTOMER_ID);
    verifyNoMoreInteractions(customerRepositoryMock);

    assertEquals(customer, returned);
  }

  @Test
  public void update() throws CustomerNotFoundException {
    Customer updated = TestUtil.createUpdatedObject();
    Customer customer = TestUtil.createModelObject();

    when(customerRepositoryMock.findOne(updated.getId())).thenReturn(customer);

    Customer returned = customerService.update(updated);

    verify(customerRepositoryMock, times(1)).findOne(updated.getId());
    verifyNoMoreInteractions(customerRepositoryMock);

    TestUtil.assertCustomer(updated, returned);
  }

  @Test(expected = CustomerNotFoundException.class)
  public void updateWhenCustomerIsNotFound() throws CustomerNotFoundException {
    Customer updated = TestUtil.createUpdatedObject();

    when(customerRepositoryMock.findOne(updated.getId())).thenReturn(null);

    customerService.update(updated);

    verify(customerRepositoryMock, times(1)).findOne(updated.getId());
    verifyNoMoreInteractions(customerRepositoryMock);
  }

}