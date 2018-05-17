package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import android.widget.ArrayAdapter;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
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
    private ArrayList<byte[]> quizzQuestions;
    private ArrayList<byte[]> quizzAnswers;

    // Security
    private byte[] nonce;
    private byte[] signature;

    public SendQuizzesAnswersCommand(int id, String quizzTitle, ArrayList<String> quizzQuestions, ArrayList<String> quizzAnswers) throws UnsupportedEncodingException, SignatureException {
        ArrayList<byte[]> finalQuizzQuestions = new ArrayList<byte[]>();
        ArrayList<byte[]> finalQuizzAnswers = new ArrayList<byte[]>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        this.id= encryption.encrypt(Integer.toString(id).getBytes("UTF-8"));
        this.quizzTitle=encryption.encrypt(quizzTitle.getBytes("UTF-8"));

        for (String location: quizzQuestions) {
            finalQuizzQuestions.add(encryption.encrypt(location.getBytes("UTF-8")));
        }
        this.quizzQuestions= finalQuizzQuestions;


        for (String location: quizzAnswers) {
            finalQuizzAnswers.add(encryption.encrypt(location.getBytes("UTF-8")));
        }
        this.quizzAnswers = finalQuizzAnswers;

        String pureNonce = "SendQuizzesAnswersCommand" + Calendar.getInstance().getTime().toString() + UUID.randomUUID().toString();
        this.nonce = encryption.encrypt(pureNonce.getBytes("UTF-8"));

        String pureSignature = pureNonce + id + quizzTitle + quizzQuestions.toString() + quizzAnswers.toString();

        this.signature = encryption.generateSignature(pureSignature.getBytes("UTF-8"));
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public int getId() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return Integer.parseInt(new String(encryption.decrypt(this.id),"UTF-8"));
    }

    public String getQuizzTitle() throws UnsupportedEncodingException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        return new String(encryption.decrypt(this.quizzTitle),"UTF-8");
    }

    public ArrayList<String> getQuizzQuestions() throws UnsupportedEncodingException {
        ArrayList<String> pureQuestions = new ArrayList<String>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        for (byte[] question: this.quizzQuestions) {
            pureQuestions.add(new String(encryption.decrypt(question),"UTF-8"));
        }

        return pureQuestions;
    }

    public ArrayList<String> getQuizzAnswers() throws UnsupportedEncodingException {
        ArrayList<String> pureAnswers = new ArrayList<String>();

        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        for (byte[] answer: this.quizzAnswers) {
            pureAnswers.add(new String(encryption.decrypt(answer),"UTF-8"));
        }

        return pureAnswers;
    }


    public boolean securityCheck() throws UnsupportedEncodingException, SignatureException {
        EncryptionUtils encryption = new EncryptionUtils("serverPublicKey.key", "clientPrivateKey.key");

        String nonce = new String(encryption.decrypt(this.nonce),"UTF-8");

        String replicateSignature = nonce + this.getId() + this.getQuizzTitle() + this.getQuizzQuestions().toString() + this.getQuizzAnswers().toString();

        return encryption.verifySignature(replicateSignature.getBytes("UTF-8"),signature);
    }
}
