package net.toadless.radio.util;

import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptUtils
{
    private final Logger LOGGER = LoggerFactory.getLogger(EncryptUtils.class);

    public String encrypt(String value, SecretKey key, String algorithm)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e)
        {
            LOGGER.error("Something went wrong whilst encrypting");
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }

    public String decrypt(String value, SecretKey key, String algorithm)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(value.getBytes())));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e)
        {
            LOGGER.error("Something went wrong whilst decrypting");
            throw new InternalServerErrorResponse("Something went wrong...");
        }
    }
}