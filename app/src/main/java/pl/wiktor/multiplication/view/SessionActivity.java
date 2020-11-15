package pl.wiktor.multiplication.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.wiktor.multiplication.R;
import pl.wiktor.multiplication.Singleton;
import pl.wiktor.multiplication.model.Question;
import pl.wiktor.multiplication.service.SessionService;

public class SessionActivity extends AppCompatActivity {
    private final SessionService sessionService = Singleton.SESSION_SERVICE.getSessionService();
    private final Handler handler = new Handler();
    private int leftSeconds;
    private TextView answerStatus;
    private EditText numberInput;
    private ProgressBar progressBar;
    private Button performActionButton;
    private TextView firstNumberTextView;
    private TextView secondNumberTextView;
    private TextView leftSecondsTextView;
    private TextView validAnswerInformation;
    private Question currentQuestion;
    private TextView answeredQuestionsTextView;
    private boolean alreadyAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LOAD VIEW FROM LAYOUT
        setContentView(R.layout.activity_session);

        //GET NEW QUESTION FROM SESSIONSERVICE
        currentQuestion = sessionService.getNextQuestion();

        //LOAD ALL NEEDED VIEWS FROM ID AND SET THEIR TEXT IF NEEDED
        answerStatus = findViewById(R.id.answerStatusTextView);
        validAnswerInformation = findViewById(R.id.validAnswerInformationTextView);
        numberInput = findViewById(R.id.numberInput);
        Button backToMenu = findViewById(R.id.backToMenuButton);
        performActionButton = findViewById(R.id.performActionButton);
        firstNumberTextView = findViewById(R.id.firstNumber);
        secondNumberTextView = findViewById(R.id.secondNumber);
        leftSecondsTextView = findViewById(R.id.leftSecondsTextView);
        answeredQuestionsTextView = findViewById(R.id.answeredQuestionsTextView);
        answeredQuestionsTextView.setText(String.format("%d/%d",
                sessionService.getAnsweredQuestionsSize(), sessionService.getAllQuestionsSize()));
        firstNumberTextView.setText(String.valueOf(currentQuestion.getFirstNumber()));
        secondNumberTextView.setText(String.valueOf(currentQuestion.getSecondNumber()));
        progressBar = findViewById(R.id.progressBar);

        //SAFE-THREAD AND SAFE-SERVICE DELETE ACTIVITY
        backToMenu.setOnClickListener(v -> stopActivity());

        //PERFORM CHECKANSWER ON BUTTON CLICK
        performActionButton.setOnClickListener(checkAnswer());

