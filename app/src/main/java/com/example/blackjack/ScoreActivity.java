package com.example.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackjack.scoreList.ScoreListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private Button buttonBack;

    private RecyclerView scoreListRV;
    private ScoreListAdapter scoreListAdapter;
    private List<User> scoreList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreListRV = findViewById(R.id.scoreListRV);
        scoreList = new ArrayList<>();

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.child("users").getChildren();
                for (DataSnapshot user : children) {

                    String username = getUsernameFromEmail(user.getValue(User.class).getUsername());
                    Integer score = user.getValue(User.class).getScore();
                    scoreList.add(new User(username,score));
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Eroare la scrierea scorului");
            }
        });
    }

    public void backToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

    private void setRecyclerView() {
        Collections.sort(scoreList, new SortByScore());
        scoreListAdapter = new ScoreListAdapter(scoreList);
        scoreListRV.setLayoutManager(new LinearLayoutManager(this));
        scoreListRV.setAdapter(scoreListAdapter);
    }


}
