package com.example.demo.service;

import com.example.demo.model.Issuer;
import com.example.demo.model.Stock;
import com.example.demo.dto.Response;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.IssuerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final StockRepository stockRepository;
    private final IssuerRepository issuerRepository;

    private final String technicalAnalysisUrl = "http://127.0.0.1:5000/generate_signal";
    private final String nlpUrl = "http://127.0.0.1:5000/analyze";
    private final String lstmUrl = "http://127.0.0.1:8000/predict-next-month-price/";

    public String technicalAnalysis(Long companyId) {
        List<Stock> data = stockRepository.findByIssuerId(companyId);
        List<Map<String, Object>> payload = new ArrayList<>();
        for (Stock d : data) {
            Map<String, Object> record = new HashMap<>();
            record.put("date", d.getDate().toString());
            record.put("close", d.getLastTransaction());
            record.put("open", (d.getMax() + d.getMin()) / 2.0);
            record.put("high", d.getMax());
            record.put("low", d.getMin());
            record.put("volume", d.getQuantity());
            payload.add(record);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                technicalAnalysisUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );
        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null && responseBody.containsKey("final_signal")) {
            return responseBody.get("final_signal").toString();
        } else {
            throw new RuntimeException("Failed to retrieve a valid signal from the Python API.");
        }
    }

    public Response nlp(Long companyId) throws Exception {
        Issuer company = issuerRepository.findById(companyId).orElseThrow(() -> new Exception("Company not found"));

        String companyCode = company.getCompanyCode();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                nlpUrl + "?company_code=" + companyCode,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody != null) {
            if (responseBody.containsKey("error")) {
                String errorMessage = (String) responseBody.get("error");
                throw new RuntimeException("Error from Python API: " + errorMessage);
            }

            Response response = new Response();
            response.sentimentScore = (Double) responseBody.get("sentiment_score");
            response.recommendation = (String) responseBody.get("recommendation");

            return response;
        } else {
            throw new RuntimeException("Failed to retrieve sentiment analysis from the Python API.");
        }
    }
    public static List<Map<String, Object>> mapToRequestData(List<Stock> historicalDataEntities) {
        return historicalDataEntities.stream().map(entity -> {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("date", entity.getDate().toString());
            dataMap.put("average_price", entity.getAverage());
            return dataMap;
        }).collect(Collectors.toList());
    }

    public Double lstm(Long companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Stock> data = stockRepository.findByIssuerIdAndDateBetween(companyId, LocalDate.now().minusMonths(3), LocalDate.now());;

        Map<String, Object> requestBody = Map.of("data", mapToRequestData(data));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        Map<String, Double> response = restTemplate.postForObject(lstmUrl, requestEntity, Map.class);

        return response != null ? response.get("predicted_next_month_price") : null;
    }


}
