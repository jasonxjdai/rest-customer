package com.example.demo.test.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.domain.Customer;
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
public class CustomerControllerTest {

  @Resource
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void create() throws Exception {
    Customer added = new CustomerBuilder().name("bruce").faxNumber("789")
        .telephoneNumber("101112").build();
    mockMvc
        .perform(
            post("/customers").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(added))
                .header("Accept", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.name", is("bruce")))
        .andExpect(jsonPath("$.faxNumber", is("789")))
        .andExpect(jsonPath("$.telephoneNumber", is("101112")));
  }

  @Test
  public void deleteById() throws Exception {
    mockMvc.perform(delete("/customers/{id}", TestUtil.CUSTOMER_ID)).andExpect(
        status().isNoContent());
  }

  @Test
  public void deleteByIdWhenCustomerIsNotFound() throws Exception {
    mockMvc.perform(delete("/customers/{id}", TestUtil.CUSTOMER_ID_NONEXIST))
        .andExpect(status().isNotFound());
  }

  @Test
  public void findAll() throws Exception {
    mockMvc.perform(get("/customers").header("Accept", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.customers", hasSize(3)))
        .andExpect(jsonPath("$.customers[0].id", is(1)))
        .andExpect(jsonPath("$.customers[0].name", is("tom")))
        .andExpect(jsonPath("$.customers[0].faxNumber", is("123")))
        .andExpect(jsonPath("$.customers[0].telephoneNumber", is("456")))
        .andExpect(jsonPath("$.customers[1].id", is(2)))
        .andExpect(jsonPath("$.customers[1].name", is("bruce")))
        .andExpect(jsonPath("$.customers[1].faxNumber", is("789")))
        .andExpect(jsonPath("$.customers[1].telephoneNumber", is("101112")))
        .andExpect(jsonPath("$.customers[2].id", is(3)))
        .andExpect(jsonPath("$.customers[2].name", is("alex")))
        .andExpect(jsonPath("$.customers[2].faxNumber", is("131415")))
        .andExpect(jsonPath("$.customers[2].telephoneNumber", is("161718")));
  }

  @Test
  public void findByIdWhenCustomerIsNotFound() throws Exception {
    mockMvc.perform(get("/customers/{id}", TestUtil.CUSTOMER_ID_NONEXIST))
        .andExpect(status().isNotFound());
  }

  @Test
  public void findByIdWhenCustomerIsFound() throws Exception {
    mockMvc
        .perform(
            get("/customers/{id}", 1L).header("Accept", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("tom")))
        .andExpect(jsonPath("$.faxNumber", is("123")))
        .andExpect(jsonPath("$.telephoneNumber", is("456")));
  }

  @Test
  public void updateByIdWhenCustomerIsNotFound() throws Exception {
    mockMvc.perform(delete("/customers/{id}", TestUtil.CUSTOMER_ID_NONEXIST))
        .andExpect(status().isNotFound());
  }

  @Test
  public void update() throws Exception {
    Customer updated = new CustomerBuilder().id(TestUtil.CUSTOMER_ID)
        .name("bruce-update").faxNumber("789-update")
        .telephoneNumber("101112-update").build();

    mockMvc.perform(
        put("/customers/{id}", TestUtil.CUSTOMER_ID).contentType(
            TestUtil.APPLICATION_JSON_UTF8).content(
            TestUtil.convertObjectToJsonBytes(updated))).andExpect(
        status().isNoContent());
  }

  @Test
  public void updateTodoWhenTodoIsNotFound() throws Exception {
    Customer updated = new CustomerBuilder().id(TestUtil.CUSTOMER_ID_NONEXIST)
        .name("bruce-update").faxNumber("789-update")
        .telephoneNumber("101112-update").build();

    mockMvc.perform(
        put("/customers/{id}", TestUtil.CUSTOMER_ID_NONEXIST).contentType(
            TestUtil.APPLICATION_JSON_UTF8).content(
            TestUtil.convertObjectToJsonBytes(updated))).andExpect(
        status().isNotFound());
  }

}