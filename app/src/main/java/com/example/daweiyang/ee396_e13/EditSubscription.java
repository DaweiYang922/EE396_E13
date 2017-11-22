package com.example.daweiyang.ee396_e13;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.daweiyang.ee396_e13.R.id.account;

public class EditSubscription extends AppCompatActivity {
    private TextView ID;
    private TextView Day;
    private TextView Month;
    private TextView MonthlyPayment;
    private Button Update;
    private Button Delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscription);

        ID = (TextView) findViewById(account);
        Day = (TextView) findViewById(R.id.day);
        Month = (TextView) findViewById(R.id.month);
        MonthlyPayment = (TextView) findViewById(R.id.payment);
        Update = (Button) findViewById(R.id.update);
        Delete = (Button) findViewById(R.id.delete);
        Intent intent = getIntent();
        String SubInfo = intent.getStringExtra("SUBINFO");
        String[] separated = SubInfo.split(";");
        String sAccount = separated[0];
        String sMonthlyPayment = separated[1];
        String sMonth = separated[2];
        String sDay = separated[3];
        ID.setText(sAccount);
        Month.setText(sMonth);
        Day.setText(sDay);
        MonthlyPayment.setText(sMonthlyPayment);

        onClickDelete();
        onClickUpdate();
    }
    public void onClickDelete() {
        Delete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        ArrayList<String> message = new ArrayList<String>();
                        message.add(ID.getText().toString());
                        message.add(Month.getText().toString());
                        message.add(Day.getText().toString());
                        message.add(MonthlyPayment.getText().toString());
                        message.add("D");
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", message);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }

    public void onClickUpdate() {
        Update.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick (View view){
                        ArrayList<String> message = new ArrayList<String>();
                        message.add(ID.getText().toString());
                        message.add(MonthlyPayment.getText().toString());
                        message.add(Month.getText().toString());
                        message.add(Day.getText().toString());
                        message.add("U");
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", message);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }
        );
    }
}//end class