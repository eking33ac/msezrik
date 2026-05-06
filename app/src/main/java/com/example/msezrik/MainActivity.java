package com.example.msezrik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.view.MotionEvent;

class MineSweeperView extends View {
    private String tag = "EEK";
    private long mouseUP=0; // Time mouse click was let go
    private long mouseDOWN=0; // Time mouse was clicked
    boolean longPress = false;
    private int touchX;
    private int touchY;
    private final int XBASE=40;
    private final int YBASE=80;
    private final int BW=40;
    private final int BH=40;
    private final int ROWS=10;
    private final int COLS=10;
    private final int NUMBOMBS = 12;
    private final int TILE = 10;
    private final int EMPTY = 0;
    private final int FLAG = 12;
    private final int BOMB = 13;
    private final int BLUE1 = 1;
    private final int BLUE2 = 2;
    private final int BLUE3 = 3;
    private final int BLUE4 = 4;
    private final int BLUE5 = 5;
    private final int BLUE6 = 6;
    private final int BLUE7 = 7;
    private final int BLUE8 = 8;

    private int flags_remaining = NUMBOMBS;
    private boolean game_over = false;
    Context activityContext;
    Canvas canvas;
    private int status[][];
    boolean isbomb[][];
    Bitmap empty_image;
    Bitmap tile_image;
    Bitmap flag_image;
    Bitmap bomb_image;
    Bitmap blue1_image;
    Bitmap blue2_image;
    Bitmap blue3_image;
    Bitmap blue4_image;
    Bitmap blue5_image;
    Bitmap blue6_image;
    Bitmap blue7_image;
    Bitmap blue8_image;
    String str="";
    private class RowCol {
        public int row;
        public int col;
    }
    private void print(String s)
    {
        // Log.i(tag, s);
        Toast.makeText(activityContext, s, Toast.LENGTH_LONG).show();
    }
    public MineSweeperView(Context context) {
        super(context);
        activityContext = context;
        // print("MineSweeperView constructor:");
        status = new int[ROWS][COLS];
        isbomb = new boolean[ROWS][COLS];

        // Set all tiles to TILE and set all isbomb to false
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {
                status[i][j] = TILE;
                isbomb[i][j] = false; // all should be initialized to false without this, so this is included for clairity and precaution
            }
        }

        // Place the 12 bombs
        int count = 0;
        while (count < NUMBOMBS) {
            int r = (int) (ROWS * Math.random());
            int c = (int) (COLS * Math.random());
            if (!isbomb[r][c]) {
                // COMMENT BELOW OUT
                // print("bomb placed at [" + r + "][" + c + "]");
                isbomb[r][c] = true;
                count++;
            }
        }

        // just for wk4 dev step
        // status[0][0] = EMPTY;

