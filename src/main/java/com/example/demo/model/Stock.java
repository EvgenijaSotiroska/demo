package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "STOCK_DATABASE")
@AllArgsConstructor
@NoArgsConstructor
public class Stock{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Double lastTransaction;
    private Double average;
    private Double max;
    private Double min;
    private Double prom;
    private Integer quantity;
    private Integer turnover;
    private Integer totalTurnover;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private Issuer issuer;

    public Stock(LocalDate date, Double lastTransactionPrice, Double maxPrice, Double minPrice, Double averagePrice, Double percentageChange, Integer quantity, Integer turnoverBest, Integer totalTurnover) {
        this.date = date;
        this.lastTransaction = lastTransactionPrice;
        this.max = maxPrice;
        this.min = minPrice;
        this.average = averagePrice;
        this.prom = percentageChange;
        this.quantity = quantity;
        this.turnover = turnoverBest;
        this.totalTurnover = totalTurnover;
    }
}
