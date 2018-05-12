/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

<<<<<<< HEAD
import quiz.Quiz;

import java.lang.reflect.Array;
=======
import javax.sound.midi.SysexMessage;
>>>>>>> Daniela
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author josea
 */
public class Session {

    private List<User> users;
    private Map<Integer, User> login;
    private Map<String, ArrayList<Quizz>> quizzes;
<<<<<<< HEAD
    private Map<String, ArrayList<QuizzAnswers>> quizzAnswers;
    private Integer idSequence = 0;
=======
    private Integer idSequence;
>>>>>>> Daniela

    public Session(){
        idSequence = 0;
        users = new ArrayList<>();
        login = new HashMap<>();
        quizzes = new HashMap<>();
        quizzAnswers = new HashMap<>();
        populateQuizzes();
        users.add(new User("a","a"));
        users.add(new User("b","b"));
    }

    public Boolean createUser(String username, String code){
        for(User u: users){
            if(u.getUsername().equals(username) || u.getCode().equals(code)){
                return false;
            }
        }

        User nu = new User(username, code);

        return users.add(nu);
    }

    public int verifyUser(String username, String password){
        int identifier = -1;

        if(verifyCredentials(username, password)){
            if(!isUserLogged(username)) {
                identifier = generateID();
                login.put(identifier, getUser(username));
            }
        }

        return identifier;
    }

    private User getUser(String username){
        for(User u: users){
            if(u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    private Boolean isUserLogged(String username){
        for(User loggedUser: login.values()){
            if(loggedUser.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    private Boolean verifyCredentials(String username, String password){
        for(User u: users){
            if(u.getUsername().equals(username) && u.getCode().equals(password))
                return true;
        }

        return false;
    }

    private Integer generateID(){
        //Change eventually
        return login.size();
    }

    public void logOutUser(Integer token) {
        login.remove(token);
        System.out.println(login.size());
    }

    public ArrayList<String> getQuizzAnswers(String monument, int page){
        return quizzes.get(monument).get(page).getAnswers();
    }

    public String getQuizzQuestion(String monument, int page){
        return quizzes.get(monument).get(page).getQuestion();
    }

    public int getQuizzSize(String monument){
        return quizzes.get(monument).size();
    }

    public void populateQuizzes(){
    	
    	ArrayList<Quizz> TdP = new ArrayList<Quizz>(Arrays.asList(
    			new Quizz("Aonde est� situado o Terreiro do Pa�o?",new ArrayList<String>(Arrays.asList("Entrecampos","Marqu�s de Pombal","Martim de Moniz")),"Baixa Pombalina"),
    			new Quizz("Que rio passa ao lado?",new ArrayList<String>(Arrays.asList("Rio Douro","Rio Mondego","Rio Vouga")),"Rio Tejo"),
    			new Quizz("Que Rei est� representado na est�tua?",new ArrayList<String>(Arrays.asList("D. Manuel I","D. Carlos","D. In�s")),"D. Jos� I"),
    			new Quizz("Que outro nome tem este monumento?",new ArrayList<String>(Arrays.asList("Pra�a da Figueira","Pra�a do Chile","Avenida de Roma")),"Pra�a do Com�rcio")
    	));
    	
    	ArrayList<Quizz> C = new ArrayList<Quizz>(Arrays.asList(
    			new Quizz("Em que ano se deu o inc�ndio no Chiado?",new ArrayList<String>(Arrays.asList("1978","1987","1990")),"1988"),
    			new Quizz("Que Igreja aqui se encontra?",new ArrayList<String>(Arrays.asList("Igreja de S. Catarina","Bas�lica da Estrela","Igreja dos Anjos")),"Igreja de Loreto"),
    			new Quizz("Que pra�a aqui se encontra?",new ArrayList<String>(Arrays.asList("Pra�a do Com�rcio","Pre�a do Chile","Pra�a de Espanha")),"Pra�a Lu�s de Cam�es"),
    			new Quizz("Pergunta 1",new ArrayList<String>(Arrays.asList("Resposta 1","Resposta 2","Resposta 3")),"Resposta 4")
    	));
    	
    	ArrayList<Quizz> fake = new ArrayList<Quizz>(Arrays.asList(
    			new Quizz("Pergunta 1",new ArrayList<String>(Arrays.asList("Resposta 1","Resposta 2","Resposta 3")),"Resposta 4"),
    			new Quizz("Pergunta 2",new ArrayList<String>(Arrays.asList("Resposta 1","Resposta 2","Resposta 3")),"Resposta 4"),
    			new Quizz("Pergunta 3",new ArrayList<String>(Arrays.asList("Resposta 1","Resposta 2","Resposta 3")),"Resposta 4"),
    			new Quizz("Pergunta 4",new ArrayList<String>(Arrays.asList("Resposta 1","Resposta 2","Resposta 3")),"Resposta 4")
	    	));

    	quizzes.put("Terreiro do Paço", TdP);
    	quizzes.put("Chiado", C);
    	quizzes.put("Castelo de São Jorge", fake);
    	quizzes.put("Praça da Figueira", fake);
    	
    }

    public Boolean verifyLogin(int identifier){
        if(login.containsKey(identifier)) return true;
        else return false;
    }
<<<<<<< HEAD

    public void quizzAnswers(String quizzTitle, ArrayList<String> quizzQuestions, ArrayList<String> answers) {
        ArrayList<QuizzAnswers> list = new ArrayList<QuizzAnswers>(Arrays.asList(
                new QuizzAnswers(quizzQuestions, answers)
        ));

        quizzAnswers.put(quizzTitle, list);

    }

    public int correctAnswers(String quizzTitle){
        ArrayList<QuizzAnswers> quizzAnswersArrayList = quizzAnswers.get(quizzTitle);
        ArrayList<Quizz> quizzArrayList = quizzes.get(quizzTitle);
        int counter = 0;

        try {
            for (int i = 0; i < quizzAnswersArrayList.size(); i++) {
                if (quizzArrayList.get(i).validateAnswer(quizzAnswersArrayList.get(i).getQuizzAnswers().get(i))) {
                    counter += 1;
                }
            }
        }
        catch (Exception e){
            return -1;
        }

        return counter;
    }
    //TODO remove from login list when logging out

=======
>>>>>>> Daniela
}
