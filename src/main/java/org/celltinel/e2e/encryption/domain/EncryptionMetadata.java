package org.celltinel.e2e.encryption.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.celltinel.e2e.encryption.dto.request.InitiateCommunicationRequest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionMetadata {

    @Id
    public String deviceId;
    public String publicKey;

    public static EncryptionMetadata from(InitiateCommunicationRequest request) {
        return new EncryptionMetadata(request.getDeviceId(), request.getPublicKey());
    }

}
