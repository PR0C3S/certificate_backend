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
                certificateDTO.getStatus(),certificateDTO.getDuration(), client);
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
    @PostMapping("/certificado/retiro")
    public Certificate withdrawalToCertificatePOST( @Valid @RequestBody TransactionDTO transactionDTO){
        Certificate certificate = certificateService.getById(transactionDTO.getCertificateId());
        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+transactionDTO.getCertificateId());
        } else if(!certificate.getStatus().equalsIgnoreCase("Active")){
            throw new ResourceNotFoundException("No se pudo retirar, el certificado con el id: "+transactionDTO.getCertificateId()+", no se encuentra activo.");
        }

        float earnInterest = 0;
        float cancellInterest = 0;
        int monthsBetween =0;
        if(certificate.getFinishDate().before(transactionDTO.getDate())){
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

        return certificateService.save(certificate);
    }


    // Simulacion deposito de meses de 1 certificado en especifico
    @PostMapping("/deposito-v1")
    public Transaction depositToCertificateV1POST(  @Valid @RequestBody TransactionDTO transactionDTO){
        Certificate certificate = certificateService.getById(transactionDTO.getCertificateId());

        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+transactionDTO.getCertificateId());
        }
        if(!certificate.getStatus().equalsIgnoreCase("Active")){
            throw new ResourceNotFoundException("No se pudo depositar, el certificado con el id: "+transactionDTO.getCertificateId()+", no se encuentra activo.");
        }

        certificate.setAmount(certificate.getAmount()+transactionDTO.getAmount());
        Transaction deposit = new Transaction(null,transactionDTO.getDate(), transactionDTO.getType(), transactionDTO.getMessage(), transactionDTO.getAmount(),certificate);
        return transactionService.save(deposit);
    }

    // Simulacion deposito de meses de los certificados activos de un cliente
    @PostMapping("/deposito-v2")
    public Transaction depositToCertificateV2POST(  @Valid @RequestBody TransactionDTO transactionDTO){
        Certificate certificate = certificateService.getById(transactionDTO.getCertificateId());



        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+transactionDTO.getCertificateId());
        }
        if(!certificate.getStatus().equalsIgnoreCase("Active")){
            throw new ResourceNotFoundException("No se pudo depositar, el certificado con el id: "+transactionDTO.getCertificateId()+", no se encuentra activo.");
        }

        certificate.setAmount(certificate.getAmount()+transactionDTO.getAmount());
        Transaction deposit = new Transaction(null,transactionDTO.getDate(), transactionDTO.getType(), transactionDTO.getMessage(), transactionDTO.getAmount(),certificate);
        return transactionService.save(deposit);
    }


    /*






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
*/



}
