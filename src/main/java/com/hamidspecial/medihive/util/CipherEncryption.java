package com.hamidspecial.medihive.util;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hamidspecial.medihive.exception.EncryptionException;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class CipherEncryption {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CipherEncryption.class);
    private final Cipher cipher;
    private final SecretKeySpec secretKeySpec;
    private final IvParameterSpec ivParameterSpec;

    public CipherEncryption(String secretKey, String secretSalt) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(ConstantUtils.AES_ENCRYPT);
        this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ConstantUtils.ALGORITHM);
        this.ivParameterSpec = new IvParameterSpec(secretSalt.getBytes(), 0, 16);
    }

    private String encrypt(String data)  throws GeneralSecurityException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedBytes = cipher.doFinal(decryptedBytes);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (GeneralSecurityException ex) {
            LOGGER.error("Encryption failed in 'encrypt' method. Error: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private String decrypt(String data) throws GeneralSecurityException{
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encryptedBytes = Base64.getDecoder().decode(data);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException ex) {
            LOGGER.error("Decryption failed in 'decrypt' method. Error: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public <T> List<T> decryptData(String data, TypeReference<List<T>> typeReference) throws EncryptionException {
        try {
            String decryptedData = decrypt(data);
            return ObjectSerializer.deserializeFromJson(decryptedData, typeReference);
        } catch (Exception ex) {
            LOGGER.error("Decryption of data into a List failed in 'decryptData(String, TypeReference)'. Error: {}", ex.getMessage(), ex);
            throw new EncryptionException("Error decrypting data", ex);
        }
    }

    public <T> T decryptData(String data, Class<T> object) throws EncryptionException{
        try {
            String decryptedData = decrypt(data);
            return ObjectSerializer.deserializeFromJson(decryptedData, object);
        }catch (Exception ex){
            LOGGER.error("Decryption of data into an object failed in 'decryptData(String, Class)'. Error: {}", ex.getMessage(), ex);
            throw new EncryptionException("Error decrypting data", ex);
        }
    }

    public <T> String encryptData(T data) throws EncryptionException {
        try {
            String serializedJson = ObjectSerializer.serializeToJson(data);
            return encrypt(serializedJson);
        }catch (Exception ex){
            LOGGER.error("Encryption of data failed in 'encryptData(T)'. Error: {}", ex.getMessage(), ex);
            throw new EncryptionException("Error encrypting data", ex);
        }
    }

    public <T> String encryptData(List<T> data) throws EncryptionException {
        try {
            String serializedJson = ObjectSerializer.serializeToJson(data);
            return encrypt(serializedJson);
        } catch (Exception ex) {
            LOGGER.error("Encryption of List data failed in 'encryptData(List)'. Error: {}", ex.getMessage(), ex);
            throw new EncryptionException("Error encrypting data", ex);
        }
    }
}

