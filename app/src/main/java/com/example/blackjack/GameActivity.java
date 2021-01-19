package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameActivity extends AppCompatActivity {

    private TextView moneyTV;
    private TextView dealerHandTV;
    private TextView playerHandTV;

    private EditText betAmountET;

    private Button betButton;
    private Button hitButton;
    private Button stayButton;
    private Button submitScoreButton;
    private Button backButton;

    private Integer money;
    private Integer betAmount;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;
    /* states:
        0 game not started
        1 game started - player turn
        2 game started - dealer turn
        3 game ended
     */
    private Integer state;

    BlackjackGame blackjackGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeViews();
        setOnClickListeners();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        money = 500;
        state = 0;

        updateTextViews();
    }

    public void initializeViews() {
        moneyTV = findViewById(R.id.moneyTV);
        dealerHandTV = findViewById(R.id.dealerCardsTV);
        playerHandTV = findViewById(R.id.playerCardsTV);
        betAmountET = findViewById(R.id.betValueET);
        betButton = findViewById(R.id.betButton);
        hitButton = findViewById(R.id.hitButton);
        stayButton = findViewById(R.id.stayButton);
        submitScoreButton = findViewById(R.id.submitScoreButton);
        backButton = findViewById(R.id.backButton);
    }

    public void setOnClickListeners() {
        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 0) {
                    betFunction();
                }
            }
        });
        hitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    hitFunction();
                }
            }
        });
        stayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) {
                    stayFunction();
                }
            }
        });
        submitScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitScoreFunction();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                backFunction();
            }
        });
    }


    public void submitScoreFunction() {


        User user = new User(mAuth.getCurrentUser().getEmail(),money);
        String username = getUsernameFromEmail(user.username);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(username).setValue(user);
    }

    public String getUsernameFromEmail(String email) {
        String username = "";
        for(int index=0;index<email.length();index++) {
            if(email.charAt(index) == '@') {
                break;
            } else {
                username+=email.charAt(index);
            }
        }
        return username;
    }

    public void backFunction(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void betFunction() {
        if (betAmountET.getText().toString().isEmpty()) {
            Toast.makeText(GameActivity.this, "Please enter a bet value.", Toast.LENGTH_SHORT).show();
        }
        else {
            betAmount = Integer.parseInt(betAmountET.getText().toString());
            if (betAmount <= money) {
                money -= betAmount;
                blackjackGame = new BlackjackGame();
                dealerHandTV.setVisibility(View.VISIBLE);
                playerHandTV.setVisibility(View.VISIBLE);
                hitButton.setVisibility(View.VISIBLE);
                stayButton.setVisibility(View.VISIBLE);
                betButton.setVisibility(View.INVISIBLE);
                betAmountET.setVisibility(View.INVISIBLE);
                state = 1;

                updateTextViews();

            } else {
                Toast.makeText(GameActivity.this, "Please enter a bet value that you can afford.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void hitFunction() {
        if (blackjackGame.checkIfHandBust(blackjackGame.getPlayerHand()) == true) {
            Toast.makeText(GameActivity.this, "You are bust. Unable to hit.", Toast.LENGTH_SHORT).show();
            gameEndFunction();
        } else {
            blackjackGame.playerHit();
            if (blackjackGame.checkIfHandBust(blackjackGame.getPlayerHand()) == true) {
                gameEndFunction();
                updateTextViews();
            }
            updateTextViews();
        }
    }

    public void stayFunction() {
        state = 2;
        dealerFunction();
    }

    public void dealerFunction() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (state == 2) {
                    if (blackjackGame.dealerDraw() == false) {
                        state = 3;
                        updateTextViews();
                        gameEndFunction();
                    } else {
                        updateTextViews();
                        dealerFunction();
                    }
                }
            }
        }, 500);
    }

    public void gameEndFunction() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                state = 3;

                if (blackjackGame.checkIfHandBust(blackjackGame.getPlayerHand()) == true) {
                    Toast.makeText(GameActivity.this, "Dealer won.", Toast.LENGTH_SHORT).show();
                    updateTextViews();
                } else if (blackjackGame.checkIfHandBust(blackjackGame.getDealerHand()) == true) {
                    Toast.makeText(GameActivity.this, "You won.", Toast.LENGTH_SHORT).show();
                    money += 2*betAmount;
                    updateTextViews();
                } else if (blackjackGame.calculateHandSum(blackjackGame.getPlayerHand()) == blackjackGame.calculateHandSum(blackjackGame.getDealerHand())) {
                    Toast.makeText(GameActivity.this, "Push.", Toast.LENGTH_SHORT).show();
                    money += betAmount;
                    updateTextViews();
                } else if (blackjackGame.calculateHandSum(blackjackGame.getPlayerHand()) >= blackjackGame.calculateHandSum(blackjackGame.getDealerHand())) {
                    Toast.makeText(GameActivity.this, "You won.", Toast.LENGTH_SHORT).show();
                    money += 2*betAmount;
                    updateTextViews();
                } else {
                    Toast.makeText(GameActivity.this, "Dealer won.", Toast.LENGTH_SHORT).show();
                    updateTextViews();
                }
                restartGameFunction();
            }
        }, 1000);
    }

    public void restartGameFunction() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                state = 0;
                dealerHandTV.setVisibility(View.INVISIBLE);
                playerHandTV.setVisibility(View.INVISIBLE);
                hitButton.setVisibility(View.INVISIBLE);
                stayButton.setVisibility(View.INVISIBLE);
                betButton.setVisibility(View.VISIBLE);
                betAmountET.setVisibility(View.VISIBLE);
                updateTextViews();
            }
        }, 1000);
    }

    public void updateTextViews() {
        String dealerHandString = "Dealer cards:\n";
        String playerHandString = "Your cards:\n";
        moneyTV.setText("Money: " + money.toString());
        if (state == 1) {
            dealerHandString += "?(?), ";
            for (Card c : blackjackGame.getPlayerHand().getCards()) {
                playerHandString += c.getValueString() + "(" + c.getSuit() + "), ";
            }
            for (int index = 1; index < blackjackGame.getDealerHand().getCards().size(); index ++ ) {
                dealerHandString += blackjackGame.getDealerHand().getCards().get(index).getValueString() + "(" + blackjackGame.getDealerHand().getCards().get(index).getSuit() + "), ";
            }
            dealerHandTV.setText(dealerHandString);
            playerHandTV.setText(playerHandString);
        } else if (state == 2 || state == 3) {
            for (Card c : blackjackGame.getDealerHand().getCards()) {
                dealerHandString += c.getValueString() + "(" + c.getSuit() + "), ";
            }
            for (Card c : blackjackGame.getPlayerHand().getCards()) {
                playerHandString += c.getValueString() + "(" + c.getSuit() + "), ";
            }
            dealerHandTV.setText(dealerHandString);
            playerHandTV.setText(playerHandString);
        }
    }

}