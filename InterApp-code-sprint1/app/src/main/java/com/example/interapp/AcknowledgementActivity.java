
package com.example.interapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * starting activity
 */
public class AcknowledgementActivity extends AppCompatActivity {


    /**
     * default method for main activity layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledgement);
    }

    /**
     * method to take control flow to next (main) activity on button click
     * @param view
     */
    public void sendMessage(View view)
    {
        Intent intent = new Intent(AcknowledgementActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
