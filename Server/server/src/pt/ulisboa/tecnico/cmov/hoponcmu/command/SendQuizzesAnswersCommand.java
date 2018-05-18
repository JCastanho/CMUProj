package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;
import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

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

    public SendQuizzesAnswersCommand(int id, String quizzTitle, ArrayList<String> quizzAnswers,int time) throws UnsupportedEncodingException, SignatureException {
        ArrayList<byte[]> finalQuizzAnswers = new ArrayList<byte[]>();

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        this.id = encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));
        this.time = encryption.encrypt(Integer.toString(time).getBytes("UTF-8"));
        this.quizzTitle = encryption.encrypt(quizzTitle.getBytes("UTF-8"));

        for (String answer : quizzAnswers) {
            finalQuizzAnswers.add(encryption.encrypt(answer.getBytes("UTF-8")));
        }
        this.quizzAnswers = finalQuizzAnswers;

        String pureNonce = "SendQuizzesAnswersCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + id + quizzTitle + quizzAnswers.toString();

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public int getId() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
    }


    public int getTime() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.time),"UTF-8"));
    }

    public String getQuizzTitle() throws UnsupportedEncodingException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        return new String(encryption.decrypt(this.quizzTitle),"UTF-8");
    }

    public ArrayList<String> getQuizzAnswers() throws UnsupportedEncodingException {
        ArrayList<String> pureAnswers = new ArrayList<String>();

    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        for (byte[] answer: this.quizzAnswers) {
            pureAnswers.add(new String(encryption.decrypt(answer),"UTF-8"));
        }

        return pureAnswers;
    }


    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
    	EncryptionUtils encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getId() + this.getQuizzTitle() + this.getQuizzAnswers().toString();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }        
}
