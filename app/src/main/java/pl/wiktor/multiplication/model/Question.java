package pl.wiktor.multiplication.model;

//A CLASS THAT HAVE FIRST NUMBER AND SECOND NUMBER THAT MULTIPLICATION SHOULD BE A ANSWER
public class Question {
    private int firstNumber;
    private int secondNumber;
    private int answer;

    public Question(int firstNumber, int secondNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public int getAnsweredNumber() {
        return answer;
    }

    public void setAnswer(int answered) {
        this.answer = answered;
    }

    public int getCorrectNumber() {
        return firstNumber * secondNumber;
    }

    public boolean isCorrectAnswered(){
        return getAnsweredNumber() == getCorrectNumber();
    }
}
