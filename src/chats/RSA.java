/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chats;

/**
 *
 * @author anfalaljamea
 *
 */

/*
from :
http://www.thedeveloperspoint.com/using-java-security-libraries-for-implementing-rsa/
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.X509EncodedKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;

// RSA algorithem class used for exchange session key
public class RSA {

    // key pair include public key and private
    public static KeyPair keyPair;

    // constructor, set baisc value of rsa
    public RSA() throws Exception {

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
        keygen.initialize(2048);

        // storage  keyPair that will be used all the time
        keyPair = keygen.generateKeyPair();
    }

    /* Generate Key with byte format of encoeded key
    Because when we recieve the byte-Public key from another entity we should get the key-object back
    */
    // para ((byte[] ,PublicKey), return encodedKey;
    public PublicKey byteToKey(byte[] encodedKey) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PublicKey publicKeyOfOther = kf.generatePublic(new X509EncodedKeySpec(encodedKey));
        return publicKeyOfOther;
    }
    
    // turn cipher bytes to string
    // important!! text being encrypted must use this convertor rather than normal ones
    public static String encodeBASE64(byte[] bytes) {
        BASE64Encoder b64 = new BASE64Encoder();
        return b64.encode(bytes);
    }

    // turn string  cipher back  to bytes
    public static byte[] decodeBASE64(String text) throws Exception {
        BASE64Decoder b64 = new BASE64Decoder();
        return b64.decodeBuffer(text);

    }
    
    // getPublicKey of object, a assistant funciton used for first step to exchange publicKey 
    // return PublicKey;
    public PublicKey getPublicKey() {

        PublicKey key = keyPair.getPublic();

        return key;
    }

    // encrypt large size data with public key of others
    // accept byte data, return encrypted byte array cipher without limit of thelength of data;
    // para ((byte[] ,PublicKey) return btye[]
    public static byte[] encryptAllByOtherPublicKey(byte[] plaintext, PublicKey key) throws Exception {

        // except for padding, only 245 usable space for encryption
        // here it's the size of block that ww transfer every time 
        // 11 = padding 
        int blockSizeX = (2048 / 8) - 11;

        // divide data into a couple of blocks, here is one block 
        byte[] dataBuffer = new byte[blockSizeX];

        // get input stream based on byte array
        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        int size = 0;

        // create a temporary ByteArrayOutputStream to read and write data;
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        
        // read data untill there is nothing (equals -1)
        while ((size = in.read(dataBuffer)) != -1) {
            
            // if the remaining is less than length of a block
            // process it particularly
            if (size < dataBuffer.length) {
                byte[] temp = new byte[size];
                for (int i = 0; i < size; i++) {
                    temp[i] = dataBuffer[i];
                }

                dataBuffer = new byte[size];
                for (int i = 0; i < size; i++) {
                    dataBuffer[i] = temp[i];
                }
            }
            // encrypt each block and then write it to result stream (for small block of data)
            buffer2.write(encryptByOtherPublicKey(dataBuffer, key));
        }
        
        // change to byte format and return
        return buffer2.toByteArray();
    }

    // functions for encryption of each small block,connect to encryptAllByOtherPublicKey
    // it can also be used individually
    static public byte[] encryptByOtherPublicKey(byte[] plaintext, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(plaintext);
        return ciphertext;
    }
    
    // decrypt huge size data with public key of others, opesite to encryptAllByOtherPublicKey function
    // accept byte data, return encrypted byte array cipher without limit of thelength of data;
    // para ((byte[] ,PublicKey) return btye[]
    public static byte[] decryptAllOtherByPublicKey(byte[] plaintext, PublicKey key) throws Exception {
        
        // size of real data block, no padding
        int blockSizeX = (2048 / 8) - 11;
        
        // each cipher block including padding 2048 / 8
        byte[] dataBuffer = new byte[256];
        
        // use ByteArrayInputStream to read and write data;
        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        int size = 0;
        
        // result stream
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        
        // read all data to result stream after decrypt them
        while ((size = in.read(dataBuffer)) != -1) {
            buffer2.write(decryptByOtherPublicKey(dataBuffer, key));
        }
        
        // return array format result
        return buffer2.toByteArray();
    }

    // functions for decryption of each small block,connect to decryptByOtherPublicKey
    // it can also be used individually
    static public byte[] decryptByOtherPublicKey(byte[] ciphertext, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return plaintext;
    }
    /*
    We have two functions that are used from sender side:
    1- encryptAllByPublicKey
    2- encryptAllByPrivateKey
    Both of them have plaintext parameter
    Both of them have 4 functions
    - two of them for encryption ( Small / Bulk data).
    - two for decryption ( Small / Bulk data).
    */
    // the rest of functions have quiet similar function as the one before
    // encrypt large size data with public key 
    // accept byte data, return encrypted byte array cipher without limit of thelength of data;
    // para ((byte[])
    public static byte[] encryptAllByPublicKey(byte[] plaintext) throws Exception {
        
        // totally the same as the fucntion above
        int blockSizeX = (2048 / 8) - 11;
        byte[] dataBuffer = new byte[blockSizeX];
        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        int size = 0;
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        while ((size = in.read(dataBuffer)) != -1) {
            if (size < dataBuffer.length) {
                byte[] temp = new byte[size];
                for (int i = 0; i < size; i++) {
                    temp[i] = dataBuffer[i];
                }

                dataBuffer = new byte[size];
                for (int i = 0; i < size; i++) {
                    dataBuffer[i] = temp[i];
                }
            }
            // use its own publicKey, no para
            buffer2.write(encryptByPublicKey(dataBuffer));
        }
        return buffer2.toByteArray();
    }

    // encrypt large size data with Private key 
    // accept byte data, return encrypted byte array cipher without limit of thelength of data;
    // para ((byte[])
    public static byte[] encryptAllByPrivateKey(byte[] plaintext) throws Exception {
        int blockSizeX = (2048 / 8) - 11;
        byte[] dataBuffer = new byte[blockSizeX];
        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        int size = 0;
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        while ((size = in.read(dataBuffer)) != -1) {
            if (size < dataBuffer.length) {
                byte[] temp = new byte[size];
                for (int i = 0; i < size; i++) {
                    temp[i] = dataBuffer[i];
                }

                dataBuffer = new byte[size];
                for (int i = 0; i < size; i++) {
                    dataBuffer[i] = temp[i];
                }
            }
            // use its own Private, no para
            buffer2.write(encryptByPrivateKey(dataBuffer));
        }
        return buffer2.toByteArray();
    }


    // functions for encryption of each small block, can be used individually
    static public byte[] encryptByPublicKey(byte[] plaintext) throws Exception {
        PublicKey key = keyPair.getPublic();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(plaintext);

        return ciphertext;
    }

    // functions for encryption of each small block, can be used individually
    static public byte[] encryptByPrivateKey(byte[] plaintext) throws Exception {
        PrivateKey key = keyPair.getPrivate();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(plaintext);
        return ciphertext;
    }

    public static byte[] decryptAllByPublicKey(byte[] plaintext) throws Exception {
        int blockSizeX = (2048 / 8) - 11;
        byte[] dataBuffer = new byte[256];
        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        int size = 0;
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        while ((size = in.read(dataBuffer)) != -1) {
            buffer2.write(decryptByPublicKey(dataBuffer));
        }
        return buffer2.toByteArray();
    }

    static public byte[] decryptByPublicKey(byte[] ciphertext) throws Exception {
        PublicKey key = keyPair.getPublic();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return plaintext;
    }


    public static byte[] decryptAllByPrivateKey(byte[] plaintext) throws Exception {
        int blockSizeX = (2048 / 8) - 11;
        byte[] dataBuffer = new byte[256];
        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        int size = 0;
        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
        while ((size = in.read(dataBuffer)) != -1) {
            buffer2.write(decryptByPrivateKey(dataBuffer));
        }
        return buffer2.toByteArray();
    }

    static public byte[] decryptByPrivateKey(byte[] ciphertext) throws Exception {
        PrivateKey key = keyPair.getPrivate();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plaintext = cipher.doFinal(ciphertext);

        return plaintext;
    }
    
    /*
          * all codes below is important for testing,
          * please don not remove;
         */

    public static void main(String[] args) throws Exception {

        
//        java.io.InputStreamReader sreader = new java.io.InputStreamReader(System.in);
//        java.io.BufferedReader breader = new java.io.BufferedReader(sreader);

////        
//        String input ="sdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfdddd";
//        
//        
//        RSA app = new RSA();
//        RSA app2 = new RSA();
//        
//        
//                            String passA = Tools.getRandomNum();
//                            byte[] byteHashedPassA = SHA.shaEncode(passA).getBytes();
//                            byte[] byteEntryptedHashedPassA = app.encryptAllByPrivateKey(byteHashedPassA); 
//                            String message = passA + encodeBASE64(byteEntryptedHashedPassA);
//                            byte[] data = app.encryptAllByOtherPublicKey(message.getBytes(), app2.getPublicKey());
//                            
//                           
//                            
//                            byte[] result = app2.decryptAllByPrivateKey(data);
//                            String d2 =  new String(result);
//                            String passAatB = d2.substring(0,100);
//                            
//                            if (SHA.shaEncode(passA) == null ? SHA.shaEncode(passAatB) == null : SHA.shaEncode(passA).equals(SHA.shaEncode(passAatB))){
//                                
//                                System.out.println("result = asf" );
//                            }
//                            String decryptedHashedPassA = new String(app2.decryptAllOtherByPublicKey(decodeBASE64(d2.substring(100)), app.getPublicKey()));
//                            
//                            
//                            
//                            String passB = Tools.getRandomNum();
//                            String PassAB = passB + passAatB;
//                            
//                            String Kab = SHA.shaEncode(PassAB);
//                            AESClass aes = new AESClass();
//                            SecretKeySpec secretKeySpec = aes.createOwnKey(Kab);
//
//                            
//                            String ecnrypt_passA = AESClass.encrypt(passAatB, secretKeySpec);
//                            
//                            
//                            String encrypt_TheHashofpassA_B = encodeBASE64(app2.encryptAllByPrivateKey((passB + ecnrypt_passA).getBytes()));
////                      
//                            byte[] temp = app2.encryptAllByOtherPublicKey((passB + ecnrypt_passA + encrypt_TheHashofpassA_B).getBytes(),  app.getPublicKey());
//                            
//                            
//                            String d3 = new String(app.decryptAllByPrivateKey(temp));
//                            
//                            String dataC = d3.substring(0,252);
//                            String passBatA = dataC.substring(0,100);
//                            String reponse = d3.substring(100, 252);
//                            String encrypt_TheHashofpassABAtA = d3.substring(252);
//                            
//                            
//                            String hashedPassBandReponse = new String(app.decryptAllOtherByPublicKey(decodeBASE64(encrypt_TheHashofpassABAtA),  app2.getPublicKey()));
//                            
//                            if( hashedPassBandReponse.equals(passBatA + reponse)){
//                                System.out.println("haha:");
//                            }
//        byte[]a = app.encryptAllByOtherPublicKey(input.getBytes(),app2.getPublicKey());
//        System.out.println("result " + new String(app2.decryptAllByPrivateKey(a)));
//        a = app.encryptAllByPublicKey(input.getBytes());
//        System.out.println("result " + new String(app.decryptAllByPrivateKey(a)));
//        a = app.encryptAllByPrivateKey(input.getBytes());
//        System.out.println("result " + new String(app.decryptAllByPublicKey(a)));
//        // Key convertor test
//        byte[] encodedKey = app.getPublicKey().getEncoded();
//
//        KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
//
//        PublicKey publicKeyOfOther = app.byteToKey(encodedKey);
//        String a = app.encrypt(input, publicKeyOfOther);
//        System.out.println("result = " + app.decrypt(a));
//
//        int blockSize = (2048 / 8) - 11;
//        byte[] buffer = new byte[blockSize];
//        String sessionKey = "";
//        String[] splitInParts = splitInParts(input, blockSize);
//        RSA app2 = new RSA();
//
//        for (int i = 0; i < splitInParts.length; i++) {
//            sessionKey = sessionKey.concat(app2.encrypt(splitInParts[i]));
//        }
//
//        String[] splitInParts2 = splitInParts(sessionKey, 352);
//
//        String decrptResult = "";
//        for (int i = 0; i < splitInParts2.length; i++) {
//            decrptResult = decrptResult.concat(app2.decrypt(splitInParts2[i]));
//        }
//
//        System.out.println("result = " + decrptResult);
    }
}
