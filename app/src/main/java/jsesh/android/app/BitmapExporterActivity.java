package jsesh.android.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;

import jsesh.graphics.export.BitmapExporter;
import jsesh.graphics.export.ExportData;

public class BitmapExporterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bitmap_exporter);
    }

    public void saveBitmap(View view) {
        if (StaticTransfer.obj instanceof ExportData) {
            String filename = ((EditText) findViewById(R.id.editText5)).getText().toString();
            int cadratHeight = Integer.parseInt(((EditText) findViewById(R.id.editText)).getText().toString());
            boolean transparency = ((Switch)findViewById(R.id.switch1)).isChecked();
            int fileOutputFormat = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId() == R.id.radioButton ? 0 : 1;
            BitmapExporter bitmapExporter = new BitmapExporter(this.getApplicationContext());
            bitmapExporter.applySettings(filename, cadratHeight, transparency, fileOutputFormat, false);
            bitmapExporter.export((ExportData) StaticTransfer.obj);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        StaticTransfer.obj = null;
        super.onDestroy();
    }

}
