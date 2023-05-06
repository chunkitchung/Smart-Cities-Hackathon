package com.example.smartcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
    This should act as the main hub of the app where users can start all their tasks
 */
public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    //Onclick to start shopping
    public void shop(View v){
        Intent i = new Intent(this, StoreSelection.class);
        startActivity(i);
    }

    //Onclick to go to my transactions
    public void transactions(View v){
        Intent i = new Intent(this, TransactionActivity.class);
        startActivity(i);
    }

    //Onclick to go to edit profile
    public void editProfile(View v){
        Intent i = new Intent(this,EditProfile.class);
        startActivity(i);
    }

}
