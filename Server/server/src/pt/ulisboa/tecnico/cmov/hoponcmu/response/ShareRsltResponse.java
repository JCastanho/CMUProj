package pt.ulisboa.tecnico.cmov.hoponcmu.response;

public class ShareRsltResponse implements Response {

	private static final long serialVersionUID = 734457624276534179L;
	private Boolean success;

	public ShareRsltResponse(Boolean success){
		this.success = success;
	}

	public Boolean getSuccess() { return this.success; }
}
