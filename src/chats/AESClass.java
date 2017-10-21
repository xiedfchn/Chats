/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chats;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/* AES algorithem class maily for data transfer
 So, we use this class after we got the session key
*/
public class AESClass {

    // 
    static Cipher cipher;
    static KeyPair keyPair;
    public SecretKeySpec secretKeySpec;

    // create own secretKey
    /* para (String) ,return SecretKeySpec
    para (String) =  the hash of PassA || PassB
    */
    public SecretKeySpec createOwnKey(String Kab) throws NoSuchAlgorithmException, NoSuchPaddingException {

        // take first 128bit as source to create a specific key;
        byte[] key = Kab.getBytes();
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        //  create AES secret key;
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        // storage them into public variables for  later use
        cipher = Cipher.getInstance("AES");
        secretKeySpec = new SecretKeySpec(key, "AES");

        return secretKeySpec;
    }

    // convert AES key object to string, which is used to be send easily
    // para (SecretKey) ,return encodedKey
    public static String keyToString(SecretKey secretKey) {

        /* Get key in encoding format */
        byte encoded[] = secretKey.getEncoded();

        /*
        * Encodes the specified byte array into a String using Base64 encoding
         */
        String encodedKey = Base64.getEncoder().encodeToString(encoded);

        return encodedKey;
    }

    // convert string back to AES key object after string format of SecretKey being received
    // para (String) ,return SecretKey
    public static SecretKey decodeKeyFromString(String keyStr) {
        /* Decodes a Base64 encoded String into a byte array */
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);

        /* Constructs a secret key from the given byte array */
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0,
                decodedKey.length, "AES");

        return secretKey;
    }

    // encrypt funcition based on byte array
    // para [byte[], SecretKey] ,return byte[]
    public static byte[] encrypt(byte[] plainTextByte, SecretKey secretKey) throws Exception {

        // initialise cipher mode and encrypt normal byte[]
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);

        // return cipher byte array for transfer
        return encryptedByte;
    }

    // decrypt funcition that is to decrypt cipher to usable byte array based on byte array
    // para (byte[] ,SecretKey) ,return byte[]
    public static byte[] decrypt(byte[] encryptedTextByte, SecretKey secretKey) throws Exception {

        // initialise cipher mode and decrypt cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);

        // return usable byte array
        return decryptedByte;
    }

    // encrypt funcition based on String, not used yet
    // para (String, SecretKey) ,return String
    public static String encrypt(String plainText, SecretKey secretKey) throws Exception {
        
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);

        return encryptedText;
    }

    // decrypt funcition based on String, not used yet
    // para (String, SecretKey) ,return String
    public static String decrypt(String encryptedText, SecretKey secretKey)
            throws Exception {
        
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        
        return decryptedText;
    }

    /*
         * all codes below is important for testing,
         * please don not remove;
         * So you don't have to read it
    
         */
    public static void main(String[] args) throws Exception {
        

//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        // public keys of each other
//        keyGenerator.init(128);
//        SecretKey secretKey = keyGenerator.generateKey();
//        cipher = Cipher.getInstance("AES");
//        
//        
////        // get base64 encoded version of the key
////        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
////                    // decode the base64 encoded string
////            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
////            // rebuild key using SecretKeySpec
////            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
////        System.out.println("Get secretKey: " + keyToString(originalKey));
//        String plainText = "AES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption Decryption";
//        System.out.println("Plain Text Before Encryption: " + plainText);
//
//        String encryptedText = encrypt(plainText, secretKey);
//        System.out.println("Encrypted Text After Encryption: " + encryptedText);
//
//        String decryptedText = decrypt(encryptedText, secretKey);
//        System.out.println("Decrypted Text After Decryption: " + decryptedText);
//        
        AESClass a = new AESClass();
        AESClass a2 = new AESClass();
        String inStr = "123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123123dsdsssssssssssss";
        a.createOwnKey(SHA.shaEncode(inStr));
        a2.createOwnKey(SHA.shaEncode(inStr));
        System.out.println("Session key" + a.secretKeySpec);
        String plainText = "AES Symmetric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Sytric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption DecryptionAES Symmetric Encryption Decryption";
        System.out.println("Plain Text Before Encryption: " + plainText);

        String encryptedText = a.encrypt(plainText, a.secretKeySpec);
        System.out.println("\nEncrypted Text After Encryption: " + encryptedText);

        String decryptedText = a2.decrypt(encryptedText, a.secretKeySpec);
        System.out.println("Decrypted Text After Decryption: " + decryptedText);
    }

}
