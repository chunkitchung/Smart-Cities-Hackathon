package com.example.smartcheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mapbox.maps.MapView;
import com.mapbox.maps.ResourceOptions;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;


public class StoreSelection extends AppCompatActivity {
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_selection);


//        ResourceOptions resourceOptions = ResourceOptions.Builder()
//                .accessToken(this.getString(R.string.mapbox_access_token))
//                .build();
//
//
//        mapView = MapView(this, MapInitOptions(this, resourceOptions).apply (textureView = true );
    }

    public void shop(View v){
        Intent i = new Intent(this, ShopActivity.class);
        startActivity(i);
    }

}