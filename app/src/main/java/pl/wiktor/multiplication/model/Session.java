package pl.wiktor.multiplication.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//A SESSION-CLASS, IT'S STORES A QUESTIONS ANSWERED, DATE WHEN CREATED AND SIZE OF QUESTIONS TO ASK
public class Session {
    private int allQuestions;
    private List<Question> questions;
    private LocalDateTime createdAt;

    public Session(int allQuestions, LocalDateTime createdAt) {
        this.allQuestions = allQuestions;
        this.createdAt = createdAt;
        questions = new ArrayList<>();
    }

    public int getAllQuestions() {
        return allQuestions;
    }

    public int getCorrectAnswered() {
        return (int) questions.stream()
                .filter(Question::isCorrectAnswered)
                .count();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }
}
