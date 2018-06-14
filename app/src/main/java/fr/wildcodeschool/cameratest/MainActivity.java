package fr.wildcodeschool.cameratest;

import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO : modifier le nom du fichier
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/videorecordtest.mp4";

        // TODO : récupérer quel côté de camera utiliser pour filmer
        //int currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        int currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

        mCamera = getCameraInstance(currentCameraId);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera, new CameraPreview.SurfaceCallback() {
            @Override
            public void onSurfaceCreated() {
                new Thread(new Runnable() {
                    public void run() {
                        // l'enregistrement à besoin d'être lancé dans un autre thread
                        startRecording();
                    }
                }).start();
            }
        });
        FrameLayout preview = findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        Button play = findViewById(R.id.play_video);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();

        mCamera.unlock();

        mRecorder.setCamera(mCamera);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
        mRecorder.setOutputFile(mFileName);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(int currentCameraId){
        Camera c = null;
        try {
            c = Camera.open(currentCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}