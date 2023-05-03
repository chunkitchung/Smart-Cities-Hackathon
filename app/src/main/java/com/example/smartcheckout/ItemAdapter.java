package com.example.smartcheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {
    private TextView name, cost;

    public ItemAdapter(@NonNull Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_view, parent, false);
        }
        Item item = getItem(position);

        //item view stuff
        name = currentItemView.findViewById(R.id.item_name);
        cost = currentItemView.findViewById(R.id.item_cost);
        name.setText(item.getName());
        cost.setText(String.format("$%.2f", item.getCost()));

        return currentItemView;
    }
}
