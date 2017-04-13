package com.example.porownywarkapaliw.SQLDataBase;

import android.provider.BaseColumns;

/**
 * @author Konstantyn
 *         Created by Администратор on 02.03.2017.
 */

public final class DBValues implements BaseColumns {

    //keys for table Uzytkownicy
    public static final String COLUMN_KEY_ID = "id_uzytkownika";
    public static final String COLUMN_KEY_NAME = "name";
    public static final String COLUMN_KEY_SURNAME = "surname";
    public static final String COLUMN_KEY_EMAIL = "email";
    public static final String COLUMN_KEY_TOWN = "town";
    public static final String COLUMN_KEY_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_KEY_PERMISSION = "permission";
    public static final String COLUMN_KEY_PASSWORD = "password";
    public static final String COLUMN_KEY_CREATION_DATA = "dataStworzenia";
    static final String DATABASE_NAME = "dbGasStation";
    static final String TABLE_NAME = "uzytkownicy";
    static final int DATA_BASE_VERSION = 1;
    static final String[] ALL_COLUMNS_KEYS = {_ID, COLUMN_KEY_NAME, COLUMN_KEY_SURNAME, COLUMN_KEY_EMAIL,
            COLUMN_KEY_TOWN, COLUMN_KEY_PHONE_NUMBER,COLUMN_KEY_PERMISSION,COLUMN_KEY_CREATION_DATA};

    //to prevent from instantiating
    private DBValues() {
    }
}
