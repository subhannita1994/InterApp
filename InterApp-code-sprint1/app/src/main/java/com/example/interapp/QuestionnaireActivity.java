package com.example.interapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

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
    final String emailBody="A new survey is successfully registered in the InterApp application.";
    final String toEmail="interapp.teamj@gmail.com";
    final String fromEmail="interapp.teamj@gmail.com";
    final String fromPassword="TeamJ@123";


    /**
     * Declaring class variables to be used to connect Layout variables.
     */
    static EditText textInputLocation, textInputSituation;

    static Spinner spinnerAge, spinnerGender, spinnerCitizenship, spinnerBirth, spinnerLanguage, spinnerEthnicIdentity, spinnerReligion;
    static Spinner spinnerEducation, spinnerProfession, spinnerProfessionalTraining, spinnerEmployerCategory, spinnerEmployerDomain, spinnerJobPosition, spinnerFrequency;

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

        if (!validationSuccess()) {
            Toast.makeText(this, "Please fill the mandatory fields first", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Submit Survey");
            ad.setMessage("Are you sure you want to submit? Press No to go back to editing");
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    logger.info("Entered the else method of submit survey");
                    boolean emailSent= sendEmail(subject, emailBody,toEmail, fromEmail,fromPassword);
                    if(emailSent) {
                        showToast("Your Survey is successfully recorded. Thank you!!");
                        Intent intent = new Intent();
                        intent.setClassName("com.example.interapp", "com.example.interapp.AcknowledgementActivity");
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

    }

    /**
     * method to check if form entered has no unfilled fields
     *
     * @return boolean value: true if all mandatory fields are filled.
     */
    public boolean validationSuccess() {
        //TODO: call all spinners and fields by their resource ids, validate if they are filled.

        boolean isValid = true;
        //int errorColorCode = Color.WHITE;
        int errorColorCode = Color.parseColor("#FF0000");

        if (textInputLocation.getText().toString().isEmpty()) {
            textInputLocation.setBackgroundColor(errorColorCode);
            isValid = false;
        }

        if (textInputSituation.getText().toString().isEmpty()) {
            textInputSituation.setBackgroundColor(errorColorCode);
            isValid = false;
        }

        /**
         Add code for other mandatory fields here.
         */

        return isValid;
    }

    /**
     * method to send email in the background thread.
     *
     * @param -> subject, body, from, to.
     */
    public boolean sendEmail(final String subject, final String emailBody, final String toEmail, final String fromEmail, final String fromPassword) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).run();
//
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
            return emailSent;

        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            logger.info(" Exception is "+e.getStackTrace());
            return emailSent;
        }
    }

    /**
     * method to display toast with message str
     */
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

}
