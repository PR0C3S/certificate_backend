package com.test.banfondesa.Controller;



import com.test.banfondesa.DTO.BalanceDTO;
import com.test.banfondesa.DTO.RevenueDTO;
import com.test.banfondesa.DTO.RevenueWithCertificate;
import com.test.banfondesa.DTO.TransactionDTO;
import com.test.banfondesa.Entity.Certificate;
import com.test.banfondesa.Entity.Client;
import com.test.banfondesa.Entity.Transaction;
import com.test.banfondesa.Exception.ResourceNotFoundException;
import com.test.banfondesa.Service.CertificateService;
import com.test.banfondesa.Service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/certificado")
@CrossOrigin(value ="http://localhost:5173")
public class CertificateController
{
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/list")
    public List<Certificate> listCertificateGET(){
        return certificateService.getAll();
    }

    @GetMapping("/balance/{id}")
    public Certificate getCertificateByIdGET(@PathVariable Integer id){
        return certificateService.getById(id);
    }

    @PostMapping("/deposito/{idCertificado}")
    public Certificate depositToCertificatePOST(@PathVariable Integer idCertificado,  @Valid @RequestBody TransactionDTO transactionDTO){
        Certificate certificate = certificateService.getById(idCertificado);

        if(certificate == null){
            throw new RuntimeException();
        }

        certificate.setAmount(certificate.getAmount()+transactionDTO.getAmount());
        Transaction deposit = new Transaction(null,transactionDTO.getDate(), transactionDTO.getType(), transactionDTO.getAmount(),0,0,certificate);
        certificate.Addtransaction(deposit);
        return certificateService.save(certificate);
    }

    @PostMapping("/certificado/retiro/{idCertificado}")
    public Certificate withdrawalToCertificatePOST(@PathVariable Integer idCertificado,  @Valid @RequestBody TransactionDTO transactionDTO){
        Certificate certificate = certificateService.getById(idCertificado);
        if(certificate == null){
            throw new RuntimeException();
        } else if (certificate.getStatus().equals("DELETE")) {
            throw new RuntimeException();
        }else if(certificate.getStatus().equals("CANCELL")){
            throw new RuntimeException();
        }

        float earnInterest = 0;
        float cancellInterest = 0;
        int monthsBetween =0;
        if(certificate.getFinishDate().isBefore(transactionDTO.getDate())){
            //with out penalty
            certificate.setStatus("FINISHED");
            monthsBetween = certificate.getFinishDate().minusMonths(certificate.getStartDate().getMonthValue()).getMonthValue();

        }else{
            //with penalty
            certificate.setStatus("CANCELL");
            monthsBetween = LocalDate.now().minusMonths(certificate.getStartDate().getMonthValue()).getMonthValue();
            cancellInterest = certificate.getCurrentCancelInterest(monthsBetween);
        }
        earnInterest =certificate.getCurrentAmount(monthsBetween);

        Transaction withdrawal = new Transaction(null,transactionDTO.getDate(), transactionDTO.getType(), earnInterest,cancellInterest,earnInterest-cancellInterest, certificate);
        certificate.Addtransaction(withdrawal);
        return certificateService.save(certificate);
    }

    @GetMapping("/update/{id}")
    public Certificate updateCertificateGET(@PathVariable Integer id){
        return certificateService.getById(id);
    }

    @GetMapping("/ganancia/{id}")
    public RevenueWithCertificate getRevenueByIdGET(@PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate==null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        List<RevenueDTO> revenueDTOList = new ArrayList<RevenueDTO>();

        LocalDate initialDate = certificate.getStartDate();
        for (int i = 0; i < certificate.getDuration() ; i++) {
            initialDate = initialDate.plusMonths(i);
            RevenueDTO newRevenuew = new RevenueDTO(initialDate.getMonth().toString(),initialDate,certificate.getCurrentAmount(i));
            revenueDTOList.add(newRevenuew);
        }
        return new RevenueWithCertificate(revenueDTOList, certificate.getCurrency(), certificate.getId(), certificate.getStatus(), certificate.getId(), certificate.getId().toString());
    }

    @GetMapping("/balance/{id}")
    public BalanceDTO getBalanceByIdGET(@PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate==null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        int monthsBetween = LocalDate.now().minusMonths(certificate.getStartDate().getMonthValue()).getMonthValue();
        return new BalanceDTO(certificate.getCurrentAmount(monthsBetween),certificate.getId(),certificate.getAccountNumber(),certificate.getStartDate(),certificate.getFinishDate(),certificate.getAmount(),certificate.getStatus(),certificate.getClient().getDNI(),certificate.getClient().getFullName());
    }

    @GetMapping("/balancecliente/{idcliente}")
    public List<BalanceDTO> getBalanceListByClientGET(@PathVariable Integer idcliente){

        Client client = clientService.getById(idcliente);
        if(client==null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+idcliente);
        }
        List<Certificate> certificateList;
        List<BalanceDTO> listBalanceDTO = new ArrayList<>();
        for (Certificate certificate: certificateList){
            int monthsBetween = LocalDate.now().minusMonths(certificate.getStartDate().getMonthValue()).getMonthValue();
            listBalanceDTO.add(new BalanceDTO(certificate.getCurrentAmount(monthsBetween),certificate));
        }

        return listBalanceDTO;
    }




}
