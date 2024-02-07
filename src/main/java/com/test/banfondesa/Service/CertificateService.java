package com.test.banfondesa.Service;


import com.test.banfondesa.Entity.Certificate;
import com.test.banfondesa.Repository.CertificateRepository;
import com.test.banfondesa.Service.Impl.CertificateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CertificateService implements CertificateServiceImpl {

    @Autowired
    private CertificateRepository certificateRepository;
    @Override
    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public void delete(Certificate certificate) {
        certificateRepository.delete(certificate);
    }

    @Override
    public Certificate getById(Integer id) {
        return certificateRepository.findById(id).orElse(null);
    }

    @Override
    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }
}
