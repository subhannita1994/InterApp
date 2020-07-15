package com.example.interapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.List;

public class MultiSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

    /**
     * default spinner constructor
     * @param context
     */
    public MultiSpinner(Context context) {
        super(context);
    }

    /**
     * spinner constructor for specified attribute set
     * @param arg0
     * @param arg1
     */
    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    /**
     * spinner constructor for two attribute sets
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    /**
     * onclick event to check the box for selected item
     * @param dialog
     * @param which
     * @param isChecked
     */
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;
    }

    /**
     * ...
     * @param dialog
     */
    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemSelected(selected);
    }

    /**
     * ...
     * @return
     */
    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    /**
     * set items for spinner
     * @param items
     * @param allText
     * @param listener
     */
    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all unselected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }

    public void setSelectedItems(ArrayList<Integer> selectedIndices){

        StringBuffer spinnerBuffer = new StringBuffer();
        for(int i =0;i< selectedIndices.size();i++) {
            selected[selectedIndices.get(i)] = true;
            spinnerBuffer.append(items.get(selectedIndices.get(i)));
            spinnerBuffer.append(", ");
        }

        String spinnerText = spinnerBuffer.toString();
        if (spinnerText.length() > 2)
            spinnerText = spinnerText.substring(0, spinnerText.length() - 2);

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { spinnerText });
        setAdapter(adapter);
    }

    /**
     * return list of indices of items that are selected
     * @return
     */
    public ArrayList<Integer> getSelectedItemPositions(){

        ArrayList<Integer> result = new ArrayList<>();
        for (int i =0;i<items.size();i++)
            if(selected[i]==true)
                result.add(i);

        return result;
    }

    public String getSelectedItems(){

        String result="";
        for(int i=0;i<items.size();i++)
            if(selected[i]==true)
                result+=items.get(i)+",";
        return result;
    }

    public static interface MultiSpinnerListener {

        void onItemSelected(boolean[] selected);
    }
}
