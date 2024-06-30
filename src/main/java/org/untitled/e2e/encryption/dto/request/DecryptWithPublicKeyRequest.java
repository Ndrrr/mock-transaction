package org.untitled.e2e.encryption.dto.request;

import lombok.Data;

@Data
public class DecryptWithPublicKeyRequest {

    private String customerId;
    private String messageBody;

}
