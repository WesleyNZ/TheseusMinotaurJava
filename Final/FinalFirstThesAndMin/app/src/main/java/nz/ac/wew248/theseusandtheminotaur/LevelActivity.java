package nz.ac.wew248.theseusandtheminotaur;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class LevelActivity extends AppCompatActivity{

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);
        loadLevels();
        context = this;
    }

    public void loadLevels() {
        AssetManager assetManager = getAssets();
        final ListView lv = (ListView) findViewById(R.id.level_list);
        TextView textView = new TextView(this);
        textView.setText(R.string.player_save);
        try {
            String[] levels = assetManager.list("levels");
            ArrayList arrAdapter = new ArrayList<String>();
            for(String item: levels){
                arrAdapter.add(item);
            }
            arrAdapter.add("playerSave.txt");

            ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.textView,arrAdapter);
//            adapter.add(textView);
            lv.setAdapter(adapter);
        } catch (IOException e) {
            System.out.print("not a thing?");
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                String item = (String) lv.getItemAtPosition(position);
                Intent intent = new Intent(context, MazeActivity.class);
                intent.putExtra("level", position);
                startActivity(intent);
                finish();
            }
        });


    }



}
