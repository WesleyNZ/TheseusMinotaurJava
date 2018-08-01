package nz.ac.wew248.simplethesmin;


import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nz.ac.wew248.simplethesmin.model.IObserver;
import nz.ac.wew248.simplethesmin.model.Point;
import nz.ac.wew248.simplethesmin.model.Wall;

public class MazeActivity extends AppCompatActivity implements View.OnTouchListener, IObserver {
    private IViewModel viewModel;
    private GestureDetector gestureDetector;
    private CustomView mazeView;
    private AlertDialog winDialog, loseDialog, options;
    private boolean winShown, loseShown, alertShown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);
        viewModel = ViewModelProviders.of(this).get(MazeViewModel.class);
        gestureDetector=new GestureDetector(this,new OnSwipeListener(){
            @Override
            public boolean onSwipe(Direction direction) {
                switch(direction) {
                    case up:
                        moveUp();
                        break;
                    case down:
                        moveDown();
                        break;
                    case left:
                        moveLeft();
                        break;
                    case right:
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
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.maze_layout);
        mazeView = (CustomView) findViewById(R.id.maze_view);
        layout.setOnTouchListener(this);
        setOptionsDialog();
        setWinDialog();
        setLoseDialog();
    }

    public void pauseMove() {
        viewModel.pauseMove();
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.attach(this);
        Bundle extras = getIntent().getExtras();
        int levelNum = (int) extras.get("level");
//        viewModel.setContext(this);
//        viewModel.loadLevel(String.valueOf(levelNum));
        loadLevel(levelNum);
    }

    public void clickReset(View view) {
        reset();
    }

    public void reset() {
        viewModel.reset();
        winShown = false;
        loseShown = false;
    }

    public void loadLevel(int level) {
        AssetManager assetManager = getAssets();
        StringBuilder fileContent = new StringBuilder();
        BufferedReader bufferedReader;
        try {
            String[] theLevels = assetManager.list("levels");//"MyLevels");
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


        Wall[][] left = getLeft();
        Wall[][] up = getUp();

        for(int yy = 0; yy < left.length; yy++) {
            for(int xx = 0; xx < left[0].length; xx++) {
                System.out.println("STARTING ROW");
                if(left[yy][xx].equals(Wall.WALL)) {
                    System.out.println("WALL AT X: " + xx  + " Y: " + yy);
                } else {
                    System.out.println("NONE AT X: " + xx  + " Y: " + yy);
                }
            }
        }





        mazeView.setLeft(left);
        mazeView.setUp(up);
        update();
        int aX = left.length;
        int aY = left[aX-1].length;
        mazeView.setSize(aX, aY);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void update() {
        Point theseus = getTheseus();
        mazeView.setTheseus(theseus);
        Point minotaur = getMinotaur();
        mazeView.setMinotaur(minotaur);
        Point exit = getExit();
        mazeView.setExit(exit);
        int count = viewModel.getMoveCount();
        mazeView.setMoveCount(count);
        if(viewModel.getWin()) {
            checkWin();
        }
        if(viewModel.getLose()) {
            checkLose();
        }
    }

    private Point getExit() {
        return viewModel.getExit();
    }

    private Point getMinotaur() {
        return viewModel.getMinotaur();
    }

    private Point getTheseus() {
        return viewModel.getTheseus();
    }

    private Wall[][] getLeft() {
        return viewModel.getLeft();
    }

    private Wall[][] getUp() {
        return viewModel.getUp();
    }

    private void moveUp() {
        viewModel.moveUp();
    }

    private void moveDown() {
        viewModel.moveDown();
    }

    private void moveLeft() {
        viewModel.moveLeft();
    }

    private void moveRight() {
        viewModel.moveRight();
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

    private void setOptionsDialog() {
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
        options = builder.create();
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
                                winShown = false;
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
                                dialog.dismiss();
                                break;
                            case 1:
                                Intent intent = new Intent(getBaseContext(), LevelActivity.class);
                                startActivity(intent);
                                loseShown = false;
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
            loseDialog.setCancelable(false);
            loseDialog.setCanceledOnTouchOutside(false);
            loseDialog.show();
            loseShown = true;
        }
    }

    private void checkWin() {
        if(!winShown) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.win);
            mediaPlayer.start();
            winDialog.show();
            winShown = true;
        }
    }

    public void clickOptions(View view) {
        showDialog();
    }

    private void showDialog() {
        if(!alertShown) {
            options.show();
            alertShown = true;
        }
    }
}
