package com.example.smartcheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    TextView transactionName, time;

    public TransactionAdapter(@NonNull Context context, ArrayList<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_view, parent, false);
        }

        //get the position of the view from array adapter
        Transaction transaction = getItem(position);
        assert transaction != null;

        //Set up the view
        transactionName = currentItemView.findViewById(R.id.transaction_name);
        time = currentItemView.findViewById(R.id.time);

        //In the future this should be something that uniquely identifies the transaction
        //Maybe what store or location the transaction was at
        transactionName.setText("Store Transaction");
        time.setText(transaction.getTimeString());

        // get the position of the view from the ArrayAdapter
        return currentItemView;
    }
}
