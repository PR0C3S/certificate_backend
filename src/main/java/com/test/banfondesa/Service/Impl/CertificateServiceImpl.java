package com.test.banfondesa.Service.Impl;



import com.test.banfondesa.Entity.Certificate;
import java.util.List;

public interface CertificateServiceImpl {
    public Certificate save(Certificate certificate);

    public void delete(Certificate certificate);
    public Certificate getById(Integer id);


    public List<Certificate> getAll();
}
