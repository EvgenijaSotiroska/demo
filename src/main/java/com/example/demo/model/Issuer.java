package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "ISSUERS_DATABASE")
@AllArgsConstructor
@NoArgsConstructor
public class Issuer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyCode;
    private LocalDate lastUpdated;

    @OneToMany(mappedBy = "issuer", fetch = FetchType.EAGER)
    private List<Stock> historicalData;

    public Issuer(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public List<Stock> getHistoricalData() {
        return historicalData;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }
}
