package com.learn.repository;

import com.learn.model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
  Page<Order> findByUserId(Long userId, Pageable page);

  Optional<Order> findByUserIdAndId(Long userId, Long orderId);

  @Transactional
  boolean deleteByUserId(Long userId);

  @Transactional
  boolean deleteByUserIdAndId(Long userId, Long id);
}
