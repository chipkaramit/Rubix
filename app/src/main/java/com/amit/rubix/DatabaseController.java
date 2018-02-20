package com.amit.rubix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by amitchipkar on 20/02/18.
 */

public class DatabaseController extends SQLiteOpenHelper {

    public static String DB_PATH = "//data//data//com.amit.rubix//databases//";
    SQLiteDatabase sqlDatabase = null, sqlConnection = null;
    Context context = null;
    String dbName = "";
    int version = 0, result = 0;


    public DatabaseController(Context context, String dbName, int version)
    {
        super(context, dbName,null, version);
        this.context = context;
        this.dbName = dbName;
        this.version = version;
        createDatabase();

    }

    public void openConnection()/* OPEN CONNECTION */
    {
        try
        {
            sqlConnection = SQLiteDatabase.openDatabase("", null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }/* END OF OPEN CONNECTION */


    public void createDatabase()
    {
        boolean dbExist = checkDatabase();
        if (dbExist)
        {
            Log.v("Database status : ", "Database is already exist");
        }
        else if (!dbExist)
        {
            this.getReadableDatabase();
            try
            {
                copyDataBase();
                this.close();
            }
            catch (Exception e)
            {
                throw new Error("Database already there " + e.getMessage());
            }
        }
    }

    public boolean checkDatabase()
    {
        boolean checkDB = false;
        try
        {
            String path = DB_PATH + dbName;
            File fileExist = new File(path);
            checkDB = fileExist.exists();
            return checkDB;
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
        }
        return checkDB;
    }

    public void copyDataBase() throws IOException {
        OutputStream writeDatabase = null;
        String strOutputPath = DB_PATH + dbName;
        int length = 0;
        try
        {
            writeDatabase = new FileOutputStream(strOutputPath);
            InputStream inputStreamReader = context.getAssets().open(dbName);
            byte[] bufferData = new byte[1024];

            while ((length = inputStreamReader.read(bufferData)) > 0)
            {
                writeDatabase.write(bufferData, 0, length);
            }
            writeDatabase.flush();
            writeDatabase.close();
            inputStreamReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() /* CLOSE CONNECTION */
    {
        super.close();
    } /*END CLOSE CONNECTION */

    @Override
    public void onCreate(SQLiteDatabase db) {}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /*
     * INSERTING RECORD
     */
    public int insert(String tableName, String [] colName, String [] colValues)
    {
        result = 0;
        try
        {

            sqlDatabase = this.getWritableDatabase();
            ContentValues value = new ContentValues();

            for(int i=0; i<colName.length; i++)
            {
                value.put(colName[i], colValues[i]);
            }

            result = (int) (sqlDatabase.insert(tableName, null, value));
            this.close();
            value.clear();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * UPDATING RECORD
     */
    public int update(String tableName, String[]column, String[] values, String condition, String [] args)
    {
        result = 0;
        try
        {

            sqlDatabase = this.getWritableDatabase();
            ContentValues contentValue = new ContentValues();
            for(int i=0; i<column.length; i++)
            {
                contentValue.put(column[i], values[i]);
            }
            result = sqlDatabase.update(tableName, contentValue, condition, args);
            this.close();
            contentValue.clear();
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * DELETING RECORD
     */
    public int delete(String tableName, String clause, String [] args)
    {
        result = 0;
        try
        {
            sqlDatabase = this.getWritableDatabase();
            result = sqlDatabase.delete(tableName, clause, args);
            this.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * FETCHING RECORD
     */
    public Cursor getValue(String str_Query)
    {
        Cursor cursor = null;
        try
        {
            sqlDatabase = this.getReadableDatabase();
            cursor = sqlDatabase.rawQuery(str_Query,null);

            if(cursor.moveToFirst())
            {
                do
                {
                    break;
                }while(cursor.moveToNext());
            }
            this.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return cursor;
    }
}
