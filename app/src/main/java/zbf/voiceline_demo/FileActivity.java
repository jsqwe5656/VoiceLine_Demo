package zbf.voiceline_demo;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件列表界面
 */
public class FileActivity extends AppCompatActivity
{
    ListView lv;
    String TAG = "zbf";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        lv = (ListView) findViewById(R.id.lv);
        final String path = Environment.getExternalStorageDirectory() + "/healforce/";
        File file = new File(path);
        Log.e(TAG, file.getPath());
        File[] files = file.listFiles(new FileNameSelector("mp3"));
        final List<String> file_name = new ArrayList<String>();
        for (File f : files)
        {
            file_name.add(f.getName());
            Log.e(TAG, f.getName());
        }
        Log.e(TAG, file_name.toString());

        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, file_name));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(FileActivity.this, PlayActivity.class);
                intent.putExtra("path", path + file_name.get(i));
                startActivity(intent);
            }
        });




    }

}
