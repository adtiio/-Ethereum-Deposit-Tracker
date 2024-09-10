package com.ethreumTracker.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ethreumTracker.model.Deposit;
import com.ethreumTracker.repository.DepositRepository;
import com.ethreumTracker.service.EmailService;
import com.ethreumTracker.service.EthereumService;

@RestController
@RequestMapping("/deposits")
public class DepositController {

    private final EthereumService ethereumService;
    private final DepositRepository depositRepository;
    private final EmailService emailService;
    

    @Autowired
    public DepositController(EthereumService ethereumService, DepositRepository depositRepository,EmailService emailService) {
        this.ethereumService = ethereumService;
        this.depositRepository = depositRepository;
        this.emailService= emailService;
    }

    @GetMapping("/track/")
    public ResponseEntity<String> trackDeposit(@RequestParam String txHash) {
        try {
            ethereumService.seperate(txHash);
            emailService.sendSuccessEmail("asbijutkar045@gmail.com", "You have successfully added the token with hash: " + txHash);
            return ResponseEntity.ok("Deposit tracked and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error: " + e.getMessage());
            
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Deposit>> getAllDeposits() {
        List<Deposit> deposits = depositRepository.findAll();
        return ResponseEntity.ok(deposits);
    }

    @GetMapping("/{txHash}")
    public ResponseEntity<Deposit> getDepositByHash(@PathVariable String txHash) {
        Optional<Deposit> deposit = depositRepository.findByHash(txHash);
        return deposit.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                            .body(null));
    }

    @GetMapping("/start-tracking")
    public ResponseEntity<String> startTracking() {
        ethereumService.startBlockListener();
        return ResponseEntity.ok("Real-time Ethereum deposit tracking started.");
    }
}
