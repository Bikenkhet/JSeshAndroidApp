package jsesh.android.app;

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
            edit.mdcDocument = reader.loadFile(new File(edit.getExternalFilesDir(null), filename));
            JMDCEditor editor = edit.findViewById(R.id.main_jmdceditor);
            editor.setHieroglyphiTextModel(edit.mdcDocument.getHieroglyphicTextModel());
            editor.getDrawingSpecifications().applyDocumentPreferences(edit.mdcDocument.getDocumentPreferences());

            RecentFiles.update(edit.getApplicationContext(), filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MDCSyntaxError mdcSyntaxError) {
            mdcSyntaxError.printStackTrace();
        }
    }

}
