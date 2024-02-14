package com.nnk.springboot.integration;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
@Sql("/banco_scripts/rulename.sql")
public class RuleNameServiceTest {
    @Autowired
    private RuleNameService ruleNameService;

    @Test
    public void ruleNameIT() {

        // Find All Test

        List<RuleName> ruleNames = ruleNameService.findAll();

        RuleName ruleName1 = ruleNames.get(0);
        RuleName ruleName2 = ruleNames.get(1);

        if (ruleName1.getId() != 1) {
            ruleName1 = ruleNames.get(1);
            ruleName2 = ruleNames.get(0);
        }

        Assertions.assertEquals("Name Test", ruleName1.getName());
        Assertions.assertEquals("Description Test", ruleName1.getDescription());
        Assertions.assertEquals("Json Test", ruleName1.getJson());
        Assertions.assertEquals("Template Test", ruleName1.getTemplate());
        Assertions.assertEquals("Sql Str Test", ruleName1.getSqlStr());
        Assertions.assertEquals("Sql Part Test", ruleName1.getSqlPart());
        Assertions.assertEquals("Name Test 2", ruleName2.getName());
        Assertions.assertEquals("Description Test 2", ruleName2.getDescription());
        Assertions.assertEquals("Json Test 2", ruleName2.getJson());
        Assertions.assertEquals("Template Test 2", ruleName2.getTemplate());
        Assertions.assertEquals("Sql Str Test 2", ruleName2.getSqlStr());
        Assertions.assertEquals("Sql Part Test 2", ruleName2.getSqlPart());

        Assertions.assertTrue(ruleNames.size() == 2);

        //Save Test

        RuleName ruleName = new RuleName();
        ruleName.setName("new Name");
        ruleName.setDescription("new Description");
        ruleName.setJson("new Json");
        ruleName.setTemplate("new Template");
        ruleName.setSqlStr("new Sql Str");
        ruleName.setSqlPart("new Sql Part");

        RuleName ruleNameResponse = ruleNameService.save(ruleName);
        Assertions.assertNotNull(ruleNameResponse.getId());
        Assertions.assertEquals(ruleName.getName(), ruleNameResponse.getName());
        Assertions.assertEquals(ruleName.getJson(), ruleNameResponse.getJson());
        Assertions.assertEquals(ruleName.getSqlPart(), ruleNameResponse.getSqlPart());

        //FindById Test

        Optional<RuleName> optionalRuleName = ruleNameService.findById(3);

        Assertions.assertTrue(optionalRuleName.isPresent());
        Assertions.assertEquals("new Sql Str", optionalRuleName.get().getSqlStr());
        Assertions.assertEquals("new Template", optionalRuleName.get().getTemplate());

        // Delete Test

        ruleNameService.deleteById(3);

        optionalRuleName = ruleNameService.findById(3);

        Assertions.assertTrue(optionalRuleName.isEmpty());
    }
}
