package com.example.demo.dto;

import lombok.Data;

@Data
public class Response {
    public String recommendation;
    public Double sentimentScore;
}