        //RUN METHOD THAT LOAD COUNTER
        runCounter();
    }

    //THE SYNCHRONIZED MIGHT BE NOT REQUIRED, BUT FOR SAFE-THREAD I PREFER TO USE THIS TO AVOID
    //SET alreadyAnswered VARIABLE SAVE STATE INCORRECTLY
    //IT CHECKS IF THE ANSWER IS GOOD OR NOT AND SAVE THE ANSWER TO SERVICE
    synchronized private void checkAnswerCondition() {
        if(alreadyAnswered) return;

        String input = numberInput.getText().toString();
        int inputNumber = Integer.parseInt(input);

        currentQuestion.setAnswer(inputNumber);
        sessionService.saveAnswer(currentQuestion);
        if (currentQuestion.isCorrectAnswered()) {
            correct();
        } else {
            incorrect();
        }
        alreadyAnswered = true;
    }

    //IT'S SAFE-THREAD AND SAFE-SERVICE BECAUSE STOP TIME AND EXTERNAL THREAD IS STOP RUNNING, SAVING
    //ANSWERED QUESTION AND DELETE CURRENT ACTIVITY
    private void stopActivity() {
        stopTime();
        checkAnswerCondition();
        finish();
    }

    //SET LEFTSECONDS TO 0 AND PROGRESS BAR TO 0 TOO
    private void stopTime() {
        leftSeconds = 0;
        progressBar.setProgress(leftSeconds);
    }

    //IF ANSWER IS CORRECT THE TEXT IS DISPLAYING THIS ON GREEN COLOR TEXT
    private void correct() {
        answerStatus.setText("POPRAWNIE");
        answerStatus.setTextColor(Color.GREEN);
    }

    //IF ANSWER IS INCORRECT THE TEXT IS DISPLAYING THIS ON RED COLOR TEXT
    //AND ADDITIONALLY SHOWS THE RIGHT ANSWER
    private void incorrect() {
        answerStatus.setText("NIEPOPRAWNIE");
        answerStatus.setTextColor(Color.RED);
        validAnswerInformation.setText(String.format("Poprawna odpowiedź to %d.", currentQuestion.getFirstNumber()*currentQuestion.getSecondNumber()));
    }

    //ITS A METHOD THAT RETURNS A CLASS TO SET IN BUTTON, BY THIS I CAN EASY MANIPULATE A PERFORMED ACTION
    //ON BUTTON PRESS
    //IN THIS STATE IT'S CHECKING IF THE ANSWER IS GOOD, STOPPING TIME AND CHANGE NEXT ACTION ON BUTTON CLICK
    private View.OnClickListener checkAnswer() {
        return v -> {
            checkAnswerCondition();
            stopTime();
            performActionButton.setOnClickListener(nextAnswer());
            performActionButton.setText("NASTĘPNE");
        };
    }

    //ITS A METHOD THAT RETURNS A CLASS TO SET IN BUTTON, BY THIS I CAN EASY MANIPULATE A PERFORMED ACTION
    //ON BUTTON PRESS
    //IN THIS STATE IT'S RESETTING ALL VARIABLES TO DEFAULT AND CHANGE NEXT ACTION ON BUTTON CLICK
    private View.OnClickListener nextAnswer() {
        return v -> {
            reset();
            performActionButton.setOnClickListener(checkAnswer());
            performActionButton.setText("ZATWIERDŹ");
        };
    }

    //RESETTING ALL VARIABLES TO DEFAULT TEXT SO PLAYER GET ANOTHER QUESTION COUNTER WILL BE RUN AGAIN
    private void reset() {
        answerStatus.setText("");
        currentQuestion = sessionService.getNextQuestion();
        firstNumberTextView.setText(String.valueOf(currentQuestion.getFirstNumber()));
        secondNumberTextView.setText(String.valueOf(currentQuestion.getSecondNumber()));
        numberInput.setText("0");
        leftSeconds = progressBar.getMax();
        progressBar.setProgress(leftSeconds);
        leftSecondsTextView.setText(String.format("%ds.", leftSeconds));
        validAnswerInformation.setText("");
        answeredQuestionsTextView.setText(String.format("%d/%d",
                sessionService.getAnsweredQuestionsSize(), sessionService.getAllQuestionsSize()));
        alreadyAnswered = false;
        runCounter();
    }

    //IT MANAGES A PROGRESSBAR, NEW THREAD IS NECESSARY BECAUSE IT WONT BE WORK IN ONE-SINGLE-THREAD,
    //WITHOUT ANOTHER ONE THREAD I WOULDNT MAKE ANYTHING MORE THAN LOOKING AT PROGRESSBAR
    //EVERY SECOND IT SLEEPS SO THE ONE DECREMENTATION IS ONE SECOND, EVERY ONE SECOND
    //I DECREMENT leftSeconds VARIABLE AND SAVE THIS INTO PROGRESSBAR SO IT MAKES SMALLER EVERY SECOND
    //AND I USE runOnUiThread BECAUSE IT ALLOWS ME TO CHANGE VIEW IN NEW THREAD
    private void runCounter(){
        leftSeconds = progressBar.getMax();
        leftSecondsTextView.setText(String.format("%ds.", leftSeconds));

        new Thread(new Runnable() {
            @Override
            synchronized public void run() {
                while (leftSeconds > 0 && !alreadyAnswered) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    leftSeconds--;
                    if(leftSeconds < 0) leftSeconds = 0;
                    handler.post(() -> {
                        progressBar.setProgress(leftSeconds);
                        leftSecondsTextView.setText(String.format("%ds.", leftSeconds));
                    });
                }
                runOnUiThread(() -> checkAnswerCondition());
            }
        }).start();
    }
}