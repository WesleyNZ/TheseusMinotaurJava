package nz.ac.wew248.theseusandtheminotaur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, MazeActivity.class);
//      hard coded, just the way i like it
        intent.putExtra("level", 1);
        startActivity(intent);
    }

    public void load(View view) {
        Intent intent = new Intent(this, LevelActivity.class);
        startActivity(intent);
    }

    public void exit(View view) {
        finish();
    }
}