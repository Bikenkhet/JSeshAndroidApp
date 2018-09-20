package jsesh.android.app;

import android.accessibilityservice.GestureDescription;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import jsesh.editor.HieroglyphicTextModel;
import jsesh.editor.JMDCEditor;
import jsesh.mdc.MDCSyntaxError;
import jsesh.resources.ResourcesManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourcesManager.setContext(this.getApplicationContext());

        setContentView(jsesh.android.app.R.layout.activity_main);


        HieroglyphicTextModel h = new HieroglyphicTextModel();
        try {
            h.setMDCCode("a");
        } catch (MDCSyntaxError mdcSyntaxError) {
            mdcSyntaxError.printStackTrace();
        }
        System.out.println(h.getModel().toMdC(true));







        //jsesh.editor.ThingTest.doThing();


    }
}
