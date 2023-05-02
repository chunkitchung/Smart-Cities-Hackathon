package com.example.smartcheckout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;


import com.example.smartcheckout.ml.LiteModelSsdMobilenetV11Metadata2;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result;

    //This is 300 because the model we use requested images of this size
    int imageSize = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //setup view stuff
        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                }else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }


    //This method will receive image from android camera and ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(resultCode == RESULT_OK){

            //for camera button on click
            if(requestCode == 3){
                //USER TAKES A NEW PICTURE

                //get the bitmap from extras
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                //Rescale our image to fit these dimensions
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                //This simply prepares imaged to be classified by out model
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }else{
                //USER USES AN IMAGE FROM GALLERY
                Uri dat = data.getData();
                Bitmap image = null;
                try{
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                }catch(IOException e){
                    e.printStackTrace();
                }

                imageView.setImageBitmap(image);

                //scale image to be classified by our model
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
        Takes a bitmap image, feeds it into our model, and returns the result
     */
    public void classifyImage(Bitmap bitmap){
        //Sample code
        try {
            LiteModelSsdMobilenetV11Metadata2 model = LiteModelSsdMobilenetV11Metadata2.newInstance(this);

            // Creates inputs for reference.
            TensorImage image = TensorImage.fromBitmap(bitmap);

            // Runs model inference and gets result.
            LiteModelSsdMobilenetV11Metadata2.Outputs outputs = model.process(image);
            LiteModelSsdMobilenetV11Metadata2.DetectionResult detectionResult = outputs.getDetectionResultList().get(0);

            // Gets result from DetectionResult.
            RectF location = detectionResult.getLocationAsRectF();
            String category = detectionResult.getCategoryAsString();
            float score = detectionResult.getScoreAsFloat();

            //trying confidence array

            //Update the view with result
            result.setText(category + " & score: " + score);

            Log.i("DEBUG", "category: " + category + ", score:" + score);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    //basic model test
    public void onImageRecog(View v){
        //Get sample pic bitmpa
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.coffee);

        //Sample code
        try {
            LiteModelSsdMobilenetV11Metadata2 model = LiteModelSsdMobilenetV11Metadata2.newInstance(this);

            // Creates inputs for reference.
            TensorImage image = TensorImage.fromBitmap(bitmap);

            // Runs model inference and gets result.
            LiteModelSsdMobilenetV11Metadata2.Outputs outputs = model.process(image);
            LiteModelSsdMobilenetV11Metadata2.DetectionResult detectionResult = outputs.getDetectionResultList().get(0);

            // Gets result from DetectionResult.
            RectF location = detectionResult.getLocationAsRectF();
            String category = detectionResult.getCategoryAsString();
            float score = detectionResult.getScoreAsFloat();

            Log.i("DEBUG", "category: " + category + ", score:" + score);
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
}