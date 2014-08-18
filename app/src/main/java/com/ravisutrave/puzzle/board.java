package com.ravisutrave.puzzle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by rasutrav on 7/30/2014.
 */
public class board extends View {
    Paint paint_red = new Paint();
    int viewWidth, viewHeight;
    short size_of_board;
    private Paint paint = new Paint();
    private Paint paint_green = new Paint();
    private PuzzleBoard queen_board;


    public board(Context context) {
        super(context);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(51, 0, 0));
        paint_green.setColor(Color.GREEN);
        paint_red.setColor(Color.RED);
        size_of_board = 4;
        queen_board = new PuzzleBoard(size_of_board);

    }

    public void resetboard() {
        queen_board = new PuzzleBoard(size_of_board);
        queen_board.state = BoardState.RE_INIT;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        if (xNew != yNew) {
            this.getLayoutParams().height = this.getLayoutParams().width;
            this.setLayoutParams(this.getLayoutParams());
            viewHeight = viewWidth = xNew;
        }

    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
            short x = (short) (Math.round(Math.ceil((event.getX()) * size_of_board / viewWidth)) - 1);
            short y = (short) (Math.round(Math.ceil(event.getY() * size_of_board / viewHeight)) - 1);
            if (queen_board.update_board(x, y) != BoardState.DO_NOTHING) {
                invalidate();
                Log.d(getClass().getName(), String.format("%d    %d::::%d,%d:::: , %f %f", viewWidth, viewHeight, x, y, event.getX(), event.getY()));
            }
        }
        return true;
    }


    public void onDraw(Canvas canvas) {
        int rectWidth = viewWidth / size_of_board;
        int rectHeight = rectWidth;
        float radius = (rectWidth * 3) / 8;

        for (int i = 0; i < size_of_board; i++) {
            for (int j = 0; j < size_of_board; j++) {
                if (i % 2 == 0) {
                    canvas.drawRect(rectWidth * i + (rectWidth * (j % 2)), rectHeight * j, rectWidth * i + rectWidth + (rectWidth * (j % 2)), rectHeight * j + rectHeight, paint);
                }
                QueenPosition pos = queen_board.is_valid_position(i, j);
                if (pos == QueenPosition.OCCUPIED) {
                    canvas.drawCircle((rectWidth * (i + 1.0f / 8)) + radius, (rectHeight * (j + 1.0f / 8)) + radius, radius, paint_green);
                } else if (pos != QueenPosition.VALID) {
                    canvas.drawRect((rectWidth * (i + 1.0f / 8)), (rectHeight * (j + 1.0f / 8)), rectWidth * (i + (7.0f / 8)), rectHeight * (j + (7.0f / 8)), paint_red);
                }
            }
        }
        if (queen_board.state == BoardState.WON) {
            Log.d(getClass().getName(), "You Won!!");
            /*TODO add multiple stages for each board size
            * By randomly placing 1 or more queens initially*/
            size_of_board++;
            queen_board.state = BoardState.RE_INIT;
            queen_board.resetBoard(size_of_board);
            invalidate();
        }

    }
}
