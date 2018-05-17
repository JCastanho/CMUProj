package pt.ulisboa.tecnico.cmov.hoponcmu.client.models;

/**
 * Created by Daniela on 08/05/2018.
 */

public class NearbyUser {

    private String name;
    private String address;

    public NearbyUser(String name, String addr){
        this.name = name;
        this.address = addr;
    }

    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }
}
