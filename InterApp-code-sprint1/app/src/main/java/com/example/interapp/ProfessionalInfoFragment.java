package com.example.interapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class ProfessionalInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HashMap<String, Integer> stateValue = new HashMap<String, Integer>();
    private final String VALUE_KEY = "profInfoKey";

    /**
     * Fires when a configuration change occurs and fragment needs to save state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(VALUE_KEY, stateValue);
        super.onSaveInstanceState(outState);
    }

    /**
     * default method to be executed on creation of generalInfo fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * return view of general info fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professionalinfo, container, false);
        if (savedInstanceState != null) {
            // restore stateValue on all corresponding fields
            stateValue = (HashMap<String, Integer>) savedInstanceState.getSerializable(VALUE_KEY);
        } else {
            //show default values for all spinners
            stateValue.put("spinnerEducation", 0);
            stateValue.put("spinnerFrequency", 0);
            //TODO: implement for remaining fields -- dont forget religion in between
        }

        //education
        QuestionnaireActivity.spinnerEducation = (Spinner) view.findViewById(R.id.spinnerEducation);
        ArrayAdapter<CharSequence> adapterEducation = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.education_array, android.R.layout.simple_spinner_item);
        adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerEducation.setAdapter(adapterEducation);
        QuestionnaireActivity.spinnerEducation.setSelection(stateValue.get("spinnerEducation"));
        QuestionnaireActivity.spinnerEducation.setOnItemSelectedListener(this);

        //profession
        //TODO: find or make list of all professions

        //professional training
        //TODO: find or make list of all professional training

        //employer category
        //TODO: find or make list of all employer category

        //employer domain
        //TODO: find or make list of all employer domain

        //job position
        //TODO: find or make list of all job positions

        //frequency of contact with immigrants
        QuestionnaireActivity.spinnerFrequency = (Spinner) view.findViewById(R.id.spinnerFrequency);
        ArrayAdapter<CharSequence> adapterFrequency = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.frequency_array, android.R.layout.simple_spinner_item);
        adapterFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerFrequency.setAdapter(adapterFrequency);
        QuestionnaireActivity.spinnerFrequency.setSelection(stateValue.get("spinnerFrequency"));
        QuestionnaireActivity.spinnerFrequency.setOnItemSelectedListener(this);



        QuestionnaireActivity.spinnerProfession = (Spinner) view.findViewById(R.id.spinnerProfession);
        QuestionnaireActivity.spinnerProfessionalTraining = (Spinner) view.findViewById(R.id.spinnerProfessionalTraining);
        QuestionnaireActivity.spinnerEmployerCategory = (Spinner) view.findViewById(R.id.spinnerEmployerCategory);
        QuestionnaireActivity.spinnerEmployerDomain = (Spinner) view.findViewById(R.id.spinnerEmployerDomain);
        QuestionnaireActivity.spinnerJobPosition = (Spinner) view.findViewById(R.id.spinnerJobPosition);

        return view;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
