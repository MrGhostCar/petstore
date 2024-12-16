package com.home.petstore.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRequestTests extends JsonTestConfig {

  public static final String TEST_API_KEY = "xxxx";
  @Autowired MockMvc mockMvc;

  String testOrder1 =
      """
         {
           "id": 0,
           "petId": 0,
           "quantity": 5,
           "shipDate": "2024-12-07T21:12:16.382Z",
           "status": "PLACED",
           "complete": false
         }
      """;

  String testOrder2 =
      """
         {
           "id": 0,
           "petId": 0,
           "quantity": 5,
           "shipDate": "2024-12-08T21:12:16.382Z",
           "status": "PLACED",
           "complete": false
         }
      """;

  @Test
  public void whenCorrectOrderPost_thenReturnOk() throws Exception {
    mockMvc
        .perform(
            post("/store/order")
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON)
                .content(testOrder2))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void givenThereAreTwoOrders_whenSearchParametersGivenForOne_ThenOneReturned()
      throws Exception {
    mockMvc
        .perform(
            post("/store/order")
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON)
                .content(testOrder1))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/store/order")
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON)
                .content(testOrder2))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/store/order")
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON)
                .param("from", "2024-12-06T13:15:53.382Z")
                .param("to", "2024-12-07T23:59:53.382Z"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  public void whenGetMadeForOne_thenReturnJson() throws Exception {
    MvcResult result =
        this.mockMvc
            .perform(
                post("/store/order")
                    .header("x-api-key", TEST_API_KEY)
                    .contentType(APPLICATION_JSON)
                    .content(testOrder2))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    OrderDTO returnedOrderFromPost = mapper.readValue(json, OrderDTO.class);

    mockMvc
        .perform(
            get("/store/order/{id}", returnedOrderFromPost.getId())
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(returnedOrderFromPost.getId()));
  }

  @Test
  public void givenThereIsAnOrder_whenDeleteCalled_thenSameOrderNotFound() throws Exception {
    MvcResult result =
        this.mockMvc
            .perform(
                post("/store/order")
                    .header("x-api-key", TEST_API_KEY)
                    .contentType(APPLICATION_JSON)
                    .content(testOrder2))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    OrderDTO returnedOrder = mapper.readValue(json, OrderDTO.class);

    mockMvc
        .perform(
            delete("/store/order/{id}", returnedOrder.getId())
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/store/order/{id}", returnedOrder.getId())
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void givenOrderPostMade_whenAPatchCalled_thenModificationIsVisibleOnNextGet()
      throws Exception {

    String patchBody =
        """
      [
        {"op": "replace", "path": "/quantity", "value": 5},
        {"op": "replace", "path": "/quantity", "value": 11}
      ]
      """;

    MvcResult result =
        this.mockMvc
            .perform(
                post("/store/order")
                    .header("x-api-key", TEST_API_KEY)
                    .contentType(APPLICATION_JSON)
                    .content(testOrder2))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    OrderDTO returnedOrder = mapper.readValue(json, OrderDTO.class);

    mockMvc
        .perform(
            patch("/store/order/{id}", returnedOrder.getId())
                .header("x-api-key", TEST_API_KEY)
                .content(patchBody)
                .contentType("application/json-patch+json"))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/store/order/{id}", returnedOrder.getId())
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantity").value("11"));
  }

  @Test
  public void givenThePetIdDoesNotExist_whenPostOrderMade_Then422Returned() throws Exception {
    String testOrder3 =
        """
         {
           "id": 0,
           "petId": 666,
           "quantity": 5,
           "shipDate": "2024-12-08T21:12:16.382Z",
           "status": "PLACED",
           "complete": false
         }
         """;

    mockMvc
        .perform(
            post("/store/order")
                .header("x-api-key", TEST_API_KEY)
                .contentType(APPLICATION_JSON)
                .content(testOrder3))
        .andDo(print())
        .andExpect(status().isUnprocessableEntity());
  }
}
