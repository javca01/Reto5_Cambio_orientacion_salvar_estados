package co.edu.unal.prueba_app_8;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.media.MediaPlayer;

public class BoardView extends View {
    private static final int GRID_WIDTH = 6;
    private Bitmap mHumanBitmap;
    private Bitmap mComputerBitmap;
    private Paint mPaint;
    private Rect mCellRect;
    private TicTacToeGame mGame;

    private MediaPlayer mHumanSound;
    private MediaPlayer mComputerSound;

    private OnCellClickListener mListener;

    public interface OnCellClickListener {
        void onCellClick(int row, int col);
    }

    public void setOnCellClickListener(OnCellClickListener listener) {
        mListener = listener;
    }

    public BoardView(Context context) {
        super(context);
        initialize(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHumanBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.x_img);
        mComputerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.o_img);
        mCellRect = new Rect();

        // Initialize MediaPlayer for sounds
        mHumanSound = MediaPlayer.create(context, R.raw.swish); // Replace with your actual sound file
        mComputerSound = MediaPlayer.create(context, R.raw.sword); // Replace with your actual sound file
    }

    public void setGame(TicTacToeGame game) {
        mGame = game;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int boardWidth = getWidth();
        int boardHeight = getHeight();
        int cellWidth = boardWidth / 3;
        int cellHeight = boardHeight / 3;

        // Draw grid lines
        mPaint.setColor(Color.LTGRAY);
        mPaint.setStrokeWidth(GRID_WIDTH);
        canvas.drawLine(cellWidth, 0, cellWidth, boardHeight, mPaint);
        canvas.drawLine(cellWidth * 2, 0, cellWidth * 2, boardHeight, mPaint);
        canvas.drawLine(0, cellHeight, boardWidth, cellHeight, mPaint);
        canvas.drawLine(0, cellHeight * 2, boardWidth, cellHeight * 2, mPaint);

        // Draw X and O images
        for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++) {
            int col = i % 3;
            int row = i / 3;

            int left = col * cellWidth + GRID_WIDTH;
            int top = row * cellHeight + GRID_WIDTH;
            int right = (col + 1) * cellWidth - GRID_WIDTH;
            int bottom = (row + 1) * cellHeight - GRID_WIDTH;

            mCellRect.set(left, top, right, bottom);

            if (mGame != null && mGame.getBoardOccupant(i) == TicTacToeGame.HUMAN_PLAYER) {
                canvas.drawBitmap(mHumanBitmap, null, mCellRect, null);
            } else if (mGame != null && mGame.getBoardOccupant(i) == TicTacToeGame.COMPUTER_PLAYER) {
                canvas.drawBitmap(mComputerBitmap, null, mCellRect, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int cellWidth = getWidth() / 3;
            int cellHeight = getHeight() / 3;

            int col = (int) (event.getX() / cellWidth);
            int row = (int) (event.getY() / cellHeight);

            if (mListener != null) {
                mListener.onCellClick(row, col);

                // Play human sound when tapped
                if (mHumanSound != null) {
                    mHumanSound.start();
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // Release MediaPlayer resources
        if (mHumanSound != null) {
            mHumanSound.release();
            mHumanSound = null;
        }
        if (mComputerSound != null) {
            mComputerSound.release();
            mComputerSound = null;
        }
    }
}
