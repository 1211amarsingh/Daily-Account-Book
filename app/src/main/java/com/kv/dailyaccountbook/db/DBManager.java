package com.kv.dailyaccountbook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private static SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public void open() {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public int insert(String fname, String lname, String shop, String address, String mobile) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FNAME, fname);
        contentValue.put(DatabaseHelper.LNAME, lname);
        contentValue.put(DatabaseHelper.SHOP, shop);
        contentValue.put(DatabaseHelper.ADDRESS, address);
        contentValue.put(DatabaseHelper.MOBILE, mobile);
        contentValue.put(DatabaseHelper.STATUS, "true");
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
        return get_last_insert_rowid();
    }

    private int get_last_insert_rowid() {
        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = database.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        return ID;
    }

    public static int isMobileExist(String mobile) {
        String[] columns = new String[]{DatabaseHelper.MOBILE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, "mobile=?", new String[]{mobile}, null, null, null);
        return cursor.getCount();
    }

    public Cursor fetch() {
        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.FNAME, DatabaseHelper.LNAME};
//        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, "status=?", new String[]{"true"}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getTrans(String type) {
        Cursor cursor;
        if (type.equalsIgnoreCase("all")) {
            cursor = database.rawQuery("SELECT PASSBOOK.*, CUSTOMERS.f_name,CUSTOMERS.l_name  FROM " + DatabaseHelper.TABLE_NAME_PASSBOOK + " LEFT JOIN " + DatabaseHelper.TABLE_NAME + " ON CUSTOMERS.id = PASSBOOK.user_id ORDER BY PASSBOOK.id DESC", null);
        } else if (type.equalsIgnoreCase("credit")) {
            cursor = database.rawQuery("SELECT PASSBOOK.*, CUSTOMERS.f_name,CUSTOMERS.l_name  FROM " + DatabaseHelper.TABLE_NAME_PASSBOOK + " LEFT JOIN " + DatabaseHelper.TABLE_NAME + " ON CUSTOMERS.id = PASSBOOK.user_id WHERE " + DatabaseHelper.TYPE + " = 0  ORDER BY PASSBOOK.id DESC", null);
        } else if (type.equalsIgnoreCase("debit")){
            cursor = database.rawQuery("SELECT PASSBOOK.*, CUSTOMERS.f_name,CUSTOMERS.l_name  FROM " + DatabaseHelper.TABLE_NAME_PASSBOOK + " LEFT JOIN " + DatabaseHelper.TABLE_NAME + " ON CUSTOMERS.id = PASSBOOK.user_id WHERE " + DatabaseHelper.TYPE + " = 1  ORDER BY PASSBOOK.id DESC", null);
        }else {
            String qry = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_PASSBOOK + " WHERE " + DatabaseHelper.USER_ID + " = "+ type +" ORDER BY id DESC";
            Log.e("qry ",qry);
            cursor = database.rawQuery(qry, null);
        }

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getCustomerDetails(int id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper._ID + " = " + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int deactivateCustomer(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.STATUS, "false");
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + id, null);
        return i;
    }

    public int updateCustomer(long _id, String fname, String lname, String shop, String address, String mobile) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FNAME, fname);
        contentValues.put(DatabaseHelper.LNAME, lname);
        contentValues.put(DatabaseHelper.SHOP, shop);
        contentValues.put(DatabaseHelper.ADDRESS, address);
        contentValues.put(DatabaseHelper.MOBILE, mobile);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public long count() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_NAME);
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public Cursor selectNewContact(long id) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper._ID + " > " + id, null);
        return cursor;
    }

    public int saveEntry(int user_id, String amount, String type, String paymentType, String summery, String date) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.USER_ID, user_id);
        contentValue.put(DatabaseHelper.AMOUNT, amount);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.PAYMENT_TYPE, paymentType);
        contentValue.put(DatabaseHelper.SUMMERY, summery);
        contentValue.put(DatabaseHelper.DATE, date);
        database.insert(DatabaseHelper.TABLE_NAME_PASSBOOK, null, contentValue);
        return get_last_insert_rowid();
    }
}

