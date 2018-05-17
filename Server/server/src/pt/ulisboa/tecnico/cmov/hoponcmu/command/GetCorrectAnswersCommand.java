package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;


public class GetCorrectAnswersCommand implements Command{

    private static final long serialVersionUID = -8807331723807741905L;
    private String quizzTitle;
    private int id;
    private int time;

    public GetCorrectAnswersCommand(String quizzTitle, int id, int time){
        this.quizzTitle = quizzTitle;
        this.id = id;
        this.time=time;
    }

    @Override
    public Response handle(CommandHandler ch) {
        return ch.handle(this);
    }

    public String getQuizzTitle() {
        return quizzTitle;
    }
    
    public int getTime(){
        return time;
    }

    public int getId(){
        return id;
    }
}
