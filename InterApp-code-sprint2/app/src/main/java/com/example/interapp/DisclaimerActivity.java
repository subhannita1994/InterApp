package com.example.interapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisclaimerActivity extends AppCompatActivity {

    /**
     * default method for diclaimer activity layout
     * when checkbox is checked, the next button on this activity is enabled
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        final Button mButton = (Button) findViewById(R.id.startSurveyBtn);
        CheckBox mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        TextView textView = (TextView) findViewById(R.id.disclaimerText);
        String text = "InterApp collects intercultural situations anonymously. The analysis of this data will help to better understand what the needs of the newcomers to Canada are.<br/>\n" +
                "<b>Privacy Notice</b><br/>\n" +
                "This privacy notice discloses the privacy practices for (InterApp). This privacy notice applies solely to information collected by this website. It will notify you of the following:<br/>\n" +
                "<b>Information Collection, Use, and Sharing </b><br/>\n" +
                "We are the sole owners of the information collected on this site. We only have access to/collect information that you voluntarily give us via the application. We will not sell or rent this information to anyone.<br/>\n" +
                "We will not keep any sensitive information that might help in identifying you.<br/>\n" +
                "<b>Security </b><br/>\n" +
                "We take precautions to protect your information. When you submit sensitive information via the application, your information is protected both online and offline.\n" +
                "Communication with the APIs is encrypted and transmitted to us in a secure way.<br/>\n" +
                "While we use encryption to protect sensitive information transmitted online, we also protect your information offline. Only employees who need the information to perform a specific job are granted access to information submitted. The computers/servers in which we store personally identifiable information are kept in a secure environment.<br/>\n" +
                "If you feel that we are not abiding by this privacy policy, you should contact us immediately via telephone at XXX YYY-ZZZZ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(text));
        }

        textView.setMovementMethod(new ScrollingMovementMethod());


        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mButton.setEnabled(true);
                    mButton.setBackgroundResource(R.color.colorCheckbox);
                }
                else
                    mButton.setEnabled(false);
            }
        });
    }

    /**
     * method to take control flow to next (questionnaire) activity on button click
     * @param view
     */
    public void sendMessage2(View view){
        Intent intent = new Intent(DisclaimerActivity.this, QuestionnaireActivity.class);
        startActivity(intent);
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
}
