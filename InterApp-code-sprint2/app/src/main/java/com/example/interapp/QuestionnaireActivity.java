package com.example.interapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.logging.Logger;

public class QuestionnaireActivity extends AppCompatActivity {

    Logger logger = Logger.getLogger(QuestionnaireActivity.class.getName());
    private GeneralInfoFragment giFrag;
    private ProfessionalInfoFragment profFrag;
    private OccasionFragment ocFrag;
    private final String GI_FRAG_TAG = "gifragtag";
    private final String PROF_FRAG_TAG = "proffragtag";
    private final String OC_FRAG_TAG = "ocfragtag";


    /**
     * Declaring class variables to be used for Confirmation Email.
     */
    final String subject=" New Survey completed in Interapp";
    String emailBody="A new survey is successfully registered in the InterApp application.";
    final String toEmail="interapp.teamj@gmail.com";
    final String fromEmail="interapp.teamj@gmail.com";
    final String fromPassword="TeamJ@123";


    /**
     * Declaring class variables to be used to connect Layout variables.
     */
    static EditText textInputLocation, textInputSituation;

    static Spinner spinnerAge, spinnerGender, spinnerCitizenship, spinnerBirth, spinnerEthnicIdentity, spinnerReligion,spinnerEducation, spinnerProfession, spinnerProfessionalTraining, spinnerEmployerCategory, spinnerEmployerDomain, spinnerJobPosition, spinnerFrequency;
    ;
    static com.example.interapp.MultiSpinner spinnerLanguage;
    /**
     * Declaring class variables to store selected information for birth country, language, location and situation
     */
    static String location, situation;
    static int countryIndex;
    static ArrayList<Integer> languageIndices;

