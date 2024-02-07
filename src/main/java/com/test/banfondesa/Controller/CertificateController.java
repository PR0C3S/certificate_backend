package com.test.banfondesa.Controller;



import com.test.banfondesa.DTO.*;
import com.test.banfondesa.Entity.Certificate;
import com.test.banfondesa.Entity.Client;
import com.test.banfondesa.Entity.Transaction;
import com.test.banfondesa.Exception.ResourceNotFoundException;
import com.test.banfondesa.Service.CertificateService;
import com.test.banfondesa.Service.ClientService;
import com.test.banfondesa.Service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
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

    @Autowired
    private TransactionService transactionService;


    @GetMapping("/list")
    public List<Certificate> listCertificateGET(){
        return certificateService.getAll();
    }



    @PostMapping("/request")
    public Certificate createCertificatePOST(@Valid @RequestBody CertificateDTO certificateDTO){
        Client client = clientService.getById(certificateDTO.getIdClient());
        if(client ==null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+certificateDTO.getIdClient());
        }
       

        Certificate certificate = new Certificate(null,null,certificateDTO.getStartDate(),certificateDTO.getFinishDate(),
                certificateDTO.getAmount(),certificateDTO.getCurrency(),certificateDTO.getEarnInterest(),certificateDTO.getCancellInterest(),
                certificateDTO.getIsAbleToCancellBefore(),certificateDTO.getIsPenaltyForCancellBefore(),certificateDTO.getIsAbleToPayBefore(),
                certificateDTO.getStatus(),certificateDTO.getDuration(), client,null);
        
        Transaction transaction = new Transaction(null,certificate.getStartDate(),"Created","Creacion del certificado",certificate.getAmount(),certificate);
        certificate.add(transaction);
       
        return certificateService.save(certificate);
    }

    @GetMapping("/balance/{id}")
    public BalanceDTO getBalanceByIdGET(@PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate==null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        return new BalanceDTO(certificate.getAmount(),certificate.getId(),certificate.getAccountNumber(),certificate.getStartDate(),certificate.getFinishDate(),certificate.getAmount(),certificate.getStatus(),certificate.getClient().getDni(),certificate.getClient().getFullName());

        //int monthsBetween = LocalDate.now().minusMonths(certificate.getStartDate().getMonth()).getMonthValue();
          //Interes = MontoPrincipal*TasaDeInteres*PlazoEnDias/DiasDelAño(365)
        //IR = Interes*15%
        //Total de interes acreditable = Interes - IR
    }

    @GetMapping("/balance/cliente/{id}")
    public List<BalanceDTO> getBalanceByClientGET(@PathVariable Integer id){
        Client client = clientService.getById(id);
        if(client==null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        }

        List<BalanceDTO> balances = new ArrayList<>();
        for (Certificate certificate : client.getCertificates()) {
            BalanceDTO balanceDTO = new BalanceDTO(certificate.getAmount(),certificate.getId(),certificate.getAccountNumber(),certificate.getStartDate(),certificate.getFinishDate(),certificate.getAmount(),certificate.getStatus(),certificate.getClient().getDni(),certificate.getClient().getFullName());
            balances.add(balanceDTO);
        }
        return balances;
    }


    // Simulacion retiro a un certificado en especifico
    @PostMapping("/certificado/retiro/{id}")
    public Certificate withdrawalByIdToCertificatePOST( @PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        } else if(!certificate.getStatus().equalsIgnoreCase("Active")){
            throw new ResourceNotFoundException("No se pudo retirar, el certificado con el id: "+id+", no se encuentra activo.");
        }

        Date today = new Date();
        if(certificate.getFinishDate().before(today)){
            //with out penalty
            certificate.setStatus("FINISHED");
        }else{
            //with penalty
            certificate.setStatus("CANCELL");
            float interestAmount = certificate.getAmount()*certificate.getCancellInterest();
            Transaction transaction = new Transaction(null,today,"Retire","Intereses por cancelacion",interestAmount);
            certificate.setAmount(certificate.getAmount()-interestAmount);
            certificate.Add(transaction);
        }

        Transaction withdrawal = new Transaction(null,transactionDTO.getDate(), "Retire","Retiro del certificado", certificate.getAmount(),certificate);
        certificate.Add(transaction);
        certificate.setAmount(0);
        return certificateService.save(certificate);
    }

    // Simulacion retiro a un certificado en especifico
    @PostMapping("/certificado/retiro/cliente/{id}")
    public List<Certificate> withdrawalByClientToCertificatePOST( @PathVariable Integer id){
        Client client = clientService.getById(id);
        if(client == null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        } 

        Date today = new Date();
        if(certificate.getFinishDate().before(today)){
            //with out penalty
            certificate.setStatus("FINISHED");
        }else{
            //with penalty
            certificate.setStatus("CANCELL");
            float interestAmount = certificate.getAmount()*certificate.getCancellInterest();
            Transaction transaction = new Transaction(null,today,"Retire","Intereses por cancelacion",interestAmount);
            certificate.setAmount(certificate.getAmount()-interestAmount);
            certificate.Add(transaction);
        }

        Transaction withdrawal = new Transaction(null,transactionDTO.getDate(), "Retire","Retiro del certificado", certificate.getAmount(),certificate);
        certificate.Add(transaction);
        certificate.setAmount(0);
        return certificateService.save(certificate);
    }

    // Simulacion deposito de meses a un certificado en especifico
    @PostMapping("/deposito/{id}")
    public Transaction depositToCertificateV1POST( @PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);

        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        if(!certificate.getStatus().equalsIgnoreCase("Active")){
            throw new ResourceNotFoundException("No se pudo depositar, el certificado con el id: "+id+", no se encuentra activo.");
        }

        //Agregar iterable que verifique cuantos depositos hay y realizar el deposito de los meses restantes
        float earnMoney = certificate.getAmount()*certificate.getEarnInterest()/12;
        certificate.setAmount(certificate.getAmount()+earnMoney);
        Transaction deposit = new Transaction(null,null, "Deposit", "Ganancia del certificado", earnMoney,certificate);
        certificate.Add(deposit);
        certificateService.save(certificate);
        return deposit;
    }

    // Simulacion deposito de meses de los certificados activos de un cliente
    @PostMapping("/deposito/cliente/{id}")
    public Transaction depositToCertificateV2POST(@PathVariable Integer id){
        Client client = clientService.getById(id);

        if(client == null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        }

        for (Certificate certificate : client.getCertificates()) {
            if(certificate.getStatus().equalsIgnoreCase("Active")){
                 //Agregar iterable que verifique cuantos depositos hay y realizar el deposito de los meses restantes
                float earnMoney = certificate.getAmount()*certificate.getEarnInterest()/12;
                certificate.setAmount(certificate.getAmount()+earnMoney);
                Transaction deposit = new Transaction(null,null, "Deposit", "Ganancia del certificado", earnMoney,certificate);
                return transactionService.save(deposit);
            }
            
        }
    }

    @GetMapping("/ganancia/{id}")
    public RevenueDTO getRevenueByIdGET(@PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate==null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        List<TransactionDTO> transactions = new ArrayList<>();

        Date initialDate = certificate.getStartDate();
        float lastAmount= certificate.getAmount();
        for (int i = 0; i < certificate.getDuration() ; i++) {
            initialDate = initialDate.plusMonths(i);
            float amount= lastAmount*(certificate.getEarnInterest()/12);
            TransactionDTO transaction = new TransactionDTO(initialDate,"Deposit","I",initialDate,amount);
            transactions.add(transaction);
            lastAmount = amount;
        }
        return RevenueDTO(certificate.getCurrency(), certificate.getId(), certificate.getStatus(), certificate.getClient().getDni(), certificate.getClient().getFullName(), transactions);
    }

}
