package com.nnk.springboot.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
public class CustomErrorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void customErrorTest() throws Exception {
        MvcResult resultActions = mockMvc.perform(get("/error")).andExpectAll(
                status().isOk(),
                model().size(2)
        ).andReturn();

        String content = resultActions.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("You are not authorized for the requested data."));
    }
}
