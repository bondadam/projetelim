package com.example.rechordapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = "record.wav";
    private ProgressBar progressBar;
    private int progress;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int SAMPLING_RATE_IN_HZ = 44100;

    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private final AtomicBoolean recordingInProgress = new AtomicBoolean(false);


    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    /**
     * Factor by that the minimum buffer size is multiplied. The bigger the factor is the less
     * likely it is that samples will be dropped, but more memory will be used. The minimum buffer
     * size is determined by {@link AudioRecord#getMinBufferSize(int, int, int)} and depends on the
     * recording settings.
     */
    private static final int BUFFER_SIZE_FACTOR = 2;

    /**
     * Size of the buffer where the audio data is stored by Android
     */
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLING_RATE_IN_HZ,
            CHANNEL_CONFIG, AUDIO_FORMAT) * BUFFER_SIZE_FACTOR;

    /**
     * Signals whether a recording is in progress (true) or not (false).
     */

    private AudioRecord recorder = null;

    private Thread recordingThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        Spinner spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countdown_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(this);
        TextView tv1 = findViewById(R.id.countdownText);
        progressBar = findViewById(R.id.determinateBar);
        tv1.setText(spinner.getSelectedItem().toString());
        progress = 0;
        //Drawable draw= res.getDrawable(R.drawable.custom_progressbar);
// set the drawable as progress drawable
        //progressBar.setProgressDrawable(draw);

        FloatingActionButton record = findViewById(R.id.playButton);
        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new CountDownTimer(Integer.parseInt(spinner.getSelectedItem().toString())*1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tv1.setText(""+millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        tv1.setText("Jouez");
                        startRecording();
                        progress = 0;
                        progressBar.setVisibility(View.VISIBLE);
                        int total_length = 5000;
                        int interval = 100;
                        new CountDownTimer(total_length, interval) {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onTick(long millisUntilFinished) {
                                progress += (interval*100)/total_length;
                                progressBar.setProgress(progress, true);
                            }

                            @Override
                            public void onFinish() {
                                stopRecording();
                            }
                        }.start();
                    }
                }.start();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Spinner spinner = findViewById(R.id.spinner);
        TextView tv1 = findViewById(R.id.countdownText);
        tv1.setText(spinner.getSelectedItem().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    public void send(File file) throws IOException {
        //OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE_WAV = MediaType.parse("audio/pcm");

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "recording.wav",
                        RequestBody.create(file, MEDIA_TYPE_WAV))
                .build();

        postRequest("http://abond.me:8080/upload", requestBody);
        /*Request request = new Request.Builder()
                .url("http://abond.me:8080/upload")
                .post(RequestBody.create(MEDIA_TYPE_WAV, file))
                .build();*/

        //Request request = new Request.Builder().url("http://abond.me:8080/upload").post(requestBody).build();
/*
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    void postRequest(String postUrl, RequestBody postBody) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Cancel the post on failure.
                call.cancel();

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(LOG_TAG, "Failed to Connect to Server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String correct = response.body().string();
                            Log.i(LOG_TAG, correct);
                            sendMessage(correct);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /** Called when the user taps the Send button */
    public void sendMessage(String message) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("success", message);
        startActivity(intent);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void startRecording() {
        recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, SAMPLING_RATE_IN_HZ,
                CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        recorder.startRecording();
        recordingInProgress.set(true);

        recordingThread = new Thread(new RecordingRunnable(), "Recording Thread");
        recordingThread.start();
    }

    private void stopRecording() {
        recorder.stop();

        recorder.release();
        recordingInProgress.set(false);

        recorder = null;

        recordingThread = null;
    }

    private class RecordingRunnable implements Runnable {

        @Override
        public void run() {
            Context context = getApplicationContext();
            final File file = new File(context.getCacheDir(), "recording.pcm");
            final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            try (final FileOutputStream outStream = new FileOutputStream(file)) {
                while (recordingInProgress.get()) {
                    int result = recorder.read(buffer, BUFFER_SIZE);
                    if (result < 0) {
                        throw new RuntimeException("Reading of audio buffer failed: " +
                                getBufferReadFailureReason(result));
                    }
                    outStream.write(buffer.array(), 0, BUFFER_SIZE);
                    buffer.clear();
                }
            } catch (IOException e) {
                throw new RuntimeException("Writing of recorded audio failed", e);
            }
            try {
                PCMToWAV converter = new PCMToWAV();
                final File file_wav = new File(context.getCacheDir(), "recording.wav");
                converter.PCMToWAV(file, file_wav, 2, 22050, 16);
                send(file_wav);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getBufferReadFailureReason(int errorCode) {
            switch (errorCode) {
                case AudioRecord.ERROR_INVALID_OPERATION:
                    return "ERROR_INVALID_OPERATION";
                case AudioRecord.ERROR_BAD_VALUE:
                    return "ERROR_BAD_VALUE";
                case AudioRecord.ERROR_DEAD_OBJECT:
                    return "ERROR_DEAD_OBJECT";
                case AudioRecord.ERROR:
                    return "ERROR";
                default:
                    return "Unknown (" + errorCode + ")";
            }
        }
    }
}