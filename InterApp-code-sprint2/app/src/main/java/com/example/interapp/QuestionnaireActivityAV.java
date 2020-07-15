package com.example.interapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class QuestionnaireActivityAV extends AppCompatActivity {



    class element{
        Button btn;
        String name;
        int state;  //0: normal, 1: playing/recording, 2: disabled
        int sourceFile; //-1 if there is no source file (yet)
        String recordedFile;
        String outputFile;
        int length; //-1 for recording buttons
        int filled = -1; //-1:NA, 0:unfilled, 1:filled
        element associatedElement;
        public element(Button btn, String name, int state, int sourceFile, String outputFile, int length){
            this.btn = btn;
            this.name = name;
            this.state = state;
            this.sourceFile = sourceFile;
            this.outputFile = outputFile;
            this.length = length;
        }

    }


    element e;
    static LinkedList<element> elements = new LinkedList<>();
    public static final int RequestPermissionCode = 1;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final String subject=" New Survey completed in Interapp";
    String emailBody="A new survey is successfully registered in the InterApp application.";
    final String toEmail="interapp.teamj@gmail.com";
    final String fromEmail="interapp.teamj@gmail.com";
    final String fromPassword="TeamJ@123";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_av);

        elements.clear();

        elements.add(new element((Button)findViewById(R.id.volBtnInstructions), "playInst", 0, R.raw.instructions, null,0));
        elements.add(new element((Button)findViewById(R.id.volBtnCountry), "playInstCountry", 0, R.raw.country, null,0));
        String temp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordingCountry.3gp";
        element recTemp = new element((Button)findViewById(R.id.recBtnCountry), "recCountry", 0, -1, temp,-1);
        findViewById(R.id.playBtnCountry).setEnabled(false);
        element playTemp = new element((Button)findViewById(R.id.playBtnCountry), "playRecCountry", 2, -1, null,0);
        recTemp.filled = 0;
        elements.add(recTemp);
        elements.add(playTemp);
        recTemp.associatedElement = playTemp;
        playTemp.associatedElement = recTemp;

        elements.add(new element((Button)findViewById(R.id.volBtnLanguage), "playInstLanguage", 0, R.raw.language, null,0));
        temp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordingLanguage.3gp";
        recTemp = new element((Button)findViewById(R.id.recBtnLanguage), "recLanguage", 0, -1, temp,-1);
        findViewById(R.id.playBtnLanguage).setEnabled(false);
        playTemp = new element((Button)findViewById(R.id.playBtnLanguage), "playRecLanguage", 2, -1, null,0);
        recTemp.filled = 0;
        elements.add(recTemp);
        elements.add(playTemp);
        recTemp.associatedElement = playTemp;
        playTemp.associatedElement = recTemp;

        elements.add(new element((Button)findViewById(R.id.volBtnLocation), "playInstLocation", 0, R.raw.location, null,0));
        temp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordingLocation.3gp";
        recTemp = new element((Button)findViewById(R.id.recBtnLocation), "recLocation", 0, -1, temp,-1);
        findViewById(R.id.playBtnLocation).setEnabled(false);
        playTemp = new element((Button)findViewById(R.id.playBtnLocation), "playRecLocation", 2, -1, null,0);
        recTemp.filled = 0;
        elements.add(recTemp);
        elements.add(playTemp);
        recTemp.associatedElement = playTemp;
        playTemp.associatedElement = recTemp;

        elements.add(new element((Button)findViewById(R.id.volBtnSituation), "playInstSituation", 0, R.raw.situation, null,0));
        temp = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recordingSituation.3gp";
        recTemp = new element((Button)findViewById(R.id.recBtnSituation), "recSituation", 0, -1, temp,-1);
        findViewById(R.id.playBtnSituation).setEnabled(false);
        playTemp = new element((Button)findViewById(R.id.playBtnSituation), "playRecSituation", 2, -1, null,0);
        recTemp.filled = 0;
        elements.add(recTemp);
        elements.add(playTemp);
        recTemp.associatedElement = playTemp;
        playTemp.associatedElement = recTemp;

    }

    public void playOnClick(View view) {


        //get element which has been clicked
        for(element i : elements){
            if(i.btn.getId()==view.getId()) {
                e = i;
                break;
            }
        }

        /**
         * if button was normal state (not playing or disabled):
         *  change state and background to 1 and pause icon respectively
         *  start mediaplayer from last time it was paused (or beginning)
         */
        if(e.state==0){
            e.state = 1;
            e.btn.setBackgroundResource(R.drawable.buttonpause_privacy);
            if(e.sourceFile != -1)
                mediaPlayer = MediaPlayer.create(this, e.sourceFile);
            else
                mediaPlayer = MediaPlayer.create(this,Uri.fromFile(new File(e.recordedFile)));
            mediaPlayer.seekTo(e.length);
            mediaPlayer.start();
            showToast("Audio playing");
            /**
             * if audio was played till completion:
             *  change state and length to 0 and 0 respectively
             *  stop and release mediaplayer
             *  change background icon accordingly
             */
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    e.state = 0;
                    e.length = 0;
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if(e.name.contains("Inst"))
                        e.btn.setBackgroundResource(R.drawable.button_volume);
                    else
                        e.btn.setBackgroundResource(R.drawable.button_privacy);
                    showToast("Audio stopped");
                }
            });
        }
        else if(e.state == 1){
            e.state = 0;
            if(e.name.contains("Inst"))
                e.btn.setBackgroundResource(R.drawable.button_volume);
            else
                e.btn.setBackgroundResource(R.drawable.button_privacy);
            if(mediaPlayer != null){
                try {
                    mediaPlayer.pause();
                    e.length = mediaPlayer.getCurrentPosition();
                }catch(IllegalStateException e){
                    Log.d("RECORDER STOP ILLEGAL","mediaPlayer does not exists");
                }
            }
            showToast("Audio Paused");
        }


    }

    public void recordOnClick(View view) {

        //get element which has been clicked
        for(element i : elements){
            if(i.btn.getId()==view.getId()) {
                e = i;
                break;
            }
        }

        /**
         * if recording button is clicked for the first time:
         *  change state and background icon
         *  prepare and start mediarecorder (consider permission)
         *
         */
        if(e.state == 0){
            e.state = 1;
            e.btn.setBackgroundResource(R.drawable.button_recordactive);
            MediaRecorderReady(e.outputFile);
            if(checkPermission()) {
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                showToast("Recording started");
            } else {
                requestPermission();
            }
        }
        else if(e.state == 1){
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Submit Recording");
            ad.setMessage("Do you want to submit your recording? Press No to record again.");
            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    e.state = 2;
                    e.filled = 1;
                    e.btn.setEnabled(false);
                    mediaRecorder.stop();
                    e.associatedElement.recordedFile = e.outputFile;
                    mediaRecorder.release();
                    e.associatedElement.btn.setEnabled(true);
                    e.associatedElement.state = 0;
                    showToast("Recording submitted");
                }
            });
            ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    dialog.dismiss();
                    e.state = 0;
                    e.btn.setBackgroundResource(R.drawable.button_record);
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    showToast("Recording discarded");
                }
            });
            AlertDialog alertDialog = ad.create();
            alertDialog.show();

        }


    }


    public void MediaRecorderReady(String outputFile){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(QuestionnaireActivityAV.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission)
                        showToast("Permission Granted");
                    else
                        showToast("Permission Denied");
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    public void exitSurveyAV(View view) {

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

    public void submitSurveyAV(View view){
        for(element i : elements){
            if(i.filled == 0){
                showToast("Please fill all fields to submit");
                return;
            }
        }
        sendEmailAsync(subject, emailBody, toEmail, fromEmail, fromPassword);
        showToast("Your Survey is successfully recorded. Thank you!!");
        Intent intent = new Intent();
        intent.setClassName("com.example.interapp", "com.example.interapp.AcknowledgementActivity");
        startActivity(intent);
    }

    private void sendEmailAsync(final String subject, final String emailBody, final String toEmail, final String fromEmail, final String fromPassword)
    {
        new AsyncTask<Void, Void, Void>() {
            @Override public Void doInBackground(Void... arg) {
                boolean emailSent=false;
                try {

                    GMailSender sender = new GMailSender(fromEmail, fromPassword);

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
                }
                return null;}
        }.execute();

    }
    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}