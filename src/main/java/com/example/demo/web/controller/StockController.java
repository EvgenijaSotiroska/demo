package com.example.demo.web.controller;

import com.example.demo.model.Issuer;
import com.example.demo.model.Stock;
import com.example.demo.service.IssuerService;
import com.example.demo.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/stock")
//@Validated
//@CrossOrigin(origins="*")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/all")
    public String findAll(Model model) {
        List<Stock> stocks = this.stockService.findAll();
        model.addAttribute("stocks", stocks);
        return "stocks";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> findById(@PathVariable Long id) {
        return this.stockService
                .findById(id)
                .map(stock -> ResponseEntity.ok().body(stock))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
