package pt.ulisboa.tecnico.cmov.hoponcmu.response;

import java.util.List;

/**
 * Created by Daniela on 03/05/2018.
 */

public class GetUsersResponse implements Response{
    private static final long serialVersionUID = 734457624276534179L;
    private List<String> users;

    public GetUsersResponse(List<String> users){
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}
