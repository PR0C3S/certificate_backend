package com.test.banfondesa.Service;


import com.test.banfondesa.Entity.Transaction;
import com.test.banfondesa.Repository.TransactionRepository;
import com.test.banfondesa.Service.Impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService implements TransactionServiceImpl {

    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void delete(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public Transaction getById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }
}
