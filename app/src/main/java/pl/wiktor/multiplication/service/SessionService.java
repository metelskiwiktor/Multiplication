package pl.wiktor.multiplication.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import pl.wiktor.multiplication.model.Question;
import pl.wiktor.multiplication.model.Session;
import pl.wiktor.multiplication.repository.SessionRepository;

public class SessionService {
    private SessionRepository sessionRepository = new SessionRepository();
    private Session currentSession;

    //CREATING A QUESTION WITH RANDOM NUMBERS, IF SESSION IS FULL OF QUESTIONS THEN CREATE NEW AND SAVE PREVIOUSLY ONE TO REPOSITORY
    public Question getNextQuestion(){
        if((currentSession == null || currentSession.getAllQuestions() <= currentSession.getQuestions().size())) {
            currentSession = new Session(15, LocalDateTime.now());
            sessionRepository.save(currentSession);
        }

        int randomFirstNumber = new Random().nextInt(10) + 1;
        int randomSecondNumber = new Random().nextInt(10) + 1;

        return new Question(randomFirstNumber, randomSecondNumber);
    }

    //ADD QUESTION WITH ANSWER TO SESSION
    public void saveAnswer(Question question){
        currentSession.addQuestion(question);
    }

    //RETURN SIZE OF ALREADY ANSWERED QUESTIONS
    public int getAnsweredQuestionsSize(){
        return currentSession.getQuestions().size() + 1;
    }

    //RETURN SIZE OF ALL QUESTIONS TO ASK AND ASKED SUM
    public int getAllQuestionsSize(){
        return currentSession.getAllQuestions();
    }

    //RETURN SESSIONS SAVED BY NOW
    public List<Session> getAllSessions(){
        return sessionRepository.getSessions();
    }
}
