package nz.ac.wew248.theseusandtheminotaur;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nz.ac.wew248.theseusandtheminotaur.model.IObserver;
import nz.ac.wew248.theseusandtheminotaur.model.Point;
import nz.ac.wew248.theseusandtheminotaur.model.Wall;

import static android.support.constraint.ConstraintSet.PARENT_ID;

public class MazeActivity extends AppCompatActivity implements View.OnTouchListener, IObserver {

    private int bg;
    private GestureDetector gestureDetector;
    private ImageView mazeView;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int width;
    private int height;
    private IViewModel viewModel;
    private Paint mPaintLine = new Paint();
    private int cellx;
    private int celly;
    private boolean level = false;
    private int otherSize;
    private ConstraintLayout layout;
    //    private LinearLayout buttonLayout;
    private Button btnReset;
    private Button btnSettings;
    private TextView txtCount;
    private AlertDialog dialog;
    private boolean alertShown;
    private AlertDialog winDialog;
    private boolean winShown;
    private AlertDialog loseDialog;
    private boolean loseShown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);
        bg = ResourcesCompat.getColor(getResources(),
                R.color.mazebg, null);
        width = 50;
        height = 50;


        viewModel = ViewModelProviders.of(this).get(MazeViewModel.class);
        mazeView = new ImageView(this);
        btnReset = new Button(this);
        btnSettings = new Button(this);
        txtCount = new TextView(this);
        mPaintLine.setColor(ResourcesCompat.getColor(getResources(), R.color.mazewall, null));
        mPaintLine.setStrokeWidth(10);
    }


    @Override
    protected void onStart() {
        super.onStart();
        layout = (ConstraintLayout) findViewById(R.id.maze_layout);
        viewModel.attach(this);

        Bundle extras = getIntent().getExtras();
        int levelNum = (int) extras.get("level");
        final ViewTreeObserver observer= layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //  todo: refactor for proper layout sizes
                        if(layout.getWidth() < layout.getHeight()) {
                            width = layout.getWidth();
                            height = layout.getWidth();
                            otherSize = layout.getHeight() - width;
                            btnReset.setWidth(width/2);
                            btnSettings.setWidth(width/2);
                            txtCount.setWidth(width);
                        } else {
                            height = layout.getHeight();
                            width = layout.getHeight();
                            otherSize = layout.getWidth() - height;
                            btnReset.setWidth(otherSize/2);
                            btnSettings.setWidth(otherSize/2);
                            txtCount.setWidth(width);
                        }


                        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                        mBitmap = Bitmap.createBitmap(width, height, conf);
                        mazeView.setImageBitmap(mBitmap);
                        mCanvas = new Canvas(mBitmap);
                        mCanvas.drawColor(bg);
                        mazeView.setScaleType(ImageView.ScaleType.FIT_XY);
                        loadLevel(levelNum);
                        update();
                        setConstraints();
                    }
                });
        btnSettings.setText(R.string.txt_settings);
        btnReset.setText(R.string.btn_reset);
        btnReset.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        btnSettings.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        layout.addView(mazeView);
        mazeView.setId(View.generateViewId());
        layout.addView(btnSettings);
        btnSettings.setId(View.generateViewId());
        layout.addView(btnReset);
        btnReset.setId(View.generateViewId());
        layout.addView(txtCount);
        txtCount.setId(View.generateViewId());
        txtCount.setTextSize(80);

        gestureDetector=new GestureDetector(this,new OnSwipeListener(){
            @Override
            public boolean onSwipe(Direction direction) {
                switch(direction) {
                    case up:
                        System.out.println("UP");
                        moveUp();
                        break;
                    case down:
                        System.out.println("DOWN");
                        moveDown();
                        break;
                    case left:
                        System.out.println("LEFT");
                        moveLeft();
                        break;
                    case right:
                        System.out.println("RIGHT");
                        moveRight();
                }
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                pauseMove();
                return super.onDoubleTap(e);
            }
        });
        mazeView.setOnTouchListener(this);
    }

    public void pauseMove() {
        viewModel.pauseMove();
    }

    private void setSettingDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.txt_settings)
                .setItems(R.array.setting_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                dialog.dismiss();
                                break;
                            case 1:
                                saveGame();
                                break;
                            case 2:
                                finish();
                                break;
                        }
                    }
                });
        dialog = builder.create();
    }

    private void showDialog() {
        if(!alertShown) {
            dialog.show();
            alertShown = true;
        }

    }



    private void setConstraints() {
        ConstraintSet mazeSet = new ConstraintSet();

        mazeSet.constrainHeight(mazeView.getId(), ConstraintSet.WRAP_CONTENT);
        mazeSet.constrainWidth(mazeView.getId(), ConstraintSet.WRAP_CONTENT);

        mazeSet.connect(mazeView.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        mazeSet.connect(mazeView.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        mazeSet.connect(mazeView.getId(), ConstraintSet.BOTTOM, PARENT_ID, ConstraintSet.BOTTOM, 0);

        mazeSet.constrainHeight(btnSettings.getId(), ConstraintSet.WRAP_CONTENT);
        mazeSet.constrainWidth(btnSettings.getId(), ConstraintSet.WRAP_CONTENT);

        mazeSet.connect(btnSettings.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        mazeSet.connect(btnSettings.getId(), ConstraintSet.BOTTOM, btnReset.getId(), ConstraintSet.TOP);

        mazeSet.constrainHeight(btnReset.getId(), ConstraintSet.WRAP_CONTENT);
        mazeSet.constrainWidth(btnReset.getId(), ConstraintSet.WRAP_CONTENT);

        mazeSet.connect(btnReset.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        mazeSet.connect(btnReset.getId(), ConstraintSet.TOP, btnSettings.getId(), ConstraintSet.TOP);
        mazeSet.connect(btnReset.getId(), ConstraintSet.BOTTOM, mazeView.getId(), ConstraintSet.TOP);

        mazeSet.constrainHeight(txtCount.getId(), ConstraintSet.WRAP_CONTENT);
        mazeSet.constrainWidth(txtCount.getId(), ConstraintSet.WRAP_CONTENT);

        mazeSet.connect(txtCount.getId(), ConstraintSet.TOP, PARENT_ID, ConstraintSet.TOP, 100);
        mazeSet.connect(txtCount.getId(), ConstraintSet.LEFT, PARENT_ID, ConstraintSet.LEFT, 0);
        mazeSet.connect(txtCount.getId(), ConstraintSet.RIGHT, PARENT_ID, ConstraintSet.RIGHT, 0);

        mazeSet.applyTo(layout);
        layout.refreshDrawableState();
        setOnClick();
        setSettingDialog();
        setLoseDialog();
        setWinDialog();
    }



    private void setOnClick() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                reset();
                showDialog();
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private void setWinDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.win_title)
                .setItems(R.array.game_finish_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                reset();
                                winShown = false;
                                break;
                            case 1:
                                Intent intent = new Intent(getBaseContext(), LevelActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                finish();
                                break;
                        }
                    }
                });
        winDialog = builder.create();
    }

    private void checkWin() {
        if(!winShown) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.win);
            mediaPlayer.start();
            winDialog.show();
            winShown = true;
        }

    }

    private void setLoseDialog(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.lose_title)
                .setItems(R.array.game_finish_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                reset();
                                loseShown = false;
                                break;
                            case 1:
                                Intent intent = new Intent(getBaseContext(), LevelActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                finish();
                                break;
                        }
                    }
                });
        loseDialog = builder.create();
    }

    private void checkLose() {
        if(!loseShown) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.lose);
            mediaPlayer.start();
            loseDialog.show();
            loseShown = true;
        }
    }

    public void update() {
        if(viewModel.getWin()) {
            checkWin();
        }
        if(viewModel.getLose()) {
            checkLose();
        }


        mazeView.invalidate();
        mCanvas.drawColor(bg);
        getUp();
        getLeft();
        Point theseus = viewModel.getTheseus();
        drawTheseus(theseus);
        Point minotaur = viewModel.getMinotaur();
        drawMinotaur(minotaur);
        Point exit = viewModel.getExit();
        drawExit(exit);
        txtCount.setText(String.valueOf(getCount()));
    }

    public void drawExit(Point point) {
        double posx = point.xAxis() * (cellx);
        double posy = point.yAxis() * (celly);
        Drawable d = getResources().getDrawable(R.drawable.exit);
        d.setBounds((int)posx, (int)posy, (int)posx + cellx, (int)posy + celly);
        d.draw(mCanvas);
    }

    public void drawTheseus(Point point) {
        double posx = point.xAxis() * (cellx);
        double posy = point.yAxis() * (celly);
        Drawable d = getResources().getDrawable(R.drawable.theseus);
        d.setBounds((int)posx, (int)posy, (int)posx + cellx, (int)posy + celly);
        d.draw(mCanvas);
    }

    public void drawMinotaur(Point point) {
        double posx = point.xAxis() * (cellx);
        double posy = point.yAxis() * (celly);
        Drawable d = getResources().getDrawable(R.drawable.minotaur);
        d.setBounds((int)posx, (int)posy, (int)posx + cellx, (int)posy + celly);
        d.draw(mCanvas);
    }

    private int getCount() {
        return viewModel.getMoveCount();
    }


    public void loadLevel(int level) {
        AssetManager assetManager = getAssets();
        StringBuilder fileContent = new StringBuilder();
        BufferedReader bufferedReader;
        try {
            String[] theLevels = assetManager.list("levels");
            if(level < theLevels.length){
                InputStream inputStream = assetManager.open("levels/" + theLevels[level]);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

            } else {
                File file = new File(this.getFilesDir(), "playerSave.txt");
                bufferedReader = new BufferedReader(new FileReader(file));
            }
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewModel.loadLevel(fileContent.toString());
    }

    //todo: create button programatically for reset
    public void reset() {
        viewModel.reset();
    }

    //  todo: create button programatically for reset
    public void undo() {
        viewModel.undo();
    }

    public void moveUp() {
        viewModel.moveUp();
    }

    public void moveDown() {
        viewModel.moveDown();
    }

    public void moveLeft() {
        viewModel.moveLeft();
    }

    public void moveRight() {
        viewModel.moveRight();
    }

    public void getUp() {
        Wall[][] walls = viewModel.getUp();
        cellx = width/walls[0].length;
        celly = height/walls.length;
        getYWalls(walls, true);
    }

    public void getLeft() {
        Wall[][] walls = viewModel.getLeft();
        cellx = width/walls[0].length;
        celly = height/walls.length;
        getYWalls(walls, false);
    }

    private void getYWalls(Wall[][] walls, boolean up) {
        for(int y = 0; y < walls.length; y++) {
            getXWalls(walls, y, up);
        }
    }

    private void getXWalls(Wall[][] walls, int y, boolean up) {
        for (int x = 0; x < walls[y].length; x++) {
            if(walls[y][x] == Wall.WALL) {
                if(up){
                    drawUpWalls((x+1)*cellx, (y+1)*celly, (x+2)*cellx);
                } else {
                    drawLeftWalls((x+1)*cellx, (y+1)*celly, (y+2)*celly);
                }
            }
        }

    }

    public void drawLeftWalls(int x, int y, int yy) {
        mCanvas.drawLine((float)x-cellx, (float)y-celly,(float)x-cellx,(float)yy-celly, mPaintLine);
    }

    public void drawUpWalls(int x, int y, int xx) {
        mCanvas.drawLine((float)x-cellx, (float)y-celly,(float)xx-cellx,(float)y-celly, mPaintLine);
    }

    public void saveGame() {
        FileOutputStream fileOutputStream;
        try{
            fileOutputStream = this.openFileOutput("playerSave.txt", MODE_PRIVATE);
            viewModel.saveGame(fileOutputStream);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
