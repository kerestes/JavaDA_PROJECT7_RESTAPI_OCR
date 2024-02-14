package com.nnk.springboot.unit;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
public class RuleNameTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @Test
    public void ruleNameListTest() throws Exception {

        RuleName ruleName1 = new RuleName();
        ruleName1.setId(1);
        ruleName1.setName("Name Test");
        ruleName1.setJson("Json Test");
        ruleName1.setDescription("Description Test");
        ruleName1.setTemplate("Template Test");
        ruleName1.setSqlPart("SQL Part test");
        ruleName1.setSqlStr("SQl String test");

        RuleName ruleName2 = new RuleName();
        ruleName2.setId(2);
        ruleName2.setName("Name Test 2");
        ruleName2.setJson("Json Test 2");
        ruleName2.setDescription("Description Test 2");
        ruleName2.setTemplate("Template Test 2");
        ruleName2.setSqlPart("SQL Part test 2");
        ruleName2.setSqlStr("SQl String test 2");


        List<RuleName> ruleNames =  new ArrayList<>(Arrays.asList(ruleName1, ruleName2));

        Mockito.when(ruleNameService.findAll()).thenReturn(ruleNames);

        MvcResult resultActions = mockMvc.perform(get("/ruleName/list")).andExpectAll(
                status().isOk(),
                model().size(3),
                model().attribute("title", "Rule Name")
        ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Name Test"));
        Assertions.assertTrue(content.contains("Name Test 2"));
        Assertions.assertTrue(content.contains("Description Test"));
        Assertions.assertTrue(content.contains("Description Test 2"));
        Assertions.assertTrue(content.contains("SQl String test"));
        Assertions.assertTrue(content.contains("SQl String test 2"));

        Mockito.verify(ruleNameService, Mockito.times(1)).findAll();
    }

    @Test
    public void ruleNameAddTest() throws Exception {

        mockMvc.perform(get("/ruleName/add")).andExpectAll(
                status().isOk(),
                model().size(2),
                model().attribute("title", "Rule Name - ADD")
        );
    }

    @Test
    public void ruleNameValidateErrorTest() throws Exception {

        RuleName ruleName = new RuleName();

        Mockito.when(ruleNameService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/ruleName/validate").flashAttr("ruleName", ruleName))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Name is mandatory"));
        Assertions.assertTrue(content.contains("Description is mandatory"));
        Assertions.assertTrue(content.contains("Json is mandatory"));
        Assertions.assertTrue(content.contains("Template is mandatory"));
        Assertions.assertTrue(content.contains("SQL String is mandatory"));
        Assertions.assertTrue(content.contains("SQL Part is mandatory"));

        Mockito.verify(ruleNameService, Mockito.times(0)).save(any());
    }

    @Test
    public void ruleNameValidateTest() throws Exception {

        RuleName ruleName = new RuleName();
        ruleName.setName("Name Test");
        ruleName.setJson("Json Test");
        ruleName.setDescription("Description Test");
        ruleName.setTemplate("Template Test");
        ruleName.setSqlPart("SQL Part test");
        ruleName.setSqlStr("SQl String test");

        Mockito.when(ruleNameService.save(any())).thenReturn(null);

        mockMvc.perform(post("/ruleName/validate").flashAttr("ruleName", ruleName))
                .andExpect(
                        redirectedUrl("/ruleName/list")
                );

        Mockito.verify(ruleNameService, Mockito.times(1)).save(any());
    }

    @Test
    public void ruleNameUpdateErrorTest() throws Exception {

        Mockito.when(ruleNameService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/ruleName/update/{id}", 1))
                .andExpect(
                        redirectedUrl("/ruleName/list?error=true")
                );

        Mockito.verify(ruleNameService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void ruleNameUpdateTest() throws Exception {

        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Name Test");
        ruleName.setJson("Json Test");
        ruleName.setDescription("Description Test");
        ruleName.setTemplate("Template Test");
        ruleName.setSqlPart("SQL Part test");
        ruleName.setSqlStr("SQl String test");

        Mockito.when(ruleNameService.findById(anyInt())).thenReturn(Optional.of(ruleName));

        mockMvc.perform(get("/ruleName/update/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        model().size(2),
                        model().attribute("title", "Rule Name - UPDATE")
                );

        Mockito.verify(ruleNameService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void ruleNameUpdateValidatorErrorTest() throws Exception {

        RuleName ruleName = new RuleName();
        ruleName.setId(1);

        Mockito.when(ruleNameService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/ruleName/update/{id}", 1).flashAttr("ruleName", ruleName))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Name is mandatory"));
        Assertions.assertTrue(content.contains("Description is mandatory"));
        Assertions.assertTrue(content.contains("Json is mandatory"));
        Assertions.assertTrue(content.contains("Template is mandatory"));
        Assertions.assertTrue(content.contains("SQL String is mandatory"));
        Assertions.assertTrue(content.contains("SQL Part is mandatory"));

        Mockito.verify(ruleNameService, Mockito.times(0)).save(any());
    }

    @Test
    public void ruleNameUpdateValidatorTest() throws Exception {

        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Name Test");
        ruleName.setJson("Json Test");
        ruleName.setDescription("Description Test");
        ruleName.setTemplate("Template Test");
        ruleName.setSqlPart("SQL Part test");
        ruleName.setSqlStr("SQl String test");

        Mockito.when(ruleNameService.save(any())).thenReturn(null);

        mockMvc.perform(post("/ruleName/update/{id}", 1).flashAttr("ruleName", ruleName))
                .andExpect(
                        redirectedUrl("/ruleName/list")
                );

        Mockito.verify(ruleNameService, Mockito.times(1)).save(any());
    }

    @Test
    public void ruleNameDeleteTest() throws Exception {

        Mockito.doNothing().when(ruleNameService).deleteById(anyInt());

        mockMvc.perform(get("/ruleName/delete/{id}", 1)).andExpect(
                redirectedUrl("/ruleName/list")
        );

        Mockito.verify(ruleNameService, Mockito.times(1)).deleteById(anyInt());
    }
}
