package org.untitled.payment.dto.request;

import lombok.Data;

@Data
public class OfflineTransactionRequest {

    private String fromCustomerId;
    private String encryptedTransactionData;

}
