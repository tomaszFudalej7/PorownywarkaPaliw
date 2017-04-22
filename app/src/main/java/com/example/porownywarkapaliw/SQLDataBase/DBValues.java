package com.example.porownywarkapaliw.SQLDataBase;

import android.provider.BaseColumns;

/**
 * @author Konstantyn
 *         Created by Администратор on 02.03.2017.
 */

public final class DBValues implements BaseColumns {

    //to prevent from instantiating
    private DBValues() {
    }

    static final String DATABASE_NAME = "dbGasStation";
    static final String TABLE_NAME = "GasStation";
    public static final String COLUMN_GAS_PRICE = "gas_price";
    public static final String COLUMN_PETROL_PRICE = "petrol_price";
    public static final String COLUMN_SERVICE_POINTS = "service_points";
    public static final String COLUMN_GAS_STATION_NAME = "station_name";
    public static final String COLUMN_GAS_STATION_LOCATION = "gas_station_location";
    static final int DATA_BASE_VERSION = 1;
    static final String[] ALL_COLUMNS_KEYS = {_ID, COLUMN_GAS_PRICE, COLUMN_PETROL_PRICE, COLUMN_SERVICE_POINTS,
            COLUMN_GAS_STATION_NAME, COLUMN_GAS_STATION_LOCATION};

    //keys for table Uzytkownicy
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOWN = "town";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_PERMISSION = "permission";
    public static final String KEY_PASSWORD = "password";
}
