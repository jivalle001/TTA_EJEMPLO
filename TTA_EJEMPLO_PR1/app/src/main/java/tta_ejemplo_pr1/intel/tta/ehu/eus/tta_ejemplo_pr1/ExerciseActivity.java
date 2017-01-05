package tta_ejemplo_pr1.intel.tta.ehu.eus.tta_ejemplo_pr1;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ExerciseActivity extends AppCompatActivity {

    private final static int READ_REQUEST_CODE = 0;
    private final static int PICTURE_REQUEST_CODE = 1;
    private final static int AUDIO_REQUEST_CODE = 2;
    private final static int VIDEO_REQUEST_CODE = 3;
    private Uri pictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        setTitle("Nuevo Ejercicio");

        Intent intent = getIntent();

        View view;
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            view = findViewById(R.id.button_take_photo);
            view.setEnabled(false);
            view = findViewById(R.id.button_record_video);
            view.setEnabled(false);
        }
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            view = findViewById(R.id.button_record_audio);
            view.setEnabled(false);
        }
    }

    public void subirFichero(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent,READ_REQUEST_CODE);
    }

    public void sacarFoto(View view) {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            Toast.makeText(this,R.string.noCamera, Toast.LENGTH_SHORT).show();
        else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null){
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                try{
                    File file = File.createTempFile("tta",".jpg",dir);
                    pictureUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,pictureUri);
                    startActivityForResult(intent,PICTURE_REQUEST_CODE);
                }
                catch (IOException e){

                }
            }
            else
                Toast.makeText(this,R.string.noApp, Toast.LENGTH_SHORT).show();
        }
    }

    public void grabarAudio(View view) {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
            Toast.makeText(this,R.string.noMicro, Toast.LENGTH_SHORT).show();
        else{
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            if(intent.resolveActivity(getPackageManager())!=null)
                startActivityForResult(intent,AUDIO_REQUEST_CODE);
            else
                Toast.makeText(this,R.string.noApp, Toast.LENGTH_SHORT).show();
        }
    }

    public void grabarVideo(View view) {
        if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            Toast.makeText(this,R.string.noCamera, Toast.LENGTH_SHORT).show();
        else{
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if(intent.resolveActivity(getPackageManager())!=null)
                startActivityForResult(intent,VIDEO_REQUEST_CODE);
            else
                Toast.makeText(this,R.string.noApp, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        if(resultCode!= Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case READ_REQUEST_CODE:
                //subirFichero(data.getData());
                break;
            case PICTURE_REQUEST_CODE:
                //subirFichero(data.getData());
                break;
            case AUDIO_REQUEST_CODE:
                //subirFichero(data.getData());
                break;
            case VIDEO_REQUEST_CODE:
                //subirFichero(data.getData());
                break;
        }
    }
}
