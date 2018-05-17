package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class GetCorrectAnswersResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;

    private int correctAnswers;
    private int time;

    public GetCorrectAnswersResponse(int correctAnswers, int time){
        this.correctAnswers = correctAnswers;
        this.time=time;
    }
    
    public int getTime(){
        return time;
    }

    public int getCorrectAnswers(){
        return correctAnswers;
    }
}
