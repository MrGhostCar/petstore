package com.home.petstore.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRequestTests extends JsonTestConfig {

  @Autowired MockMvc mockMvc;

  String testOrder1 =
      "{\n"
          + "  \"id\": 0,\n"
          + "  \"petId\": null,\n"
          + "  \"quantity\": 5,\n"
          + "  \"shipDate\": \"2024-12-07T13:15:53.382Z\",\n"
          + "  \"status\": \"PLACED\",\n"
          + "  \"complete\": false\n"
          + "}";

  String testOrder2 =
      "{\n"
          + "  \"id\": 0,\n"
          + "  \"petId\": null,\n"
          + "  \"quantity\": 5,\n"
          + "  \"shipDate\": \"2024-12-08T13:15:53.382Z\",\n"
          + "  \"status\": \"PLACED\",\n"
          + "  \"complete\": false\n"
          + "}";

  @Test
  public void whenCorrectOrderPost_thenReturnOk() throws Exception {
    mockMvc
        .perform(post("/store/order").contentType(APPLICATION_JSON).content(testOrder2))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void givenThereAreTwoOrders_whenSearchParametersGivenForOne_ThenOneReturned()
      throws Exception {
    mockMvc
        .perform(post("/store/order").contentType(APPLICATION_JSON).content(testOrder1))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(post("/store/order").contentType(APPLICATION_JSON).content(testOrder2))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/store/order")
                .contentType(APPLICATION_JSON)
                .param("from", "2024-12-01T13:15:53.382Z")
                .param("to", "2024-12-07T23:59:53.382Z"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").exists())
        .andExpect(jsonPath("$[0].shipDate").value("2024-12-07T13:15:53.382"));
  }

  @Test
  public void whenGetMadeForOne_thenReturnJson() throws Exception {
    MvcResult result =
        this.mockMvc
            .perform(post("/store/order").contentType(APPLICATION_JSON).content(testOrder2))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    OrderDTO returnedOrderFromPost = mapper.readValue(json, OrderDTO.class);

    mockMvc
        .perform(
            get("/store/order/{id}", returnedOrderFromPost.getId()).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(returnedOrderFromPost.getId()));
  }

  @Test
  public void givenThereIsAnOrder_whenDeleteCalled_thenSameOrderNotFound() throws Exception {
    MvcResult result =
        this.mockMvc
            .perform(post("/store/order").contentType(APPLICATION_JSON).content(testOrder2))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    OrderDTO returnedOrder = mapper.readValue(json, OrderDTO.class);

    mockMvc
        .perform(delete("/store/order/{id}", returnedOrder.getId()).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/store/order/{id}", returnedOrder.getId()).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void givenOrderPostMade_whenAPatchCalled_thenModificationIsVisibleOnNextGet()
      throws Exception {

    String patchBody =
            "[\n" +
                    "    {\"op\":\"replace\",\"path\":\"/quantity\",\"value\":\"11\"} \n" +
                    "]";


    MvcResult result =
        this.mockMvc
            .perform(post("/store/order").contentType(APPLICATION_JSON).content(testOrder2))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    OrderDTO returnedOrder = mapper.readValue(json, OrderDTO.class);

    mockMvc
        .perform(
            patch("/store/order/{id}", returnedOrder.getId())
                .content(patchBody)
                .contentType("application/json-patch+json"))
        .andDo(print())
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/store/order/{id}", returnedOrder.getId()).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.quantity").value("11"));
  }
}
