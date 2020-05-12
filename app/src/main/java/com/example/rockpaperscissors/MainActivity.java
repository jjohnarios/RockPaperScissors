package com.example.rockpaperscissors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.util.Random;

import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {


    private MediaPlayer scissorsSound; // ήχος κατά το πάτημα του κουμπιού "Scissors"
    private MediaPlayer rockSound; // ήχος κατά το πάτημα του κουμπιού "Rock"
    private MediaPlayer paperSound; // ήχος κατά το πάτημα του κουμπιού "Paper"

    private Button rockButton; // κουμπί "Πέτρας"
    private Button paperButton; // κουμπί "Χαρτιού"
    private Button scissorsButton; // κουμπί "Ψαλιδιού"

    private TextView score; //σκορ παιχνιδιού
    private TextView winnerMsg; //μήνυμα για το ποιος ειναι νικητής
    private TextView nameOfUser; // πεδίο για το όνομα του χρήστη
    private TextView nameOfBot; // πεδίο για το όνομα του bot

    private ImageView userImg; // εικόνα για την κίνηση του User
    private ImageView botImg; // εικόνα για την κίνηση του Bot

    private String username; //όνομα χρήστη(default name:"User")
    private int userScore = 0, botScore = 0;

    private TextView userTextMove; // κίνηση του User - λεκτική ετικέτα
    private TextView botTextMove; // κίνηση του Bot - λεκτική ετικέτα

    private int currentWinner; // τρέχων νικητής
    private int currentUserMove; // τρέχων κίνηση User
    private int currentBotMove; // τρέχων κίνηση Bot


    private Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponents();


        if (savedInstanceState == null) {
            loadValues();
        }

    }

    /*
    Συνάρτηση που διασφαλίζει να μην χανονται οι τρέχουσες πληροφορίες της δραστηριότητας
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        savedInstanceState.putInt("userScore", userScore);
        savedInstanceState.putInt("botScore", botScore);
        savedInstanceState.putString("userName", username);
        savedInstanceState.putInt("winner", currentWinner);
        savedInstanceState.putInt("botmove", currentBotMove);
        savedInstanceState.putInt("usermove", currentUserMove);

    }

    /*
        Συνάρτηση που διασφαλίζει το φόρτωμα των πληροφοριών οι οποίες
        σώθηκαν μέσω της μεθόδου  onSaveInstanceState()
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        userScore = savedInstanceState.getInt("userScore");
        botScore = savedInstanceState.getInt("botScore");
        currentWinner = savedInstanceState.getInt("winner");
        currentBotMove = savedInstanceState.getInt("botmove");
        currentUserMove = savedInstanceState.getInt("usermove");
        username = savedInstanceState.getString("userName");


        rockButton.setText(getResources().getString(R.string.Rock));
        paperButton.setText(getResources().getString(R.string.Paper));
        scissorsButton.setText(getResources().getString(R.string.Scissors));
        nameOfUser.setText(username);
        nameOfBot.setText(getResources().getString(R.string.Bot));


        // Αν είναι 0-0 δεν εμφανίζει κάτι στο rotate.Όμως αν η 1η (και n-πρωτες συννεχομενες) φορά είναι ισοπαλία τοτε θελω να εμφανιζει
        if ((userScore != 0 || botScore != 0) || (currentBotMove != -1 && currentUserMove != -1)) {

            // To print the winner before the rotate
            if (currentWinner == 0) {
                winnerMsg.setText(getResources().getString(R.string.Draw));
            } else if (currentWinner == 1) {
                winnerMsg.setText(getResources().getString(R.string.Winner) + username);
            } else if (currentWinner == -1) {
                winnerMsg.setText(getResources().getString(R.string.Winner) + getResources().getString(R.string.Bot));
            }
            score.setText(userScore + " : " + botScore);

            //To print the images of moves
            if (currentBotMove >= 0 && currentBotMove <= 2) {

                if (currentBotMove == 0) {
                    botImg.setImageResource(R.drawable.rock_img);
                    botTextMove.setText(getResources().getString(R.string.Rock));
                } else if (currentBotMove == 1) {
                    botImg.setImageResource(R.drawable.paper_img);
                    botTextMove.setText(getResources().getString(R.string.Paper));
                } else if (currentBotMove == 2) {
                    botImg.setImageResource(R.drawable.scissors_img);
                    botTextMove.setText(getResources().getString(R.string.Scissors));
                }

                if (currentUserMove == 0) {
                    userImg.setImageResource(R.drawable.rock_img);
                    userTextMove.setText(getResources().getString(R.string.Rock));
                } else if (currentUserMove == 1) {
                    userImg.setImageResource(R.drawable.paper_img);
                    userTextMove.setText(getResources().getString(R.string.Paper));
                } else if (currentUserMove == 2) {
                    userImg.setImageResource(R.drawable.scissors_img);
                    userTextMove.setText(getResources().getString(R.string.Scissors));
                }

            }
        } else {
            score.setText(userScore + " : " + botScore);
        }

    }

    /*
    Συνάρτηση για την φόρτωση των "συστατικών" σύμφωνα με το id τους.
    Καλείται από την oncreate().
     */
    private void loadComponents() {
        rockButton = (Button) findViewById(R.id.RockButton);
        paperButton = (Button) findViewById(R.id.PaperButton);
        scissorsButton = (Button) findViewById(R.id.ScissorsButton);


        score = (TextView) findViewById(R.id.scoreText);
        winnerMsg = (TextView) findViewById(R.id.winner);
        nameOfUser = (TextView) findViewById(R.id.userName);
        nameOfBot = (TextView) findViewById(R.id.botName);
        userTextMove = (TextView) findViewById(R.id.userMoveText);
        botTextMove = (TextView) findViewById(R.id.botMoveText);

        userImg = (ImageView) findViewById(R.id.userMoveImage);
        botImg = (ImageView) findViewById(R.id.botMoveImage);

        rand = new Random();


        rockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rockClicked();
            }
        });

        paperButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                paperClicked();
            }
        });

        scissorsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                scissorsClicked();
            }
        });


    }

    /*
    @param name όνομα χρήστη
     Μέθοδος η οποια αρχικοποιεί την μεταβλητή με το όνομα του Χρήστη
     */
    private void setUsername(String name) {
        if (name.equals("")) {
            username = getResources().getString(R.string.DefaultName);
        } else {
            username = name;
        }

    }

    /*
    Μέθοδος η οποία φορτώνει τις κατάλληλες τιμές στα στοιχεία της Δραστηριότητας
    Καλείται απο την μέθοδο OnCreate() μόνο την πρώτη φορά που φορτώνεται η εφαρμογή , |
    δηλαδή δεν καλείται στην περιστροφής της οθόνης καθώς τα στοιχεία σε αυτήν την περίπτωση
    λαμβάνουν τις τιμές τους απο την μέθοδο onRestoreInstanceState()
     */
    private void loadValues() {

        Intent intent = getIntent();
        String nametemp = intent.getStringExtra("nameOfUser");

        setUsername(nametemp);
        userScore = 0;
        botScore = 0;


        rockButton.setText(getResources().getString(R.string.Rock));
        paperButton.setText(getResources().getString(R.string.Paper));
        scissorsButton.setText(getResources().getString(R.string.Scissors));
        nameOfUser.setText(username);
        nameOfBot.setText(getResources().getString(R.string.Bot));

        currentBotMove = -1; // indicates that Bot hasn't made a move yet
        currentWinner = -1; // indicates that User hasn't made a move yet


        score.setText(userScore + " : " + botScore);

    }

    /*
    Αυτή η μέθοδος είναι υπεύθυνη για την επιλογή του παιξίματος απο το bot
    καθώς και για το φόρτωμα της κατάλληλης εικόνας και λεκτικού .
     */
    private int randomChoice() {

        int choice = rand.nextInt(3);
        if (choice == 0) {
            botImg.setImageResource(R.drawable.rock_img);
            botTextMove.setText(getResources().getString(R.string.Rock));
        } else if (choice == 1) {
            botImg.setImageResource(R.drawable.paper_img);
            botTextMove.setText(getResources().getString(R.string.Paper));

        } else {
            botImg.setImageResource(R.drawable.scissors_img);
            botTextMove.setText(getResources().getString(R.string.Scissors));

        }

        return choice;
    }

    /*
    Η μέθοδος αυτη καλείται απο τον OnClickListener και υλοποιεί το παίξιμο του
    χρήστη όταν αυτός πατάει το κουμπί "Rock"
    Αναλαμβάνει να παίξει τον κατάλληλο ήχο , να φορτώσει την αντίστοιχη εικόνα και λεκτική περιγραφή.
     */
    public void rockClicked() {
        if (rockSound == null) {
            rockSound = MediaPlayer.create(this, R.raw.rock);
        }
        rockSound.start();
        userTextMove.setText(getResources().getString(R.string.Rock));
        userImg.setImageResource(R.drawable.rock_img);
        manageResult(findResult(0, randomChoice()));
    }

    /*
       Η μέθοδος αυτη καλείται απο τον OnClickListener και υλοποιεί το παίξιμο του
       χρήστη όταν αυτός πατάει το κουμπί "Paper"
       Αναλαμβάνει να παίξει τον κατάλληλο ήχο , να φορτώσει την αντίστοιχη εικόνα και λεκτική περιγραφή.
     */
    public void paperClicked() {
        if (paperSound == null) {
            paperSound = MediaPlayer.create(this, R.raw.paper);
        }
        paperSound.start();
        userTextMove.setText(getResources().getString(R.string.Paper));
        userImg.setImageResource(R.drawable.paper_img);
        manageResult(findResult(1, randomChoice()));
    }

    /*
       Η μέθοδος αυτη καλείται απο τον OnClickListener και υλοποιεί το παίξιμο του
       χρήστη όταν αυτός πατάει το κουμπί "Scissors"
       Αναλαμβάνει να παίξει τον κατάλληλο ήχο , να φορτώσει την αντίστοιχη εικόνα και λεκτική περιγραφή.
      */
    public void scissorsClicked() {
        if (scissorsSound == null) {
            scissorsSound = MediaPlayer.create(this, R.raw.scissorssound);
        }
        scissorsSound.start();
        userTextMove.setText(getResources().getString(R.string.Scissors));
        userImg.setImageResource(R.drawable.scissors_img);
        manageResult(findResult(2, randomChoice()));
    }

    /*
    @param move1 κίνηση bot
    @param move2 κίνηση παίκτη
     Αυτή η μέθοδος δέχεται ως όρισμα τις κινήσεις του Χρήστη και του bot
     ( 0 για πέτρα , 1 για χαρτί , 2 για ψαλίδι )
     και επιστρεφει το αποτέλεσμα ( 0 -> ισοπαλία ,  1 -> νίκη για τον Χρήστη , -1 ήττα για τον Χρήστη )
     */
    private int findResult(int move1, int move2) {
        currentBotMove = move2;
        currentUserMove = move1;
        if (move1 == move2) {
            return 0; //Draw
        }
        // Αν ο χρήστης επιλέξει την κίνηση μειων ενα απο την κίνηση του bot χανει
        // η μειων ενα της πετρας θεωρείται το ψαλίδι
        if ((move1 + 1) % 3 == move2) {
            return -1; // User Loses
        }
        return 1; // User Wins
    }

    /*
     Δέχεται ως όρισμα το αποτέλεσμα ( -1,0,1)
     και αναλαμβάνει να υπολογίσει το σκορ και να εμφανίσει το κατάλληλο μήνυμα.
     */
    private void manageResult(int result) {
        currentWinner = result;
        if (result == 0) {
            winnerMsg.setText(getResources().getString(R.string.Draw));
        } else if (result == 1) {
            winnerMsg.setText(getResources().getString(R.string.Winner) + username);
            userScore++;
        } else if (result == -1) {
            winnerMsg.setText(getResources().getString(R.string.Winner) + getResources().getString(R.string.Bot));
            botScore++;
        }
        score.setText(userScore + " : " + botScore);
    }
}
