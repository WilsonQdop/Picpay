package com.WilsonPicpay.Picpay.Repositories;

import com.WilsonPicpay.Picpay.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
