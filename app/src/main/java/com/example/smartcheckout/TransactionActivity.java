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
    private ArrayAdapter<HashMap<String, Object>> adapter;
    private ArrayList<HashMap<String, Object>> transactions;
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

        //Get transactions from database
        db.collection("users").document(user.getUid()).collection("transactions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                //add all transactions to the list of transactions
                                transactions.add((HashMap<String, Object>) document.getData());
                            }
                            Log.i("DEBUG", "Added transactions");

                        }

                    }
                });

        //Setup views
        transactionView = findViewById(R.id.transaction_list_view);
        adapter = new TransactionAdapter(this, transactions);
    }
}
