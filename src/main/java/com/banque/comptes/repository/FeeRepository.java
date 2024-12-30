package com.banque.comptes.repository;

import com.banque.comptes.Entities.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeeRepository extends JpaRepository<Fee, UUID> {
}
