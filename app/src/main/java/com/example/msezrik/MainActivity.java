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
    private final int EMPTY = 11;
    private final int FLAG = 12;
    private final int BOMB = 13;
    private int flags_remaining = NUMBOMBS;
    private boolean game_over = false;
    Context activityContext;
    Canvas canvas;
    private int status[][];
    Bitmap empty_image;
    Bitmap tile_image;
    String str="";
    private class RowCol {
        public int row;
        public int col;
    }
    private void print(String s)
    {
        Log.i(tag, s);
        //Toast.makeText(activityContext, s, 1).show();
    }
    public MineSweeperView(Context context) {
        super(context);
        activityContext = context;
        print("MineSweeperView constructor:");
        status = new int[ROWS][COLS];
        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {
                status[i][j] = TILE;
            }
        }
        // just for wk4 dev step
        status[0][0] = EMPTY;
        empty_image = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        tile_image = BitmapFactory.decodeResource(getResources(), R.drawable.tile);
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
        print("onDraw: w = " + w + ", h = " + h);
        canvas.drawColor(Color.WHITE);
        fg.setColor(Color.BLACK);
        // <SNIP!>

        for (int i=0; i<ROWS; i++) {
            for (int j=0; j<COLS; j++) {
                int xlocat = XBASE+BW*i;
                int ylocat = YBASE+BH*j;
                if (status[i][j] == TILE) {
                    drawbmp(tile_image, xlocat, ylocat, BW, BH);

                } else if (status[i][j] == EMPTY) {
                    drawbmp(empty_image, xlocat, ylocat, BW, BH);
                }
            }
        }
        // Game Board Border
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
        fg.setColor(Color.BLACK);
        fg.setTextSize(24);
        this.canvas.drawText("x = " + touchX + ", y = " + touchY, touchX, touchY, fg);
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

        print("getIndex: r = " + rc.row + ", c = " + rc.col);
        return rc;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        touchX = (int) e.getX();
        touchY = (int) e.getY();
        print("onTouchEvent: x = " + touchX + ", y = " + touchY);
        RowCol rc;
        rc = getIndex(touchX, touchY);
        // validate row and col? and update status array // (set -1)
        // <snip>
        // if not invalid, update status array
        if (!(rc.row == -1 || rc.col == -1)) {
            int xlocat = XBASE+BW*rc.col;
            int ylocat = YBASE+BH*rc.row;
            // update TILE to EMPTY
            if (status[rc.col][rc.row] == TILE) {
                status[rc.col][rc.row] = EMPTY;
            } // Update EMPTY to TILE
            else if (status[rc.col][rc.row] == EMPTY) {
                status[rc.col][rc.row] = TILE;
            }
        } // else, do not update status array

        invalidate();


        return super.onTouchEvent(e);
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