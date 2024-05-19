package org.celltinel.e2e.encryption.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateCommunicationResponse {

    public String publicKey;

}
