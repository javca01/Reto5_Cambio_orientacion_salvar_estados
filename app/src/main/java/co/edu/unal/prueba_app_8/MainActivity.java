package co.edu.unal.prueba_app_8;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TicTacToeGame mGame;
    private BoardView mBoardView;
    private TextView mStatusTextView;
    private boolean mGameOver;
    private boolean mHumanFirst = true;

    private int mHumanScore = 0;
    private int mComputerScore = 0;
    private int mTieScore = 0;

    //private int mHumanWins = 0;
    //private int mAndroidWins = 0;
    //private int mTies = 0;

    private TextView mHumanScoreTextView;
    private TextView mAndroidScoreTextView;
    private TextView mTiesScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGame = new TicTacToeGame();

        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);

        mStatusTextView = findViewById(R.id.status);

        mHumanScoreTextView = findViewById(R.id.human_score);
        mAndroidScoreTextView = findViewById(R.id.android_score);
        mTiesScoreTextView = findViewById(R.id.ties_score);

        Button newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(view -> startNewGame());

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            startNewGame();
        }

        mBoardView.setOnCellClickListener((row, col) -> onCellClicked(row, col));
    }

    private void startNewGame() {
        mGame.clearBoard();
        mBoardView.invalidate();
        mGameOver = false;

        if (mHumanFirst) {
            mStatusTextView.setText(R.string.first_human);
        } else {
            mStatusTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }

        mHumanFirst = !mHumanFirst;
        mBoardView.invalidate();
    }

    public void onCellClicked(int row, int col) {
        if (mGameOver) return;

        int location = row * 3 + col;
        if (mGame.getBoardOccupant(location) == TicTacToeGame.EMPTY) {
            mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location);
            mBoardView.invalidate();

            if (checkGameOver()) return;

            int computerMove = mGame.getComputerMove();
            mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, computerMove);
            mBoardView.invalidate();

            checkGameOver();
        }
    }

    private boolean checkGameOver() {
        int winner = mGame.checkForWinner();
        if (winner == 0) {
            mStatusTextView.setText(mGame.getCurrentPlayer() == TicTacToeGame.HUMAN_PLAYER ?
                    R.string.turn_human : R.string.turn_computer);
            return false;
        }

        mGameOver = true;

        switch (winner) {
            case 1:
                mStatusTextView.setText(R.string.tie);
                mTieScore++;
                break;
            case 2:
                mStatusTextView.setText(R.string.you_win);
                mHumanScore++;
                break;
            case 3:
                mStatusTextView.setText(R.string.android_wins);
                mComputerScore++;
                break;
        }

        updateScores();
        return true;
    }

    private void updateScores() {
        // Update score display logic if you have dedicated TextViews
        mHumanScoreTextView.setText("Human: " + mHumanScore);
        mAndroidScoreTextView.setText("Android: " + mComputerScore);
        mTiesScoreTextView.setText("Ties: " + mTieScore);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("board", mGame.getBoard());
        outState.putInt("humanScore", mHumanScore);
        outState.putInt("computerScore", mComputerScore);
        outState.putInt("tieScore", mTieScore);
        outState.putBoolean("gameOver", mGameOver);
        outState.putBoolean("humanFirst", mHumanFirst);
    }

    private void restoreState(Bundle savedInstanceState) {
        mGame.setBoard(savedInstanceState.getIntArray("board"));
        mHumanScore = savedInstanceState.getInt("humanScore");
        mComputerScore = savedInstanceState.getInt("computerScore");
        mTieScore = savedInstanceState.getInt("tieScore");
        mGameOver = savedInstanceState.getBoolean("gameOver");
        mHumanFirst = savedInstanceState.getBoolean("humanFirst");

        updateScores();
        mBoardView.invalidate();
    }
}
