package org.celltinel.e2e.encryption.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.celltinel.e2e.encryption.dto.request.DecryptMessageRequest;
import org.celltinel.e2e.encryption.dto.request.EncryptMessageRequest;
import org.celltinel.e2e.encryption.dto.request.InitiateCommunicationRequest;
import org.celltinel.e2e.encryption.dto.response.CryptoConversionResponse;
import org.celltinel.e2e.encryption.dto.response.InitiateCommunicationResponse;
import org.celltinel.e2e.encryption.service.E2EEncryptionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/e2e-encryption")
@RequiredArgsConstructor
public class E2EEncryptionController {

    private final E2EEncryptionService e2EEncryptionService;

    @PostMapping("/initiate-communication")
    public InitiateCommunicationResponse initiateCommunication(
            @Valid @RequestBody InitiateCommunicationRequest request) {
        return e2EEncryptionService.initiateCommunication(request);
    }

    @PostMapping("/encrypt")
    public CryptoConversionResponse encryptMessage(
            @RequestBody @Valid EncryptMessageRequest request) {
        return e2EEncryptionService.encryptMessage(request);
    }

    @PostMapping("/decrypt")
    public CryptoConversionResponse decryptMessage(
            @RequestBody @Valid DecryptMessageRequest request) {
        return e2EEncryptionService.decryptMessage(request);
    }

}
