package com.nnk.springboot.integration;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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
@Sql("/banco_scripts/bidlist.sql")
public class BidListServiceTest {

    @Autowired
    private BidListService bidListService;

    @Test
    public void bidListIT(){

        // Find All Test

        List<BidList> bidLists = bidListService.findAll();

        BidList bidList1 = bidLists.get(0);
        BidList bidList2 = bidLists.get(1);

        if(bidList1.getBidListId() != 1){
            bidList1 = bidLists.get(1);
            bidList2 = bidLists.get(0);
        }

        Assertions.assertEquals("Account Test", bidList1.getAccount());
        Assertions.assertEquals("Type Test", bidList1.getType());
        Assertions.assertEquals(3.2, bidList1.getBidQuantity());
        Assertions.assertEquals("Account Test 2", bidList2.getAccount());
        Assertions.assertEquals("Type Test 2", bidList2.getType());
        Assertions.assertEquals(5.2, bidList2.getBidQuantity());

        Assertions.assertTrue(bidLists.size() == 2);

        //Save Test

        BidList bidList = new BidList();
        bidList.setBidQuantity(7.0);
        bidList.setAccount("New Account Test");
        bidList.setType("New Type Test");

        BidList bidListResponse = bidListService.save(bidList);
        Assertions.assertNotNull(bidListResponse.getBidListId());
        Assertions.assertEquals(bidList.getAccount(), bidListResponse.getAccount());

        //FindById Test

        Optional<BidList> optionalBidList = bidListService.findById(3);

        Assertions.assertTrue(optionalBidList.isPresent());
        Assertions.assertEquals(7, optionalBidList.get().getBidQuantity());
        Assertions.assertEquals("New Account Test", optionalBidList.get().getAccount());

        // Delete Test

        bidListService.deleteById(3);

        optionalBidList = bidListService.findById(3);

        Assertions.assertTrue(optionalBidList.isEmpty());
    }
}
