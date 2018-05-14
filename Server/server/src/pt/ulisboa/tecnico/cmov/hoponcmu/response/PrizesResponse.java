package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.Map;

public class PrizesResponse implements Response{
    private static final long serialVersionUID = 734457624276534179L;
    private Map<String, Integer> map;

    public PrizesResponse(Map<String, Integer> map){
        this.map = map;
    }
    
    public Map<String, Integer> getMap() {
	return this.map;
    }
}
