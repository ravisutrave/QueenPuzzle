package com.ravisutrave.puzzle;


/**
 * Created by Ravichandra Sutrave on 8/15/14.
 */
public class PuzzleBoard {
    public BoardState state = BoardState.IN_PROGRESS;
    private short size_of_board;
    private short positionsX[];
    private short positionsY[];
    private boolean positionsLeft[];
    private boolean positionsLeft2[];
    private boolean positionsRight[];
    private boolean positionsRight2[];
    private short count_queens = 0;

    PuzzleBoard(short n) {
        size_of_board = n;
        positionsX = new short[n];
        positionsY = new short[n];
        positionsLeft = new boolean[n];
        positionsLeft2 = new boolean[n];
        positionsRight = new boolean[n];
        positionsRight2 = new boolean[n];
        count_queens = 0;
    }

    public void resetBoard(short n) {
        this.state = BoardState.IN_PROGRESS;
        size_of_board = n;
        positionsX = new short[n];
        positionsY = new short[n];
        positionsLeft = new boolean[n];
        positionsLeft2 = new boolean[n];
        positionsRight = new boolean[n];
        positionsRight2 = new boolean[n];
        count_queens = 0;
    }

    public QueenPosition is_valid_position(int x, int y) {
        /*No loops here. Its O(1)!!*/
        if (x >= size_of_board || y >= size_of_board || x < 0 || y < 0) {
            /* Outside board!! Return invalid*/
            return QueenPosition.INVALID;
        } else if (positionsX[y] == x + 1 || positionsY[x] == y + 1) {
            /*Oh! already occupied. still invalid*/
            return QueenPosition.OCCUPIED;
        } else if (positionsX[y] > 0 || positionsY[x] > 0 ||
                (x <= y && positionsLeft[y - x]) || (x > y && positionsLeft2[x - y]) ||
                (x + y >= (size_of_board - 1) && positionsRight[x + y - size_of_board + 1]) || (x + y < (size_of_board - 1) && positionsRight2[x + y])) {
            /*This is tricky. positionsLeft and positionsRight are used to make this function run in O(1) using O(n) memory
            * This actually test if there are any queens already placed diagonally to current position */
            return QueenPosition.INVALID;
        }
        return QueenPosition.VALID;
    }

    public BoardState update_board(short x, short y) {
        QueenPosition pos = is_valid_position(x, y);
        BoardState returnValue = BoardState.DO_NOTHING;
        switch (pos) {
            case VALID:
                /* add queen */
                positionsX[y] = (short) (x + 1);
                positionsY[x] = (short) (y + 1);
                if (x <= y) {
                    positionsLeft[y - x] = true;
                } else {
                    positionsLeft2[x - y] = true;
                }
                if (x + y >= size_of_board - 1) {
                    positionsRight[x + y - size_of_board + 1] = true;
                } else {
                    positionsRight2[x + y] = true;
                }
                count_queens++;
                state = BoardState.ADDED;
                if (count_queens == size_of_board) {
                    state = BoardState.WON;
                }
                returnValue = BoardState.REDRAW;
                break;

            case OCCUPIED:
                /*Delete position*/
                positionsX[y] = 0;
                positionsY[x] = 0;
                if (x <= y) {
                    positionsLeft[y - x] = false;
                } else {
                    positionsLeft2[x - y] = false;
                }
                if (x + y >= size_of_board - 1) {
                    positionsRight[x + y - size_of_board + 1] = false;
                } else {
                    positionsRight2[x + y] = false;
                }
                count_queens--;
                state = BoardState.REMOVED;
                returnValue = BoardState.REDRAW;
                break;
        }
        return returnValue;
    }
}
