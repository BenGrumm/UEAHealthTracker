package model;

import java.util.Base64;

/**
 * This class is used to encrypt and decrypt data. It will be used when retrieving and adding to the database.
 * @author Harry Burns
 */

public class Security {

    /**
     * This method is used to convert plaintext into ciphertext of the string provided then returns the ciphertext.
     * @param plaintext Plaintext given to be encoded to ciphertext.
     * @return ciphertext that is the encrypted version of the input information.
     */
    public static String encrypt(String plaintext) {
        return Base64.getEncoder().encodeToString(plaintext.getBytes());
    }

    /**
     * This method is used to decrypt the ciphertext provided and return the original plaintext.
     * @param ciphertext Ciphertext given to be decoded to plaintext.
     * @return plaintext that is the original string.
     */
    public static String decrypt(String ciphertext) {
        return new String(Base64.getDecoder().decode(ciphertext.getBytes()));
    }

}
