package com.example.demo.web.controller;

import com.example.demo.model.Issuer;
import com.example.demo.model.Stock;
import com.example.demo.service.IssuerService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/issuer")
@Validated
@CrossOrigin(origins="*")
public class IssuerController {
    private final IssuerService issuerService;

    public IssuerController(IssuerService issuerService) {
        this.issuerService = issuerService;
    }

    @GetMapping("/all")
    public String findAll(Model model) {
        List<Issuer> issuers = this.issuerService.findAll();
        model.addAttribute("issuers", issuers);
        return "issuersList";
    }

    @GetMapping("/issuer_stocks")
    public String findById(@RequestParam(name = "companyId") Long id, Model model) {
        List<Map<String, Object>> issuerData = new ArrayList<>();
        Issuer issuer = issuerService.findById(id).orElse(new Issuer());
        Map<String, Object> data = new HashMap<>();
        data.put("companyCode", issuer.getCompanyCode());
        data.put("lastUpdated", issuer.getLastUpdated());

        List<Double> prices = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        for (Stock historicalData : issuer.getHistoricalData()) {
            dates.add(historicalData.getDate());
            prices.add(historicalData.getLastTransaction());
        }

        data.put("dates", dates);
        data.put("prices", prices);
        data.put("id", issuer.getId());
        issuerData.add(data);
        model.addAttribute("companyData", issuerData);
        return "issuer";
    }

}
