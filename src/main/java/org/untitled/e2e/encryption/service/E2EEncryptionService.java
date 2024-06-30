package org.untitled.e2e.encryption.service;

import lombok.RequiredArgsConstructor;
import org.untitled.common.error.ServiceException;
import org.untitled.common.config.properties.SecurityProperties;
import org.untitled.e2e.encryption.domain.EncryptionMetadata;
import org.untitled.e2e.encryption.dto.request.DecryptWithPrivateKeyRequest;
import org.untitled.e2e.encryption.dto.request.DecryptWithPublicKeyRequest;
import org.untitled.e2e.encryption.dto.request.EncryptMessageRequest;
import org.untitled.e2e.encryption.dto.request.InitiateCommunicationRequest;
import org.untitled.e2e.encryption.dto.response.CryptoConversionResponse;
import org.untitled.e2e.encryption.dto.response.InitiateCommunicationResponse;
import org.untitled.e2e.encryption.error.ErrorCodes;
import org.untitled.e2e.encryption.repository.EncryptionRepository;
import org.untitled.e2e.encryption.util.EncryptionUtil;
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
                encryptionRepository.findByCustomerId(request.getCustomerId());
        if (encryptionMetadata == null) {
            throw new ServiceException(
                    ErrorCodes.MISSING_PUBLIC_KEY.name(), "Public key need to be exchanged first");
        }

        return new CryptoConversionResponse(
                EncryptionUtil.encryptWithPublicKey(
                        request.getMessageBody(), encryptionMetadata.getPublicKey()));
    }

    public CryptoConversionResponse decryptMessage(DecryptWithPrivateKeyRequest request) {
        byte[] aesKey = EncryptionUtil.decryptWithPrivateKey(
                request.getAesKey(), securityProperties.getPrivateKey());
        return new CryptoConversionResponse(
                EncryptionUtil.decryptWithAES(request.getMessageBody(), aesKey));
    }

    public String decryptMessage(DecryptWithPublicKeyRequest request) {
        EncryptionMetadata encryptionMetadata =
                encryptionRepository.findByCustomerId(request.getCustomerId());
        if (encryptionMetadata == null) {
            throw new ServiceException(
                    ErrorCodes.MISSING_PUBLIC_KEY.name(), "Public key need to be exchanged first");
        }
        return EncryptionUtil.decryptWithPublicKey(
                request.getMessageBody(), encryptionMetadata.getPublicKey());
    }

}
