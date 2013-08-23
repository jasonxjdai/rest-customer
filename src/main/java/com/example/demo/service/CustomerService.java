package com.example.demo.service;

import java.util.List;

import com.example.demo.domain.Customer;
import com.example.demo.exception.CustomerNotFoundException;

/**
 * Declares methods used to obtain and modify customer information.
 */
public interface CustomerService {

    /**
     * Creates a new customer.
     * @param created   The information of the created customer.
     * @return  The created customer.
     */
    public Customer create(Customer created);

    /**
     * Deletes a customer.
     * @param customerId  The id of the deleted customer.
     * @return  The deleted customer.
     * @throws CustomerNotFoundException  if no customer is found with the given id.
     */
    public Customer delete(Long customerId) throws CustomerNotFoundException;

    /**
     * Finds all customers.
     * @return  A list of customers.
     */
    public List<Customer> findAll();

    /**
     * Finds customer by id.
     * @param id    The id of the wanted customer.
     * @return  The found customer. If no customer is found, this method returns null.
     */
    public Customer findById(Long id);

    /**
     * Updates the information of a customer.
     * @param updated   The information of the updated customer.
     * @return  The updated customer.
     * @throws CustomerNotFoundException  if no customer is found with given id.
     */
    public Customer update(Customer updated) throws CustomerNotFoundException;
}