package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class SendQuizzesAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;


    private int id;

    public SendQuizzesAnswersResponse(int id) {
        this.id= id;
    }

    public int getId() {
        return id;
    }
}
