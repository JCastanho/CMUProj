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

    public GetAnsweredQuizzesResponse(List<String> locations) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        ArrayList<byte[]> finalLocations = new ArrayList<byte[]>();
        for (String location: locations) {
            finalLocations.add(encryption.encrypt(location.getBytes("UTF-8")));
        }
        this.locations=finalLocations;

        String pureNonce = "CreateUserCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + locations.toString();

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    public List<String> getLocations() throws UnsupportedEncodingException {

        ArrayList<String> pureLocations = new ArrayList<String>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        for (byte[] location: this.locations) {
            pureLocations.add(new String(encryption.decrypt(location),"UTF-8"));
        }

        return pureLocations;
    }

    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getLocations().toString();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
