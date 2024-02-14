package com.nnk.springboot.unit;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
public class BidTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @Test
    public void bindListTest() throws Exception {

        BidList bidList1 = new BidList();
        bidList1.setBidListId(1);
        bidList1.setAccount("Account Test");
        bidList1.setType("Type test");
        bidList1.setBidQuantity(2.3);

        BidList bidList2 = new BidList();
        bidList2.setBidListId(2);
        bidList1.setAccount("Account Test2");
        bidList1.setType("Type test2");
        bidList1.setBidQuantity(5.2);

        List<BidList> bidList =  new ArrayList<>(Arrays.asList(bidList1, bidList2));

        Mockito.when(bidListService.findAll()).thenReturn(bidList);

        MvcResult resultActions = mockMvc.perform(get("/bidList/list")).andExpectAll(
                status().isOk(),
                model().size(3),
                model().attribute("title", "Bid List")
        ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Account Test"));
        Assertions.assertTrue(content.contains("Account Test2"));
        Assertions.assertTrue(content.contains("Type test"));
        Assertions.assertTrue(content.contains("Type test2"));

        Mockito.verify(bidListService, Mockito.times(1)).findAll();
    }

    @Test
    public void bidAddTest() throws Exception {

        mockMvc.perform(get("/bidList/add")).andExpectAll(
                status().isOk(),
                model().size(2),
                model().attribute("title", "Bid List - ADD")
        );
    }

    @Test
    public void bidValidateErrorTest() throws Exception {

        BidList bidList = new BidList();

        Mockito.when(bidListService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/bidList/validate").flashAttr("bidList", bidList))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Account is mandatory"));
        Assertions.assertTrue(content.contains("Type is mandatory"));
        Assertions.assertTrue(content.contains("Bid Quantity is mandatory"));

        Mockito.verify(bidListService, Mockito.times(0)).save(any());
    }

    @Test
    public void bidValidateTest() throws Exception {

        BidList bidList = new BidList();
        bidList.setAccount("Account Test");
        bidList.setType("Type Teste");
        bidList.setBidQuantity(5.2);

        Mockito.when(bidListService.save(any())).thenReturn(null);

        mockMvc.perform(post("/bidList/validate").flashAttr("bidList", bidList))
                .andExpect(
                        redirectedUrl("/bidList/list")
                );

        Mockito.verify(bidListService, Mockito.times(1)).save(any());
    }

    @Test
    public void bidUpdateErrorTest() throws Exception {

        Mockito.when(bidListService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/bidList/update/{id}", 1))
                .andExpect(
                            redirectedUrl("/bidList/list?error=true")
                        );

        Mockito.verify(bidListService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void bidUpdateTest() throws Exception {

        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("Account Test");
        bidList.setType("Type Teste");
        bidList.setBidQuantity(5.2);

        Mockito.when(bidListService.findById(anyInt())).thenReturn(Optional.of(bidList));

        mockMvc.perform(get("/bidList/update/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        model().size(2),
                        model().attribute("title", "Bid List - UPDATE")
                );

        Mockito.verify(bidListService, Mockito.times(1)).findById(anyInt());
    }

    @Test
    public void bidUpdateValidatorErrorTest() throws Exception {

        BidList bidList = new BidList();
        bidList.setBidListId(1);

        Mockito.when(bidListService.save(any())).thenReturn(null);

        MvcResult resultActions = mockMvc.perform(post("/bidList/update/{id}", 1).flashAttr("bidList", bidList))
                .andExpect(
                        status().isOk()
                ).andReturn();

        String content = resultActions.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("Account is mandatory"));
        Assertions.assertTrue(content.contains("Type is mandatory"));
        Assertions.assertTrue(content.contains("Bid Quantity is mandatory"));

        Mockito.verify(bidListService, Mockito.times(0)).save(any());
    }

    @Test
    public void bidUpdateValidatorTest() throws Exception {

        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("Account Test");
        bidList.setType("Type Teste");
        bidList.setBidQuantity(5.2);

        Mockito.when(bidListService.save(any())).thenReturn(null);

       mockMvc.perform(post("/bidList/update/{id}", 1).flashAttr("bidList", bidList))
                .andExpect(
                        redirectedUrl("/bidList/list")
                );

        Mockito.verify(bidListService, Mockito.times(1)).save(any());
    }

    @Test
    public void bidDeleteTest() throws Exception {

        Mockito.doNothing().when(bidListService).deleteById(anyInt());

        mockMvc.perform(get("/bidList/delete/{id}", 1)).andExpect(
                redirectedUrl("/bidList/list")
        );

        Mockito.verify(bidListService, Mockito.times(1)).deleteById(anyInt());
    }
}
