package jsesh.android.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import jsesh.editor.JMDCEditor;

public class ShadingChooserFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_shading_chooser, null);
        builder.setView(alertView);
        builder.setTitle(R.string.shading);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        final Dialog dialog = builder.create();

        int[] views = new int[] {
                R.id.shading0,
                R.id.shading1,
                R.id.shading2,
                R.id.shading3,
                R.id.shading4,
                R.id.shading5,
                R.id.shading6,
                R.id.shading7,
                R.id.shading8,
                R.id.shading9,
                R.id.shading10,
                R.id.shading11,
                R.id.shading12,
                R.id.shading13,
                R.id.shading14,
                R.id.shading15,
        };

        for (int vi=0; vi<views.length; vi++) {
            final int shadingCode = vi;
            alertView.findViewById(views[vi]).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JMDCEditor editor = ((EditActivity)getActivity()).findViewById(R.id.main_jmdceditor);
                    editor.getWorkflow().doShade(shadingCode);
                    dialog.dismiss();
                }
            });
        }

        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
