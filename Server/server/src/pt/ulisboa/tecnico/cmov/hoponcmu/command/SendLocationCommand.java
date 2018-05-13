package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class SendLocationCommand implements Command{
    
    private static final long serialVersionUID = -8807331723807741905L;
    private List<String> locations;
    private final String locationVariable="location";

    // Data
    private byte[] location;

    // Security
    private byte[] nonce;
    private byte[] signature;

    public SendLocationCommand(String location) throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        this.location= encryption.encrypt(location.getBytes("UTF-8"));

        String pureNonce = "SendLocationCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + location;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler chi) {
        return chi.handle(this);
    }

    public String getLocation() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return new String(encryption.decrypt(this.location),"UTF-8");
    }
    
    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getLocation();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
    
    public List<String> verifyString(String location){
        if(location.equals(locationVariable)){
            populateList();
            return locations;
        }
        return null;
    }
    
    public void populateList(){
        locations = new ArrayList<>();
        locations.add("Terreiro do Pa�o");
        locations.add("Chiado");
        locations.add("Castelo de S�o Jorge");
        locations.add("Pra�a da Figueira");
    }
}
