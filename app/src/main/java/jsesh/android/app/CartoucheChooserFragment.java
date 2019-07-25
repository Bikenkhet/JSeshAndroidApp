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

public class CartoucheChooserFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_cartouche_chooser, null);
        builder.setView(alertView);
        builder.setTitle(R.string.cartouches);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        final Dialog dialog = builder.create();

        int[] views = new int[] {
                R.id.cartouche0, 'c', 1, 2,
                R.id.cartouche1, 'c', 1, 1,
                R.id.cartouche2, 'c', 2, 1,
                R.id.cartouche3, 'c', 2, 1,
                R.id.cartouche4, 'c', 0, 1,
                R.id.cartouche5, 'c', 1, 0,
                R.id.cartouche6, 'c', 2, 0,
                R.id.cartouche7, 'c', 0, 2,
                R.id.cartouche8, 's', 1, 2,
                R.id.cartouche9, 's', 2, 1,
                R.id.cartouche10, 'h', 1, 2,
                R.id.cartouche11, 'h', 1, 3,
                R.id.cartouche12, 'h', 1, 1,
                R.id.cartouche13, 'h', 1, 0,
                R.id.cartouche14, 'h', 2, 1,
                R.id.cartouche15, 'h', 2, 0,
                R.id.cartouche16, 'h', 3, 1,
                R.id.cartouche17, 'h', 3, 0,
                R.id.cartouche18, 'h', 0, 2,
                R.id.cartouche19, 'h', 0, 3,
                R.id.cartouche20, 'h', 0, 1,
                R.id.cartouche21, 'h', 0, 0,
                //R.id.cartouche21, 'f', 0, 0,  //Broken in desktop version
        };

        for (int vi=0; vi<views.length/4; vi++) {
            int id = views[vi*4];
            final int type = views[vi*4 + 1];
            final int left = views[vi*4 + 2];
            final int right = views[vi*4 + 3];
            alertView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JMDCEditor editor = ((EditActivity)getActivity()).findViewById(R.id.main_jmdceditor);
                    editor.getWorkflow().addCartouche(type, left, right);
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
