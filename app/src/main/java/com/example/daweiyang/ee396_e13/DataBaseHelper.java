package com.example.daweiyang.ee396_e13;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ProgrammingKnowledge on 4/3/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Account1.db";
    public static final String TABLE_NAME = "subscription_table";
    public static final String COL_1 = "ACCOUNT";
    public static final String COL_2 = "COST";
    public static final String COL_3 = "MONTH";
    public static final String COL_4 = "DAY";
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ACCOUNT TEXT,COST TEXT,MONTH TEXT, DAY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String account,String cost,String month, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,account);
        contentValues.put(COL_2,cost);
        contentValues.put(COL_3,month);
        contentValues.put(COL_4,day);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }


    public boolean updateData(String account,String cost,String month, String day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,account);
        contentValues.put(COL_2,cost);
        contentValues.put(COL_3,month);
        contentValues.put(COL_4,day);
        db.update(TABLE_NAME, contentValues, "Account = ?",new String[] { account });
        return true;
    }

    public Integer deleteData (String account) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Account = ?",new String[] {account});
    }


}