package com.example.smartcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.smartcheckout.ml.LiteModelSsdMobilenetV11Metadata2;

import org.tensorflow.lite.support.image.TensorImage;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void init(){

    }

    public void onInitialized(){

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