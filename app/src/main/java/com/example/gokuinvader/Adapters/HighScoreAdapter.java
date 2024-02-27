package com.example.gokuinvader.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.gokuinvader.Interfaces.HighscoreCallbacks;
import com.example.gokuinvader.Models.HighScore;
import com.example.gokuinvader.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.HighScoreViewHolder> {
    public static final int HIGHSCORE_SIZE = 10;
    private SortedList<HighScore> highScoreList;
    private Context context;
    private HighscoreCallbacks highscoreCallbacks;

    public HighScoreAdapter(SortedList<HighScore> highScoreList, Context context) {
        this.highScoreList = highScoreList;
        this.context = context;
    }

    public HighScoreAdapter setHighscoreCallbacks(HighscoreCallbacks highscoreCallbacks) {
        this.highscoreCallbacks = highscoreCallbacks;
        return this;
    }

    public SortedList<HighScore> getHighScoreList() {
        return highScoreList;
    }

    @NonNull
    @Override
    public HighScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_score_item, parent, false);
        return new HighScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HighScoreViewHolder holder, int position) {
        HighScore score = getHighscore(position);
        holder.scoreView.setText(String.valueOf(score.getScore()));
        holder.nameView.setText(score.getName());
        if (highscoreCallbacks != null) {
            holder.itemView.setOnClickListener(v -> highscoreCallbacks.onLocationChange(getHighscore(holder.getAdapterPosition()).getPostion(),score.getName()));
        }
    }

    private HighScore getHighscore(int position) {
        return highScoreList.get(position);
    }

    @Override
    public int getItemCount() {
        if (highScoreList != null) {
            return Math.min(highScoreList.size(), HIGHSCORE_SIZE);
        }
        return 0;
    }

    public class HighScoreViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView nameView;
        private MaterialTextView scoreView;

        public HighScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.highscore_name);
            scoreView = itemView.findViewById(R.id.highscore_score);

        }
    }
}

