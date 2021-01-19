package com.example.blackjack.scoreList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blackjack.R;
import com.example.blackjack.User;

import java.util.List;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListViewHolder> {

    private List<User> choicesList;
    private Context context;

    @NonNull
    @Override
    public ScoreListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row_list, parent, false);
        ScoreListViewHolder viewHolder = new ScoreListViewHolder(contactView);
        return viewHolder;
    }

    public ScoreListAdapter(List<User> list) {
        this.choicesList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreListViewHolder holder, int position) {
        final User user = choicesList.get(position);
        holder.setValues(user.getUsername(),user.getScore());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return choicesList.size();
    }
}
