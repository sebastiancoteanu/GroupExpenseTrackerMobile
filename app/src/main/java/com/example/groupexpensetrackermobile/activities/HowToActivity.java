package com.example.groupexpensetrackermobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groupexpensetrackermobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class HowToActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        initializeBottomNavigationBar();
        initializeVideos();
    }

    private void initializeVideos() {
//        initializeVideo("S0Q4gqBUs7c", R.id.first_video);
//        initializeVideo("S0Q4gqBUs7c", R.id.second_video);
    }

    private void initializeVideo(String videoId, @IdRes int videoRefId) {
        YouTubePlayerView youTubePlayerView = findViewById(videoRefId);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f);
            }
        });
    }

    private void initializeBottomNavigationBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_how_to);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_profile:
                    if (bottomNavigationView.getSelectedItemId() != R.id.action_profile) {
                        Intent intent = new Intent(this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.action_main:
                    if (bottomNavigationView.getSelectedItemId() != R.id.action_main) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.action_how_to:
                    if (bottomNavigationView.getSelectedItemId() != R.id.action_how_to) {
                        Intent intent = new Intent(this, HowToActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
            return true;
        });
    }
}
