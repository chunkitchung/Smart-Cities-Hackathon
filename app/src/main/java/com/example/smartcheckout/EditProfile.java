package com.example.smartcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


/*
    class to edit the users profiel
    TODO: Allow user to change their name, eamil, and password,
     Also allow them to configure their payment mehtods,
     also allow them to upload a picture
 */
public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }
}