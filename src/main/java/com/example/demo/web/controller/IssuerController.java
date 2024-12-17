package com.example.demo.web.controller;

import com.example.demo.model.Issuer;
import com.example.demo.service.IssuerService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Issuer> findById(@PathVariable Long id) {
        return this.issuerService
                .findById(id)
                .map(issuer -> ResponseEntity.ok().body(issuer))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
