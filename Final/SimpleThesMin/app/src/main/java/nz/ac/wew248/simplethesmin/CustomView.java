package nz.ac.wew248.simplethesmin;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import nz.ac.wew248.simplethesmin.model.Point;
import nz.ac.wew248.simplethesmin.model.Wall;

public class CustomView extends AppCompatImageView {
    private Paint wall, thesColor, minColor, moveCountPaint;
    private int x = 5;
    private int y = 5;
    private int cellX, cellY;
    private int widthSize, heightSize, moveCount;
    private boolean mapSet;
    private Canvas canvas;
    private Point theseus, minotaur, exit;
    private Wall[][] left, up;

    public CustomView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        wall = new Paint();
        thesColor = new Paint();
        minColor = new Paint();
        wall.setColor(Color.parseColor("#FFFFFF"));
        thesColor.setColor(Color.parseColor("#000000"));
        minColor.setColor(Color.parseColor("#000FFF"));
        wall.setStrokeWidth(10);

        moveCountPaint = new Paint();
        moveCountPaint.setColor(Color.BLACK);
        moveCountPaint.setTextSize(100);
    }

    @Override
    public void onDraw(Canvas acanvas) {
        super.onDraw(acanvas);
        canvas = acanvas;
        canvas.drawColor(getResources().getColor(R.color.mazebg));
        if(mapSet) {
            update();
        }
        invalidate();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(widthMeasureSpec != 0) {
            widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightSize = MeasureSpec.getSize(heightMeasureSpec);
        } else{
            widthSize = 50;
            heightSize = 50;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    public void setSize(int aX, int aY) {
        x = aX;
        y = aY;
        mapSet = true;
    }

    public void setTheseus(Point thes) {
        theseus = thes;
    }

    public void setMinotaur(Point min) {
        minotaur = min;
    }

    public void setExit(Point ex) {
        exit = ex;
    }

    public void setLeft(Wall[][] walls) {
        left = walls;
    }

    public void setUp(Wall[][] walls) {
        up = walls;
    }

    public void setMoveCount(int themovecount) {
        moveCount = themovecount;
    }

    public void update(){
        drawLeftWalls();
        drawUpWalls();
        drawExit();
        drawTheseus();
        drawMinotaur();
        drawMoveCount();
    }

    public void drawExit() {
        double posx = exit.xAxis() * (cellX);
        double posy = exit.yAxis() * (cellY);
        Drawable d = getResources().getDrawable(R.drawable.exit);
        d.setBounds((int)posx, (int)posy, (int)posx + cellX, (int)posy + cellY);
        d.draw(canvas);
    }

    public void drawTheseus() {
        double posx = theseus.xAxis() * (cellX);
        double posy = theseus.yAxis() * (cellY);
        Drawable d = getResources().getDrawable(R.drawable.theseus);
        d.setBounds((int)posx, (int)posy, (int)posx + cellX, (int)posy + cellY);
        d.draw(canvas);
    }

    public void drawMinotaur() {
        double posx = minotaur.xAxis() * (cellX);
        double posy = minotaur.yAxis() * (cellY);
        Drawable d = getResources().getDrawable(R.drawable.minotaur);
        d.setBounds((int)posx, (int)posy, (int)posx + cellX, (int)posy + cellY);
        d.draw(canvas);
    }

    public void drawLeftWalls() {
        cellX = widthSize/this.x;
        cellY = widthSize/this.y;
        for(int yy = 0; yy < left.length; yy++){
            for(int xx = 0; xx < left[0].length; xx++) {
                if(left[yy][xx] == Wall.WALL)
                    canvas.drawLine((float)(xx+1)*cellX-cellX, (float)(yy+1)*cellY-cellY,(float)(xx+1)*cellX-cellX,(float)(yy+2)*cellY-cellY, wall);
            }
        }
    }

    public void drawMoveCount() {
        cellX = widthSize/this.x;
        cellY = widthSize/this.y;
        canvas.drawText(String.valueOf(moveCount), (cellX) , (cellY) * (y+1) , moveCountPaint);
    }

    public void drawUpWalls() {
        cellX = widthSize/this.x;
        cellY = widthSize/this.y;
        for(int yy = 0; yy < up.length; yy++){
            for(int xx = 0; xx < up[0].length; xx++) {
                if(up[yy][xx] == Wall.WALL)
                canvas.drawLine((float)(xx+1)*cellX-cellX, (float)(yy+1)*cellY-cellY,(float)(xx+2)*cellX-cellX,(float)(yy+1)*cellY-cellY, wall);
            }
        }

    }

}
