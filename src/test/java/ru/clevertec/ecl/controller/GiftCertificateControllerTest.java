package ru.clevertec.ecl.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({SpringExtension.class})
@WebAppConfiguration
@ContextConfiguration(classes = GiftCertificateControllerTestConfig.class)
class GiftCertificateControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void mockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void givenPaginationRequestParams_whenFindAll_thenResponseStatusOk() throws Exception {
        mvc.perform(get("/api/v1/gift-certificates")
                .param("number", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
                .param("sort", "id"))
            .andExpect(status().isOk());
    }
}