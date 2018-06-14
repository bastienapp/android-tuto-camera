package fr.wildcodeschool.cameratest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    private static String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/videorecordtest.mp4";

        VideoView videoView = findViewById(R.id.video_view);
        videoView.setVideoPath(mFileName);
        videoView.start();
    }
}
