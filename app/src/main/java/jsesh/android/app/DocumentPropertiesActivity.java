package jsesh.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import jsesh.graphics.export.BitmapExporter;
import jsesh.graphics.export.ExportData;

public class DocumentPropertiesActivity extends AppCompatActivity {

    private UnitMediator unitMediator = new UnitMediator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_document_properties);

        Intent data = getIntent();

        ((EditText)findViewById(R.id.editText3)).setText(""+data.getFloatExtra("a1SignHeight", 0));
        ((EditText)findViewById(R.id.editText4)).setText(""+data.getFloatExtra("lineSpacing", 0));
        ((EditText)findViewById(R.id.editText6)).setText(""+data.getFloatExtra("spaceBetweenQuadrants", 0));
        ((EditText)findViewById(R.id.editText7)).setText(""+data.getFloatExtra("columnSkip", 0));
        ((EditText)findViewById(R.id.editText8)).setText(""+data.getFloatExtra("maximalQuadrantHeight", 0));
        ((EditText)findViewById(R.id.editText9)).setText(""+data.getFloatExtra("maximalQuadrantWidth", 0));
        ((EditText)findViewById(R.id.editText10)).setText(""+data.getDoubleExtra("smallFontBody", 0));
        ((EditText)findViewById(R.id.editText11)).setText(""+data.getFloatExtra("cartoucheLineWidth", 0));
        ((CheckBox)findViewById(R.id.checkBox1)).setChecked(data.getBooleanExtra("useLinesForShading", false));

        unitMediator.attachToComboBox((Spinner)findViewById(R.id.spinnerUnits));
        int[] fields = new int[] {
                R.id.editText3,
                R.id.editText4,
                R.id.editText6,
                R.id.editText7,
                R.id.editText8,
                R.id.editText9,
                R.id.editText10,
                R.id.editText11,
        };
        for (int f : fields) unitMediator.managedTextField((EditText) findViewById(f));
    }

    private float getLength(int id) {
        return (float) unitMediator.getManagedFieldInPoints((EditText) findViewById(id));
    }

    public void setProperties(View view) {

        Intent data = new Intent();

        data.putExtra("a1SignHeight",           getLength(R.id.editText3));
        data.putExtra("lineSpacing",            getLength(R.id.editText4));
        data.putExtra("spaceBetweenQuadrants",  getLength(R.id.editText6));
        data.putExtra("columnSkip",             getLength(R.id.editText7));
        data.putExtra("maximalQuadrantHeight",  getLength(R.id.editText8));
        data.putExtra("maximalQuadrantWidth",   getLength(R.id.editText9));
        data.putExtra("smallFontBody",          getLength(R.id.editText10));
        data.putExtra("cartoucheLineWidth",     getLength(R.id.editText11));
        data.putExtra("useLinesForShading",     ((CheckBox)findViewById(R.id.checkBox1)).isChecked());
        setResult(RESULT_OK, data);

        finish();

    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
