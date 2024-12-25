package com.example.demo.repository;

import com.example.demo.model.Issuer;
import com.example.demo.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByDateAndIssuer(LocalDate date, Issuer issuer);
    List<Stock> findByIssuerIdAndDateBetween(Long companyId, LocalDate from, LocalDate to);
    List<Stock> findByIssuerId(Long companyId);

}