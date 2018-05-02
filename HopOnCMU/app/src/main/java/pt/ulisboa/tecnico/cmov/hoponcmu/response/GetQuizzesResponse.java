package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class GetQuizzesResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private String quizzes;

	public GetQuizzesResponse(String quizzes){
		this.quizzes = quizzes;
	}

	public String getQuizzes() {
		return this.quizzes;
	}
}
