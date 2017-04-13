package com.example.porownywarkapaliw.SQLDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.porownywarkapaliw.ShowLogs;

//manage database creation and version management.
public class DBAdapter {
    private SQLiteDatabase sqLiteDatabase;
    private DataBaseHelper dataBaseHelper;
    private int id;
    private String name,  surname, email, town, phoneNumber, permission, creationData;
    private String where = null;
    public DBAdapter(Context context){
        dataBaseHelper = new DataBaseHelper(context);
    }

    public void GetDataToSaveInDB(int id, String name, String surname,String email,
                                  String town,String phoneNumber,String permission,String creationData){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.surname = surname;
        this.email = email;
        this.town = town;
        this.phoneNumber = phoneNumber;
        this.permission = permission;
        this.creationData = creationData;
    }

    private ContentValues getContentValuesUsersTable(int id, String name, String surname,String email,
                                                    String town,String phoneNumber,String permission,String creationData){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBValues.COLUMN_KEY_ID, id);
        contentValues.put(DBValues.COLUMN_KEY_NAME, name);
        contentValues.put(DBValues.COLUMN_KEY_SURNAME,surname);
        contentValues.put(DBValues.COLUMN_KEY_EMAIL,email);
        contentValues.put(DBValues.COLUMN_KEY_TOWN,town);
        contentValues.put(DBValues.COLUMN_KEY_PHONE_NUMBER,phoneNumber);
        contentValues.put(DBValues.COLUMN_KEY_PERMISSION,permission);
        contentValues.put(DBValues.COLUMN_KEY_CREATION_DATA,creationData);
        return  contentValues;
    }

    public DBAdapter OpenAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"OpenAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public long InsertRowAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"InsertRowAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();


        //Return the row ID of the newly inserted row, or -1 if an error occurred
        return sqLiteDatabase.insert(DBValues.TABLE_NAME,null,
                getContentValuesUsersTable( id,  name,  surname, email,
                 town, phoneNumber, permission, creationData));
    }

    public boolean DeleteRowAlarmDB(long rowIDtoDelete){
        if(ShowLogs.LOG_STATUS) ShowLogs.i("DBAdapter "+"DeleteAllRowsAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        where = DBValues._ID + "=" + rowIDtoDelete;
        return sqLiteDatabase.delete(DBValues.TABLE_NAME, where,null) !=0;
    }

    public void DeleteAllRowsAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"DeleteAllRowsAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBValues.TABLE_NAME, null,null);
    }

    public Cursor GetAllRowsAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"GetAllRowsAlarmDB");
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(true, DBValues.TABLE_NAME, DBValues.ALL_COLUMNS_KEYS,
                null,null,null,null,null,null);

        if(cursor!= null)
            cursor.moveToFirst();

        return cursor;
    }

    public Cursor GetRowAlarmDB(long rowIdToGet){
        sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        where = DBValues._ID + "=" + rowIdToGet;
        Cursor cursor = sqLiteDatabase.query(true, DBValues.TABLE_NAME, DBValues.ALL_COLUMNS_KEYS,
                where,null,null,null,null,null);
        if(cursor !=null)
            cursor.moveToFirst();

        return cursor;
    }

    public boolean UpdateRowAlarmDB(long rowIdToUpdate){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"UpdateRowAlarmDB");
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        where = DBValues._ID + "=" + rowIdToUpdate;
        return sqLiteDatabase.update(DBValues.TABLE_NAME,
                getContentValuesUsersTable( id,  name,  surname, email, town, phoneNumber, permission, creationData)
                ,where,null) != 0;
    }

    public void closeAlarmDB(){
        if(ShowLogs.LOG_STATUS)ShowLogs.i("DBAdapter "+"closeAlarmDB");
        dataBaseHelper.close();
    }

    private static class DataBaseHelper extends SQLiteOpenHelper{
        private DataBaseHelper(Context context) {
            super(context, DBValues.DATABASE_NAME, null, DBValues.DATA_BASE_VERSION);
        }
        //run when the database file did not exist and was just created
        @Override
        public void onCreate(SQLiteDatabase db) {
            String DB_CREATE_TABLE = "CREATE TABLE " + DBValues.TABLE_NAME +
                    " ( " + DBValues.COLUMN_KEY_ID + " INTEGER PRIMARY KEY, " +
                    DBValues.COLUMN_KEY_NAME + " TEXT, " +
                    DBValues.COLUMN_KEY_SURNAME + " TEXT, "+
                    DBValues.COLUMN_KEY_EMAIL + " TEXT, "+
                    DBValues.COLUMN_KEY_TOWN + " TEXT, "+
                    DBValues.COLUMN_KEY_PHONE_NUMBER + " TEXT, "+
                    DBValues.COLUMN_KEY_PERMISSION + " TEXT, "+
                    DBValues.COLUMN_KEY_CREATION_DATA + " TEXT );";
            db.execSQL(DB_CREATE_TABLE);
        }

        //Called when the database file exists but the stored version number is different tan requested in constructor
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ShowLogs.i(" DBAdapter OnUpgrade");
            String DB_DROP_TABLE = "DROP TABLE IF EXISTS " + DBValues.TABLE_NAME;

            db.execSQL(DB_DROP_TABLE);
            //recreate new database (if version changed)
            onCreate(db);
        }
    }

}
