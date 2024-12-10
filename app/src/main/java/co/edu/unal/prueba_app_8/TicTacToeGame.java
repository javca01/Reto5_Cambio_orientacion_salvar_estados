package co.edu.unal.prueba_app_8;

import java.util.Random;


public class TicTacToeGame {
    public static final int HUMAN_PLAYER = 1;
    public static final int COMPUTER_PLAYER = 2;
    public static final int EMPTY = 0;
    public static final int BOARD_SIZE = 9;
    private int[] mBoard;
    private int mCurrentPlayer;
    private Random mRand;
    private TicTacToeGame mGame;
    private BoardView mBoardView;

    public TicTacToeGame() {
        mBoard = new int[BOARD_SIZE];
        mCurrentPlayer = HUMAN_PLAYER;  // Start with human player
        mRand = new Random();
        clearBoard();
    }

    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard[i] = EMPTY;
        }
    }

    public boolean setMove(int player, int pos) {
        if (pos < 0 || pos >= BOARD_SIZE || mBoard[pos] != EMPTY) {
            return false; // Invalid move
        }

        mBoard[pos] = player;

        // After a successful move, switch players
        if (player == HUMAN_PLAYER) {
            mCurrentPlayer = COMPUTER_PLAYER;
        } else {
            mCurrentPlayer = HUMAN_PLAYER;
        }

        return true;
    }

    public int getBoardOccupant(int pos) {
        return mBoard[pos];
    }

    public int getCurrentPlayer() {
        return mCurrentPlayer;
    }

    private void makeComputerMove() {
        int move = mGame.getComputerMove();
        mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        mBoardView.invalidate();
    }
    public int getComputerMove() {
        int move = getWinningMove();
        if (move != -1) {
            return move;
        }

        move = getBlockingMove();
        if (move != -1) {
            return move;
        }

        return getRandomMove(); // If no winning or blocking move, pick random
    }
    public int getRandomMove() {
        int move;
        do {
            move = mRand.nextInt(BOARD_SIZE);
        } while (mBoard[move] != EMPTY);
        return move;
    }
    public int getBlockingMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == EMPTY) {
                mBoard[i] = HUMAN_PLAYER;
                if (checkForWinner() == 2) {
                    mBoard[i] = EMPTY;
                    return i;
                }
                mBoard[i] = EMPTY;
            }
        }
        return -1;
    }
    public int getWinningMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == EMPTY) {
                mBoard[i] = COMPUTER_PLAYER;
                if (checkForWinner() == 3) {
                    mBoard[i] = EMPTY;
                    return i;
                }
                mBoard[i] = EMPTY;
            }
        }
        return -1;
    }
    public int checkForWinner() {
        // Horizontal lines
        for (int i = 0; i <= 6; i += 3) {
            if (mBoard[i] != EMPTY && mBoard[i] == mBoard[i + 1] && mBoard[i + 1] == mBoard[i + 2]) {
                return mBoard[i] == HUMAN_PLAYER ? 2 : 3;
            }
        }

        // Vertical lines
        for (int i = 0; i <= 2; i++) {
            if (mBoard[i] != EMPTY && mBoard[i] == mBoard[i + 3] && mBoard[i + 3] == mBoard[i + 6]) {
                return mBoard[i] == HUMAN_PLAYER ? 2 : 3;
            }
        }

        // Diagonal lines
        if (mBoard[0] != EMPTY && mBoard[0] == mBoard[4] && mBoard[4] == mBoard[8]) {
            return mBoard[0] == HUMAN_PLAYER ? 2 : 3;
        }
        if (mBoard[2] != EMPTY && mBoard[2] == mBoard[4] && mBoard[4] == mBoard[6]) {
            return mBoard[2] == HUMAN_PLAYER ? 2 : 3;
        }

        // Check for empty spaces
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == EMPTY) {
                return 0; // Game ongoing
            }
        }

        return 1; // Tie
    }

    public int[] getBoard() {
        return mBoard.clone();
    }

    public void setBoard(int[] board) {
        System.arraycopy(board, 0, mBoard, 0, BOARD_SIZE);
    }


}
