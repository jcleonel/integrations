package com.sensedia.desafio.integrations.repository;

import com.sensedia.desafio.integrations.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
