package org.celltinel.e2e.encryption.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InitiateCommunicationRequest {

    @NotBlank
    public String deviceId;

    @NotBlank
    public String publicKey;

}
