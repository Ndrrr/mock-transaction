package org.celltinel.e2e.encryption.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EncryptMessageRequest {

    @NotBlank
    private String deviceId;

    @NotBlank
    private String messageBody;

}
