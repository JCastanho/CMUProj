package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import android.widget.ArrayAdapter;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

public class SendQuizzesAnswersCommand implements Command {

    private static final long serialVersionUID = -8807331723807741905L;

    // Data
    private byte[] id;
    private byte[] quizzTitle;
    private ArrayList<byte[]> quizzAnswers;
    private byte[] time;

    // Security
    private byte[] nonce;
    private byte[] signature;
    private transient boolean verified = false;

    public SendQuizzesAnswersCommand(int id, String quizzTitle, ArrayList<String> quizzAnswers,int time) throws UnsupportedEncodingException, SignatureException {
        ArrayList<byte[]> finalQuizzAnswers = new ArrayList<byte[]>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.id = encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));
        this.time = encryption.encrypt(Integer.toString(time).getBytes("UTF-8"));
        this.quizzTitle = encryption.encrypt(quizzTitle.getBytes("UTF-8"));

        for (String answer : quizzAnswers) {
            finalQuizzAnswers.add(encryption.encrypt(answer.getBytes("UTF-8")));
        }
        this.quizzAnswers = finalQuizzAnswers;

        String pureNonce = "SendQuizzesAnswersCommand" +"#"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()).toString() +"#"+ UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + id + quizzTitle + quizzAnswers.toString() + time;

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public int getId() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
        }
        return -1;
    }

    public int getTime() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            return Integer.parseInt(new String(encryption.decrypt(this.time),"UTF-8"));
        }
        return -1;
    }

    public String getQuizzTitle() throws UnsupportedEncodingException {
        if(this.verified){
            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            return new String(encryption.decrypt(this.quizzTitle),"UTF-8");
        }
        return "NOK";
    }

    public ArrayList<String> getQuizzAnswers() throws UnsupportedEncodingException {
        if(this.verified) {
            ArrayList<String> pureAnswers = new ArrayList<String>();

            EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

            for (byte[] answer : this.quizzAnswers) {
                pureAnswers.add(new String(encryption.decrypt(answer), "UTF-8"));
            }

            return pureAnswers;
        }
        return null;
    }

    private int getInternalId() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
    }

    private int getInternalTime() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.time),"UTF-8"));
    }

    private String getInternalQuizzTitle() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return new String(encryption.decrypt(this.quizzTitle),"UTF-8");
    }

    private ArrayList<String> getInternalQuizzAnswers() throws UnsupportedEncodingException {
        ArrayList<String> pureAnswers = new ArrayList<String>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        for (byte[] answer: this.quizzAnswers) {
            pureAnswers.add(new String(encryption.decrypt(answer),"UTF-8"));
        }

        return pureAnswers;
    }

    public byte[] getNonce (){
        return this.nonce;
    }

    public void securityCheck(String nonce) throws UnsupportedEncodingException, SignatureException {
        if(nonce.equals("NOK")){
            this.verified = false;
        }

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String replicateSignature = nonce + this.getInternalId() + this.getInternalQuizzTitle() + this.getInternalQuizzAnswers().toString();

        this.verified = encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }


    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getId() + this.getQuizzTitle() + this.getQuizzAnswers().toString() + this.getInternalTime();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
