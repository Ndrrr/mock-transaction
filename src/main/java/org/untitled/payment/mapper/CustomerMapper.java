package org.untitled.payment.mapper;

import org.untitled.payment.domain.Customer;
import org.untitled.payment.dto.request.CreateCustomerRequest;

public class CustomerMapper {

    public static Customer toCustomer(CreateCustomerRequest request) {
        var customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhoneNumber(request.getPhoneNumber());
        return customer;
    }
}
