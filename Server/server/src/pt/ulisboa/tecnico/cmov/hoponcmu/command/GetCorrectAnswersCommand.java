package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;


public class GetCorrectAnswersCommand implements Command{

    private static final long serialVersionUID = -8807331723807741905L;

    // Data
    private byte[] id;
    private byte[] quizzTitle;

    // Security
    private byte[] nonce;
    private byte[] signature;
    private transient boolean verified = false;

    public GetCorrectAnswersCommand(int id ,String quizzTitle) throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        this.id= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));
        this.quizzTitle=encryption.encrypt(quizzTitle.getBytes("UTF-8"));

        String pureNonce = "GetCorrectAnswersCommand" +"#"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()).toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + id + quizzTitle;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public String getQuizzTitle() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

            return new String(encryption.decrypt(this.quizzTitle),"UTF-8");
        }
        return "NOK";
    }

    public int getId() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

            return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
        }
        return -1;
    }

    private String getInternalQuizzTitle() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return new String(encryption.decrypt(this.quizzTitle),"UTF-8");
    }

    private int getInternalId() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
    }

    public byte[] getNonce (){
        return this.nonce;
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

        EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String replicateSignature = nonce + this.getInternalId() + this.getInternalQuizzTitle();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
