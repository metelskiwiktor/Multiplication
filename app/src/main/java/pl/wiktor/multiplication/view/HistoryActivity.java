package pl.wiktor.multiplication.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import pl.wiktor.multiplication.R;
import pl.wiktor.multiplication.Singleton;
import pl.wiktor.multiplication.model.Question;
import pl.wiktor.multiplication.service.SessionService;

public class HistoryActivity extends AppCompatActivity {
    private final SessionService sessionService = Singleton.SESSION_SERVICE.getSessionService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SET VIEW BY LAYOUT
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //GET BUTTON TO MENU BACK FROM ID
        Button backToMenu = findViewById(R.id.historyBackToMenuButton);

        //USE finish() ON BUTTON CLICK TO FINISH CURRENT ACTIVITY AND BACK TO MENU
        backToMenu.setOnClickListener(v -> stopActivity());

        //GET LISTVIEW FROM ID
        ListView list = (ListView) findViewById(R.id.historyList);

        //MAP ALL OF DATA TO READABLE LIST OF STRINGS
        @SuppressLint("DefaultLocale") List<String> history = sessionService.getAllSessions().stream()
                .map(session -> {
                    StringBuilder section = new StringBuilder(String.format("%s\nPoprawnie udzielone odpowiedzi: %d/%d\n",
                            session.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.mm.yyyy HH:mm:ss")),
                            session.getCorrectAnswered(), session.getAllQuestions()));
                    for(Question question: session.getQuestions()){
                        section.append(String.format("%d*%d=%d, odpowiedziano: %d, %s\n",
                                question.getFirstNumber(), question.getSecondNumber(),
                                question.getCorrectNumber(), question.getAnsweredNumber(),
                                question.isCorrectAnswered() ? "POPRAWNIE" : " NIEPOPRAWNIE"));
                    }
                    return section.toString();
                })
                .collect(Collectors.toList());

        //IF THERE IS NO DATA SAY THAT THERE IS NO DATA
        if(history.size() == 0){
            history.add("Brak danych. Poucz sie i wtedy sprawdź.");
        }

        //USE ADAPTER TO LOAD ALL DATA
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row, history);

        //SET ADAPTER TO LISTVIEW
        list.setAdapter(adapter);
    }

    //METHOD TO FINISH CURRENT ACTIVITY LIKE I SAID PREVIOUSLY
    private void stopActivity() {
        finish();
    }

}