//ANDROID MODIFIED

package jsesh.android.app;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A changeable unit holder to use in Swing applications. Manages a combobox and
 * a list of text fields.
 *
 * @author rosmord
 */
public class UnitMediator {

    private LengthUnit currentUnit = LengthUnit.POINT;
    private Spinner managedCombobox = null;
    private final List<EditText> textFields = new ArrayList<>();

    private ArrayAdapter<LengthUnit> adapter;

    public void setCurrentUnit(LengthUnit newUnit) {
        LengthUnit oldValue = this.currentUnit;
        if (oldValue != newUnit) {
            this.currentUnit= newUnit;
            // Update display.
            if (managedCombobox.getSelectedItem() != currentUnit) {
                managedCombobox.setSelection(adapter.getPosition(currentUnit));
            }
            // Update text fields (which are supposed to be originaly in points.
            for (EditText f : textFields) {
                // convert the old length in points:
                double points= oldValue.toPoints(Double.parseDouble(f.getText().toString()));
                double newValue= this.currentUnit.fromPoints(points);
                f.setText(newValue+"");
            }
        }
    }

    public LengthUnit getCurrentUnit() {
        return currentUnit;
    }

    public void managedTextField(EditText field) {
        textFields.add(field);
    }

    public double getManagedFieldInPoints(EditText field) {
        return currentUnit.getValueFromTextField(field);
    }

    /**
     * Attach this UnitMediator to a combobox for two ways linking.
     *
     * @param unitField
     */
    public void attachToComboBox(Spinner unitField) {
        adapter = new ArrayAdapter<LengthUnit>(unitField.getContext(), R.layout.adapter_units, R.id.textViewAdapterUnits, LengthUnit.values());
        unitField.setAdapter(adapter);
        managedCombobox = unitField;
        managedCombobox.setSelection(adapter.getPosition(currentUnit));
        unitField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatedCombobox();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO Throw exception?
            }
        });
    }

    private void updatedCombobox() {
        if (managedCombobox.getSelectedItem() != null
                && managedCombobox.getSelectedItem() != currentUnit) {
            setCurrentUnit((LengthUnit) managedCombobox.getSelectedItem());
        }
    }

}