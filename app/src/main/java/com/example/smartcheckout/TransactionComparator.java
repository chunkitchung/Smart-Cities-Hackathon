package com.example.smartcheckout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class TransactionComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        String  time1 = t1.getTimeString();
        String time2 = t2.getTimeString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy h:m:s");
        try {
            if(sdf.parse(time1).after(sdf.parse(time2))){
                return 1;
            }else{
                return -1;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
