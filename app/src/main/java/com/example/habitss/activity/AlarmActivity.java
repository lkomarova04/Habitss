package com.example.habitss.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habitss.R;
import com.example.habitss.databinding.ActivityAlarmBinding;

public class AlarmActivity extends AppCompatActivity {

    private static AlarmActivity inst;
    private ActivityAlarmBinding binding;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mediaPlayer = MediaPlayer.create(this, R.raw.notification);
        mediaPlayer.start();

        binding.title.setText("");
        binding.closeButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            finish();
        });
    }

    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        inst = null;
    }
}
