package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class GetCorrectAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private int correct;

    public GetCorrectAnswersResponse(int correct) {
        this.correct = correct;
    }

    public int getCorrect() {
        return correct;
    }
}
