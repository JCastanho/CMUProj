/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author josea
 */
public class Session {

    private List<User> users;
    private Map<Integer, User> login;
    private Map<String, ArrayList<Quizz>> quizzes;
    private Map<String, ArrayList<QuizzAnswers>> quizzAnswers;
    private Map<Integer, Map<String, ArrayList<QuizzAnswers>>> userAnswers;
    private Integer idSequence;


    public Session(){
        idSequence = 0;
        users = new ArrayList<>();
        login = new HashMap<>();
        quizzes = new HashMap<>();
        quizzAnswers = new HashMap<>();
        userAnswers = new HashMap<>();
        populateQuizzes();
        createUser("a","a");
        createUser("b","b");
        verifyUser("b","b");
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

    public String getUserById(int id){
        return login.get(id).getUsername();
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
        //System.out.println(login.size());
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
    			new Quizz("Aonde está situado o Terreiro do Paço?",new ArrayList<String>(Arrays.asList("Entrecampos","Marquês de Pombal","Martim de Moniz")),"Baixa Pombalina"),
    			new Quizz("Que rio passa ao lado?",new ArrayList<String>(Arrays.asList("Rio Douro","Rio Mondego","Rio Vouga")),"Rio Tejo"),
    			new Quizz("Que Rei está representado na estátua?",new ArrayList<String>(Arrays.asList("D. Manuel I","D. Carlos","D. Inês")),"D. José I"),
    			new Quizz("Que outro nome tem este monumento?",new ArrayList<String>(Arrays.asList("Praça da Figueira","Praça do Chile","Avenida de Roma")),"Praça do Comércio")
    	));
    	
    	ArrayList<Quizz> C = new ArrayList<Quizz>(Arrays.asList(
    			new Quizz("Em que ano se deu o incêndio no Chiado?",new ArrayList<String>(Arrays.asList("1978","1987","1990")),"1988"),
    			new Quizz("Que Igreja aqui se encontra?",new ArrayList<String>(Arrays.asList("Igreja de S. Catarina","Basílica da Estrela","Igreja dos Anjos")),"Igreja de Loreto"),
    			new Quizz("Que praia aqui se encontra?",new ArrayList<String>(Arrays.asList("Praça do Comércio","Praça do Chile","Praça de Espanha")),"Praça Luís de Camões"),
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

    public void quizzAnswers(int id, String quizzTitle, ArrayList<String> quizzQuestions, ArrayList<String> answers) {
        ArrayList<QuizzAnswers> list = new ArrayList<QuizzAnswers>(Arrays.asList(
                new QuizzAnswers(quizzQuestions, answers)
        ));
        quizzAnswers.put(quizzTitle, list);
        userAnswers.put(id, quizzAnswers);
    }

    public int correctAnswers(int id, String quizzTitle){

        ArrayList<QuizzAnswers> quizzAnswersArrayList = userAnswers.get(id).get(quizzTitle);
        ArrayList<Quizz> quizzArrayList = quizzes.get(quizzTitle);

        int counter = 0;

        try {
            for (int i = 0; i < quizzAnswersArrayList.get(0).getQuizzAnswers().size(); i++) {
                if (quizzArrayList.get(i).validateAnswer(quizzAnswersArrayList.get(0).getQuizzAnswers().get(i))) {
                    counter += 1;
                }
            }
        }
        catch (Exception e){
            return -1;
        }
        //TODO ADICIONAR O COUNTER AO HASHMAP DE RESPOSTAS CERTAS DO USER
        User u = login.get(id);
        u.setQuizzAnswser(quizzTitle,counter);
        return counter;
    }

    public Map<String, Integer> getQuizzesPrizes(int id){
        Map<String, Integer> users = new HashMap<>();
        User user = login.get(id);
        
        for(int idAux: login.keySet()){
            User userAux = login.get(idAux);
            int counter = 0;

            for(String quizz: userAux.getQuizzAnswser().keySet()){
                counter+=userAux.getQuizzAnswser().get(quizz);
            }

            if(user==userAux){
                for(String quizz: userAux.getQuizzAnswser().keySet()){
                    counter+=userAux.getQuizzAnswser().get(quizz);
                }
                if(user.getQuizzAnswser().keySet().size()==4){
                    users.put("FINALSELECTED"+user.getUsername()+"/"+user.getTimeForQuizz(), counter);
                }
                else{
                    users.put("SELECTED"+user.getUsername()+"/"+user.getTimeForQuizz(), counter);
                }
            }
            else{
                System.out.println("USER: " + userAux.getUsername());
                users.put(userAux.getUsername(), counter);
            }
        }

        List<Entry<String, Integer>> list = new ArrayList<>(users.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        Map<String, Integer> OrderUsers = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list) {
            OrderUsers.put(entry.getKey(), entry.getValue());
        }
        
        return OrderUsers;
    }
    
    public void saveTime(int id, int timeForQuizz){
        User u = login.get(id);
        u.setTimeForQuizz(timeForQuizz);
    }

    public List<String> getAnsweredQuizzes(int id){
        Map<String, ArrayList<QuizzAnswers>> map = userAnswers.get(id);
        List<String> answeredQuizzes = new ArrayList<>();
        try{
            answeredQuizzes = new ArrayList<>(map.keySet());
            return answeredQuizzes;
        }catch (Exception e){
            return answeredQuizzes;
        }
    }
}
