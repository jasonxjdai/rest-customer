package com.example.demo.test.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.domain.Customer;
import com.example.demo.service.CustomerService;
import com.example.demo.test.helper.CustomerBuilder;
import com.example.demo.test.helper.TestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-core.xml",
    "classpath:applicationContext-web.xml",
    "classpath:applicationContext-mock.xml" })
@WebAppConfiguration
public class CustomerControllerTest {

  private MockMvc mockMvc;

  @Resource
  private CustomerService customerServiceMock;

  @Resource
  private WebApplicationContext webApplicationContext;

  @Before
  public void setUp() {
    // We have to reset our mock between tests because the mock objects
    // are managed by the Spring container. If we would not reset them,
    // stubbing and verified behavior would "leak" from one test to another.
    Mockito.reset(customerServiceMock);
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void create_Customer_ShouldReturnHttpStatusCode200() throws Exception {
    Customer created = new CustomerBuilder().name("tom").faxNumber("123")
        .telephoneNumber("456").build();
    Customer model = new CustomerBuilder().id(1L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();
  
    when(customerServiceMock.create(created)).thenReturn(model);
  
    mockMvc.perform(post("/customers").contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(created))).andExpect(status().isOk());

    verify(customerServiceMock, times(1)).create((Customer) anyObject());
    verifyNoMoreInteractions(customerServiceMock);
  }

  @Test
  public void findAll_CustomersFound_ShouldReturnFoundCustomeEntries()
      throws Exception {
    Customer first = new CustomerBuilder().id(1L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();
    Customer second = new CustomerBuilder().id(2L).name("bruce")
        .faxNumber("789").telephoneNumber("101112").build();

    when(customerServiceMock.findAll())
        .thenReturn(Arrays.asList(first, second));

    mockMvc.perform(get("/customers").header("Accept", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.customers", hasSize(2)))
        .andExpect(jsonPath("$.customers[0].id", is(1)))
        .andExpect(jsonPath("$.customers[0].name", is("tom")))
        .andExpect(jsonPath("$.customers[0].faxNumber", is("123")))
        .andExpect(jsonPath("$.customers[0].telephoneNumber", is("456")))
        .andExpect(jsonPath("$.customers[1].id", is(2)))
        .andExpect(jsonPath("$.customers[1].name", is("bruce")))
        .andExpect(jsonPath("$.customers[1].faxNumber", is("789")))
        .andExpect(jsonPath("$.customers[1].telephoneNumber", is("101112")));

    verify(customerServiceMock, times(1)).findAll();
    verifyNoMoreInteractions(customerServiceMock);
  }

  @Test
  public void findById_CustomerNotFound_ShouldReturnHttpStatusCode404()
      throws Exception {
    when(customerServiceMock.findById(1L)).thenReturn(null);

    mockMvc.perform(get("/customers/{id}", 1L))
        .andExpect(status().isNotFound());

    verify(customerServiceMock, times(1)).findById(1L);
    verifyNoMoreInteractions(customerServiceMock);
  }

  @Test
  public void findById_CustomerFound_ShouldReturnFoundCustomer()
      throws Exception {
    Customer found = new CustomerBuilder().id(1L).name("tom").faxNumber("123")
        .telephoneNumber("456").build();

    when(customerServiceMock.findById(1L)).thenReturn(found);

    mockMvc
        .perform(
            get("/customers/{id}", 1L).header("Accept", "application/json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("tom")))
        .andExpect(jsonPath("$.faxNumber", is("123")))
        .andExpect(jsonPath("$.telephoneNumber", is("456")));

    verify(customerServiceMock, times(1)).findById(1L);
    verifyNoMoreInteractions(customerServiceMock);
  }
}