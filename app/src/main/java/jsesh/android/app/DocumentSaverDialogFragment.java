package jsesh.android.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;

import jsesh.mdc.file.MDCDocument;

public class DocumentSaverDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.activity_document_saver, null);
        builder.setView(alertView);
        builder.setTitle(R.string.save);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String filename = ((EditText)alertView.findViewById(R.id.editTextSaveFile)).getText().toString();
                if (filename.length() == 0) filename = getString(R.string.default_file_name);
                MDCDocument mdcDocument = (MDCDocument) StaticTransfer.obj;
                mdcDocument.setFile(new File(getContext().getExternalFilesDir(null), filename + ".gly"));
                try {
                    mdcDocument.save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    @Override
    public void onDestroy() {
        StaticTransfer.obj = null;
        super.onDestroy();
    }
}
