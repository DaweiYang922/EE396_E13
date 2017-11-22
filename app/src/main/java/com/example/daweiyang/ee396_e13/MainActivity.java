package com.example.daweiyang.ee396_e13;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    private ListView gridView;
    public static ArrayList<String> subData = new ArrayList<String>();
    private TextView monthlyPayment;
    private Button addSubscription;
    DataBaseHelper myDb;
    ArrayAdapter<String> adapter;
    float total=0;

    String s_account;
    String s_cost;
    String s_month;
    String s_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthlyPayment =(TextView) findViewById(R.id.monthlyPayment);
        addSubscription= (Button) findViewById(R.id.addSubscription);
        gridView = (ListView) findViewById(R.id.GridView);
        adapter = new ArrayAdapter<String>(this,
                  R.layout.grid_list , subData);
        adapter.notifyDataSetChanged();
        myDb = new DataBaseHelper (this);
        configure_add_subscription();
//save to database and show in gridview
            Cursor readData = myDb.getAllData();
                while (readData.moveToNext()) {
                    subData.add(readData.getString(0));
                    total += Float.parseFloat(readData.getString(1));
                }

        setMonthlyPayment();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) gridView.getItemAtPosition(position);
                        String passValue = " ";
                        Cursor readData = myDb.getAllData();
                        readData.moveToFirst();
                        passValue = readData.getString(0) + ";" + readData.getString(1) + ";" +
                                readData.getString(2) + ";" + readData.getString(3);
                        while(!readData.getString(0).equals(item)){
                            if(readData.isLast()) return;
                            readData.moveToNext();
                            passValue = readData.getString(0) + ";" + readData.getString(1) + ";" +
                                    readData.getString(2) + ";" + readData.getString(3);
                        }
                        Intent intent = new Intent("com.example.daweiyang.ee396_e13.EditSubscription");
                        intent.putExtra("SUBINFO", passValue);
                        startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);

                    }
                }
        );

    }
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 10:
                //button
                add_to_calendar();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                adapter.clear();
                gridView.setAdapter(adapter);
                ArrayList<String> returnString = data.getStringArrayListExtra("MESSAGE");
                if (returnString.get(4).equals("A") ) {
                    myDb.insertData(returnString.get(0)
                            , returnString.get(1)
                            , returnString.get(2)
                            , returnString.get(3));
                    s_account = returnString.get(0);
                    s_cost = returnString.get(1);
                    s_month = returnString.get(2);
                    s_day = returnString.get(3);
                    add_to_calendar();

                    //end add
                }else if(returnString.get(4).equals("D")){
                    myDb.deleteData(returnString.get(0).replace(";",""));
                    int EventId = ListSelectedCalendars(returnString.get(0));
                    if(EventId==0){
                        Toast.makeText(this, "Event DNE !", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(this, "Event Deleted: id-> " + Integer.toString(EventId), Toast.LENGTH_LONG).show();
                        DeleteCalendarEntry(EventId);
                    }
                    //end delete
                }else if(returnString.get(4).equals("U")){
                    myDb.updateData(returnString.get(0).replace(";","")
                            , returnString.get(1).replace(";", "")
                            , returnString.get(2).replace(";", "")
                            , returnString.get(3).replace(";", ""));

                }   //end update
                Cursor readData = myDb.getAllData();
                total = 0;
                while(readData.moveToNext()) {
                    subData.add(readData.getString(0));
                    total += Float.parseFloat(readData.getString(1));
                }
                gridView.setAdapter(adapter);
                setMonthlyPayment();
            }
        }
    }

    public void configure_add_subscription(){
        addSubscription.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent("com.example.daweiyang.ee396_e13.AddSubscription");
                        startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);
                    }
                }
        );
    }


    void add_to_calendar(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED
              && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}
                      ,10);
                requestPermissions(new String[]{Manifest.permission.READ_CALENDAR}
                      ,10);
          }
          return;
      }

                      Cursor readData = myDb.getAllData();
                      readData.moveToFirst();
                        while(!readData.isLast()) {

                            int startmonth = Integer.valueOf(s_month);
                            int startday = Integer.valueOf(s_day);
                            Calendar beginCal = Calendar.getInstance();
                            beginCal.set(2017, startmonth - 1, startday);
                            long startTime = beginCal.getTimeInMillis();
                            Calendar endCal = Calendar.getInstance();
                            endCal.set(2017, startmonth - 1, startday );
                            long endTime = endCal.getTimeInMillis();
                            Intent calIntent = new Intent(Intent.ACTION_INSERT);
                            calIntent.setType("vnd.android.cursor.item/event");
                            calIntent.putExtra(CalendarContract.Events.TITLE, s_account);
                            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "Cost $"+s_cost);
                            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
                            calIntent.putExtra(CalendarContract.Events.RRULE, "FREQ=MONTHLY");
                            startActivity(calIntent);
                            readData.moveToNext();
                        }

  }

    void setMonthlyPayment(){
        if(total != 0)  {
            String t = String.format("%.2f", total);
            monthlyPayment.setText("Monthly Payment: $"+ String.valueOf(t));
        }else{
            monthlyPayment.setText("NONE");
        }
    }


    private int DeleteCalendarEntry(int entryID) {
        int iNumRowsDeleted = 0;
        Uri eventUri = ContentUris.withAppendedId(getCalendarUriBase(), entryID);
        iNumRowsDeleted = getContentResolver().delete(eventUri, null, null);
        return iNumRowsDeleted;
    }

    private Uri getCalendarUriBase() {
        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            eventUri = Uri.parse("content://calendar/events");
        } else {
            eventUri = Uri.parse("content://com.android.calendar/events");
        }
        return eventUri;
    }

    private int ListSelectedCalendars(String eventtitle) {
        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            eventUri = Uri.parse("content://calendar/events");
        } else {
            eventUri = Uri.parse("content://com.android.calendar/events");
        }

        int result = 0;
        String projection[] = { "_id", "title" };
        Cursor cursor = getContentResolver().query(eventUri, null, null, null, null);
        if (cursor.moveToFirst()) {
            String calName;
            String calID;
            int nameCol = cursor.getColumnIndex(projection[1]);
            int idCol = cursor.getColumnIndex(projection[0]);
            do {
                calName = cursor.getString(nameCol);
                calID = cursor.getString(idCol);
                if (calName != null && calName.contains(eventtitle)) {
                    result = Integer.parseInt(calID);
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return result;
    }


}//end main






