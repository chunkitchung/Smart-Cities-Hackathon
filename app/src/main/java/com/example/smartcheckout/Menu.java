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
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }
}