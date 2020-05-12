package com.example.rockpaperscissors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartingActivity extends AppCompatActivity {

    private Button startingButton; // κουμπί "START"
    private EditText inputText;  // γραμμή εισόδου για το όνομα χρήστη

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        loadComponents();
        loadValues();
    }

    /*
      Συνάρτηση για την φόρτωση των "συστατικών" σύμφωνα με το id τους.
      Καλείται από την oncreate().
       */
    private void loadComponents() {

        startingButton = (Button) findViewById(R.id.startingButton);
        startingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startClicked();
            }
        });
        inputText = (EditText) findViewById(R.id.input);
    }

    /*
       Μέθοδος η οποία φορτώνει τις κατάλληλες τιμές στα στοιχεία της Δραστηριότητας
       Καλείται απο την μέθοδο OnCreate().
        */
    private void loadValues() {

        startingButton.setText(R.string.Start);

    }


    /*
    Η μέθοδος αυτη καλείται απο τον OnClickListener και υλοποιεί την εκκίνηση
    της επόμενης δραστηριότητας.
     */
    private void startClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nameOfUser", inputText.getText().toString()); // περνάμε το όνομα του user στην επόμενη δραστηριότητα
        startActivity(intent);
    }

}
