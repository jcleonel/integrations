package com.sensedia.desafio.integrations.repository;

import com.sensedia.desafio.integrations.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}