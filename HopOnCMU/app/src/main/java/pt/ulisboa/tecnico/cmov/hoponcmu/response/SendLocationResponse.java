package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class SendLocationResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    // Data
    private List<byte[]> locations;

    // Security
    private byte[] nonce;
    private byte[] signature;
    private transient boolean verified = false;

    public SendLocationResponse(List<String> locations) throws UnsupportedEncodingException, SignatureException {
        List<byte[]> finalLocations = new ArrayList<byte[]>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");


        for (String location: locations) {
            finalLocations.add(encryption.encrypt(location.getBytes("UTF-8")));
        }
        this.locations= finalLocations;

        String pureNonce = "SendLocationResponse" +"#"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()).toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + locations.toString();

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));

    }

    public List<String> getLocations() throws UnsupportedEncodingException {
        if(verified){
            List<String> pureLocations = new ArrayList<String>();

            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            for (byte[] location: this.locations) {
                pureLocations.add(new String(encryption.decrypt(location),"UTF-8"));
            }

            return pureLocations;
        }
        return null;
    }

    private List<String> getInternalLocations() throws UnsupportedEncodingException {
        List<String> pureLocations = new ArrayList<String>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        for (byte[] location: this.locations) {
            pureLocations.add(new String(encryption.decrypt(location),"UTF-8"));
        }

        return pureLocations;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String replicateSignature = nonce + this.getInternalLocations().toString();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }

}
