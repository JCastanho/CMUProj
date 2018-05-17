package pt.ulisboa.tecnico.cmov.hoponcmu.response;


public class PrizesResponse implements Response{
    
    private static final long serialVersionUID = 734457624276534179L;
    private String res;

    public PrizesResponse(String res){
        this.res = res;
    }

    public String getRes() {
        return this.res;
    }
}
