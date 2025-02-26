package com.sensedia.desafio.integrations.repository;

import com.sensedia.desafio.integrations.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}