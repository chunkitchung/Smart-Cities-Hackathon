package com.example.smartcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class TransactionViewActivity extends AppCompatActivity {
    private TextView transactionName, transactionTime;
    private ListView itemListView;
    private ItemAdapter adapter;
    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_view);

        transactionName = findViewById(R.id.transaction_name);
        transactionTime = findViewById(R.id.transaction_time);
        itemListView = findViewById(R.id.item_list_view);

        //Get transaction clicked from the intent
        transaction = (Transaction)  getIntent().getSerializableExtra("transaction");
        transactionName.setText("Store Transaction");
        transactionTime.setText(transaction.getTimeString());
        adapter = new ItemAdapter(this, transaction.getItems());
        itemListView.setAdapter(adapter);
    }
}