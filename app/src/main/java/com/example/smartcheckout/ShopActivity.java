package com.example.smartcheckout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcheckout.ml.Mobilenetv1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

import org.checkerframework.checker.units.qual.A;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Allows the user to start a tracked shopping experience, where they can add items into their cart using image recognition
 */
public class ShopActivity extends AppCompatActivity {
    private ArrayList<String> cart;
    private ListView cartListView;
    private ArrayAdapter<String> adapter;
    private View itemPopup;

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;

    private ImageView itemImage;
    private Button acceptButton, denyButton;
    //This is 300 because the model we use requested images of this size
    int imageSize = 300;
    //image size for mobilenet model
    int imageSizeMobile = 224;
    private Bitmap image;

    private TextView classification;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        //Setting up view items and adapter
        cartListView = findViewById(R.id.cart_list_view);
        cart = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cart);
        cartListView.setAdapter(adapter);

    }

    /*
        For now sends user to the android camera to take a picture
        of the item they wish to add to their cart
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addItem(View v){
        //Try to start camera application
        if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 3);
        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    //Should catch the result of the camera or gallery access
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(resultCode == RESULT_OK){

            //open popup
            itemPopup = getLayoutInflater().inflate(R.layout.item_popup, null);

            //Show popup
            dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(itemPopup);
            dialog = dialogBuilder.create();
            dialog.show();

            //setup popup views
            itemImage = itemPopup.findViewById(R.id.item_image);
            acceptButton = itemPopup.findViewById(R.id.accept_button);
            denyButton = itemPopup.findViewById(R.id.deny_button);
            classification = itemPopup.findViewById(R.id.classification);

            //if the user approves classification
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.add(classification.getText().toString());
                    updateAdapter();
                }
            });

            //for camera button on click
            if(requestCode == 3){
                //USER TAKES A NEW PICTURE

                //get the bitmap from extras
                image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                //Rescale our image to fit these dimensions
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                itemImage.setImageBitmap(image);
                //This simply prepares imaged to be classified by out model
                image = Bitmap.createScaledBitmap(image, imageSizeMobile, imageSizeMobile, false);
                //clasift image
                String result = classifyImageMobile(image);
                classification.setText(result);

            }else{
                //USER USES AN IMAGE FROM GALLERY
                Uri dat = data.getData();
                Bitmap image = null;
                try{
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                }catch(IOException e){
                    e.printStackTrace();
                }

                itemImage.setImageBitmap(image);

                //scale image to be classified by our model
                image = Bitmap.createScaledBitmap(image, imageSizeMobile, imageSizeMobile, false);
                classifyImageMobile(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
     Using the mobile net model
        Takes a bitmap image, feeds it into our model, and returns the result as a string
    */
    public String classifyImageMobile(Bitmap bitmap) {
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

            // Releases model resources if no longer used.
            model.close();
            return category.getLabel();
        } catch (IOException e) {
            // TODO Handle the exception
        }
        return "";

    }

    //Simple method to update cart list view adapter after changes
    public void updateAdapter(){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cart);
        cartListView.setAdapter(adapter);
    }

    /*
        Checkout the user
        TODO: present user with their total cost accumulatied
        and also send this transaction into their db collection for transactions
     */
    public void checkout(View v){

        //initialize auth
        mAuth = FirebaseAuth.getInstance();
        //get the current user
        user = mAuth.getCurrentUser();
        //initialize db
        db = FirebaseFirestore.getInstance();

        //Build transaction

        //A transaction is made up of a string array of items and a time
        Map<String, Object> transaction = new HashMap<String, Object>();
        transaction.put("items", cart);
        transaction.put("time", LocalDateTime.now());

        //add a new transaction to user's document
        db.collection("users").document(user.getUid()).collection("transactions").add(transaction)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ShopActivity.this, "Transaction has been recorded", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ShopActivity.this, "Unable to proceed with transaction", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Send user back to home screen
        finish();
    }

}