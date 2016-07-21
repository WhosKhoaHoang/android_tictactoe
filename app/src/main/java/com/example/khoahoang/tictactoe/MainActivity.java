package com.example.khoahoang.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;


//TODO: Find way to shut down app if player doesn't want to play again!
public class MainActivity extends AppCompatActivity {
    int gameState[] = { 2, 2, 2, 2, 2, 2, 2, 2, 2 }; //2 represents an empty cell
    int activePlayer = 0; //0 for O and 1 for X
    int numPieces = 0;
    boolean gameActive = true;
    int[][] winningArrangements = { {0,1,2}, {3,4,5}, {6,7,8},     //Horizontals
                                    {0,3,6}, {1,4,7}, {2,5,8},     //Verticals
                                    {0,4,8}, {2,4,6} };            //Diagonals

    public void drop(View view) {
        if (gameActive) {
            ImageView piece = (ImageView) view;
            int cellNum = Integer.parseInt(piece.getTag().toString());
            if (gameState[cellNum] == 2) {
                //piece.setTranslationY(-500f); //lift piece off board (for connect-N)
                gameState[cellNum] = activePlayer;
                //Set the piece color
                if (activePlayer == 1) {
                    piece.setImageResource(R.drawable.x);
                    activePlayer = 0;
                } else {
                    piece.setImageResource(R.drawable.o);
                    activePlayer = 1;
                }
                //piece.animate().translationYBy(500f).setDuration(300); //drop piece (for connect-N)
                numPieces++;

                int gameWinner = checkGameActive();
                if (gameWinner >= 0) {
                    TextView resultText = (TextView)findViewById(R.id.resultText);
                    LinearLayout resultPopUp = (LinearLayout)findViewById(R.id.resultPopUp);

                    if (gameWinner == 2) {
                        resultText.setText("Draw game!");
                    }
                    else {
                        String winner = "X";
                        if (gameWinner == 0) { winner = "O"; }
                        resultText.setText(winner + " wins!");
                    }

                    resultPopUp.setVisibility(View.VISIBLE);
                    gameActive = false;
                }
            }
        }
    }


    public void yesPlayAgain(View view) {
        LinearLayout resultPopUp = (LinearLayout)findViewById(R.id.resultPopUp);
        GridLayout gameBoard = (GridLayout)findViewById(R.id.gameBoard);

        for (int i = 0; i < gameState.length; i++) { gameState[i] = 2; }
        activePlayer = 0;
        numPieces = 0;
        gameActive = true;

        for (int i = 0; i < gameBoard.getChildCount(); i++) {
            ImageView curPiece = (ImageView)gameBoard.getChildAt(i);
            curPiece.setImageResource(0); //Setting the source for an ImageView to nothing
        }
        resultPopUp.setVisibility(View.INVISIBLE);
    }


    //Hmm, this code doesn't seem to actually shut down the app though!!
    public void noPlayAgain(View view) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //========= HELPER FUNCTIONS =========
    /**
     Checks if non-empty positions in the gameState correspond to a winning arrangement.
     @return 0 (for a winner of O), 1 (for a winner of X), 2 for draw, or -1 for the game continuing.
     */
    private int checkGameActive() {
        for (int[] winningArr : winningArrangements) {
            if (gameState[winningArr[0]] == gameState[winningArr[1]] &&
                    gameState[winningArr[1]] == gameState[winningArr[2]] &&
                    gameState[winningArr[0]] != 2) {  //Could have checked any element in winningArr here
                return gameState[winningArr[0]]; //Could have returned any element in winningAr
            }
        }

        if (numPieces == 9) { return 2; }

        return -1;
    }
}
