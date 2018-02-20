package com.amit.rubix;

import android.content.Context;

/**
 * Created by amitchipkar on 20/02/18.
 */

public class SingleTon {
    static DatabaseController dbSingleTon = null;
    public static String dbName = "db_electronics.db";
    public static int version = 1;

    public SingleTon(){}

    public static DatabaseController getInstance(Context context, String dbName, int version) throws Exception
    {
        if(dbSingleTon == null)
        {
            dbSingleTon = new DatabaseController(context, dbName, version);
            return dbSingleTon;
        }
        else
        {
            return dbSingleTon;
        }
    }
}
