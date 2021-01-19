package com.example.blackjack.scoreList;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackjack.R;

public class ScoreListViewHolder extends RecyclerView.ViewHolder {

    private TextView username;
    private TextView score;

    public ScoreListViewHolder(@NonNull View itemView) {
        super(itemView);
        initializeViews();
    }

    private void initializeViews() {
        username = itemView.findViewById(R.id.tv_row_username);
        score = itemView.findViewById(R.id.tv_row_score);
    }

    public void setValues(String username, Integer score) {
        this.username.setText(username);
        this.score.setText(score.toString());
    }
}
