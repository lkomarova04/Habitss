package com.example.habitss.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
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

        // Initialize MediaPlayer and other logic
        mediaPlayer = new MediaPlayer();

        // Example of setting values or listeners
        binding.title.setText("");
        binding.closeButton.setOnClickListener(v -> {
            // Handle button click
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            finish();
        });
    }

    // Static method to get the current instance of AlarmActivity
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
