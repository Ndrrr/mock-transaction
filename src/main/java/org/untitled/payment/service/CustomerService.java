package org.untitled.payment.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.untitled.payment.domain.Customer;
import org.untitled.payment.dto.CustomerDto;
import org.untitled.payment.dto.request.CreateCustomerRequest;
import org.untitled.payment.mapper.CustomerMapper;
import org.untitled.payment.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public void saveCustomer(CreateCustomerRequest customer) {
        customerRepository.save(CustomerMapper.toCustomer(customer));
    }

    public CustomerDto getCustomerById(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber)
                .map(CustomerDto::from)
                .orElse(null);
    }

}
