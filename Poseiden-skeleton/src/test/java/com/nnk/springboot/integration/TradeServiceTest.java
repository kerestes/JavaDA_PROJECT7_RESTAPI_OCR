package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
@Sql("/banco_scripts/trade.sql")
public class TradeServiceTest {
    @Autowired
    private TradeService tradeService;

    @Test
    public void tradeIT() {

        // Find All Test

        List<Trade> trades = tradeService.findAll();

        Trade trade1 = trades.get(0);
        Trade trade2 = trades.get(1);

        if (trade1.getTradeId() != 1) {
            trade1 = trades.get(1);
            trade2 = trades.get(0);
        }

        Assertions.assertEquals("Account Test", trade1.getAccount());
        Assertions.assertEquals("Type Test", trade1.getType());
        Assertions.assertEquals(5.5, trade1.getBuyQuantity());
        Assertions.assertEquals("Account Test 2", trade2.getAccount());
        Assertions.assertEquals("Type Test 2", trade2.getType());
        Assertions.assertEquals(9.9, trade2.getBuyQuantity());

        Assertions.assertTrue(trades.size() == 2);

        //Save Test

        Trade trade = new Trade();
        trade.setBuyQuantity(12.13);
        trade.setAccount("new Account");
        trade.setType("new Type");

        Trade tradeResponse = tradeService.save(trade);
        Assertions.assertNotNull(tradeResponse.getTradeId());
        Assertions.assertEquals(trade.getAccount(), tradeResponse.getAccount());
        Assertions.assertEquals(trade.getType(), tradeResponse.getType());

        //FindById Test

        Optional<Trade> optionalTrade = tradeService.findById(3);

        Assertions.assertTrue(optionalTrade.isPresent());
        Assertions.assertEquals("new Account", optionalTrade.get().getAccount());
        Assertions.assertEquals(12.13, optionalTrade.get().getBuyQuantity());

        // Delete Test

        tradeService.deleteById(3);

        optionalTrade = tradeService.findById(3);

        Assertions.assertTrue(optionalTrade.isEmpty());
    }
}
