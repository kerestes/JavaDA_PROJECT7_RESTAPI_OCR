package com.nnk.springboot.unit;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.services.TradeService;
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
public class TradeTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    public void tradeListTest() throws Exception {

        Trade trade1 = new Trade();
        trade1.setTradeId(1);
        trade1.setAccount("Account Test");
        trade1.setType("Type Test");
        trade1.setBuyQuantity(100.0);

        Trade trade2 = new Trade();
        trade2.setTradeId(2);
        trade2.setAccount("Account Test 2");
        trade2.setType("Type Test 2");
        trade2.setBuyQuantity(200.0);


        List<Trade> trades =  new ArrayList<>(Arrays.asList(trade1, trade2));

        Mockito.when(tradeService.findAll()).thenReturn(trades);

        MvcResult resultActions = mockMvc.perform(get("/trade/list")).andExpectAll(
                status().isOk(),
                model().size(3),
                model().attribute("title", "Trade")
        ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Account Test"));
        Assertions.assertTrue(content.contains("Account Test 2"));
        Assertions.assertTrue(content.contains("Type Test"));
        Assertions.assertTrue(content.contains("Type Test 2"));

        Mockito.verify(tradeService, Mockito.times(1)).findAll();
    }

    @Test
    public void tradeAddTest() throws Exception {

        mockMvc.perform(get("/trade/add")).andExpectAll(
                status().isOk(),
                model().size(2),
                model().attribute("title", "Trade - ADD")
        );
    }

    @Test
    public void tradeValidateErrorTest() throws Exception {

        Trade trade = new Trade();

        Mockito.when(tradeService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/trade/validate").flashAttr("trade", trade))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Account is mandatory"));
        Assertions.assertTrue(content.contains("Type is mandatory"));
        Assertions.assertTrue(content.contains("Buy Quantity is mandatory"));

        Mockito.verify(tradeService, Mockito.times(0)).save(any());
    }

    @Test
    public void tradeValidateTest() throws Exception {

        Trade trade = new Trade();
        trade.setAccount("Account Test");
        trade.setType("Type Test");
        trade.setBuyQuantity(100.0);

        Mockito.when(tradeService.save(any())).thenReturn(null);

        mockMvc.perform(post("/trade/validate").flashAttr("trade", trade))
                .andExpect(
                        redirectedUrl("/trade/list")
                );

        Mockito.verify(tradeService, Mockito.times(1)).save(any());
    }

    @Test
    public void tradeUpdateErrorTest() throws Exception {

        Mockito.when(tradeService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/trade/update/{id}", 1))
                .andExpect(
                        redirectedUrl("/trade/list?error=true")
                );

        Mockito.verify(tradeService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void tradeUpdateTest() throws Exception {

        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account Test");
        trade.setType("Type Test");
        trade.setBuyQuantity(100.0);

        Mockito.when(tradeService.findById(anyInt())).thenReturn(Optional.of(trade));

        mockMvc.perform(get("/trade/update/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        model().size(2),
                        model().attribute("title", "Trade - UPDATE")
                );

        Mockito.verify(tradeService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void tradeUpdateValidatorErrorTest() throws Exception {

        Trade trade = new Trade();
        trade.setTradeId(1);

        Mockito.when(tradeService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/trade/update/{id}", 1).flashAttr("trade", trade))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Account is mandatory"));
        Assertions.assertTrue(content.contains("Type is mandatory"));
        Assertions.assertTrue(content.contains("Buy Quantity is mandatory"));

        Mockito.verify(tradeService, Mockito.times(0)).save(any());
    }

    @Test
    public void tradeUpdateValidatorTest() throws Exception {

        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account Test");
        trade.setType("Type Test");
        trade.setBuyQuantity(100.0);

        Mockito.when(tradeService.save(any())).thenReturn(null);

        mockMvc.perform(post("/trade/update/{id}", 1).flashAttr("trade", trade))
                .andExpect(
                        redirectedUrl("/trade/list")
                );

        Mockito.verify(tradeService, Mockito.times(1)).save(any());
    }

    @Test
    public void tradeDeleteTest() throws Exception {

        Mockito.doNothing().when(tradeService).deleteById(anyInt());

        mockMvc.perform(get("/trade/delete/{id}", 1)).andExpect(
                redirectedUrl("/trade/list")
        );

        Mockito.verify(tradeService, Mockito.times(1)).deleteById(anyInt());
    }
}
