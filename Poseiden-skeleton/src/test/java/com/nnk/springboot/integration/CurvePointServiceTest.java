package com.nnk.springboot.integration;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
@Sql("/banco_scripts/curvepoint.sql")
public class CurvePointServiceTest {
    @Autowired
    private CurvePointService curvePointService;

    @Test
    public void curvePointIT(){

        // Find All Test

        List<CurvePoint> curvePoints = curvePointService.findAll();

        CurvePoint curvePoint1 = curvePoints.get(0);
        CurvePoint curvePoint2 = curvePoints.get(1);

        if(curvePoint1.getId() != 1){
            curvePoint1 = curvePoints.get(1);
            curvePoint2 = curvePoints.get(0);
        }

        Assertions.assertEquals(1.1, curvePoint1.getTerm());
        Assertions.assertEquals(2.2, curvePoint1.getValue());
        Assertions.assertEquals(3.3, curvePoint2.getTerm());
        Assertions.assertEquals(4.4, curvePoint2.getValue());

        Assertions.assertTrue(curvePoints.size() == 2);

        //Save Test

        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setTerm(5.5);
        curvePoint.setValue(6.6);

        CurvePoint curvePointResponse = curvePointService.save(curvePoint);
        Assertions.assertNotNull(curvePointResponse.getId());
        Assertions.assertEquals(curvePoint.getTerm(), curvePointResponse.getTerm());

        //FindById Test

        Optional<CurvePoint> optionalCurvePoint = curvePointService.findById(3);

        Assertions.assertTrue(optionalCurvePoint.isPresent());
        Assertions.assertEquals(5.5, optionalCurvePoint.get().getTerm());
        Assertions.assertEquals(6.6, optionalCurvePoint.get().getValue());

        // Delete Test

        curvePointService.deleteById(3);

        optionalCurvePoint = curvePointService.findById(3);

        Assertions.assertTrue(optionalCurvePoint.isEmpty());
    }
}