    /**
     * default method for questionnaire activity layout
     * display fragment according to the selected tab
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            giFrag = (GeneralInfoFragment) getSupportFragmentManager().findFragmentByTag(GI_FRAG_TAG);
            ocFrag = (OccasionFragment) getSupportFragmentManager().findFragmentByTag(OC_FRAG_TAG);
            profFrag = (ProfessionalInfoFragment) getSupportFragmentManager().findFragmentByTag(PROF_FRAG_TAG);
        } else {
            if (giFrag == null)
                giFrag = new GeneralInfoFragment();
            if (ocFrag == null)
                ocFrag = new OccasionFragment();
            if (profFrag == null)
                profFrag = new ProfessionalInfoFragment();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, giFrag, GI_FRAG_TAG).commit();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, giFrag, GI_FRAG_TAG).commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profFrag, PROF_FRAG_TAG).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ocFrag, OC_FRAG_TAG).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * exit button handler
     * when exit button is clicked -> confirm and then go to main activity
     *
     * @param view
     */
    public void exitSurvey(View view) {

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Exit Survey");
        ad.setMessage("Do you really want to exit? You will lose all information");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClassName("com.example.interapp", "com.example.interapp.MainActivity");
                startActivity(intent);
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

    /**
     * next button handler
     *
     * @param view
     */
    public void nextSurvey(View view) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        switch (view.getId()) {
            case R.id.nextBtnGeneralInfo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profFrag, PROF_FRAG_TAG).commit();
                tabLayout.getTabAt(1).select();
                break;
            case R.id.nextBtnprofessionalInfo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ocFrag, OC_FRAG_TAG).commit();
                tabLayout.getTabAt(2).select();
                break;

        }

    }

    /**
     * previous button handler
     *
     * @param view
     */
    public void prevSurvey(View view) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        switch (view.getId()) {
            case R.id.prevBtnProfessionalInfo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, giFrag, GI_FRAG_TAG).commit();
                tabLayout.getTabAt(0).select();
                break;
            case R.id.prevBtnOccasion:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profFrag, PROF_FRAG_TAG).commit();
                tabLayout.getTabAt(1).select();
                break;
        }
    }

    /**
     * submit button handler
     * if validation is successful : display confirmation dialogue and go to acknowledgment activity
     * else : display alert dialogue and highlight all fields
     *
     * @param view
     */
    public void submitSurvey(View view) {

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Submit Survey");
        ad.setMessage("Are you sure you want to submit? Press No to go back to editing");
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                logger.info("Entered the else method of submit survey");
                if(validationSuccess()){
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
                else{
                    Intent intent = new Intent();
                    intent.setClassName("com.example.interapp", "com.example.interapp.ValidationActivity");
                    startActivity(intent);
                }

            }
        });
        ad.setNegativeButton("Go back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();

    }

    private String constructEmailBody() {

        String body = "";
        if(spinnerAge.getSelectedItemPosition()!=0)
            body+= "Age: "+spinnerAge.getSelectedItem().toString()+'\n';
        else
            body+="Age: Unfilled\n";
        if(spinnerGender.getSelectedItemPosition()!=0)
            body+= "Gender: "+spinnerGender.getSelectedItem().toString()+'\n';
        else
            body+="Gender: Unfilled\n";
        if(spinnerCitizenship.getSelectedItemPosition()!=0)
            body+= "Citizenship: "+spinnerCitizenship.getSelectedItem().toString()+'\n';
        else
            body+="Citizenship: Unfilled\n";
        body+= "Country of birth: "+spinnerBirth.getSelectedItem().toString()+'\n';
        body+= "Spoken Language(s): "+ spinnerLanguage.getSelectedItems()+'\n';
        if(spinnerEthnicIdentity.getSelectedItemPosition()!=0)
            body+= "Ethnic Identity: "+spinnerEthnicIdentity.getSelectedItem().toString()+'\n';
        else
            body+="Ethnic Identity: Unfilled\n";
        if(spinnerReligion.getSelectedItemPosition()!=0)
            body+= "Religion: "+spinnerReligion.getSelectedItem().toString()+'\n';
        else
            body+="Religion: Unfilled\n";
        if(spinnerEducation.getSelectedItemPosition()!=0)
            body+= "Education: "+spinnerEducation.getSelectedItem().toString()+'\n';
        else
            body+="Education: Unfilled\n";
        if(spinnerProfession.getSelectedItemPosition()!=0)
            body+= "Profession: "+spinnerProfession.getSelectedItem().toString()+'\n';
        else
            body+="Profession: Unfilled\n";
        if(spinnerProfessionalTraining.getSelectedItemPosition()!=0)
            body+= "Professional training: "+spinnerProfessionalTraining.getSelectedItem().toString()+'\n';
        else
            body+="Professional Training: Unfilled\n";
        if(spinnerEmployerCategory.getSelectedItemPosition()!=0)
            body+= "Employer Category: "+ spinnerEmployerCategory.getSelectedItem().toString()+'\n';
        else
            body+="Employer Category: Unfilled\n";
        if(spinnerEmployerDomain.getSelectedItemPosition()!=0)
            body+= "Employer Domain: "+spinnerEmployerDomain.getSelectedItem().toString()+'\n';
        else
            body+="Employer Domain: Unfilled\n";
        if(spinnerJobPosition.getSelectedItemPosition()!=0)
            body+= "Job position: "+ spinnerJobPosition.getSelectedItem().toString()+'\n';
        else
            body+="Job position: Unfilled\n";
        if(spinnerFrequency.getSelectedItemPosition()!=0)
            body+= "Frequency of contact with people of immigrant origin: "+spinnerFrequency.getSelectedItem().toString()+'\n';
        else
            body+="Frequency of contact with people of immigrant origin: Unfilled\n";
        body+= "Where did the interaction occur: "+textInputLocation.getText().toString()+'\n';
        body+="Describe the situation: "+textInputSituation.getText().toString()+'\n';


        return body;
    }

    /**
     * method to check if form entered has no unfilled fields for birth country, language, location and situation
     *
     * @return boolean value: true if all mandatory fields are filled.
     */
    public boolean validationSuccess() {

        location = textInputLocation.getText().toString();
        situation = textInputSituation.getText().toString();
        countryIndex = spinnerBirth.getSelectedItemPosition();
        languageIndices = spinnerLanguage.getSelectedItemPositions();

        if( !location.isEmpty() && !situation.isEmpty() && countryIndex!=0 && languageIndices.size()>0)
            return true;
        else
            return false;
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

    /**
     * method to display toast with message str
     */
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
