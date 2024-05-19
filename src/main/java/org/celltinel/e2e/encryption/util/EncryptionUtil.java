package org.celltinel.e2e.encryption.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.celltinel.e2e.encryption.common.error.ServiceException;
import org.celltinel.e2e.encryption.error.ErrorCodes;

@Slf4j
public class EncryptionUtil {

    @SneakyThrows
    public static String encryptWithPublicKey(String message, String publicKey) {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, EncryptionUtil.getPublicKey(publicKey));
        byte[] secretMsg = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMsg = encryptCipher.doFinal(secretMsg);
        return Base64.getEncoder().encodeToString(encryptedMsg);
    }

    private static PublicKey getPublicKey(String publicKey) {
        try {
            byte[] encodedPublicKey = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedPublicKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception ex) {
            log.error("Failed to build PublicKey, {}", ex.getMessage());
        }
        return null;
    }

    @SneakyThrows
    public static byte[] decryptWithPrivateKey(String encryptedMessage, String privateKey) {
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, EncryptionUtil.getPrivateKey(privateKey));
            byte[] encryptedMsg = Base64.getDecoder().decode(encryptedMessage);
            return decryptCipher.doFinal(encryptedMsg);
        } catch (Exception ex) {
            log.error("Failed to decrypt message, {}", ex.getMessage());
            throw new ServiceException(ErrorCodes.INVALID_MESSAGE.name(), "Invalid message body");
        }
    }

    @SneakyThrows
    public static String decryptWithAES(String encryptedMessage, byte[] key) {
        try {
            Cipher decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, EncryptionUtil.getAESKey(key));
            byte[] encryptedMsg = Base64.getDecoder().decode(encryptedMessage);
            byte[] decryptedMsg = decryptCipher.doFinal(encryptedMsg);
            return new String(decryptedMsg, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("Failed to decrypt message, {}", ex.getMessage());
            throw new ServiceException(ErrorCodes.INVALID_MESSAGE.name(), "Invalid message body");
        }
    }

    private static SecretKey getAESKey(byte[] key) {
        return new SecretKeySpec(key, "AES");
    }

    private static PrivateKey getPrivateKey(String privateKey) {
        try {
            byte[] encodedPrivateKey = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (Exception ex) {
            log.error("Failed to build PrivateKey, {}", ex.getMessage());
        }
        return null;
    }

}
