package org.untitled.e2e.encryption.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InitiateCommunicationRequest {

    @NotBlank
    public String customerId;

    @NotBlank
    public String publicKey;

}