        // create all tile-version images
        empty_image = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        tile_image = BitmapFactory.decodeResource(getResources(), R.drawable.tile);
        flag_image = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        bomb_image = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        blue1_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum1);
        blue2_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum2);
        blue3_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum3);
        blue4_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum4);
        blue5_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum5);
        blue6_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum6);
        blue7_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum7);
        blue8_image = BitmapFactory.decodeResource(getResources(), R.drawable.bluenum8);
    }
    private void drawbmp(Bitmap bm, int x, int y, int w, int h)
    {
        canvas.drawBitmap(bm, null, new Rect(x,y,x+w-1, y+h-1), null);
    }
    @Override
    public void onDraw(Canvas canvas) {
        Paint fg = new Paint();
        int w = getWidth();
        int h = getHeight();
        this.canvas = canvas;
        // print("onDraw: w = " + w + ", h = " + h);
        canvas.drawColor(Color.WHITE);
        fg.setColor(Color.BLACK);

        // DRAW BOARD
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {
                int xlocat = XBASE+BW*i;
                int ylocat = YBASE+BH*j;
                if (status[i][j] == TILE) {
                    drawbmp(tile_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == EMPTY) {
                    drawbmp(empty_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BOMB) {
                    drawbmp(bomb_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == FLAG) {
                    drawbmp(flag_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE1) {
                    drawbmp(blue1_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE2) {
                    drawbmp(blue2_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE3) {
                    drawbmp(blue3_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE4) {
                    drawbmp(blue4_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE5) {
                    drawbmp(blue5_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE6) {
                    drawbmp(blue6_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE7) {
                    drawbmp(blue7_image, xlocat, ylocat, BW, BH);
                } else if (status[i][j] == BLUE8) {
                    drawbmp(blue8_image, xlocat, ylocat, BW, BH);
                }
            }
        }

        // DRAW Game Board Border
        int dfb = 2;    // distance of border from board
        int leftX = XBASE-dfb;
        int rightX = XBASE+BW*COLS+dfb; // should it be +(dfb-1)? It looks like 1px may be included by default because of the line or something.
        int topY = YBASE-dfb;
        int bottomY = YBASE+BH*ROWS+dfb;

        // Horizontal Lines
        // top border line
        canvas.drawLine(leftX, topY, rightX, topY, fg);
        // bottom border
        canvas.drawLine(leftX, bottomY, rightX, bottomY, fg);

        // Vertical Lines
        // left border line
        canvas.drawLine(leftX, topY, leftX, bottomY, fg);
        // right border line
        canvas.drawLine(rightX, topY, rightX, bottomY, fg);


        // display coordinants
        /* fg.setColor(Color.BLACK);
        fg.setTextSize(24);
        this.canvas.drawText("x = " + touchX + ", y = " + touchY, touchX, touchY, fg); */
    }

    private RowCol getIndex(int x, int y)
    {
        RowCol rc = new RowCol();
        // use x, y and XBASE, YBASE, ROWS, COLS, BW, BH to validate x & y!
        // calculate row and column index given x and y coordinates
        // <snip!>

        int XMAX = XBASE + BW * COLS;
        int YMAX = YBASE + BH * ROWS;
        // if x and y are both outside the board, set both to -1
        if (!(x >= XBASE && x <= XMAX) || !(y >= YBASE && y <= YMAX)) {
            rc.row = -1;
            rc.col = -1;
        } else {
            // else, it IS inside the board, so find which tile it is on
            rc.row = (y - YBASE) / BH;
            rc.col = (x - XBASE) / BW;
        }

        // print("getIndex: r = " + rc.row + ", c = " + rc.col);
        return rc;
    }
    private int countNeighboringBombs(int r, int c)
    {
        int count = 0;
        final int C = COLS - 1;
        final int R = ROWS - 1;
        if ((r > 0) && (c > 0) && (isbomb[r - 1][c - 1])) count++;
        if ((r > 0) && (isbomb[r - 1][c])) count++;
        if ((r > 0) && (c < C) && (isbomb[r - 1][c + 1])) count++;
        if ((c > 0) && (isbomb[r][c - 1])) count++;
        if ((c < C) && (isbomb[r][c + 1])) count++;
        if ((r < R) && (c > 0) && (isbomb[r + 1][c - 1])) count++;
        if ((r < R) && (isbomb[r + 1][c])) count++;
        if ((r < R) && (c < C) && (isbomb[r + 1][c + 1])) count++;
        return count;
    }

    private void clearBlankNeighbors(int r, int c) {
        status[r][c] = EMPTY;
        int n = countNeighboringBombs(r, c);
        if (n == 0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    // Clear rows & columns if the tile
                    // has not already been EMPTY
                    if ((r + i < ROWS) && (r + i >= 0) && (c + j < COLS) && (c + j >= 0) &&
                            (status[r + i][c + j] == TILE)) {
                        // Recursive call to clear open tiles.
                        clearBlankNeighbors(r + i, c + j);
                    }
                }
            }
        } else {
            status[r][c] = n;
        }
    }

    private boolean checkForWin() {
        boolean userWon = false; // return value

        int tileCounter = 0;
        int flagCounter = 0;

        // for each row
        for (int i = 0; i < ROWS; i++) {
            // for each column
            for (int j = 0; j < COLS; j++) {
                // If tile is a TILE, increment tile count
                if (status[i][j] == TILE) {
                    tileCounter++;
                }
                // if tile is a FLAG, increment flag count
                else if (status[i][j] == FLAG) {
                    flagCounter++;
                }
            }
        }

        // Check if there are NO TILES AND FLAGS == NUMBOMBS
        if (tileCounter == 0 && flagCounter == NUMBOMBS) {
            userWon = true;
        }

        return userWon;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        // don't do anything if game is over
        if (game_over) { return true; } // Must return SOMETHING, so I set it to true like at the end of the method
        touchX = (int) e.getX();
        touchY = (int) e.getY();
        // print("onTouchEvent: x = " + touchX + ", y = " + touchY);

        // Detect Longtouch event
        int action = e.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            mouseDOWN = System.currentTimeMillis();
        } else if (action == MotionEvent.ACTION_UP) { // all ontouch info goes in here because otherwise it runs onMouseDown
            mouseUP = System.currentTimeMillis();
            if (mouseUP - mouseDOWN > 200) {
                // print("LONG PRESS!");
                longPress = true;
            } else {
                longPress = false;
            }
            RowCol rc;
            rc = getIndex(touchX, touchY);
            // if place user clicked is on the board (not invalid) update status array
            if (!(rc.row == -1 || rc.col == -1)) {
                int xlocat = XBASE + BW * rc.col;
                int ylocat = YBASE + BH * rc.row;
                // If a short/normal click and status is TILE
                if (!longPress && status[rc.col][rc.row] == TILE) {
                    // If there is a bomb at that row-col index
                    if (isbomb[rc.col][rc.row]) {
                        status[rc.col][rc.row] = BOMB; // set status to bomb
                        print("Game Over"); // toast GAME OVER
                        game_over = true; // set game_over to true
                    } else {
                        // count neighboring bombs and assign that to the status array at this row-col index
                        // status[rc.col][rc.row] = countNeighboringBombs(rc.col, rc.row);

                        int n = countNeighboringBombs(rc.col, rc.row);
                        if (n > 0) {
                            status[rc.col][rc.row] = n;
                        } else {
                            clearBlankNeighbors(rc.col, rc.row);
                        }
                    }
                }
                else if (longPress) { // if longpress is true
                    if (status[rc.col][rc.row] == FLAG) { // if status is FLAG
                        status[rc.col][rc.row] = TILE; // Set status to TILE
                    }
                    else if (status[rc.col][rc.row] == TILE) { // if status is TILE
                        status[rc.col][rc.row] = FLAG; // set status to FLAG
                    }
                }
            }

            // If the user won
            if (checkForWin()) {
                print("You won!");
                game_over = true;
            }

            invalidate(); // Update the screen
        }
        // return super.onTouchEvent(e);
        return true;
    }

}



public class MainActivity extends AppCompatActivity {

    private String tag = "EEK";
    MineSweeperView mineSweeperView;
    private void print(String s)
    {
        Log.i(tag, s);
        //Toast.makeText(this, s, 1).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        print("onCreate:");
        Toast.makeText(this, "MineSweeper", Toast.LENGTH_LONG).show();
        mineSweeperView = new MineSweeperView(this);
        setContentView(mineSweeperView);
    }
}