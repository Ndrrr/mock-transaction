package org.untitled.e2e.encryption.dto.request;

import lombok.Data;

@Data
public class DecryptWithPrivateKeyRequest {

    private String messageBody;
    private String aesKey; // AES key encrypted with RSA public key

}
