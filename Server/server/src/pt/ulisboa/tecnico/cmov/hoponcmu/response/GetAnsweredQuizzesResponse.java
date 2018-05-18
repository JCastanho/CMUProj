package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class GetAnsweredQuizzesResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    // Data
    private List<byte[]> locations;

    // Security
    private byte[] nonce;
    private byte[] signature;
    private transient boolean verified = false;

    public GetAnsweredQuizzesResponse(List<String> locations) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        ArrayList<byte[]> finalLocations = new ArrayList<byte[]>();
        for (String location: locations) {
            finalLocations.add(encryption.encrypt(location.getBytes("UTF-8")));
        }
        this.locations=finalLocations;

        String pureNonce = "GetAnsweredQuizzesResponse" +"#"+ Calendar.getInstance().getTime().toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + locations.toString();

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public List<String> getLocations() throws UnsupportedEncodingException {
        if(this.verified){
            ArrayList<String> pureLocations = new ArrayList<String>();

        	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

            for (byte[] location: this.locations) {
                pureLocations.add(new String(encryption.decrypt(location),"UTF-8"));
            }

            return pureLocations;
        }
        return null;
    }

    private List<String> getInternalLocations() throws UnsupportedEncodingException {

        ArrayList<String> pureLocations = new ArrayList<String>();

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        for (byte[] location: this.locations) {
            pureLocations.add(new String(encryption.decrypt(location),"UTF-8"));
        }

        return pureLocations;
    }

    public byte[] getNonce (){
        return this.nonce;
    }


    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String replicateSignature = nonce + this.getInternalLocations().toString();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
