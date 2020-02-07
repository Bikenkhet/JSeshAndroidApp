package jsesh.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jsesh.editor.JMDCEditor;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.file.MDCDocumentReader;

public class OpenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        populateList();

        ListView listView = findViewById(R.id.open_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String filename = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("filename", filename);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    public void populateList() {

        File directory = getExternalFilesDir(null);
        File[] files = directory.listFiles();

        List<String> filenames = new ArrayList<String>();
        for (File file : files) filenames.add(file.getName());

        String[] filenameArray = new String[files.length];
        filenames.toArray(filenameArray);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.textview_item_open, filenameArray);

        ListView listView = findViewById(R.id.open_list);
        listView.setAdapter(adapter);
    }

}
