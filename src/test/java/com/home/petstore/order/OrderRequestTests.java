package com.home.petstore.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRequestTests extends JsonTestConfig {

  @Autowired MockMvc mockMvc;

  OrderDTO testOrder = new OrderDTO(null, null, 2, null, OrderStatus.APPROVED, false);

  @Test
  public void whenCorrectOrderPost_thenReturnOk() throws Exception {
    mockMvc
        .perform(post("/store/order").contentType(APPLICATION_JSON).content(getJson(testOrder)))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
