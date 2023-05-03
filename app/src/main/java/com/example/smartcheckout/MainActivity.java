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
import com.example.smartcheckout.ml.Mobilenetv1;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button camera, gallery;
    ImageView imageView;
    TextView result, score;

    //This is 300 because the model we use requested images of this size
    int imageSize = 300;
    //image size for mobilenet model
    int imageSizeMobile = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Temp setup!!!!!
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);

        //setup view stuff
        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        score = findViewById(R.id.score);

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
                image = Bitmap.createScaledBitmap(image, imageSizeMobile, imageSizeMobile, false);
                classifyImageMobile(image);
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
                image = Bitmap.createScaledBitmap(image, imageSizeMobile, imageSizeMobile, false);
                classifyImageMobile(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
      Using the mobile net model
     Takes a bitmap image, feeds it into our model, and returns the result
     */
    public void classifyImageMobile(Bitmap bitmap) {
        try {
            Mobilenetv1 model = Mobilenetv1.newInstance(this);

            // Creates inputs for reference.
            TensorImage image = TensorImage.fromBitmap(bitmap);

            // Runs model inference and gets result.
            Mobilenetv1.Outputs outputs = model.process(image);
            List<Category> probability = outputs.getProbabilityAsCategoryList();

            float maxScore = 0;
            Category category = null;

            for(Category c : probability){
                if(c.getScore() > maxScore){
                    maxScore = c.getScore();
                    category = c;
                }
            }

            result.setText(category.getLabel());
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

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

//            //trying confidence array
//            float [] confidences = outputFeature0.getFloatArray();
//            //find the index of the class with bigges confidence
//            int maxPos = 0;
//            float maxConfidence = 0;
//            for(int i = 0; i < confidences.length; i++){
//                if(confidences[i] > maxConfidence){
//                    maxConfidence = confidences[i];
//                    maxPos = i;
//                }
//            }

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