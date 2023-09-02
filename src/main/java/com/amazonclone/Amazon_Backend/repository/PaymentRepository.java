package com.amazonclone.Amazon_Backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amazonclone.Amazon_Backend.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
