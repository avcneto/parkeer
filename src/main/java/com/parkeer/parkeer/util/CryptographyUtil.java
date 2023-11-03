package com.parkeer.parkeer.util;

import com.parkeer.parkeer.exception.FailedDependencyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static java.lang.String.format;

@Slf4j
@Component
public class CryptographyUtil {

    private static final String AES = "AES";
    private static final Integer KEY_SIZE = 128;
    private static final String ERROR_ENCRYPTING_DATA = "Error encrypting data: %s";
    private static final String ERROR_ENCRYPTING = "Error encrypting";
    private static final String ERROR_DECRYPTING_DATA = "Error decrypting data: %s";
    private static final String ERROR_DECRYPTING = "Error decrypting";
    private static final String ERROR_CREATING_SECRET_KEY = "Error creating secret key";

    private static Mono<SecretKey> createSecretKey() {
        return Mono.defer(() -> {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
                keyGenerator.init(KEY_SIZE);
                SecretKey secretKey = keyGenerator.generateKey();
                return Mono.just(secretKey);
            } catch (NoSuchAlgorithmException ex) {
                return Mono.error(new FailedDependencyException(ERROR_CREATING_SECRET_KEY, ex));
            }
        });
    }

    public static Mono<String> encrypt(String data) {
        return createSecretKey().flatMap(secretKey -> Mono.fromCallable(() -> {
            try {
                var cipher = Cipher.getInstance(AES);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                return Base64.getEncoder().encodeToString(encryptedData);
            } catch (Exception ex) {
                log.error(format(ERROR_ENCRYPTING_DATA, ex.getMessage()));
                throw new FailedDependencyException(ERROR_ENCRYPTING, ex);
            }
        })).subscribeOn(Schedulers.boundedElastic());
    }

    public static Mono<String> decrypt(String encryptedData) {
        return createSecretKey().flatMap(it ->
                Mono.fromCallable(() -> {
                    try {
                        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData);
                        Cipher cipher = Cipher.getInstance(AES);
                        cipher.init(Cipher.DECRYPT_MODE, it);
                        byte[] decryptedData = cipher.doFinal(encryptedDataBytes);
                        return new String(decryptedData, StandardCharsets.UTF_8);
                    } catch (Exception ex) {
                        log.error(format(ERROR_DECRYPTING_DATA, ex.getMessage()));
                        throw new FailedDependencyException(ERROR_DECRYPTING, ex);
                    }
                })
        ).subscribeOn(Schedulers.boundedElastic());
    }
}
