package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class SendQuizzesAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    private String quizzAnswers;

    public SendQuizzesAnswersResponse(String quizzAnswers) {
        this.quizzAnswers = quizzAnswers;
    }

    public String getQuizzAnswers() {
        return this.quizzAnswers;
    }
}
