package org.untitled.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.untitled.payment.dto.TransactionDto;
import org.untitled.payment.service.TransactionService;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public void makeOfflineTransaction(@RequestBody TransactionDto request) {
        transactionService.makeOfflineTransaction(request);
    }

}
