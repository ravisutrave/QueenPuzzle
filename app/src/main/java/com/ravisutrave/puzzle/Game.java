package com.ravisutrave.puzzle;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class Game extends ActionBarActivity {
    board puzBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puzBoard = new board(this);
        setContentView(puzBoard);
    }


}
