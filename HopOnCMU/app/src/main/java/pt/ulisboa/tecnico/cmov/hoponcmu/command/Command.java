package pt.ulisboa.tecnico.cmov.hoponcmu.command;

import java.io.Serializable;

import pt.ulisboa.tecnico.cmov.hoponcmu.response.Response;

public interface Command extends Serializable {
	Response handle(CommandHandler ch);
}
