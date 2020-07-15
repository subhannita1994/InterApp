package com.example.interapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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
        final Button mButton = (Button) findViewById(R.id.button2);
        CheckBox mCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mButton.setEnabled(true);
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
}
