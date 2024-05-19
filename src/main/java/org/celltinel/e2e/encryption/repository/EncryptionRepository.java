package org.celltinel.e2e.encryption.repository;

import org.celltinel.e2e.encryption.domain.EncryptionMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EncryptionRepository extends MongoRepository<EncryptionMetadata, String> {

    EncryptionMetadata findByDeviceId(String deviceId);

}
