package org.untitled.e2e.encryption.dto.request;

import lombok.Data;

@Data
public class DecryptMessageRequest {

    private String messageBody;
    private String aesKey;

}
