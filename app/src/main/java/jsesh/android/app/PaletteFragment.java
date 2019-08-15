package jsesh.android.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Pattern;

import jsesh.android.graphics.ImageIconFactory;
import jsesh.editor.JMDCEditor;
import jsesh.hieroglyphs.CompositeHieroglyphsManager;
import jsesh.hieroglyphs.GardinerCode;
import jsesh.hieroglyphs.ManuelDeCodage;
import jsesh.hieroglyphs.Possibility;
import jsesh.hieroglyphs.SignDescriptionConstants;

public class PaletteFragment extends DialogFragment {

    private static final Pattern phoneticPattern = Pattern.compile("[A-Za-z]*");

    private String selectedCode = null;

    private PaletteHieroglyphFamilyManager familyManager;

    private Set<String> userPaletteSigns = new HashSet<>();

    private boolean initialised = false;

    private List<String> recentlySelectedSigns = new ArrayList<>();

    private List<String> latestSigns = new ArrayList<>();

    private List<String> displayedSigns = new ArrayList<>();

    private CompositeHieroglyphsManager hieroglyphsManager = CompositeHieroglyphsManager.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_palette, null);
        builder.setView(alertView);
        builder.setTitle(R.string.palette);

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

        //TODO Check
        if (initialised) {

        }
        else {
            init();
        }


    }

    private void init() {

        familyManager = new PaletteHieroglyphFamilyManager(getContext());

        RecyclerView recyclerView = getDialog().findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //TODO Check this
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        //FIXME Adjust for screen size
        ImageIconFactory.getInstance().setCadratHeight(90);

        PaletteCellAdapter adapter = new PaletteCellAdapter(new GlyphBitmap[0], this);
        recyclerView.setAdapter(adapter);

        //Spinners
        final Spinner family = getDialog().findViewById(R.id.spinner);
        final Spinner subFamily = getDialog().findViewById(R.id.spinner2);
        final Spinner subSubFamily = getDialog().findViewById(R.id.spinner3);

        //Family



        PaletteHieroglyphFamily[] familyNames = familyManager.getFamilies();

        family.setAdapter(new ArrayAdapter<>(getContext(), R.layout.adapter_spinner_basic, familyNames));
        //family.setSelection(0);
        family.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final PaletteHieroglyphFamily paletteFamily = (PaletteHieroglyphFamily)family.getSelectedItem();
                        final boolean showAll = ((CheckBox)getDialog().findViewById(R.id.checkBox)).isChecked();

                        //Set sub
                        Set<String> tags = new HashSet<>();
                        Collection<String> codes = getSignsCodeInPaletteFamily(paletteFamily, showAll);
                        for (String code : codes) {
                            tags.addAll(hieroglyphsManager.getTagsForSign(code));
                        }
                        List<String> sfn = new ArrayList<>(tags);


                        sfn.add(0, "");
                        final String[] subFamilyNames = new String[sfn.size()];
                        sfn.toArray(subFamilyNames);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subFamily.setAdapter(new ArrayAdapter<>(getContext(), R.layout.adapter_spinner_basic, subFamilyNames));
                            }
                        });

                    }
                }).start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        subFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        PaletteHieroglyphFamily paletteFamily = (PaletteHieroglyphFamily)family.getSelectedItem();
                        String subFamilyName = subFamily.getSelectedItem().toString();
                        boolean showAll = ((CheckBox)getDialog().findViewById(R.id.checkBox)).isChecked();

                        List<String> ssfn;

                        if (subFamilyName.equals("")) ssfn = new ArrayList<>();
                        else {
                            Set<String> tags = new HashSet<>();
                            Collection<String> codes = getSignsCodeInPaletteFamily(paletteFamily, showAll);
                            for (String code : codes) {
                                Collection<String> signTags = hieroglyphsManager.getTagsForSign(code);
                                if (signTags.contains(subFamilyName)) tags.addAll(signTags);
                            }
                            tags.remove(subFamilyName);
                            ssfn = new ArrayList<>(tags);
                        }

                        ssfn.add(0, "");
                        final String[] subSubFamilyNames = new String[ssfn.size()];
                        ssfn.toArray(subSubFamilyNames);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                subSubFamily.setAdapter(new ArrayAdapter<>(getContext(), R.layout.adapter_spinner_basic, subSubFamilyNames));
                            }
                        });

                    }
                }).start();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        subSubFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchByFamilyAsync();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ((CheckBox)getDialog().findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                searchByFamilyAsync();
            }
        });

        ((EditText)getDialog().findViewById(R.id.editText12)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String text = v.getText().toString();

                    if (text.matches(".*[0-9].*")) {
                        searchByPartialCodeAsync(text);
                    }
                    else searchByTransliteration(text);

                    return true;
                }
                return false;
            }
        });


        //Sign area

        getDialog().findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JMDCEditor editor = getActivity().findViewById(R.id.main_jmdceditor);
                if (selectedCode != null) {
                    editor.getWorkflow().insertMDC(selectedCode);
                    Toast.makeText(getContext(), selectedCode, Toast.LENGTH_SHORT).show();
                    addLatestSign(selectedCode);
                }
            }
        });

        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                SignInfoFragment.newInstance(selectedCode).show(getActivity().getSupportFragmentManager(), "sign info");
            }
        });
        gestureDetector.setIsLongpressEnabled(true);

        getDialog().findViewById(R.id.imageView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });






        ((CheckBox)getDialog().findViewById(R.id.checkBox2)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleUserPaletteSign(selectedCode, isChecked);
            }
        });

        getDialog().findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIconsAsync(new ArrayList<>(hieroglyphsManager.getVariants(selectedCode)));
            }
        });

        getDialog().findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycleRecentlySelectedSigns();
            }
        });

        getDialog().findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createIconsAsync(new ArrayList<>(hieroglyphsManager.getSignsContaining(selectedCode)));
            }
        });

        //Finish init

        initialised = true;

    }





    private void updateDataset(GlyphBitmap[] dataset) {
        final RecyclerView recyclerView = getDialog().findViewById(R.id.recyclerView);
        final PaletteCellAdapter adapter = new PaletteCellAdapter(dataset, this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public GlyphBitmap createIcon(String code) {
        return new GlyphBitmap(code);
    }

    public void createIcons(List<String> codes) {
        GlyphBitmap[] dataset = new GlyphBitmap[codes.size()];
        for (int i=0; i<dataset.length; i++) dataset[i] = createIcon(codes.get(i));
        createIconsAsync(dataset);
        updateDataset(dataset);
    }

    public void createIconsAsync(final GlyphBitmap[] dataset) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (GlyphBitmap b : dataset) b.getBitmap();
            }
        }).start();
    }

    public void createIconsAsync(final List<String> codes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GlyphBitmap[] dataset = new GlyphBitmap[codes.size()];
                for (int i=0; i<dataset.length; i++) {
                    dataset[i] = createIcon(codes.get(i));
                    //Create the first icons that will be required to prevent freezing
                    //TODO Find actual size needed
                    if (i < 30) dataset[i].getBitmap();
                }
                createIconsAsync(dataset);
                updateDataset(dataset);
                displayedSigns = codes;
            }
        }).start();
    }

    public void searchByFamilyAsync() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchByFamily();
            }
        }).start();
    }

    public void searchByFamily() {
        System.out.println("SEARCH");

        PaletteHieroglyphFamily family = (PaletteHieroglyphFamily)((Spinner)getDialog().findViewById(R.id.spinner)).getSelectedItem();
        String subFamilyName = (String)((Spinner)getDialog().findViewById(R.id.spinner2)).getSelectedItem();
        String subSubFamilyName = (String)((Spinner)getDialog().findViewById(R.id.spinner3)).getSelectedItem();
        boolean showAll = ((CheckBox)getDialog().findViewById(R.id.checkBox)).isChecked();

        Collection<String> familySigns = getSignsCodeInPaletteFamily(family, showAll);

        List<String> codes = new ArrayList<>();

        for (String code : familySigns) {
            if (subFamilyName != null && !subFamilyName.equals("")) {
                Collection<String> tags = hieroglyphsManager.getTagsForSign(code);
                if (tags.contains(subFamilyName)) {
                    if (subSubFamilyName != null && !subSubFamilyName.equals("")) {
                        if (tags.contains(subSubFamilyName)) codes.add(code);
                    }
                    else codes.add(code);
                }
            }
            else codes.add(code);
        }

        createIconsAsync(codes);
    }

    public void searchByTransliteration(String value) {
        List<Possibility> possibilityList = hieroglyphsManager.getPossibilityFor(value, SignDescriptionConstants.PALETTE).asList();
        List<String> codes = new ArrayList<>();
        for (Possibility poss : possibilityList) {
            String code = poss.getCode();
            if (!phoneticPattern.matcher(code).matches()) codes.add(code);
        }
        GlyphBitmap[] dataset = new GlyphBitmap[codes.size()];
        for (int i=0; i<dataset.length; i++) dataset[i] = createIcon(codes.get(i));
        updateDataset(dataset);
    }

    public void searchByPartialCodeAsync(final String value) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<Possibility> possibilityList = (hieroglyphsManager.getSuitableSignsForCode(value)
                        .add(hieroglyphsManager.getCodesStartingWith(value))).asList();
                List<String> codes = new ArrayList<>();
                for (Possibility poss : possibilityList) {
                    String code = poss.getCode();
                    if (!phoneticPattern.matcher(code).matches()) codes.add(code);
                }
                createIconsAsync(codes);

            }
        }).start();
    }

    public void updateSelectedSign(String code) {
        TextView codeText = getDialog().findViewById(R.id.textView26);
        TextView valuesText = getDialog().findViewById(R.id.textView27);
        ImageView signImage = getDialog().findViewById(R.id.imageView);
        CheckBox userPalette = getDialog().findViewById(R.id.checkBox2);


        this.selectedCode = code;

        codeText.setText(code);

        String valuesString = "";
        List<String> values = hieroglyphsManager.getValuesFor(code);
        if (values.size() != 0) {
            for (int i=0; i<values.size()-1; i++) valuesString += values.get(i) + ", ";
            valuesString += values.get(values.size()-1);
        }
        valuesText.setText(valuesString);

        Bitmap bitmap = ImageIconFactory.getInstance().buildScaledGlyphImage(code, 150);
        signImage.setImageBitmap(bitmap);

        userPalette.setChecked(userPaletteSigns.contains(code));

        addToRecentlySelectedSigns(code);
    }

    private Collection<String> getSignsCodeInPaletteFamily(PaletteHieroglyphFamily family, boolean showAll) {

        //TODO Family 'other'

        Collection<String> result;

        //TODO Should not use numbers here
        if (family.id == 0) {
            result = latestSigns;
        }
        else if (family.id == 1) {
            result = userPaletteSigns;
        }
        else if (family.id == 31) {
            result = hieroglyphsManager.getCodesForFamily("", showAll);
        }
        else if (family.id == 32) {
            result = ManuelDeCodage.getInstance().getTallNarrowSigns();
        }
        else if (family.id == 33) {
            result = ManuelDeCodage.getInstance().getLowBroadSigns();
        }
        else if (family.id == 34) {
            result = ManuelDeCodage.getInstance().getLowNarrowSigns();
        }
        else {
            result = hieroglyphsManager.getCodesForFamily(family.getFamilyCode(), showAll);
        }
        return new ArrayList<>(result);
    }

    private void toggleUserPaletteSign(String code, boolean inUserPalette) {
        if (code == null) return;
        if (inUserPalette) userPaletteSigns.add(code);
        else userPaletteSigns.remove(code);
    }

    private void addToRecentlySelectedSigns(String code) {
        recentlySelectedSigns.remove(code);
        recentlySelectedSigns.add(code);
        //A maximum of six signs that can be cycled through
        if (recentlySelectedSigns.size() > 6) recentlySelectedSigns.remove(0);
    }

    private void cycleRecentlySelectedSigns() {
        int size = recentlySelectedSigns.size();
        if (size > 0) {
            String oldCode = recentlySelectedSigns.get(size-1);
            recentlySelectedSigns.remove(size-1);
            recentlySelectedSigns.add(0, oldCode);
            updateSelectedSign(recentlySelectedSigns.get(size-1));
        }
    }

    private void addLatestSign(String code) {
        latestSigns.remove(code);
        latestSigns.add(0, code);
    }

    private void selectFilteredContaining(String code) {
        if (code != null) {
            // Compute the closure of the part of relation.
            TreeSet<String> containingSign = new TreeSet<>();
            Stack<String> toDo = new Stack<>();
            toDo.add(code);
            while (!toDo.isEmpty()) {
                String sign = toDo.pop();
                if (!containingSign.contains(sign)) {
                    toDo.addAll(hieroglyphsManager.getSignsContaining(sign));
                    containingSign.add(sign);
                }
            }
            // Closure computed. Now the list of displayed signs:
            TreeSet<String> codes = new TreeSet<>(GardinerCode.getCodeComparator());
            codes.addAll(getDisplayedSigns());
            codes.retainAll(containingSign);
            //selectNoFamily(); //TODO
            createIcons(new ArrayList<>(codes));
        }
    }

    private List<String> getDisplayedSigns() {
        return displayedSigns;
    }


}


