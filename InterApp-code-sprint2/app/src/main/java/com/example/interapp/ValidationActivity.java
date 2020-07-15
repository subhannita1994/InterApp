package com.example.interapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import java.util.logging.Logger;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class ValidationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Logger logger = Logger.getLogger(ValidationActivity.class.getName());

    Spinner spinnerBirthValidation;
    MultiSpinner spinnerLanguageValidation;
    EditText textInputLocationValidation, textInputSituationValidation;


    /**
     * Declaring class variables to be used for Confirmation Email.
     */
    final String subject=" New Survey completed in Interapp";
    String emailBody="A new survey is successfully registered in the InterApp application.";
    final String toEmail="interapp.teamj@gmail.com";
    final String fromEmail="interapp.teamj@gmail.com";
    final String fromPassword="TeamJ@123";

    /**
     * default method for validation activity layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Oops!");
        ad.setMessage("Please fill the required fields. Click OK to continue.");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();


        //country and languages list
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> birthCountries = new ArrayList<String>();
        ArrayList<String> languages = new ArrayList<String>();

        String languageDefault = "Select a language";
        String birthDefault = "Select a Birth Country";
        String notApplicable ="Not applicable";

        birthCountries.add(birthDefault);
        String country, language;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            language = loc.getDisplayLanguage();
            if (country.length() > 0 && !birthCountries.contains(country))
            {
                birthCountries.add(country);
            }
            if (language.length() > 0 && !languages.contains(language))
                languages.add(language);
        }

        Collections.sort(languages.subList(1, languages.size()), String.CASE_INSENSITIVE_ORDER);
        Collections.sort(birthCountries.subList(1,birthCountries.size()), String.CASE_INSENSITIVE_ORDER);

        birthCountries.add(notApplicable);
        languages.add(notApplicable);



        //country of birth
        spinnerBirthValidation = (Spinner) findViewById(R.id.spinnerBirthValidation);
        ArrayAdapter<String> adapterBirth = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, birthCountries);
        spinnerBirthValidation.setAdapter(adapterBirth);
        spinnerBirthValidation.setSelection(QuestionnaireActivity.countryIndex);

        //language
        spinnerLanguageValidation = (com.example.interapp.MultiSpinner) findViewById(R.id.spinnerLanguageValidation);
        spinnerLanguageValidation.setItems(languages, languageDefault, new com.example.interapp.MultiSpinner.MultiSpinnerListener(){

            @Override
            public void onItemSelected(boolean[] selected) {
            }
        });
        if(QuestionnaireActivity.languageIndices.size()>0)
            spinnerLanguageValidation.setSelectedItems(QuestionnaireActivity.languageIndices);

        //location text input
        textInputLocationValidation = (EditText) findViewById(R.id.textInputLocationValidation);
        textInputLocationValidation.setText(QuestionnaireActivity.location, TextView.BufferType.EDITABLE);
//        QuestionnaireActivity.textInputLocation.setOnFocusChangeListener(this);

        //situation text input
        textInputSituationValidation = (EditText) findViewById(R.id.textInputSituationValidation);
        textInputSituationValidation.setText(QuestionnaireActivity.situation,TextView.BufferType.EDITABLE);
//        QuestionnaireActivity.textInputLocation.setOnFocusChangeListener(this);




    }


    public void submitSurveyValidation(View view){

        if(validationSuccess()){
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Submit Survey");
            ad.setMessage("Are you sure you want to submit? Press No to go back to editing");
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    try {
                        emailBody = constructEmailBody();
                        sendEmailAsync(subject, emailBody, toEmail, fromEmail, fromPassword);
                        showToast("Your Survey is successfully recorded. Thank you!!");
                        Intent intent = new Intent();
                        intent.setClassName("com.example.interapp", "com.example.interapp.AcknowledgementActivity");
                        startActivity(intent);
                    }
                    catch(Exception e) {
                        Log.e("AsyncSendMail", e.getMessage(), e);
                        logger.info(" Exception is "+e.getStackTrace());
                    }
                }
            });
            ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = ad.create();
            alertDialog.show();
        }
        else
            showToast("Please fill all the required fields");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * method to display toast with message str
     */
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     *send aynchronous email on successful validation
     * @param subject
     * @param emailBody
     * @param toEmail
     * @param fromEmail
     * @param fromPassword
     */
    private void sendEmailAsync(final String subject, final String emailBody, final String toEmail, final String fromEmail, final String fromPassword)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                boolean emailSent=false;
                try {
                    logger.info(" Inside try, before gmailsender");

                    GMailSender sender = new GMailSender(fromEmail, fromPassword);
                    logger.info(" after sender object created");

                    if (android.os.Build.VERSION.SDK_INT > 9)
                    {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                    }

                    emailSent= sender.sendMail(subject,
                            emailBody,
                            fromEmail,
                            toEmail);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    logger.info(" Exception is "+e.getStackTrace());
                }
                return null;}
        }.execute();

    }


    private boolean validationSuccess(){

        String location = textInputLocationValidation.getText().toString();
        String situation = textInputSituationValidation.getText().toString();
        int countryIndex = spinnerBirthValidation.getSelectedItemPosition();
        ArrayList<Integer> languageIndices = spinnerLanguageValidation.getSelectedItemPositions();

        if( !location.isEmpty() && !situation.isEmpty() && countryIndex!=0 && languageIndices.size()>0)
            return true;
        else
            return false;
    }

    private String constructEmailBody() {

        String body = "";
        if(QuestionnaireActivity.spinnerAge.getSelectedItemPosition()!=0)
            body+= "Age: "+QuestionnaireActivity.spinnerAge.getSelectedItem().toString()+'\n';
        else
            body+="Age: Unfilled\n";
        if(QuestionnaireActivity.spinnerGender.getSelectedItemPosition()!=0)
            body+= "Gender: "+QuestionnaireActivity.spinnerGender.getSelectedItem().toString()+'\n';
        else
            body+="Gender: Unfilled\n";
        if(QuestionnaireActivity.spinnerCitizenship.getSelectedItemPosition()!=0)
            body+= "Citizenship: "+QuestionnaireActivity.spinnerCitizenship.getSelectedItem().toString()+'\n';
        else
            body+="Citizenship: Unfilled\n";
        body+= "Country of birth: "+spinnerBirthValidation.getSelectedItem().toString()+'\n';
        body+= "Spoken Language(s): "+ spinnerLanguageValidation.getSelectedItems()+'\n';
        if(QuestionnaireActivity.spinnerEthnicIdentity.getSelectedItemPosition()!=0)
            body+= "Ethnic Identity: "+QuestionnaireActivity.spinnerEthnicIdentity.getSelectedItem().toString()+'\n';
        else
            body+="Ethnic Identity: Unfilled\n";
        if(QuestionnaireActivity.spinnerReligion.getSelectedItemPosition()!=0)
            body+= "Religion: "+QuestionnaireActivity.spinnerReligion.getSelectedItem().toString()+'\n';
        else
            body+="Religion: Unfilled\n";
        if(QuestionnaireActivity.spinnerEducation.getSelectedItemPosition()!=0)
            body+= "Education: "+QuestionnaireActivity.spinnerEducation.getSelectedItem().toString()+'\n';
        else
            body+="Education: Unfilled\n";
        if(QuestionnaireActivity.spinnerProfession.getSelectedItemPosition()!=0)
            body+= "Profession: "+QuestionnaireActivity.spinnerProfession.getSelectedItem().toString()+'\n';
        else
            body+="Profession: Unfilled\n";
        if(QuestionnaireActivity.spinnerProfessionalTraining.getSelectedItemPosition()!=0)
            body+= "Professional training: "+QuestionnaireActivity.spinnerProfessionalTraining.getSelectedItem().toString()+'\n';
        else
            body+="Professional Training: Unfilled\n";
        if(QuestionnaireActivity.spinnerEmployerCategory.getSelectedItemPosition()!=0)
            body+= "Employer Category: "+ QuestionnaireActivity.spinnerEmployerCategory.getSelectedItem().toString()+'\n';
        else
            body+="Employer Category: Unfilled\n";
        if(QuestionnaireActivity.spinnerEmployerDomain.getSelectedItemPosition()!=0)
            body+= "Employer Domain: "+QuestionnaireActivity.spinnerEmployerDomain.getSelectedItem().toString()+'\n';
        else
            body+="Employer Domain: Unfilled\n";
        if(QuestionnaireActivity.spinnerJobPosition.getSelectedItemPosition()!=0)
            body+= "Job position: "+ QuestionnaireActivity.spinnerJobPosition.getSelectedItem().toString()+'\n';
        else
            body+="Job position: Unfilled\n";
        if(QuestionnaireActivity.spinnerFrequency.getSelectedItemPosition()!=0)
            body+= "Frequency of contact with people of immigrant origin: "+QuestionnaireActivity.spinnerFrequency.getSelectedItem().toString()+'\n';
        else
            body+="Frequency of contact with people of immigrant origin: Unfilled\n";
        body+= "Where did the interaction occur: "+textInputLocationValidation.getText().toString()+'\n';
        body+="Describe the situation: "+textInputSituationValidation.getText().toString()+'\n';


        return body;
    }
}
