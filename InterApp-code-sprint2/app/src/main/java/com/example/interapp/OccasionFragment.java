package com.example.interapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class OccasionFragment extends Fragment implements AdapterView.OnFocusChangeListener {

    private HashMap<String,Object> stateValue = new HashMap<String,Object>();
    private final String VALUE_KEY = "occasionKey";
    View mainView;


    /**
     * Fires when a configuration change occurs and fragment needs to save state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(VALUE_KEY,stateValue);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_occasion, container, false);
        if (savedInstanceState != null) {
            // restore stateValue on all corresponding fields
            stateValue = (HashMap<String, Object>) savedInstanceState.getSerializable(VALUE_KEY);
        }

        else {
            //show default values for all spinners
            stateValue.put("spinnerRelation",0);
            stateValue.put("textInputLocation","");
            stateValue.put("textInputSituation","");
        }

        //location text input
        QuestionnaireActivity.textInputLocation = (EditText) mainView.findViewById(R.id.textInputLocation);
        QuestionnaireActivity.textInputLocation.setText((String)stateValue.get("textInputLocation"), TextView.BufferType.EDITABLE);
        QuestionnaireActivity.textInputLocation.setOnFocusChangeListener(this);

        //situation text input
        QuestionnaireActivity.textInputSituation = (EditText) mainView.findViewById(R.id.textInputSituation);
        QuestionnaireActivity.textInputSituation.setText((String)stateValue.get("textInputSituation"),TextView.BufferType.EDITABLE);
        QuestionnaireActivity.textInputLocation.setOnFocusChangeListener(this);

        return mainView;
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }


}