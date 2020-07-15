package com.example.interapp;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class audioTestingActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {


    Button recordBtn, playBtn;

    static int recordState, playState;

    public static final int RequestPermissionCode = 1;

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    private String outputFile;

    /**
     * default method for main activity layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiotesting);

        recordBtn = (Button) findViewById(R.id.recordBtn);
        playBtn = (Button) findViewById(R.id.playBtn);

        recordState = 0;    //0: start recording, 1:stop and submit recording
        playState = 0;      //0: play recording from start, 1:stop play

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";



    }


    public void recordOnClick(View view) {

        if(recordState == 0){
            recordState = 1;
            recordBtn.setText("Submit");
            MediaRecorderReady();
            if(checkPermission()) {
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(audioTestingActivity.this, "Recording started",
                        Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
        else if(recordState == 1){
            recordState = 0;
            recordBtn.setText("Record");
            mediaRecorder.stop();
            mediaRecorder.release();
            Toast.makeText(audioTestingActivity.this, "Recording Completed",
                    Toast.LENGTH_LONG).show();
        }


    }


    public void playOnClick(View view) {

        if(playState==0){
            playState=1;
            playBtn.setText("Stop");
            mediaPlayer = new MediaPlayer();
            mediaPlayer = MediaPlayer.create(this,R.raw.savedaudio);
            mediaPlayer.start();
            Toast.makeText(audioTestingActivity.this, "Audio Playing",
                    Toast.LENGTH_LONG).show();
            mediaPlayer.setOnCompletionListener(this);
        }
        else if(playState==1){
            playState = 0;
            playBtn.setText("Play");
            if(mediaPlayer != null){
                try {
                    mediaPlayer.stop();
                }catch(IllegalStateException e){
                    Log.d("RECORDER STOP ILLEGAL","mediaPlayer does not exists");
                }
                mediaPlayer.release();
            }
            Toast.makeText(audioTestingActivity.this, "Audio Stopped",
                    Toast.LENGTH_LONG).show();
        }


    }

    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(audioTestingActivity.this, new
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

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(audioTestingActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(audioTestingActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
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


    @Override
    public void onCompletion(MediaPlayer mp) {
        playState = 1;
        playBtn.setText("Play");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Toast.makeText(audioTestingActivity.this, "Audio Stopped",
                Toast.LENGTH_LONG).show();
    }
}
