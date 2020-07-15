
package com.example.interapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * starting activity
 */
public class MainActivity extends AppCompatActivity {


    /**
     * default method for main activity layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * method to take control flow to next (disclaimer) activity on button click
     * @param view
     */
    public void sendMessage(View view)
    {
        Intent intent = new Intent(MainActivity.this, DisclaimerActivity.class);
        startActivity(intent);
    }
}
