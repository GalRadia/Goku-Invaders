package com.example.gokuinvader.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gokuinvader.Adapters.HighScoreAdapter;
import com.example.gokuinvader.Interfaces.HighscoreCallbacks;
import com.example.gokuinvader.Logic.SortedListComperator;
import com.example.gokuinvader.Models.HighScore;
import com.example.gokuinvader.R;
import com.example.gokuinvader.Utils.SharedPreferencesManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecyclerViewFragment extends Fragment {
    SortedListComperator sortedListComperator = new SortedListComperator();
    RecyclerView highscoreRecyclerView;

    HighscoreCallbacks highscoreCallbacks;
    private SortedList<HighScore> highScoreList = new SortedList<>(HighScore.class, sortedListComperator);

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    public void setCallbacks(HighscoreCallbacks callbacks) {
        highscoreCallbacks = callbacks;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initScoreList() {
        List<HighScore> scores = SharedPreferencesManager.getInstance().getHighscoreList();
        scores.forEach(highScore -> highScoreList.add(highScore));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initScoreList();
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        HighScoreAdapter highScoreAdapter = new HighScoreAdapter(highScoreList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        highscoreRecyclerView = view.findViewById(R.id.frg_LST_highscores);
        highscoreRecyclerView.setLayoutManager(linearLayoutManager);
        highscoreRecyclerView.setAdapter(highScoreAdapter);
        highScoreAdapter.setHighscoreCallbacks((position,name) -> highscoreCallbacks.onLocationChange(position,name));
        return view;
    }
}