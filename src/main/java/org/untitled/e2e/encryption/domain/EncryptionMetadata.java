package org.untitled.e2e.encryption.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.untitled.e2e.encryption.dto.request.InitiateCommunicationRequest;

@Data
@Entity(name = "encryption_metadata")
@AllArgsConstructor
@NoArgsConstructor
public class EncryptionMetadata {

    @Id
    public String customerId;

    @Column(columnDefinition = "TEXT")
    public String publicKey;

    public static EncryptionMetadata from(InitiateCommunicationRequest request) {
        return new EncryptionMetadata(request.getCustomerId(), request.getPublicKey());
    }

}
