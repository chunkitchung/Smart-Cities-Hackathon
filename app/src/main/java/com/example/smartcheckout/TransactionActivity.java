package com.example.smartcheckout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionActivity extends AppCompatActivity {
    private ListView transactionView;
    private TransactionAdapter adapter;
    private ArrayList<Transaction> transactions;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //Setup database
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        transactions = new ArrayList<Transaction>();

        //Get transactions from database
        db.collection("users").document(user.getUid()).collection("transactions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //Goes through each transaction document resulting from our query
                            for(QueryDocumentSnapshot document : task.getResult()){
                                //add transaction to list
                                transactions.add((Transaction) document.toObject(Transaction.class));
                            }
                            Log.i("DEBUG", "Added transactions");
                            for(Transaction t: transactions){
                                Log.i("DEBUGtime", t.getTimeString());
                            }
                            updateAdapter();
                        }

                    }
                });

        //Setup views
        transactionView = findViewById(R.id.transaction_list_view);
        adapter = new TransactionAdapter(this, transactions);
        transactionView.setAdapter(adapter);

    }

    public void updateAdapter(){
        adapter = new TransactionAdapter(this, transactions);
        transactionView.setAdapter(adapter);
    }

}
