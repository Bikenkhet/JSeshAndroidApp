package jsesh.android.app;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import jsesh.editor.JMDCEditor;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.file.MDCDocumentReader;

public class FileOpener {

    private FileOpener() {}

    public static void openFile(EditActivity edit, String filename) {
        MDCDocumentReader reader = new MDCDocumentReader();
        try {
            File file = new File(edit.getExternalFilesDir(null), filename);
            edit.mdcDocument = reader.loadFile(file);

            JMDCEditor editor = edit.findViewById(R.id.main_jmdceditor);
            editor.setHieroglyphiTextModel(edit.mdcDocument.getHieroglyphicTextModel());
            editor.getDrawingSpecifications().applyDocumentPreferences(edit.mdcDocument.getDocumentPreferences());

            edit.getSupportActionBar().setTitle(file.getName());

            RecentFiles.update(edit.getApplicationContext(), filename);

            Toast.makeText(edit.getApplicationContext(), file.getName(), Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (MDCSyntaxError mdcSyntaxError) {
            mdcSyntaxError.printStackTrace();
        }
    }

}
