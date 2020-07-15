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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class GeneralInfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private HashMap<String, Integer> stateValue = new HashMap<String, Integer>();
    private ArrayList<Integer> languageValue = new ArrayList<>();
    private final String VALUE_KEY = "genInfoKey";
    private final String MULTI_VALUE_KEY = "genInfoMultiSelectionKey";

    /**
     * Fires when a configuration change occurs and fragment needs to save state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(VALUE_KEY, stateValue);
        outState.putSerializable(MULTI_VALUE_KEY, languageValue);
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
        View view = inflater.inflate(R.layout.fragment_generalinfo, container, false);

        if (savedInstanceState != null) {
            // restore stateValue on all corresponding fields
            stateValue = (HashMap<String, Integer>) savedInstanceState.getSerializable(VALUE_KEY);
            languageValue = (ArrayList<Integer>) savedInstanceState.getSerializable(MULTI_VALUE_KEY);
        } else {
            //show default values for all spinners
            stateValue.put("spinnerAge", 0);
            stateValue.put("spinnerGender", 0);
            stateValue.put("spinnerCitizenship", 0);
            stateValue.put("spinnerBirth", 0);
            stateValue.put("spinnerEthnicIdentity", 0);
            stateValue.put("spinnerReligion", 0);
        }


        //age
        QuestionnaireActivity.spinnerAge = (Spinner) view.findViewById(R.id.spinnerAge);
        ArrayAdapter<CharSequence> adapterAge = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.age_array, android.R.layout.simple_spinner_item);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerAge.setAdapter(adapterAge);
        QuestionnaireActivity.spinnerAge.setSelection(stateValue.get("spinnerAge"));
        QuestionnaireActivity.spinnerAge.setOnItemSelectedListener(this);

        //gender
        QuestionnaireActivity.spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerGender.setAdapter(adapterGender);
        QuestionnaireActivity.spinnerGender.setSelection(stateValue.get("spinnerGender"));
        QuestionnaireActivity.spinnerGender.setOnItemSelectedListener(this);

        //country and languages list
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        ArrayList<String> birthCountries = new ArrayList<String>();
        ArrayList<String> languages = new ArrayList<String>();

        String countryDefault = "Select a Country";
        String languageDefault = "Select a Language";
        String birthDefault = "Select a Birth Country";
        String notApplicable ="Not applicable";

        countries.add(countryDefault);
        birthCountries.add(birthDefault);
        String country, language;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            language = loc.getDisplayLanguage();
            if (country.length() > 0 && !countries.contains(country))
            {
                countries.add(country);
                birthCountries.add(country);
            }
            if (language.length() > 0 && !languages.contains(language))
                languages.add(language);
        }


        Collections.sort(countries.subList(1, countries.size()), String.CASE_INSENSITIVE_ORDER);
        Collections.sort(languages.subList(1, languages.size()), String.CASE_INSENSITIVE_ORDER);
        Collections.sort(birthCountries.subList(1,birthCountries.size()), String.CASE_INSENSITIVE_ORDER);

        birthCountries.add(notApplicable);
        countries.add(notApplicable);
        languages.add(notApplicable);


        //citizenship
        QuestionnaireActivity.spinnerCitizenship = (Spinner) view.findViewById(R.id.spinnerCitizenship);
        ArrayAdapter<String> adapterCitizenship = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, countries);
        QuestionnaireActivity.spinnerCitizenship.setAdapter(adapterCitizenship);
        QuestionnaireActivity.spinnerCitizenship.setSelection(stateValue.get("spinnerCitizenship"));
        QuestionnaireActivity.spinnerCitizenship.setOnItemSelectedListener(this);

        //country of birth
        QuestionnaireActivity.spinnerBirth = (Spinner) view.findViewById(R.id.spinnerBirth);
        ArrayAdapter<String> adapterBirth = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, birthCountries);
        QuestionnaireActivity.spinnerBirth.setAdapter(adapterBirth);
        QuestionnaireActivity.spinnerBirth.setSelection(stateValue.get("spinnerBirth"));
        QuestionnaireActivity.spinnerBirth.setOnItemSelectedListener(this);

        //language
        QuestionnaireActivity.spinnerLanguage = (com.example.interapp.MultiSpinner) view.findViewById(R.id.spinnerLanguage);
//        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, languages);
//        QuestionnaireActivity.spinnerLanguage.setAdapter(adapterLanguage);
        QuestionnaireActivity.spinnerLanguage.setItems(languages, languageDefault, new com.example.interapp.MultiSpinner.MultiSpinnerListener(){

            @Override
            public void onItemSelected(boolean[] selected) {
                languageValue.clear();
                languageValue = QuestionnaireActivity.spinnerLanguage.getSelectedItemPositions();
            }
        });
        if(languageValue.size()>0)
            QuestionnaireActivity.spinnerLanguage.setSelectedItems(languageValue);
//        languageDefaultPosition = adapterLanguage.getPosition(languageDefault);
//        QuestionnaireActivity.spinnerLanguage.setSelection(languageDefaultPosition);

        //ethnic identity
        QuestionnaireActivity.spinnerEthnicIdentity = (Spinner) view.findViewById(R.id.spinnerEthnicIdentity);
        ArrayAdapter<CharSequence> adapterEthnicIdentity = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.ethnic_array, android.R.layout.simple_spinner_item);
        adapterEthnicIdentity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerEthnicIdentity.setAdapter(adapterEthnicIdentity);
        QuestionnaireActivity.spinnerEthnicIdentity.setSelection(stateValue.get("spinnerEthnicIdentity"));
        QuestionnaireActivity.spinnerEthnicIdentity.setOnItemSelectedListener(this);

        //religion
        QuestionnaireActivity.spinnerReligion = (Spinner) view.findViewById(R.id.spinnerReligion);
        ArrayAdapter<CharSequence> adapterReligion = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.religion_array, android.R.layout.simple_spinner_item);
        adapterReligion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        QuestionnaireActivity.spinnerReligion.setAdapter(adapterReligion);
        QuestionnaireActivity.spinnerReligion.setSelection(stateValue.get("spinnerReligion"));
        QuestionnaireActivity.spinnerReligion.setOnItemSelectedListener(this);

        return view;
    }

    /**
     * respond to item selected in spinners
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}