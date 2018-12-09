package jsesh.android.app;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import jsesh.android.AndroidUtils;
import jsesh.editor.JMDCEditor;
import jsesh.editor.JMDCEditorWorkflow;
import jsesh.graphics.export.BitmapExporter;
import jsesh.graphics.export.ExportData;
import jsesh.resources.ResourcesManager;


public class EditActivity extends AppCompatActivity {

    private boolean inEditMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourcesManager.setContext(this.getApplicationContext());

        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }

        toolbar.setVisibility(View.GONE);

        findViewById(R.id.main_jmdceditor).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setEditMode(true);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (inEditMode) setEditMode(false);
        else super.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event != null){
            int keyCode = event.getKeyCode();
            if(keyCode == KeyEvent.KEYCODE_BACK){
                if (inEditMode) setEditMode(false);
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void setEditMode(boolean editMode) {
        if (!editMode && inEditMode) {
            inEditMode = false;
            Toolbar toolbar = findViewById(R.id.edit_toolbar);
            toolbar.setVisibility(View.VISIBLE);
            AndroidUtils.hideKeyboard(this);

            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                View editor = findViewById(R.id.main_jmdceditor);
                FrameLayout.LayoutParams layoutParams = ((FrameLayout.LayoutParams) editor.getLayoutParams());
                layoutParams.topMargin = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                editor.setLayoutParams(layoutParams);
            }
            toolbar.requestFocus();
        }
        else if (editMode && !inEditMode) {
            inEditMode = true;
            Toolbar toolbar = findViewById(R.id.edit_toolbar);
            toolbar.setVisibility(View.GONE);
            AndroidUtils.hideKeyboard(this);

            View editor = findViewById(R.id.main_jmdceditor);
            FrameLayout.LayoutParams layoutParams = ((FrameLayout.LayoutParams) editor.getLayoutParams());
            layoutParams.topMargin = 0;
            editor.setLayoutParams(layoutParams);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        JMDCEditor editor = findViewById(R.id.main_jmdceditor);
        JMDCEditorWorkflow workflow = editor.getWorkflow();
        switch (item.getItemId()) {
            //Edit
            case R.id.undo:
                workflow.undo();
                return true;
            case R.id.redo:
                workflow.redo();
                return true;
            case R.id.cut:
                //TODO
                return true;
            case R.id.copy:
                //TODO
                return true;
            case R.id.paste:
                //TODO
                return true;
            case R.id.duplicate:
                //TODO
                return true;
            case R.id.delete:
                workflow.doBackspace();
                return true;
            case R.id.select_all:
                workflow.selectAll();
                return true;
            case R.id.deselect_all:
                workflow.clearMark();
                return true;

            //Group manipulation
            case R.id.group_horizontally:
                workflow.groupHorizontally();
                return true;
            case R.id.group_vertically:
                workflow.groupVertically();
                return true;
            case R.id.ligature_elements:
                workflow.ligatureElements();
                return true;
            case R.id.ligature_group_with_hieroglyph:
                workflow.ligatureGroupWithHieroglyph();
                return true;
            case R.id.ligature_hieroglyph_with_group:
                workflow.ligatureHieroglyphWithGroup();
                return true;
            case R.id.explode_group:
                workflow.explodeGroup();
                return true;
            case R.id.edit_group:
                //TODO
                return true;
            case R.id.insert_space:
                workflow.insertSpace();
                return true;
            case R.id.insert_half_space:
                workflow.insertHalfSpace();
                return true;
            case R.id.new_page:
                workflow.insertPageBreak();
                return true;
            case R.id.insert_red_point:
                //TODO
                return true;
            case R.id.insert_black_point:
                //TODO
                return true;
            case R.id.insert_full_size_shading:
                //TODO
                return true;
            case R.id.insert_horizontal_shading:
                //TODO
                return true;
            case R.id.insert_vertical_shading:
                //TODO
                return true;
            case R.id.insert_quarter_shading:
                //TODO
                return true;
            case R.id.shade_zone:
                workflow.shadeZone();
                return true;
            case R.id.unshade_zone:
                workflow.unshadeZone();
                return true;
            case R.id.paint_zone_in_red:
                workflow.paintZoneInRed();
                return true;
            case R.id.paint_zone_in_black:
                workflow.paintZoneInBlack();
                return true;
            case R.id.shading:
                //TODO
                return true;
            case R.id.cartouches:
                //TODO
                return true;
            case R.id.philological_markup:
                //TODO
                return true;

            //Export
            case R.id.export_bitmap:
                StaticTransfer.obj = new ExportData(editor.getDrawingSpecifications(), editor.getWorkflow().getCaret(), editor.getHieroglyphicTextModel().getModel(), 1);
                Intent intent = new Intent(this, BitmapExporterActivity.class);
                startActivity(intent);
//                BitmapExporter be = new BitmapExporter(this.getApplicationContext());
//                be.export((ExportData) StaticTransfer.obj);
//                StaticTransfer.obj = null;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
