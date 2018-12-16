package utilities;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import database.Database;
import org.apache.commons.codec.binary.Base64;

//TODO WZORZEC PROJEKTOWY ZASTOSOWANY  - SINGLETON
//Using AES WITH GCM COUNTER to encrypt data in database.
//Decided to use 128bit key because application is vulnerable without double authentication anyway.
//Basically this version of algorithm is not the most secure because of problem with key storing but
//its good enough for me
public class Encryptor {

    private byte[] key;
    private static Encryptor instance = null;
    private static boolean loginFlag = false;

    //Applied singleton to simplyfiy access
    private Encryptor(){ }
    public static Encryptor getInstance(){
        if(instance == null){
            instance = new Encryptor();
        }
        return instance;
    }

    public String setKey(String password){
        //16 bytes because 16 * 8 = 128 bit
        key = new byte[16];
        try{
            SecureRandom secureRandom = new SecureRandom();
            //Since encrypting/decrypting key act also as password
            //we need to give user freedom of choosing his password length
            //so we fill rest of the table with random values and write them down in database
            //but we have to keep it below 16 bytes;
            if(password.length() > 16){
                throw new RuntimeException("Password is too long for the key, max length is 16");
            }
            byte[] passwordBytes = password.getBytes("UTF-8");
            byte[] keyPostFix = new byte[16-password.length()];
            secureRandom.nextBytes(keyPostFix);
            //Assembling key from what we have;
            for(int i =0; i < password.length(); i++){
                key[i] = passwordBytes[i];
            }
            for(int i = password.length(),y=0; i < 16; i++, y++){
                key[i] = keyPostFix[y];
            }
            //returning "the rest of the password" in form of string to store into database
            return Base64.encodeBase64String(keyPostFix);

        } catch(UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        } catch(Exception e){
            System.out.println(e.getMessage());
        } finally{
        }
        return "null";
    }

    //Used for login purposes
    public boolean checkIfPasswordIsCorrect(String password){
        key = new byte[16];
        String decryptedMessage = "null";
        try{
            if(password.length() > 16){
                throw new RuntimeException("Password is too long for the key, max length is 16");
            }
            byte[] passwordBytes = password.getBytes("UTF-8");
            byte[] keyPostFix = Base64.decodeBase64(
                    Database.getInstance().select(
                    "password_postfix" , "Config","ID = '1'").getString(1));
            //Assembling key from what we have;
            for(int i =0; i < password.length(); i++){
                key[i] = passwordBytes[i];
            }
            for(int i = password.length(),y=0; i < 16; i++, y++){
                key[i] = keyPostFix[y];
            }

            //Loading encrypted message stored in database, succesfully decoding it with user password
            //connected with postfix key loaded from database guarantees user access and data decryption
            decryptedMessage = decrypt(Database.getInstance().select(
                    "password_sample","Config").getString(1));
            System.out.println(decryptedMessage);
            //Setting flag to signal application that user logged successfully, and data
            //is decrypted and can be ecrypted again on exit
        } catch(UnsupportedEncodingException e){
            System.out.println(e.getMessage());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

        //Checking if decrypted password matches string
        //Then, returning boolean value for LoginScreenModel to continue with application execution
        if(decryptedMessage.equals("decodeSample")){
            loginFlag = true;
            return true;
        }

        return false;
    }

    //Used for encrypting single string
    public String encrypt(String word) throws Exception {

        //Generating key for AES algorithm
        SecureRandom secureRandom = new SecureRandom();
        SecretKey secretKey = new SecretKeySpec(key,"AES");

        //Generating initialization vector for GCM
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);

        //Initializing cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128,iv);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,parameterSpec);

        //Alternatively CipherInputStream but not expecting big data here
        byte[] cipherText = cipher.doFinal(word.getBytes("UTF-8"));

        //Concating all into one
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        byte[] cipherMessage = byteBuffer.array();


        //Transforming byte table to Base64 string
        return new Base64().encodeToString(cipherMessage);
    }

    //Used for decrypting single string;
    public String decrypt(String word) throws Exception{
        //Extracting data from a Base54 string
        byte[] cipherMsg= Base64.decodeBase64(word);
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMsg);
        int ivLength = byteBuffer.getInt();

        //Making sure that initialization vector is not corrupted

        if(ivLength < 12 || ivLength >= 16)
            throw new IllegalArgumentException("invalid initialization vector key");

        //Reading initialization vector
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);

        //Reading string
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        //Initializing cipher
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(128,iv));

        byte[] plainText = cipher.doFinal(cipherText);

        //Transforming byte table to string
        return new String(plainText) ;
    }

    public boolean getLoginFlag(){
        return loginFlag;
    }
}
