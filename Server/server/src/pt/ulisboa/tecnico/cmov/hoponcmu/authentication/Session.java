/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ulisboa.tecnico.cmov.hoponcmu.authentication;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import pt.ulisboa.tecnico.cmov.hoponcmu.utils.EncryptionUtils;

/**
 *
 * @author josea
 */
public class Session {

    private List<User> users;
    private Map<Integer, User> login;
    private Map<String, ArrayList<Quizz>> quizzes;
    private Map<String, ArrayList<QuizzAnswers>> quizzAnswers;
    private Map<String, Map<String, ArrayList<QuizzAnswers>>> userAnswers;
    private Integer idSequence;
    
    // Security
    private EncryptionUtils encryption;
    private ArrayList<String> nonces;
    private Date lastClear;


    public Session(){
        idSequence = 0;
        users = new ArrayList<>();
        login = new HashMap<>();
        quizzes = new HashMap<>();
        quizzAnswers = new HashMap<>();
        userAnswers = new HashMap<>();
    	encryption = new EncryptionUtils("clientPublicKey.key", "serverPrivateKey.key");
        nonces = new ArrayList<String>();
    	lastClear = Calendar.getInstance().getTime();
        populateQuizzes();
        createUser("a","a");
        createUser("b","b");
        //verifyUser("b","b");
    }

