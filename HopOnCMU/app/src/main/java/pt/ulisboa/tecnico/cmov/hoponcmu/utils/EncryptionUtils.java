package pt.ulisboa.tecnico.cmov.hoponcmu.utils;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.SecureRandom;

import static pt.ulisboa.tecnico.cmov.hoponcmu.client.ApplicationContextProvider.getApplication;

public class EncryptionUtils {

    /*
    Name of the encryption algorithm
    */
    private static final String ALGORITHM = "RSA";
    private static final String CIPHER = "RSA/ECB/PKCS1Padding";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 512;

    //Name of the private key file
    private String privateKeyPath = null;
    //Name of the public key file
    private String publicKeyPath = null;

    public EncryptionUtils(){}

    public EncryptionUtils(String name){
        this.publicKeyPath = name+"PublicKey.key";
        this.privateKeyPath = name+"PrivateKey.key";
    }

    public EncryptionUtils(String publicKeyName, String privateKeyName){

        this.publicKeyPath = publicKeyName;
        this.privateKeyPath = privateKeyName;
    }

    public void setKeyName(String publicKeyName, String privateKeyName){
        this.publicKeyPath = publicKeyName;
        this.privateKeyPath = privateKeyName;
    }

    public PrivateKey getPrivateKey() throws IOException {

        PrivateKey privateKey = null;
        Context context = getApplication().getApplicationContext();
        InputStream stream = context.getAssets().open(privateKeyPath);

        try {
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            privateKey = (PrivateKey) inputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class was not found.");
            e.printStackTrace();
        }

        return privateKey;
    }

    public PublicKey getPublicKey() throws IOException {

        PublicKey publicKey = null;

        Context context = getApplication().getApplicationContext();

        InputStream stream = context.getAssets().open(publicKeyPath);
        try {
            ObjectInputStream inputStream = new ObjectInputStream(stream);
            publicKey = (PublicKey) inputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class was not found.");
            e.printStackTrace();
        }

        return publicKey;
    }

    public byte[] pubKeyToByteArray() throws IOException {
        byte[] publicKeyBytes;

        publicKeyBytes = getPublicKey().getEncoded();

        return publicKeyBytes;
    }

    public byte[] privKeyToByteArray() throws IOException {
        byte[] privKeyBytes;

        privKeyBytes = getPrivateKey().getEncoded();

        return privKeyBytes;
    }

    public Key byteArrayToPrivKey(byte[] privateKey){
        PrivateKey privKey = null;

        try {
            privKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey));
        } catch (InvalidKeySpecException e) {
            System.err.println("The key is invalid.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("There is no such Algorithm.");
            e.printStackTrace();
        }

        return privKey;
    }

    public Key byteArrayToPubKey(byte[] publicKey){

        PublicKey pubKey = null;
        try {
            pubKey = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
        } catch (InvalidKeySpecException e) {
            System.err.println("The key is invalid.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("There is no such Algorithm.");
            e.printStackTrace();
        }

        return pubKey;
    }


    // Generate Signature
    public byte[] generateSignature(byte[] dataToBeSigned) throws SignatureException {
        Signature rsaForSign = null;

        try{
            rsaForSign = Signature.getInstance(SIGNATURE_ALGORITHM);
            rsaForSign.initSign(getPrivateKey());
            rsaForSign.update(dataToBeSigned);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("There's no such algorithm.");
            e.printStackTrace();
        } catch (SignatureException e) {
            System.err.println("Exception with the Signature");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("Key is invalid.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rsaForSign.sign();
    }
    // Verify Signature
    public boolean verifySignature(byte[] data, byte[] signature) throws SignatureException {
        Signature rsaForVerify = null;

        try{
            rsaForVerify = Signature.getInstance(SIGNATURE_ALGORITHM);
            rsaForVerify.initVerify(getPublicKey());
            rsaForVerify.update(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            System.err.println("There is no Signature.");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("The key is not valid.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rsaForVerify.verify(signature);
    }


    // Encrypt data
    public byte[] encrypt(byte[] toEncrypt){
        byte[] cipheredData;
        byte[] encodedCipheredData = null;

        try{
            //Prepare to Encrypt
            final Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());

            // Encrypt
            cipheredData = cipher.doFinal(toEncrypt);
            // Encodes to Base64
            encodedCipheredData = base64Encoder(cipheredData);
        } catch (NoSuchPaddingException e) {
            System.err.println("There is no such padding.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("There is no such Algorithm.");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("The key is invalid.");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("Bad padding.");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.err.println("Block size not accepted.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encodedCipheredData;
    }
    // Decrypt data
    public byte[] decrypt(byte[] toDecrypt){
        // Decodes from Base64
        byte[] decodedCipherText = base64Decoder(toDecrypt);
        byte[] decipheredData = null;

        try{
            // Prepares to decrypt
            final Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE,getPrivateKey());

            // Decrypts
            decipheredData = cipher.doFinal(decodedCipherText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.err.println("The key is invalid.");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.err.println("There is no such padding.");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.err.println("Bad padding.");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.err.println("Block size not accepted.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return decipheredData;
    }


    // Encode data do base64
    public byte[] base64Encoder(byte[] toEncode){
        return Base64.encode(toEncode, Base64.NO_WRAP);
    }
    // Decode data from base64
    public byte[] base64Decoder(byte[] toDecode){
        return Base64.decode(toDecode, Base64.NO_WRAP);
    }
}
