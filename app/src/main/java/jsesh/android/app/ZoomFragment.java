package jsesh.android.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import jsesh.editor.JMDCEditor;

public class ZoomFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_zoom, null);
        builder.setView(alertView);
        builder.setTitle(R.string.zoom);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        final Dialog dialog = builder.create();

        final JMDCEditor editor = getActivity().findViewById(R.id.main_jmdceditor);
        final TextView zoomText = alertView.findViewById(R.id.textView20);

        final double[] zooms = new double[] {
                25, 50, 75, 100, 112, 128, 150, 200, 400, 600, 800, 1600, 3200, 6400, 12800
        };

        SeekBar seek = alertView.findViewById(R.id.seekBar);
        seek.setMax(zooms.length-1);

        boolean set = false;
        for (int p=0; p<zooms.length; p++) {
            if ((int)zooms[p] >= (int)(editor.getScale()*100.0)) {
                seek.setProgress(p);
                set = true;
                break;
            }
        }
        if (!set) seek.setProgress(zooms.length-1);

        zoomText.setText((int)(editor.getScale()*100.0)+"%");

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoomText.setText((int)zooms[progress]+"%");
                editor.setScale(zooms[progress]*0.01);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

        });


        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
