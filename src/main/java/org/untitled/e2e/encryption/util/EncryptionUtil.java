package org.untitled.e2e.encryption.util;

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
import org.untitled.common.error.ServiceException;
import org.untitled.e2e.encryption.error.ErrorCodes;

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
    public static String encryptWithPrivateKey(String message, String privateKey) {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, EncryptionUtil.getPrivateKey(privateKey));
        byte[] secretMsg = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMsg = encryptCipher.doFinal(secretMsg);
        return Base64.getEncoder().encodeToString(encryptedMsg);
    }

    public static String decryptWithPublicKey(String encryptedMessage, String publicKey) {
        try {
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, EncryptionUtil.getPublicKey(publicKey));
            byte[] encryptedMsg = Base64.getDecoder().decode(encryptedMessage);
            return new String(decryptCipher.doFinal(encryptedMsg), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("Failed to decrypt message, {}", ex.getMessage());
            throw new ServiceException(ErrorCodes.INVALID_MESSAGE.name(), "Invalid message body");
        }
    }

    public static void main(String[] args) {
        String msg = "{\n" +
                "    \"from\": \"994705105514\",\n" +
                "    \"to\": \"994705105513\",\n" +
                "    \"date\": \"2019-01-01T00:00:00Z\",\n" +
                "    \"amount\": \"21\"\n" +
                "}";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyfC4oX8MZT7C/vcQljwyP4rs4U31AjZC2HxV7BfU9BA9i+K1bu00NIZQKFBOQ9novxq7Gs1sDABA+Ei62O1mfJME8CAUSAjEowwepMrVm20YH/3fGFDeXFjC9cVslQBM9ALTRVq7tpPwW5rXQlWIactXFuSzqrbbYllhvA/Tu7sLayfUkNLK0k++9jUuNqSKhxPrldPIyz83xrVZE3oQ/Aqy98X5FkzMBe9CosJjYUGI3tJDEvkr1UBMZzengmJrTxQ+aMFOkQPgdA/PrL5GUVeyCQG4b4b+Y1J/96tUvBiJfRFMMoHKEERFOWiS8FviBGrbAVSAGS8dfYs4KZM5kwIDAQAB";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDJ8LihfwxlPsL+9xCWPDI/iuzhTfUCNkLYfFXsF9T0ED2L4rVu7TQ0hlAoUE5D2ei/GrsazWwMAED4SLrY7WZ8kwTwIBRICMSjDB6kytWbbRgf/d8YUN5cWML1xWyVAEz0AtNFWru2k/BbmtdCVYhpy1cW5LOqtttiWWG8D9O7uwtrJ9SQ0srST772NS42pIqHE+uV08jLPzfGtVkTehD8CrL3xfkWTMwF70KiwmNhQYje0kMS+SvVQExnN6eCYmtPFD5owU6RA+B0D8+svkZRV7IJAbhvhv5jUn/3q1S8GIl9EUwygcoQREU5aJLwW+IEatsBVIAZLx19izgpkzmTAgMBAAECggEAD7x9dsBCxAT/BwVTupFYTRFTYj9D73cxAFO4ElzNEHPPh/4Tq2A6qDkrN+kCIFFzzKiVmgIWAF1osrBgNxVQ3jZKtTeavP5J7Ha+UJMp0CHRNnT/FJMAsOuNzb8jtfy/AmjR896dMB4ZJdpMGfQ2XCAnWjZaNdxn/iL+9bu6AS/voXijdL2vZhNxk86Vl9cXxEt3yLU6D3nnSSGLviOsCtlyGJVrj4GFOwnCobrsQUioYXVVJlQGIhrLaYUDxKb7WiXh3qP1ZSTV8UUKbMMuZyVANUcEtSv7CYacdfp6xLb0t8ULdslMONgriGVB0dCYfy43DTSNQAdMnQcJcZJUIQKBgQD4dWwmyS76zjy/TSKkW/eaz8EAD0po0WJx/7+WFBbPO3EJObmtCqHqNWGLPM6vOsnUFQLKA4zW03kHz/0aUurHizMHgs3STYvSCDOC3Lp0HOaC9TtwI4HsG9bsUKX3LCUaAVFjhCtNBHAmCxaJe4CjieMZrutsYt5KH1Qqpa+ggwKBgQDQEddNd2EJOR3kx5eBj9nrlhPn9Sq/CEAv2EMqIAzHNxOgHdC9FhcFE4Yp6hwbZu+G91cjDxBGdq3HAZPQUpGn0acqim1kpgHzv5Lt1Mv4lgHVX4FohO4NcQ5MywPsqntout8tNqVWl2nVQ9XpOJUmh21tPMZ9zXJtyzWTUxOVsQKBgHW7tp7+cEualMCVvZeimMb8MVlk5X6b0YlxhgiIxnI2mY6kku6DfkwsNWe0TcBNHDxqJGMzoXZeyRhBn47swaICvRCPFBbWdi31gus8ywGgfD6MFDlEKhmG66YZZTul6ILVIwsEIzygoL/2Oxy0sDVObpXKBHbctrbq94gp8PofAoGALqNxITobFBgrbTaX7UCzVi1DtQCtXjiZOqA9HdolQxpDWeaU/W1QJ6Eit450UmgWVKhobcFYopLDjRH7cfywDW/fCpgQwTi8w0gzxP+n4MavDobj2OvC+FxZGjl/XbMxCl6o29ed4/T4kgcskaRPOl2s14mB7Hz6o0WjcqxyfWECgYEAliksGBOK5JvQ+jHffmjZpa95T6ZyWYFdEzm6Fdg4Mf95d+D/vJ51xVbd0bYvGl8Xs5VQClYVvTle+Bdn2Xkd2bHwtPB7vd+LJ7ExAmuboGECJbX62n98BIB9G+XtEGJtlFKJE5PLtpajZJarhmSfMrC6huEu/WztY9YMj3yl1qY=";
        String encryptedMsg = EncryptionUtil.encryptWithPrivateKey(msg, privateKey);
        System.out.println("Encrypted message: " + encryptedMsg);
        String decryptedMsg = EncryptionUtil.decryptWithPublicKey(encryptedMsg, publicKey);
        System.out.println("Decrypted message: " + decryptedMsg);
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
