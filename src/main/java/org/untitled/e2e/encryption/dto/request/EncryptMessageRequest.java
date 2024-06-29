package org.untitled.e2e.encryption.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EncryptMessageRequest {

    @NotBlank
    private String customerId;

    @NotBlank
    private String messageBody;

}
