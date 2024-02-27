package com.example.gokuinvader.Logic;

import androidx.recyclerview.widget.SortedList;

import com.example.gokuinvader.Models.HighScore;

public class SortedListComperator extends SortedList.Callback<HighScore> {
    public SortedListComperator() {
    }

    @Override
    public int compare(HighScore o1, HighScore o2) {
        return o2.compareTo(o1);
    }

    @Override
    public void onChanged(int position, int count) {

    }

    @Override
    public boolean areContentsTheSame(HighScore oldItem, HighScore newItem) {
        return false;
    }

    @Override
    public boolean areItemsTheSame(HighScore item1, HighScore item2) {
        return false;
    }

    @Override
    public void onInserted(int position, int count) {

    }

    @Override
    public void onRemoved(int position, int count) {

    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {

    }
}
