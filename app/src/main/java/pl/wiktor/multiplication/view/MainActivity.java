package pl.wiktor.multiplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.wiktor.multiplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LOAD VIEW FROM LAYOUT
        setContentView(R.layout.activity_main);

        //GET BUTTONS FROM ID
        Button startSession = findViewById(R.id.startSessionButton);
        Button history = findViewById(R.id.history);

        //CREATE INTENT TO START NEW ACTIVITY WHEN NEEDED
        final Intent sessionIntent = new Intent(this, SessionActivity.class);
        final Intent historyIntent = new Intent(this, HistoryActivity.class);

        //ON BUTTON CLICK CREATE NEW ACTIVITY
        startSession.setOnClickListener(v -> startActivity(sessionIntent));
        history.setOnClickListener(v -> startActivity(historyIntent));
    }
}