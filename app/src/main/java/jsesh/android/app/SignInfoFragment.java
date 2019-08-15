package jsesh.android.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import jsesh.editor.JMDCEditor;
import jsesh.hieroglyphs.CompositeHieroglyphsManager;
import jsesh.hieroglyphs.DefaultHieroglyphicFontManager;
import jsesh.hieroglyphs.ShapeChar;

public class SignInfoFragment extends DialogFragment {

    public static SignInfoFragment newInstance(String code) {
        SignInfoFragment f = new SignInfoFragment();

        Bundle args = new Bundle();
        args.putString("code", code);
        f.setArguments(args);

        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_sign_info, null);
        builder.setView(alertView);
        builder.setTitle(R.string.sign_description);

        builder.setNegativeButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        String code = getArguments().getString("code");
        setFields(code);

    }

    public void setFields(String code) {

        //TODO i18n

        CompositeHieroglyphsManager hieroglyphsManager = CompositeHieroglyphsManager.getInstance();
        JMDCEditor signDescriptionField = getDialog().findViewById(R.id.jmdceditor);
        TextView glyphDescriptionField = getDialog().findViewById(R.id.textView29);
        //TODO Use correct display size
        signDescriptionField.setScale(3);

        String fullDescription = hieroglyphsManager.getDescriptionFor(code);

        try {
            signDescriptionField.setMDCText(fullDescription);
        } catch (RuntimeException e) {
            e.printStackTrace();
            signDescriptionField.setMDCText("+lErroneous code for +s"
                    + code + "+l " + code
                    + " description. Please notice or correct.");
        }

        ShapeChar shape = DefaultHieroglyphicFontManager.getInstance().get(
                code);
        if (shape != null) {
            glyphDescriptionField.setText(shape.getDocumentation());
        } else {
            glyphDescriptionField.setText("");
        }

    }

}
