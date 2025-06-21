package com.ZETA.KN.controller;

import com.ZETA.KN.model.MemberDetail;
import com.ZETA.KN.model.TransactionDetail;
import com.ZETA.KN.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calculate")
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @GetMapping("/total/{groupName}")
    public ResponseEntity<Integer> getTotal(@PathVariable String groupName) {
        int total = calculationService.calculateTotal(groupName);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/average/{groupName}")
    public ResponseEntity<Double> getAverage(@PathVariable String groupName) {
        double average = calculationService.calculateAverage(groupName);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/details/{groupName}")
    public ResponseEntity<List<MemberDetail>> getMemberDetails(@PathVariable String groupName) {
        List<MemberDetail> details = calculationService.getMemberDetailsWithNetAmount(groupName);
        return ResponseEntity.ok(details);
    }

}


