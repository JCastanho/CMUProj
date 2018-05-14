package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.Map;

public class PrizesResponse implements Response {

    private static final long serialVersionUID = 734457624276534179L;
    private Map<String, Integer> map;
    private String user;

    public PrizesResponse(Map<String, Integer> map, String user){
        this.map = map;
        this.user = user;
    }

    public Map<String, Integer> getMap() {
        return this.map;
    }

    public String getUser(){
        return this.user;
    }
}
