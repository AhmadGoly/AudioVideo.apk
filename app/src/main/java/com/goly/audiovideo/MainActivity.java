package com.goly.audiovideo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener , MediaPlayer.OnCompletionListener {

    private VideoView myVideoView;
    private Button btnPlayVideo;
    private MediaController mc;
    private Button btnPlayMusic,btnPauseMusic;
    private MediaPlayer mp;
    private SeekBar volumeSB;
    private AudioManager am;
    private TextView showVolumeTextView;
    private SeekBar moveBackAndForthSeekBar;
    private Timer timer;

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        timer.cancel();
        Toast.makeText(this, "Music Ended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if(b)   //b=fromUser    i=progress
        {
            switch (seekBar.getId()){
                case R.id.seekBarVolume:
                    showVolumeTextView.setText("Volume: "+i);
                    am.setStreamVolume(AudioManager.STREAM_MUSIC,i,0);  //he didnt say about flags.
                    break;
                case R.id.seekBarMove:
                    mp.seekTo(i);
                    break;
            }
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekBarVolume:
                break;
            case R.id.seekBarMove:
                mp.pause();
                break;
        }
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
            switch (seekBar.getId()) {
                case R.id.seekBarVolume:
                    break;
                case R.id.seekBarMove:
                    mp.start();
                    break;
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://"+ getPackageName() + "/"+R.raw.video);
                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mc);
                mc.setAnchorView(myVideoView);
                myVideoView.start();
                break;
            case R.id.btnPauseMusic:
                mp.pause();
                timer.cancel();
                break;
            case R.id.btnPlayMusic:
                mp.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveBackAndForthSeekBar.setProgress(mp.getCurrentPosition());
                    }
                },0,1000);
                break;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        mc = new MediaController(this);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        mp = MediaPlayer.create(this,R.raw.imaginedragonszero);

        moveBackAndForthSeekBar = findViewById(R.id.seekBarMove);
        moveBackAndForthSeekBar.setMax(mp.getDuration());

        showVolumeTextView = findViewById(R.id.showVolumeTextView);
        volumeSB = findViewById(R.id.seekBarVolume);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolumeOfUser = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumeOfUser = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSB.setMax(maxVolumeOfUser);
        volumeSB.setProgress(currentVolumeOfUser);




        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);
        volumeSB.setOnSeekBarChangeListener(MainActivity.this);
        moveBackAndForthSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        mp.setOnCompletionListener(MainActivity.this);

    }
}