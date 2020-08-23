package jsesh.android.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jsesh.android.AndroidUtils;
import jsesh.editor.HieroglyphicTextModel;
import jsesh.editor.JMDCEditor;
import jsesh.editor.JMDCEditorWorkflow;
import jsesh.editor.MDCModelEditionAdapter;
import jsesh.editor.caret.MDCCaret;
import jsesh.graphics.export.BitmapExporter;
import jsesh.graphics.export.ExportData;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.constants.TextDirection;
import jsesh.mdc.constants.TextOrientation;
import jsesh.mdc.file.MDCDocument;
import jsesh.mdc.file.MDCDocumentReader;
import jsesh.mdc.model.operations.ModelOperation;
import jsesh.mdcDisplayer.preferences.DrawingSpecification;
import jsesh.mdcDisplayer.preferences.DrawingSpecificationsImplementation;
import jsesh.mdcDisplayer.preferences.ShadingStyle;
import jsesh.resources.ResourcesManager;


public class EditActivity extends AppCompatActivity {

    private boolean inEditMode = true;

    MDCDocument mdcDocument;

    public static final int READ_REQUEST_CODE = 1;
    public static final int BITMAP_EXPORTER_REQUEST_CODE = 100;
    public static final int PDF_EXPORTER_REQUEST_CODE = 101;
    public static final int DOCUMENT_PROPERTIES_REQUEST_CODE = 200;
    public static final int OPEN_FILE_REQUEST_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResourcesManager.setContext(this.getApplicationContext());

        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }

        toolbar.setVisibility(View.GONE);

        final JMDCEditor editor = findViewById(R.id.main_jmdceditor);

        editor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setEditMode(true);
            }
        });

        mdcDocument = new MDCDocument();


        //Code and separator

        final EditText editTextCurrentCode = findViewById(R.id.editTextCurrentCode);
        final EditText editTextCurrentSeparator = findViewById(R.id.editTextCurrentSeparator);
        final EditText editTextCurrentMDC = findViewById(R.id.editTextCurrentMDC);

        editTextCurrentCode.setEnabled(false);
        editTextCurrentSeparator.setEnabled(false);

        editTextCurrentCode.setInputType(InputType.TYPE_NULL);
        editTextCurrentSeparator.setInputType(InputType.TYPE_NULL);

        editTextCurrentMDC.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    boolean success = editor.getWorkflow().setCurrentLineTo(editTextCurrentMDC.getText().toString());
                    if (!success) Toast.makeText(getApplicationContext(), R.string.invalid_mdc_format, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        editor.addCodeChangeListener(new MDCModelEditionAdapter() {
            @Override
            public void textEdited(ModelOperation op) {
                editTextCurrentMDC.setText(editor.getWorkflow().getCurrentLineAsString());
                editTextCurrentMDC.setSelection(editTextCurrentMDC.getText().length());
            }

            @Override
            public void textChanged() {
                editTextCurrentMDC.setText(editor.getWorkflow().getCurrentLineAsString());
                editTextCurrentMDC.setSelection(editTextCurrentMDC.getText().length());
            }

            @Override
            public void separatorChanged() {
                String sep = Character.toString(editor.getWorkflow().getCurrentSeparator());
                editTextCurrentSeparator.setText(sep);
            }

            @Override
            public void codeChanged(StringBuffer code) {
                editTextCurrentCode.setText(editor.getWorkflow().getCurrentCode());
                editTextCurrentCode.setSelection(editTextCurrentCode.getText().length());
            }

            @Override
            public void focusGained(StringBuffer code) {
                //NO-OP
            }

            @Override
            public void focusLost() {
                //NO-OP
            }

            @Override
            public void caretChanged(MDCCaret caret) {
                editTextCurrentMDC.setText(editor.getWorkflow().getCurrentLineAsString());
                editTextCurrentMDC.setSelection(editTextCurrentMDC.getText().length());
            }
        });


        //Handle intent

        Intent intent = getIntent();
        String action = intent.getAction();

        if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (uri != null) {
                try {

                    InputStream is = getContentResolver().openInputStream(uri);
                    MDCDocumentReader reader = new MDCDocumentReader();
                    mdcDocument = reader.readStream(is, new File(".gly"));
                    mdcDocument.setFile(null);

                    editor.setHieroglyphiTextModel(mdcDocument.getHieroglyphicTextModel());
                    editor.getDrawingSpecifications().applyDocumentPreferences(mdcDocument.getDocumentPreferences());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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

            /*TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                View editor = findViewById(R.id.main_jmdceditor);
                FrameLayout.LayoutParams layoutParams = ((FrameLayout.LayoutParams) editor.getLayoutParams());
                layoutParams.topMargin = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                editor.setLayoutParams(layoutParams);
            }*/
            toolbar.requestFocus();
        }
        else if (editMode && !inEditMode) {
            inEditMode = true;
            Toolbar toolbar = findViewById(R.id.edit_toolbar);
            toolbar.setVisibility(View.GONE);
            AndroidUtils.hideKeyboard(this);

            /*View editor = findViewById(R.id.main_jmdceditor);
            FrameLayout.LayoutParams layoutParams = ((FrameLayout.LayoutParams) editor.getLayoutParams());
            layoutParams.topMargin = 0;
            editor.setLayoutParams(layoutParams);*/
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        MenuCompat.setGroupDividerEnabled(menu, true);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        RecentFiles.updateMenu(getApplicationContext(), menu.findItem(R.id.open_recent));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        JMDCEditor editor = findViewById(R.id.main_jmdceditor);
        JMDCEditorWorkflow workflow = editor.getWorkflow();

        switch (item.getGroupId()) {
            case R.id.recent_files_group:
                FileOpener.openFile(this, item.toString());
                break;
        }

        switch (item.getItemId()) {
            //Edit
            case R.id.undo:
                workflow.undo();
                return true;
            case R.id.redo:
                workflow.redo();
                return true;
            case R.id.cut:
                editor.cut();
                return true;
            case R.id.copy:
                editor.copy();
                return true;
            case R.id.paste:
                editor.paste();
                return true;
            case R.id.duplicate:
                editor.copy();
                editor.paste();
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
            case R.id.edit_hieroglyphic_text:
                workflow.setMode('s');
                return true;
            case R.id.edit_latin_text:
                workflow.setMode('l');
                return true;
            case R.id.edit_italic_text:
                workflow.setMode('i');
                return true;
            case R.id.edit_bold_text:
                workflow.setMode('b');
                return true;
            case R.id.edit_transliteration_text:
                workflow.setMode('t');
                return true;
            case R.id.edit_line_numbers:
                workflow.setMode('|');
                return true;
            case R.id.zoom:
                new ZoomFragment().show(getSupportFragmentManager(), "zoom");
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
                Toast.makeText(this, "Not currently supported.", Toast.LENGTH_LONG).show();
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
                try {
                    workflow.getHieroglyphicTextModel().insertMDCText(workflow.getCaret().getInsertPosition().getIndex(), "o");
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                }
                return true;
            case R.id.insert_black_point:
                try {
                    workflow.getHieroglyphicTextModel().insertMDCText(workflow.getCaret().getInsertPosition().getIndex(), "O");
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                }
                return true;
            case R.id.insert_full_size_shading:
                try {
                    workflow.getHieroglyphicTextModel().insertMDCText(workflow.getCaret().getInsertPosition().getIndex(), "//");
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                }
                return true;
            case R.id.insert_horizontal_shading:
                try {
                    workflow.getHieroglyphicTextModel().insertMDCText(workflow.getCaret().getInsertPosition().getIndex(), "h/");
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                }
                return true;
            case R.id.insert_vertical_shading:
                try {
                    workflow.getHieroglyphicTextModel().insertMDCText(workflow.getCaret().getInsertPosition().getIndex(), "v/");
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                }
                return true;
            case R.id.insert_quarter_shading:
                try {
                    workflow.getHieroglyphicTextModel().insertMDCText(workflow.getCaret().getInsertPosition().getIndex(), "/");
                } catch (MDCSyntaxError mdcSyntaxError) {
                    mdcSyntaxError.printStackTrace();
                }
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
                new ShadingChooserFragment().show(getSupportFragmentManager(), "shading chooser");
                return true;
            case R.id.cartouches:
                new CartoucheChooserFragment().show(getSupportFragmentManager(), "cartouche chooser");
                return true;
            case R.id.philological_markup:
                new PhilologicalChooserFragment().show(getSupportFragmentManager(), "philological chooser");
                return true;

            //File
            case R.id.new_file:
                mdcDocument = new MDCDocument();
                editor.setHieroglyphiTextModel(new HieroglyphicTextModel());
                editor.setDrawingSpecifications(new DrawingSpecificationsImplementation());
                editor.getWorkflow().setMode('s');
                return true;
            case R.id.open:
                startActivityForResult(new Intent(this, OpenActivity.class), OPEN_FILE_REQUEST_CODE);
                return true;
            case R.id.open_recent:
                //NO-OP
                return true;
            case R.id.clear_recent_files:
                RecentFiles.clear(getApplicationContext());
                return true;
            case R.id.close:
                //TODO
                Toast.makeText(this, "Not currently supported.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.save:
                if (mdcDocument.getFile() != null) {
                    MDCDocument newDocument = new MDCDocument(editor.getHieroglyphicTextModel().getModel(), editor.getDrawingSpecifications());
                    newDocument.setFile(mdcDocument.getFile());
                    newDocument.setDialect(mdcDocument.getDialect());
                    newDocument.setEncoding(mdcDocument.getEncoding());
                    mdcDocument = newDocument;
                    try {
                        mdcDocument.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RecentFiles.update(getApplicationContext(), mdcDocument.getFile().getName());
                    return true;
                }
            case R.id.save_as:
                mdcDocument = new MDCDocument(editor.getHieroglyphicTextModel().getModel(), editor.getDrawingSpecifications());
                new DocumentSaverDialogFragment().show(getSupportFragmentManager(), "save");
                return true;
            case R.id.import_file:
                //TODO
                Toast.makeText(this, "Files must currently be moved into Android/data/jsesh.android.app/files", Toast.LENGTH_LONG).show();
                return true;
            case R.id.export:
                //NO-OP
                return true;
            case R.id.set_as_model:
                ModelPreferences.setAsModel(getApplicationContext(), editor.getDrawingSpecifications());
                return true;
            case R.id.use_model_preferences:
                ModelPreferences.useModelPreferences(getApplicationContext(), editor.getDrawingSpecifications());
                updateFullModel(editor);
                return true;
            case R.id.document_properties:
                Intent data =  new Intent(this, DocumentPropertiesActivity.class);
                DrawingSpecification spec = editor.getDrawingSpecifications();
                data.putExtra("a1SignHeight", spec.getStandardSignHeight());
                data.putExtra("lineSpacing", spec.getLineSkip());
                data.putExtra("spaceBetweenQuadrants", spec.getSmallSkip());
                data.putExtra("columnSkip", spec.getColumnSkip());
                data.putExtra("maximalQuadrantHeight", spec.getMaxCadratHeight());
                data.putExtra("maximalQuadrantWidth", spec.getMaxCadratWidth());
                data.putExtra("smallFontBody", spec.getSmallBodyScaleLimit());
                data.putExtra("cartoucheLineWidth", spec.getCartoucheLineWidth());
                data.putExtra("useLinesForShading", spec.getShadingStyle() == ShadingStyle.LINE_HATCHING);
                startActivityForResult(data, DOCUMENT_PROPERTIES_REQUEST_CODE);
                return true;
            case R.id.format:
                //NO-OP
                return true;
            case R.id.text_in_lines:
                item.setChecked(true);
                editor.setTextOrientation(TextOrientation.HORIZONTAL);
                return true;
            case R.id.text_in_columns:
                item.setChecked(true);
                editor.setTextOrientation(TextOrientation.VERTICAL);
                return true;
            case R.id.left_to_right_text:
                item.setChecked(true);
                editor.setMDCTextDirection(TextDirection.LEFT_TO_RIGHT);
                return true;
            case R.id.right_to_left_text:
                item.setChecked(true);
                editor.setMDCTextDirection(TextDirection.RIGHT_TO_LEFT);
                return true;
            case R.id.center_small_signs:
                item.setChecked(!item.isChecked());
                editor.setSmallSignsCentered(item.isChecked());
                updateFullModel(editor);
                return true;
            case R.id.justify_text:
                item.setChecked(!item.isChecked());
                editor.getDrawingSpecifications().setJustified(item.isChecked());
                return true;

            //Export
            case R.id.export_bitmap:
                Intent intent = new Intent(this, BitmapExporterActivity.class);
                startActivityForResult(intent, BITMAP_EXPORTER_REQUEST_CODE);
                return true;
            case R.id.export_pdf:
                Intent intent2 = new Intent(this, PDFExporterActivity.class);
                startActivityForResult(intent2, PDF_EXPORTER_REQUEST_CODE);
                return true;

                //Palette
            case R.id.palette:
                new PaletteFragment().show(getSupportFragmentManager(), "palette");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Commonly accessed
        JMDCEditor editor = findViewById(R.id.main_jmdceditor);


        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case READ_REQUEST_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        FileOpener.openFile(this, uri.getPath());
                    }
                    break;

                case OPEN_FILE_REQUEST_CODE:
                    String filename = data.getStringExtra("filename");
                    FileOpener.openFile(this, filename);
                    break;

                case BITMAP_EXPORTER_REQUEST_CODE:
                    BitmapExporter bitmapExporter = new BitmapExporter(getApplicationContext());
                    bitmapExporter.applySettings(
                            data.getStringExtra("filename"),
                            data.getIntExtra("cadratHeight", 0),
                            data.getBooleanExtra("transparency", false),
                            data.getIntExtra("fileOutputFormat", 0),
                            false);
                    bitmapExporter.export(new ExportData(editor.getDrawingSpecifications(), editor.getWorkflow().getCaret(), editor.getHieroglyphicTextModel().getModel(), 1));
                    break;

                case PDF_EXPORTER_REQUEST_CODE:
//                    PDFExporter pdfExporter = new PDFExporter(getApplicationContext());
//                    pdfExporter.applySettings(
//                            data.getStringExtra("filename"),
//                            data.getIntExtra("cadratHeight", 0),
//                            data.getBooleanExtra("cadratHeight", false),
//                            data.getIntExtra("fileOutputFormat", 0),
//                            false);
//                    bitmapExporter.export(new ExportData(editor.getDrawingSpecifications(), editor.getWorkflow().getCaret(), editor.getHieroglyphicTextModel().getModel(), 1));
                    break;

                case DOCUMENT_PROPERTIES_REQUEST_CODE:
                    DrawingSpecification spec = editor.getDrawingSpecifications();

                    //TODO Maybe check that all the values exist, rather than failing silently (though there is probably no reason why it would fail)
                    spec.setStandardSignHeight(data.getFloatExtra("a1SignHeight", spec.getStandardSignHeight()));
                    spec.setLineSkip(data.getFloatExtra("lineSpacing", spec.getLineSkip()));
                    spec.setSmallSkip(data.getFloatExtra("spaceBetweenQuadrants", spec.getSmallSkip()));
                    spec.setColumnSkip(data.getFloatExtra("columnSkip", spec.getColumnSkip()));
                    spec.setMaxCadratHeight(data.getFloatExtra("maximalQuadrantHeight", spec.getMaxCadratHeight()));
                    spec.setMaxCadratWidth(data.getFloatExtra("maximalQuadrantWidth", spec.getMaxCadratWidth()));
                    spec.setSmallBodyScaleLimit(data.getFloatExtra("smallFontBody", (float)spec.getSmallBodyScaleLimit()));
                    spec.setCartoucheLineWidth(data.getFloatExtra("cartoucheLineWidth", spec.getCartoucheLineWidth()));

                    if (data.getBooleanExtra("useLinesForShading", spec.getShadingStyle() == ShadingStyle.LINE_HATCHING)) {
                        spec.setShadingStyle(ShadingStyle.LINE_HATCHING);
                    }
                    else spec.setShadingStyle(ShadingStyle.GRAY_SHADING);

                    updateFullModel(editor);
                    break;

            }

        }

    }

    private void updateFullModel(JMDCEditor editor) {
        //Hack to update the entire model
        //Seems to work, though probably inefficient to rebuild the model
        //TODO There must be a better way to do this
        String code = editor.getWorkflow().getMDCCode();

        MDCCaret caret = editor.getWorkflow().getCaret();
        int insertIndex = caret.getInsertPosition().getIndex();
        int markIndex = -1; //-1 if there is no mark
        if (caret.hasMark()) markIndex = caret.getMarkPosition().getIndex();

        try {
            editor.getWorkflow().setMDCCode(code);
            editor.getWorkflow().getCaret().moveInsertTo(insertIndex);
            if (markIndex != -1) editor.getWorkflow().getCaret().setMarkAt(markIndex);
        } catch (MDCSyntaxError mdcSyntaxError) {
            //TODO Something went wrong? Though should be impossible
            mdcSyntaxError.printStackTrace();
        }
    }

}
