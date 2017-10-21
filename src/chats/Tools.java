/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

// utilities that help our work
public class Tools {

    // concate two byte[] string into one
    public static byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen + bLen];
        //
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    // receive data from socket dataInputStream
    // return result in byte array
    // pra(DataInputStream) return byte[]
    public static byte[] getByteData(DataInputStream in) throws IOException {
        int a = 0;
        // length of data
        long length = in.readLong();

        // data loding block
        byte[] bytes = new byte[32];

        ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();

        // data readding loop
        while (length > 0) {
            if (length < bytes.length) {
                a = in.read(bytes, 0, (int) length);
            } else {
                a = in.read(bytes, 0, bytes.length);
            }
            System.out.println("bytes:" + a);
            System.out.println("length:" + length);
            length -= a;
            buffer2.write(bytes, 0, a);
        }
        // return result in bytes array
        return buffer2.toByteArray();
    }

    // sending data through socket to other ends
    // write byte data to socket outputstream with DataOutputStream, meanwhile lable its type for identification
    // pram (byte[], DataOutputStream, char type)
    public static void sendByteData(byte[] data, DataOutputStream out, char type) throws IOException {

        // write  type of transfer and length of data
        int length = (int) data.length;
        out.writeChar(type);
        out.writeLong(length);

        int lengthBlock = 32;
        byte[] bytes2 = new byte[lengthBlock];
        InputStream is = new ByteArrayInputStream(data);

        int a = 0;

        // writing data loop 
        while ((a = is.read(bytes2, 0, bytes2.length)) > 0) {
            out.write(bytes2);
            // send a lock of data even if it is not big enough
        }

        // emit 
        out.flush();
    }

    // generate a 100 random number in String format, that's challenge
    // return String
    public static String getRandomNum() throws Exception {

        Random rnd = new Random(System.currentTimeMillis());
        String result = "";
        for (int index = 0; index < 100; index++) {
            Integer intInstance = new Integer(rnd.nextInt(10));
            result = result.concat(intInstance.toString());
        };

        return result;
    }

    // byte to string and string to bytes array
    public static String byteToString(byte[] bytes) throws Exception {
        return new String(bytes);
    }

    public static byte[] stringToByte(String text) throws Exception {
        byte[] bytes = text.getBytes();
        return bytes;
    }
}
