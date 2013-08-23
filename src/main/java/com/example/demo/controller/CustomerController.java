package com.example.demo.controller;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.domain.Customer;
import com.example.demo.domain.CustomerList;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.service.CustomerService;

@Controller
@RequestMapping(value = "/customers")
public class CustomerController {
  private static final Logger LOGGER = LoggerFactory
      .getLogger(CustomerController.class);

  @Resource
  private CustomerService customerService;

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  public Customer create(@RequestBody Customer customer) {
    LOGGER.info("Creating new customer {}", customer);
    return customerService.create(customer);
  }

  @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
  @ResponseBody
  public Customer read(@PathVariable(value = "customerId") long customerId)
      throws CustomerNotFoundException {
    LOGGER.info("Reading customer with id {}", customerId);

    Customer customer = customerService.findById(customerId);

    if (null == customer) {
      LOGGER.debug("No customer found with id: " + customerId);
      throw new CustomerNotFoundException();
    }

    return customer;
  }

  @RequestMapping(value = "/{customerId}", method = RequestMethod.PUT)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void update(@PathVariable(value = "customerId") long customerId,
      @RequestBody Customer customer) throws CustomerNotFoundException {
    LOGGER.info("Updating customer with id {} with {}", customerId, customer);

    Validate.isTrue(customerId == customer.getId(),
        "customerId doesn't match URL customerId: " + customer.getId());

    customerService.update(customer);
  }

  @RequestMapping(value = "/{customerId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void delete(@PathVariable(value = "customerId") long customerId)
      throws CustomerNotFoundException {
    LOGGER.info("Deleting customer with id {}", customerId);

    customerService.delete(customerId);
  }

  @RequestMapping(method = RequestMethod.GET)
  @ResponseBody
  public CustomerList list() {
    LOGGER.info("Listing customers");
    return new CustomerList(Collections.unmodifiableList(customerService.findAll()));
  }

  @ExceptionHandler(CustomerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleCustomerNotFoundErrors(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ex.getMessage();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleClientErrors(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return ex.getMessage();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public String handleServerErrors(Exception ex) {
    LOGGER.error(ex.getMessage(), ex);
    return "System Error";
  }
}
