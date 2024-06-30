package org.untitled.payment.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.untitled.payment.domain.Customer;

@Data
@AllArgsConstructor
public class CustomerDto {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private BigDecimal balance;

    public static CustomerDto from(Customer customer) {
        return new CustomerDto(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getBalance()
        );
    }

}
