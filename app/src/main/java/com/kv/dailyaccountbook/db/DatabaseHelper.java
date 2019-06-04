package com.kv.dailyaccountbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    private static final String DB_NAME = "ACCOUNT.DB";

    // database version
    private static final int DB_VERSION = 1;

    // Table Name
    static final String TABLE_NAME = "CUSTOMERS";

    // Table columns
    public static final String _ID = "id";
    public static final String FNAME = "f_name";
    public static final String LNAME = "l_name";
    public static final String SHOP = "shop";
    public static final String ADDRESS = "address";
    public static final String STATUS = "status";
    public static final String MOBILE = "mobile";

    public static final String USER_ID = "user_id";
    public static final String AMOUNT = "amount";
    public static final String TYPE = "type";
    public static final String PAYMENT_TYPE = "paymentType";
    public static final String SUMMERY = "summery";
    public static final String DATE = "date";
    static final String TABLE_NAME_PASSBOOK = "PASSBOOK";

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FNAME + " TEXT NOT NULL, "
            + LNAME + " TEXT NOT NULL, "
            + SHOP + " TEXT NOT NULL, "
            + ADDRESS + " TEXT NOT NULL, "
            + STATUS + " TEXT NOT NULL, "
            + MOBILE + " TEXT NOT NULL UNIQUE"
            + ");";

    // Creating table query
    private static final String CREATE_TABLE_PASSBOOK = "create table " + TABLE_NAME_PASSBOOK + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + USER_ID + " TEXT NOT NULL, "
            + AMOUNT + " TEXT NOT NULL, "
            + TYPE + " TEXT NOT NULL, "
            + PAYMENT_TYPE + " TEXT NOT NULL, "
            + SUMMERY + " TEXT NOT NULL, "
            + DATE + " TEXT NOT NULL"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_PASSBOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PASSBOOK);
        onCreate(db);
    }
}
