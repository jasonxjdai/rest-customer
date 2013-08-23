package com.example.demo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Customer;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;

/**
 * This implementation of the CustomerService interface communicates with
 * the database by using a Spring Data JPA repository.
 */
@Service
public class CustomerServiceImpl implements CustomerService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
 
  @Resource
  private CustomerRepository customerRepository;

  @Transactional
  @Override
  public Customer create(Customer created) {
      LOGGER.debug("Creating a new customer with information: " + created);
      
      return customerRepository.save(created);
  }

  @Transactional(rollbackFor = CustomerNotFoundException.class)
  @Override
  public Customer delete(Long customerId) throws CustomerNotFoundException {
      LOGGER.debug("Deleting customer with id: " + customerId);
      
      Customer deleted = customerRepository.findOne(customerId);
      
      if (deleted == null) {
          LOGGER.debug("No customer found with id: " + customerId);
          throw new CustomerNotFoundException();
      }
      
      customerRepository.delete(deleted);
      return deleted;
  }

  @Transactional(readOnly = true)
  @Override
  public List<Customer> findAll() {
      LOGGER.debug("Finding all customers");
      return customerRepository.findAll();
  }

  @Transactional(readOnly = true)
  @Override
  public Customer findById(Long id) {
      LOGGER.debug("Finding customer by id: " + id);
      return customerRepository.findOne(id);
  }

  @Transactional(rollbackFor = CustomerNotFoundException.class)
  @Override
  public Customer update(Customer updated) throws CustomerNotFoundException {
      LOGGER.debug("Updating customer with information: " + updated);
      
      Customer customer = customerRepository.findOne(updated.getId());
      
      if (customer == null) {
          LOGGER.debug("No customer found with id: " + updated.getId());
          throw new CustomerNotFoundException();
      }
      
      customer.update(updated);

      return customer;
  }
}
