package org.untitled.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.untitled.common.error.ServiceException;
import org.untitled.common.util.JsonProvider;
import org.untitled.e2e.encryption.dto.request.DecryptWithPublicKeyRequest;
import org.untitled.e2e.encryption.service.E2EEncryptionService;
import org.untitled.payment.domain.Customer;
import org.untitled.payment.domain.Transaction;
import org.untitled.payment.dto.TransactionDto;
import org.untitled.payment.dto.request.OfflineTransactionRequest;
import org.untitled.payment.error.ErrorCodes;
import org.untitled.payment.repository.CustomerRepository;
import org.untitled.payment.repository.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final E2EEncryptionService e2EEncryptionService;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final JsonProvider jsonProvider;

    @Transactional
    public void makeOfflineTransaction(TransactionDto transaction) {
//    public void makeOfflineTransaction(DecryptWithPublicKeyRequest request) {
//        String transactionRequestStr = e2EEncryptionService.decryptMessage(request);
//        System.out.println(transactionRequestStr);
//        TransactionDto transaction = jsonProvider.toObject(transactionRequestStr, TransactionDto.class);
        Customer fromCustomer = customerRepository.findByPhoneNumber(transaction.getFrom())
                .orElseThrow(() -> exCustomerNotFoundException(transaction.getFrom()));

        Customer toCustomer = customerRepository.findByPhoneNumber(transaction.getTo())
                .orElseThrow(() -> exCustomerNotFoundException(transaction.getTo()));

        if (fromCustomer.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new ServiceException(ErrorCodes.INSUFFICIENT_BALANCE.name(), "Insufficient balance");
        }

        fromCustomer.setBalance(fromCustomer.getBalance().subtract(transaction.getAmount()));
        toCustomer.setBalance(toCustomer.getBalance().add(transaction.getAmount()));
        customerRepository.save(fromCustomer);
        customerRepository.save(toCustomer);
        transactionRepository.save(Transaction.from(transaction));
    }


    private ServiceException exCustomerNotFoundException(String phoneNumber) {
        return new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND.name(),
                "Customer not found with phone number: " + phoneNumber);
    }
}
