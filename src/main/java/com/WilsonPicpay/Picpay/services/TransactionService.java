package com.WilsonPicpay.Picpay.services;

import com.WilsonPicpay.Picpay.Repositories.TransactionRepository;
import com.WilsonPicpay.Picpay.domain.transaction.Transaction;
import com.WilsonPicpay.Picpay.domain.user.User;
import com.WilsonPicpay.Picpay.dtos.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransation(TransactionDTO transactionDTO) throws Exception {
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        userService.validateTransction(sender, transactionDTO.value());

        boolean isAuthorized = !this.authorizeTransaction(sender, transactionDTO.value());
        if (!isAuthorized) {
            throw new Exception("Transação recusada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimeStamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sandNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sandNotification(receiver, "Transação recebida com sucesso");

        return newTransaction;

    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        String url = "https://util.devi.tools/api/v2/authorize";
        try {
            ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(url, Map.class);
            if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
                Map responseBody = authorizationResponse.getBody();
                if (responseBody != null && Boolean.TRUE.equals(responseBody.get("authorized"))) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            System.err.println("Erro ao autorizar a transação: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
