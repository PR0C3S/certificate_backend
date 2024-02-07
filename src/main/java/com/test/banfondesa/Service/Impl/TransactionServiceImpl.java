package com.test.banfondesa.Service.Impl;

import com.test.banfondesa.Entity.Transaction;
import java.util.List;

public interface TransactionServiceImpl {
    public Transaction save(Transaction transaction);

    public void delete(Transaction transaction);
    public Transaction getById(Integer id);


    public List<Transaction> getAll();
}
