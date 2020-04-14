package com.example.struna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Player extends AppCompatActivity {

    MediaPlayer mp = new MediaPlayer();
    AudioManager am;
    Timer ti = new Timer();
    String songUrl;
    int length;
    Bitmap background;
    ImageView playbt;
    ImageView pausebt;
    ImageView stopbt;
    ImageView repeatOne;
    boolean statusLooping = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[] {}));
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        songUrl = getIntent().getStringExtra("Choosen_Song");
        background = getIntent().getParcelableExtra("Background_Song");
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_player);
        ImageView songWindow = (ImageView) findViewById(R.id.songImage);
        songWindow.setImageBitmap(background);
        System.out.println(songUrl);
        playbt = findViewById(R.id.playBtt);
        playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mp.isPlaying()){
                    mp.start();
                    playbt.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                } else{
                    mp.pause();
                    playbt.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
            }
        });
        pausebt = findViewById(R.id.pauseBtt);
        pausebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()){
                    mp.pause();
                }
            }
        });
        stopbt = findViewById(R.id.stopBtt);
        stopbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()){
                    mp.pause();
                    mp.seekTo(0);
                }
            }
        });
        repeatOne = findViewById(R.id.setLooping);
        repeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( statusLooping == false){
                    mp.setLooping(statusLooping);
                    statusLooping = true;
                    Toast.makeText(Player.this,"Zapętlanie utworu wyłączone!",Toast.LENGTH_SHORT).show();
                } else{
                    mp.setLooping(statusLooping);
                    statusLooping = false;
                    Toast.makeText(Player.this,"Zapętlanie utworu włączone!",Toast.LENGTH_SHORT).show();

                }
            }
        });
        try {
            mp.setDataSource(songUrl);
            mp.prepareAsync();

            SeekBar volume = findViewById(R.id.volumeSeekBar);
            int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            volume.setMax(maxVolume);
            volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.i("Info",Integer.toString(progress));
                    am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,50);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            final SeekBar progress = findViewById(R.id.progressSeekBar);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    Toast.makeText(Player.this,"Spróbuj za chwilę, buforowanie utworu...",Toast.LENGTH_SHORT).show();
                    length = mp.getDuration();
                    System.out.println("pierwszy stopien:" + length);
                    String durationText = DateUtils.formatElapsedTime(length / 1000);
                    progress.setMax(length);

                }

            });
            progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mp.seekTo(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            ti.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    progress.setProgress(mp.getCurrentPosition());
                }
            },0,1000);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public void play(View view){

        mp.start();
        mp.setLooping(true);
    }

    public void stop(View view){
        mp.pause();
        mp.seekTo(0);

    }

    public void pause(View view){
        mp.pause();
    }
}
