package com.example.daweiyang.ee396_e13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.ArrayList;


public class AddSubscription extends AppCompatActivity {
    EditText account;
    EditText monthlyPayment;
    EditText passwordHint;
    Button saveAccount;
    NumberPicker npMonth;
    NumberPicker npDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);
        npMonth = (NumberPicker) findViewById(R.id.month);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);
        npDay = (NumberPicker) findViewById(R.id.date);
        npDay.setMaxValue(28);
        npDay.setMinValue(1);
        account = (EditText) findViewById(R.id.account);
        monthlyPayment = (EditText) findViewById(R.id.monthlyPayment);
        saveAccount = (Button) findViewById(R.id.saveAccount);
        onClickButtonlistener();
    }

    public void onClickButtonlistener() {
        saveAccount.setOnClickListener(
            new View.OnClickListener(){
            @Override
                public void onClick (View view){
                String month = Integer.toString(npMonth.getValue());
                String day = Integer.toString(npDay.getValue());
                ArrayList<String> message = new ArrayList<String>();
                message.add(account.getText().toString());
                message.add(monthlyPayment.getText().toString());
                message.add(month);
                message.add(day);
                message.add("A");
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(Activity.RESULT_OK, intent);
                finish();
                }
            }
        );
    }
}