package org.celltinel.e2e.encryption.service;

import lombok.RequiredArgsConstructor;
import org.celltinel.e2e.encryption.common.error.ServiceException;
import org.celltinel.e2e.encryption.config.properties.SecurityProperties;
import org.celltinel.e2e.encryption.domain.EncryptionMetadata;
import org.celltinel.e2e.encryption.dto.request.DecryptMessageRequest;
import org.celltinel.e2e.encryption.dto.request.EncryptMessageRequest;
import org.celltinel.e2e.encryption.dto.request.InitiateCommunicationRequest;
import org.celltinel.e2e.encryption.dto.response.CryptoConversionResponse;
import org.celltinel.e2e.encryption.dto.response.InitiateCommunicationResponse;
import org.celltinel.e2e.encryption.error.ErrorCodes;
import org.celltinel.e2e.encryption.repository.EncryptionRepository;
import org.celltinel.e2e.encryption.util.EncryptionUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class E2EEncryptionService {

    private final SecurityProperties securityProperties;
    private final EncryptionRepository encryptionRepository;

    public InitiateCommunicationResponse initiateCommunication(
            InitiateCommunicationRequest request) {
        encryptionRepository.save(EncryptionMetadata.from(request));
        return new InitiateCommunicationResponse(securityProperties.getPublicKey());
    }

    public CryptoConversionResponse encryptMessage(EncryptMessageRequest request) {
        EncryptionMetadata encryptionMetadata =
                encryptionRepository.findByDeviceId(request.getDeviceId());
        if (encryptionMetadata == null) {
            throw new ServiceException(
                    ErrorCodes.MISSING_PUBLIC_KEY.name(), "Public key need to be exchanged first");
        }

        return new CryptoConversionResponse(
                EncryptionUtil.encryptWithPublicKey(
                        request.getMessageBody(), encryptionMetadata.getPublicKey()));
    }

    public CryptoConversionResponse decryptMessage(DecryptMessageRequest request) {
        return new CryptoConversionResponse(
                EncryptionUtil.decryptWithPrivateKey(
                        request.getMessageBody(), securityProperties.getPrivateKey()));
    }

}