    public Boolean createUser(String username, String code){
        for(User u: users){
            if(u.getUsername().equals(username) || u.getCode().equals(code)){
                return false;
            }
        }

        User nu = new User(username, code);
        /*if(nu.getUsername().equals("b")){
            nu.setQuizzAnswser("chiado",4);
            for(int i: login.keySet()){
                if(login.get(i)==nu){
                    saveTime(i,"chiado",5);
                }
            }
        }*/

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

    public String getUsernameById(int id){
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
        Integer id = login.size();
        System.out.println(id);
        return id;
    }

    public void logOutUser(Integer token) {
        if(login.containsKey(token)) {
            login.remove(token);
            System.out.println("The user logged out successfully");
        } else {
            System.out.println("The user logged out was already logged out or had an broken token");
        }

        System.out.println("Remanining users: " + login.size());
    }

    public ArrayList<ArrayList<String>> getQuizzAnswers(String monument){
        ArrayList<Quizz> quizzArrayList = quizzes.get(monument);
        ArrayList<ArrayList<String>> answers = new ArrayList<>();
        for(int i = 0; i < quizzArrayList.size(); i++){
            answers.add(quizzArrayList.get(i).getAnswers());
        }
        return answers;
    }

    public ArrayList<String> getQuizzQuestion(String monument){
        ArrayList<Quizz> quizzArrayList = quizzes.get(monument);
        ArrayList<String> questions = new ArrayList<>();
        for(int i=0; i<quizzArrayList.size(); i++){
            questions.add(quizzArrayList.get(i).getQuestion());
        }
        return questions;
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

    public void quizzAnswers(int id, String quizzTitle, ArrayList<String> answers) {
        ArrayList<QuizzAnswers> list = new ArrayList<QuizzAnswers>(Arrays.asList(
                new QuizzAnswers(answers)
        ));
        quizzAnswers.put(quizzTitle, list);
        userAnswers.put(getUsernameById(id), quizzAnswers);
    }

    public int correctAnswers(int id, String quizzTitle){

        ArrayList<QuizzAnswers> quizzAnswersArrayList = userAnswers.get(getUsernameById(id)).get(quizzTitle);
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

    public String getQuizzesPrizes(int id){
        Map<String, Integer> users = new HashMap<>();
        User user = login.get(id);
        boolean flag=false;
        boolean resFinal=false;
        String res="";

        for(int idAux: login.keySet()){
            User userAux = login.get(idAux);
            int counter = 0;

            for(String quizz: userAux.getQuizzAnswser().keySet()){
                counter+=userAux.getQuizzAnswser().get(quizz);
            }
            //System.out.println("User: " + userAux.getUsername() + " counter: " + counter);
            users.put(userAux.getUsername(), counter);
        }

        Map<String, Integer> pontos = new HashMap<>();
        for(String i: users.keySet()){
            int pont=users.get(i);
            //System.out.println("User: " + getUser(i).getUsername() + " keySet : " + getUser(i).getQuizzAnswser().keySet());
            if(getUser(i).getQuizzAnswser().keySet().size()>=1){
                if(getUser(i).getUsername().equals(user.getUsername())){
                    flag=true;
                }
                if(getUser(i).getQuizzAnswser().keySet().size()==4){
                    resFinal=true;
                }
                //System.out.println("entrei");
                pont = (users.get(i)*50) - (getUser(i).allQuizzTimes());
                if(pont<50){
                    pont=50;
                }
            }
            //System.out.println("i: " + i + " pont: " + pont);
            pontos.put(i,pont);
        }

        for(String s: pontos.keySet()){
            System.out.println("User: " + s + " Pontos: " + pontos.get(s));
        }
        
        List<Entry<String, Integer>> list = new ArrayList<>(pontos.entrySet());
        list.sort(Entry.comparingByValue());
        Collections.reverse(list);
        
        Map<String, Integer> OrderUsers = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list) {
            OrderUsers.put(entry.getKey(), entry.getValue());
        }

        int rank=1;
        for(String s: OrderUsers.keySet()){
            if(user.getUsername().equals(s)){
                res = flag+"/"+ rank+"/"+OrderUsers.get(s)+"/"+resFinal;
            }
            rank++;
        }

        return res;
    }

    public void saveTime(int id, String quizzTitle, int time){
        User u = login.get(id);
        u.setTimeForQuizz(quizzTitle,time);
    }

    public int getTime(int id, String quizzTitle){
        User u = login.get(id);

        return u.getTimeForQuizz(quizzTitle);
    }

    public List<String> getAnsweredQuizzes(int id){
        List<String> answeredQuizzes = new ArrayList<>();

        try{
            Map<String, ArrayList<QuizzAnswers>> map = userAnswers.get(getUsernameById(id));
            answeredQuizzes = new ArrayList<>(map.keySet());
        } catch (Exception e){
        }

        return answeredQuizzes;
    }
    
    public String checkNonce(byte[] encryptedNonce){
    	try {
			String nonce = new String(encryption.decrypt(encryptedNonce),"UTF-8");
			
			if(nonces.contains(nonce)) {
				return "NOK";
			}
			
			String[] parts = nonce.split("#");
			
			// Test Freshness
			Calendar nonceCalendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			nonceCalendar.setTime(sdf.parse(parts[1]));
			
			Date nonceDate = nonceCalendar.getTime();
			Calendar now = Calendar.getInstance();
			Date dNow = now.getTime();

			Calendar limit = now;
			limit.add(Calendar.HOUR, -2);
			Date dLimit = limit.getTime();

			if(dLimit.before(nonceDate) && dNow.after(nonceDate)) {
				nonces.add(nonce);
				return nonce;
			};
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return "NOK";
    }
    
    private void cleanNonces() {
		try {
			Calendar now = Calendar.getInstance();
			Calendar scheduled = now;
			scheduled.add(Calendar.DATE, -1);
			
			if(scheduled.before(lastClear)) {
				for(String nonce : nonces) {
					String[] parts = nonce.split("#");
					
					
					Calendar nonceCalendar = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat();
						nonceCalendar.setTime(sdf.parse(parts[1]));
					Date nonceDate = nonceCalendar.getTime();
					
					Calendar limit = now;
					limit.add(Calendar.HOUR, -1);
					
					if(limit.after(nonceDate)) {
						nonces.remove(nonce);
					};
		    	}
				
		    	lastClear = Calendar.getInstance().getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
