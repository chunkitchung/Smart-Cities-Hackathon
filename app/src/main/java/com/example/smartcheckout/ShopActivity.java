package com.example.smartcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/*
    Allows the user to start a tracked shopping experience, where they can add items into their cart using image recognition
 */
public class ShopActivity extends AppCompatActivity {
    private ArrayList<String> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        cart = new ArrayList<String>();

    }

    //Add item
    public void addItem(View v){

    }

}