package com.example.demo.web.controller;

import com.example.demo.dto.Response;
import com.example.demo.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping("/analyze")
    public ResponseEntity<Response> nlp(@RequestParam(name = "companyId") Long companyId) throws Exception {
        Response response = predictionService.nlp(companyId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/predict")
    public ResponseEntity<String> technicalAnalysis(@RequestParam(name = "companyId") Long companyId) {
        String response = predictionService.technicalAnalysis(companyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/predict-next-month-price")
    public ResponseEntity<Double> lstm(@RequestParam(name = "companyId") Long companyId) {
        Double response = predictionService.lstm(companyId);
        return ResponseEntity.ok(response);
    }
}
