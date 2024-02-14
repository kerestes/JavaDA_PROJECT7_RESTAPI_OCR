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
public class HomeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homeRenderedTest() throws Exception {
        MvcResult resultActions = mockMvc.perform(get("/")).andExpectAll(
                status().isOk()
        ).andReturn();

        String content = resultActions.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("HOME PAGE"));
    }

    @Test
    public void adminHomeTest() throws Exception {
        mockMvc.perform(get("/admin/home")).andExpectAll(
                redirectedUrl("/bidList/list")
        );
    }
}
