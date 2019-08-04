package jsesh.android.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import jsesh.editor.JMDCEditor;
import jsesh.mdc.constants.SymbolCodes;
import jsesh.mdc.model.Hieroglyph;

public class PhilologicalChooserFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_philological_chooser, null);
        builder.setView(alertView);
        builder.setTitle(R.string.philological_markup);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        final Dialog dialog = builder.create();

        //TODO This class should be cleaned up, perhaps the full layout could be constructed programmatically
        //TODO Maybe align in 3 columns for direct correspondence

        int[] views = new int[] {
                R.id.philological0,
                R.id.philological1,
                R.id.philological2,
                R.id.philological3,
                R.id.philological4,
                R.id.philological5,
                R.id.philological6,
                R.id.philological7,
                R.id.philological8,
                R.id.philological9,
                R.id.philological10,
                R.id.philological11,
                R.id.philological12,
                R.id.philological13,
                R.id.philological14,
                R.id.philological15,
                R.id.philological16,
                R.id.philological17,
                R.id.philological18,
                R.id.philological19,
                R.id.philological20,
                R.id.philological21,
                R.id.philological22,
                R.id.philological23,
                R.id.philological24,
                R.id.philological25,
        };

        GridLayout grid = alertView.findViewById(R.id.gridLayoutPhilological);

        List<ImageView> children = new ArrayList<>();
        int childCount = grid.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView v = (ImageView) grid.getChildAt(i);
            children.add(v);
        }

        grid.removeAllViews();

        for (int vi=0; vi<views.length; vi++) {
            final int code = vi;

            RelativeLayout l = new RelativeLayout(alertView.getContext());
            ImageView v = children.get(code);
            l.addView(v);
            v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            grid.addView(l);
            l.setLayoutParams(new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED, 1f), GridLayout.spec(GridLayout.UNDEFINED, 1f)));


            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JMDCEditor editor = ((EditActivity)getActivity()).findViewById(R.id.main_jmdceditor);

                    int[] codes = new int[] {
                            SymbolCodes.EDITORADDITION,
                            SymbolCodes.ERASEDSIGNS,
                            SymbolCodes.PREVIOUSLYREADABLE,
                            SymbolCodes.SCRIBEADDITION,
                            SymbolCodes.EDITORSUPERFLUOUS,
                            SymbolCodes.MINORADDITION,
                            SymbolCodes.DUBIOUS,
                            SymbolCodes.BEGINERASE,
                            SymbolCodes.ENDERASE,
                            SymbolCodes.BEGINEDITORADDITION,
                            SymbolCodes.ENDEDITORADDITION,
                            SymbolCodes.BEGINEDITORSUPERFLUOUS,
                            SymbolCodes.ENDEDITORSUPERFLUOUS,
                            SymbolCodes.BEGINPREVIOUSLYREADABLE,
                            SymbolCodes.ENDPREVIOUSLYREADABLE,
                            SymbolCodes.BEGINSCRIBEADDITION,
                            SymbolCodes.ENDSCRIBEADDITION,
                            SymbolCodes.BEGINMINORADDITION,
                            SymbolCodes.ENDMINORADDITION,
                            SymbolCodes.BEGINDUBIOUS,
                            SymbolCodes.ENDDUBIOUS,
                    };

                    String[] signs = new String[] {
                            "PF1",
                            "PF2",
                            "PF3",
                            "PF4",
                            "PF5",
                    };

                    if (code < 7) editor.getWorkflow().addPhilologicalMarkup(codes[code]);
                    else if (code < 21) editor.getWorkflow().insertElement(new Hieroglyph(codes[code]).buildTopItem());
                    else  editor.getWorkflow().addSign(signs[code-21]);

                    dismiss();

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
