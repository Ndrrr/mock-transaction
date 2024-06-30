package org.untitled.payment.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.untitled.payment.domain.Customer;
import org.untitled.payment.dto.CustomerDto;
import org.untitled.payment.dto.request.CreateCustomerRequest;
import org.untitled.payment.service.CustomerService;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{phoneNumber}")
    public CustomerDto getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        return customerService.getCustomerById(phoneNumber);
    }

    @PostMapping
    public void saveCustomer(@RequestBody CreateCustomerRequest customer) {
        customerService.saveCustomer(customer);
    }

}
