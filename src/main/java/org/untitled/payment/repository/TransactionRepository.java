package org.untitled.payment.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.untitled.payment.domain.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
