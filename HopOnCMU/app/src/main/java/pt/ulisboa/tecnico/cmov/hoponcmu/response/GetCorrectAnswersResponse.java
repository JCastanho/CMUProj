package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class GetCorrectAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private int correctAnswers;

    public GetCorrectAnswersResponse(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }
}
