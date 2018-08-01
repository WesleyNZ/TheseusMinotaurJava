package nz.ac.wew248.theseusandtheminotaur;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MazeViewModel viewModel = ViewModelProviders.of(this).get(MazeViewModel.class);
//      package used for gif animations, manually doing that ish is too hard...
//      And i'm next level lazy...
        glideAnimation();

        File file = this.getFileStreamPath("playerSave.txt");
        if(!file.exists()) {
            file = new File(this.getFilesDir(), "playerSave.txt");
        }

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(new Runnable() {public void run(){loadMenu();}}, 1, TimeUnit.SECONDS);
    }

    private void glideAnimation() {
        ImageView screenSplash = (ImageView) findViewById(R.id.splash);
        Glide.with(this)
                .asGif()
                .load(R.raw.animated_min_splash)
                .into(screenSplash);
    }

    private void loadMenu() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();

    }
}
