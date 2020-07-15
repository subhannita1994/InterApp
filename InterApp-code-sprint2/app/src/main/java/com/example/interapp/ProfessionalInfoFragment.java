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
            stateValue.put("spinnerProfession", 0);
            stateValue.put("spinnerProfessionalTraining", 0);
            stateValue.put("spinnerEmployerCategory", 0);
            stateValue.put("spinnerEmployerDomain", 0);
            stateValue.put("spinnerJobPosition", 0);
        }

        //education
        QuestionnaireActivity.spinnerEducation = (Spinner) view.findViewById(R.id.spinnerEducation);
        ArrayAdapter<CharSequence> adapterEducation = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.education_array, android.R.layout.simple_spinner_item);
        adapterEducation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerEducation.setAdapter(adapterEducation);
        QuestionnaireActivity.spinnerEducation.setSelection(stateValue.get("spinnerEducation"));
        QuestionnaireActivity.spinnerEducation.setOnItemSelectedListener(this);

        //profession
        QuestionnaireActivity.spinnerProfession = (Spinner) view.findViewById(R.id.spinnerProfession);
        ArrayAdapter<CharSequence> adapterProfession = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.profession_array, android.R.layout.simple_spinner_item);
        adapterProfession.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerProfession.setAdapter(adapterProfession);
        QuestionnaireActivity.spinnerProfession.setSelection(stateValue.get("spinnerProfession"));
        QuestionnaireActivity.spinnerProfession.setOnItemSelectedListener(this);

        //professional training
        QuestionnaireActivity.spinnerProfessionalTraining = (Spinner) view.findViewById(R.id.spinnerProfessionalTraining);
        ArrayAdapter<CharSequence> adapterProfessionalTraining = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.professional_training_array, android.R.layout.simple_spinner_item);
        adapterProfessionalTraining.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerProfessionalTraining.setAdapter(adapterProfessionalTraining);
        QuestionnaireActivity.spinnerProfessionalTraining.setSelection(stateValue.get("spinnerProfessionalTraining"));
        QuestionnaireActivity.spinnerProfessionalTraining.setOnItemSelectedListener(this);

        //employer category
        QuestionnaireActivity.spinnerEmployerCategory = (Spinner) view.findViewById(R.id.spinnerEmployerCategory);
        ArrayAdapter<CharSequence> adapterEmployerCategory = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.employer_category_array, android.R.layout.simple_spinner_item);
        adapterEmployerCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerEmployerCategory.setAdapter(adapterEmployerCategory);
        QuestionnaireActivity.spinnerEmployerCategory.setSelection(stateValue.get("spinnerEmployerCategory"));
        QuestionnaireActivity.spinnerEmployerCategory.setOnItemSelectedListener(this);


        //employer domain
        QuestionnaireActivity.spinnerEmployerDomain = (Spinner) view.findViewById(R.id.spinnerEmployerDomain);
        ArrayAdapter<CharSequence> adapterEmployerDomain = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.employer_domain_array, android.R.layout.simple_spinner_item);
        adapterEmployerDomain.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerEmployerDomain.setAdapter(adapterEmployerDomain);
        QuestionnaireActivity.spinnerEmployerDomain.setSelection(stateValue.get("spinnerEmployerDomain"));
        QuestionnaireActivity.spinnerEmployerDomain.setOnItemSelectedListener(this);

        //job position
        QuestionnaireActivity.spinnerJobPosition = (Spinner) view.findViewById(R.id.spinnerJobPosition);
        ArrayAdapter<CharSequence> adapterJobPosition = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.job_position_array, android.R.layout.simple_spinner_item);
        adapterJobPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerJobPosition.setAdapter(adapterJobPosition);
        QuestionnaireActivity.spinnerJobPosition.setSelection(stateValue.get("spinnerJobPosition"));
        QuestionnaireActivity.spinnerJobPosition.setOnItemSelectedListener(this);



        //frequency of contact with immigrants
        QuestionnaireActivity.spinnerFrequency = (Spinner) view.findViewById(R.id.spinnerFrequency);
        ArrayAdapter<CharSequence> adapterFrequency = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.frequency_array, android.R.layout.simple_spinner_item);
        adapterFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerFrequency.setAdapter(adapterFrequency);
        QuestionnaireActivity.spinnerFrequency.setSelection(stateValue.get("spinnerFrequency"));
        QuestionnaireActivity.spinnerFrequency.setOnItemSelectedListener(this);




        return view;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
