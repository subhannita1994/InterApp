package com.example.interapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DisclaimerActivityAV extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    Button playBtn, yesBtn;

    static int playState, length = 0;


    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer_av);
        playBtn = (Button) findViewById(R.id.buttonPrivacyPlay);
        yesBtn = (Button) findViewById(R.id.buttonYesPrivacy);

        yesBtn.setEnabled(false);
        playState = 0;      //0: play recording from start, 1:stop play
    }

    public void playOnClick(View view) {

        if(playState==0){
            playState=1;
            playBtn.setBackgroundResource(R.drawable.buttonpause_privacy);
            mediaPlayer = MediaPlayer.create(this,R.raw.privacy);
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            showToast("Audio Playing");
            mediaPlayer.setOnCompletionListener(this);
        }
        else if(playState==1){
            playState = 0;
//            playBtn.setText("Play");
            playBtn.setBackgroundResource(R.drawable.button_privacy);
            if(mediaPlayer != null){
                try {
                    mediaPlayer.pause();
                    length=mediaPlayer.getCurrentPosition();
                }catch(IllegalStateException e){
                    Log.d("RECORDER STOP ILLEGAL","mediaPlayer does not exists");
                }
            }
            showToast("Audio Paused");
        }


    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        playState = 0;
        playBtn.setBackgroundResource(R.drawable.button_privacy);
        if(mediaPlayer != null){
            length = 0;
            mediaPlayer.stop();
            mediaPlayer.release();
            yesBtn.setEnabled(true);
        }
        showToast("Audio Complete");
    }

    public void showToast(String message){
        Toast.makeText(DisclaimerActivityAV.this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * method to take control flow to next (questionnaire) activity on button click
     * @param view
     */
    public void sendMessage2(View view){
        Intent intent = new Intent(DisclaimerActivityAV.this, QuestionnaireActivityAV.class);
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
