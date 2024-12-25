package com.example.demo.repository;

import com.example.demo.model.Issuer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuerRepository extends JpaRepository<Issuer, Long> {
    Optional<Issuer> findByCompanyCode(String companyCode);
    List<Issuer> findAllByOrderByIdAsc();

}
