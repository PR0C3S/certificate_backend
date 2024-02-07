package com.test.banfondesa.Controller;



import com.test.banfondesa.DTO.*;
import com.test.banfondesa.Entity.Certificate;
import com.test.banfondesa.Entity.Client;
import com.test.banfondesa.Entity.Transaction;
import com.test.banfondesa.Exception.BadRequestException;
import com.test.banfondesa.Exception.ResourceNotFoundException;
import com.test.banfondesa.Service.CertificateService;
import com.test.banfondesa.Service.ClientService;
import com.test.banfondesa.Service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

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

    private final String deposit = "Deposito";
    private final String finished = "Finalizado";
    private final String active = "Activo";
    private final String cancell = "Cancelado";
    private final String retire = "Retiro";
    private final String create = "Creacion";

    // create instance of Random class
    Random rand = new Random();


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
       

        Certificate certificate = new Certificate(null,rand.nextInt(96000000,96999999),certificateDTO.getStartDate(),certificateDTO.getStartDate().plusMonths(certificateDTO.getDuration()),
                certificateDTO.getAmount(),certificateDTO.getCurrency(),certificateDTO.getEarnInterest(),certificateDTO.getCancellInterest(),
                certificateDTO.getIsAbleToCancellBefore(),certificateDTO.getIsPenaltyForCancellBefore(),certificateDTO.getIsAbleToPayBefore(),
                active,certificateDTO.getDuration(), client,null);
        
        Transaction transaction = new Transaction(null,certificate.getStartDate(),create,"Creacion del certificado",certificate.getAmount());
        certificate.Add(transaction);
        return certificateService.save(certificate);
    }

    @GetMapping("/balance/{id}")
    public BalanceDTO getBalanceByIdGET(@PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate==null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        return new BalanceDTO(certificate.getId(),certificate.getAccountNumber(),certificate.getStartDate(),certificate.getFinishDate(),certificate.getAmount(),certificate.getCurrency(),certificate.getStatus(),certificate.getClient().getDni(),certificate.getClient().getFullName(),certificate.getTransactions());
    }

    @GetMapping("/balance/cliente/{id}")
    public List<BalanceDTO> getBalanceByClientGET(@PathVariable Integer id){
        Client client = clientService.getById(id);
        if(client==null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        }

        List<BalanceDTO> balances = new ArrayList<>();
        for (Certificate certificate : client.getCertificates()) {
            BalanceDTO balanceDTO = new BalanceDTO(certificate.getId(),certificate.getAccountNumber(),certificate.getStartDate(),certificate.getFinishDate(),certificate.getAmount(),certificate.getCurrency(),certificate.getStatus(),certificate.getClient().getDni(),certificate.getClient().getFullName(),certificate.getTransactions());
            balances.add(balanceDTO);
        }
        return balances;
    }

    @GetMapping("/ganancia/{id}")
    public RevenueDTO getRevenueByIdGET(@PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);
        if(certificate==null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        List<Transaction> transactions = new ArrayList<>();

        LocalDate simulateDate = certificate.getStartDate();
        float amount = getCreateAmount(certificate);
        float totalAmount = amount;
        float lastAmount= amount;
        for (int i = 0; i < certificate.getDuration() ; i++) {
            float currentAmount= lastAmount*(certificate.getEarnInterest()/12);
            Transaction transaction = new Transaction(i,simulateDate.plusMonths(i),deposit,"Interes generados",currentAmount);
            transactions.add(transaction);
            lastAmount = currentAmount;
            totalAmount+=currentAmount;
        }
        return new RevenueDTO(certificate.getCurrency(), certificate.getId(), certificate.getStartDate(),certificate.getFinishDate(),certificate.getStatus(), certificate.getClient().getDni(), certificate.getClient().getFullName(),certificate.getAmount(), totalAmount,transactions);
    }

    // Simulacion deposito de 1 mes a un certificado en especifico
    @PostMapping("/deposito/{id}")
    public Certificate depositOneMonthToCertificateV1POST( @PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);

        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        if(!certificate.getStatus().equalsIgnoreCase(active)){
            throw new BadRequestException("No se pudo completar la transaccion, el certificado con el id: "+id+", no se encuentra activo.");
        }

        int currentDeposit = checkDepositByType(certificate.getTransactions(),deposit);
        if(currentDeposit >= certificate.getDuration()){
            throw  new BadRequestException("No se puede realizar mas depositos a el certificado con el id: "+id);
        }

        LocalDate simulateDate = certificate.getStartDate();


        float earnMoney = certificate.getAmount()*certificate.getEarnInterest()/12;
        certificate.setAmount(certificate.getAmount()+earnMoney);
        if(certificate.getDuration()==currentDeposit+1){
            certificate.setStatus(finished);
        }
        Transaction depositTransaction = new Transaction(null,simulateDate.plusMonths(currentDeposit+1), deposit, "Intereses generados", earnMoney);
        certificate.Add(depositTransaction);
        return certificateService.save(certificate);
    }

    // Simulacion deposito de meses restantes a un certificado en especifico
    @PostMapping("/deposito/completo/{id}")
    public Certificate depositRestMonthToCertificateV1POST( @PathVariable Integer id){
        Certificate certificate = certificateService.getById(id);

        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        }
        if(!certificate.getStatus().equalsIgnoreCase(active)){
            throw new BadRequestException("No se pudo realizar la transaccion, el certificado con el id: "+id+", no se encuentra activo.");
        }

        int currentDeposit = checkDepositByType(certificate.getTransactions(),deposit);
        if(currentDeposit >= certificate.getDuration()){
            throw  new BadRequestException("No se puede realizar mas depositos a el certificado con el id: "+id);
        }

        float earnInterestByMonth=certificate.getEarnInterest()/12;
        float lastAmount = certificate.getAmount();
        float total = certificate.getAmount();

        for (int i = currentDeposit+1; i <= certificate.getDuration() ; i++) {
            float earnMoney = lastAmount*earnInterestByMonth;

            LocalDate simulateDate = certificate.getStartDate();
            Transaction depositTransaction = new Transaction(null,simulateDate.plusMonths(i), deposit, "Intereses generados", earnMoney);
            certificate.Add(depositTransaction);
            lastAmount = earnMoney;
            total+=earnMoney;
        }
        certificate.setAmount(total);
        certificate.setStatus(finished);
        return certificateService.save(certificate);
    }


    // Simulacion retiro a un certificado en especifico
    @PostMapping("/retiro/{id}")
    public Certificate withdrawalByIdToCertificatePOST(@PathVariable Integer id, @Valid @RequestBody TransactionDTO transactionDTO){
        Certificate certificate = certificateService.getById(id);
        if(certificate == null){
            throw new ResourceNotFoundException("No se pudo encontrar el certificado con el id: "+id);
        } else if(certificate.getAmount()<=0){
            throw new BadRequestException("No se pudo realizar la transaccion, el certificado con el id: "+id+", no tiene dinero.");
        }
        LocalDate today = transactionDTO.getDate();

        if(certificate.getFinishDate().isBefore(today)){
            //with out penalty
            certificate.setStatus(finished);
        }else{
            //with penalty
            certificate.setStatus(cancell);
            float interestAmount = certificate.getAmount()*certificate.getCancellInterest();
            Transaction transaction = new Transaction(null,today,retire,"Intereses por cancelacion",interestAmount);
            certificate.setAmount(certificate.getAmount()-interestAmount);
            certificate.Add(transaction);
        }

        Transaction withdrawal = new Transaction(null,today, retire,"Retiro del certificado", certificate.getAmount());
        certificate.Add(withdrawal);
        certificate.setAmount(0);
        return certificateService.save(certificate);
    }



    // Simulacion retiro a un certificado en especifico
    @PostMapping("/retiro/cliente/{id}")
    public Client withdrawalByClientToCertificatePOST( @PathVariable Integer id, @Valid @RequestBody TransactionDTO transactionDTO){
        Client client = clientService.getById(id);
        if(client == null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        }
        List<Certificate> certificateList = new ArrayList<>();
        LocalDate today = transactionDTO.getDate();
        for(Certificate certificate: client.getCertificates()){
            if(certificate.getAmount()>0){
                if(certificate.getFinishDate().isBefore(today)){
                    //with out penalty
                    certificate.setStatus(finished);
                }else{
                    //with penalty
                    certificate.setStatus(cancell);
                    float interestAmount = certificate.getAmount()*certificate.getCancellInterest();
                    Transaction transaction = new Transaction(null,today,retire,"Intereses por cancelacion",interestAmount);
                    certificate.setAmount(certificate.getAmount()-interestAmount);
                    certificate.Add(transaction);
                }

                Transaction withdrawal = new Transaction(null,today, retire,"Retiro del certificado", certificate.getAmount());
                certificate.Add(withdrawal);
                certificate.setAmount(0);
                certificateList.add(certificateService.save(certificate));
            }else{
                certificateList.add(certificate);
            }
        }
        client.setCertificates(certificateList);
        return client;
    }


    private int checkDepositByType(List<Transaction> transactions, String type){
        int total=0;
        for (Transaction transaction: transactions){
            if(transaction.getType().equalsIgnoreCase(type)){
                total++;
            }
        }
        return  total;
    }
    private float getCreateAmount(Certificate certificateList){
        for (Transaction transaction: certificateList.getTransactions()){
            if(transaction.getType().equalsIgnoreCase(create)){
                return transaction.getAmount();
            }
        }
        return 0;
    }

}
