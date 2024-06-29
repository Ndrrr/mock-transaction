package org.untitled.e2e.encryption.repository;

import org.untitled.e2e.encryption.domain.EncryptionMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncryptionRepository extends JpaRepository<EncryptionMetadata, String> {

    EncryptionMetadata findByCustomerId(String customerId);

}
