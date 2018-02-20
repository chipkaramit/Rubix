package com.amit.rubix;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expandableListView = null;
    private List<String> listItem = null, childItem = null;
    static Map<String, Boolean> mapExpandLess = null;
    Map<String, List<String>> eCollection = null;
    private int eID = 0;
    private String strQuery = "";
    private ArrayList<String> arrayListGroupData = null;
    Map<String, List<String>> dataCollection = null;
    AdapterElectronics expandableAdapter = null;

    private Bundle readCarData = null;

    public MainActivity() {
        eCollection = new HashMap<String, List<String>>();
        mapExpandLess = new HashMap<String, Boolean>();

        listItem = new ArrayList<String>();
        arrayListGroupData = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        expandableListView = (ExpandableListView) findViewById(R.id.elv);
    /*
        *   CALCULATING DEVICE SCREEN SIZE AND PLACING INDICATOR AT RIGHT SIDE.
        * */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        Drawable drawable_groupIndicator = getResources().getDrawable(R.drawable.group_indicator);
        int drawable_width = drawable_groupIndicator.getMinimumWidth();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBounds(width - (drawable_width + 15), expandableListView.getWidth());
        } else {
            expandableListView.setIndicatorBoundsRelative(width - (drawable_width + 15), expandableListView.getWidth());
        }

        /* PLACING DATA ON SPECIFICATION TAB.
         * I HAVE USED CUSTOM ADAPTER FOR EXPANDABLE LIST-VIEW.
        */
        getGroupData();
        createGroup();
        createCollection();
        expandableAdapter = new AdapterElectronics(getApplicationContext(), listItem, dataCollection);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.setGroupIndicator(null);

         /* Opening each child element in expandable list view */
        for (int i = 0; i < listItem.size(); i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return true;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                TextView textView = (TextView) v.findViewById(R.id.tv_group_name);
                ImageView imageView = (ImageView) v.findViewById(R.id.iv_less_more_icon);

                String strHeaderName = textView.getText().toString().trim();
                Iterator iterator = mapExpandLess.keySet().iterator();

                while (iterator.hasNext()) {
                    String strKey = iterator.next().toString();
                    boolean flag = mapExpandLess.get(strKey);

                    if (strHeaderName.equals(strKey)) {
                        if (flag == false) {
                            mapExpandLess.put(strKey, true);
                            imageView.setImageResource(R.drawable.ic_expand_less);
                        } else {
                            mapExpandLess.put(strKey, false);
                            imageView.setImageResource(R.drawable.ic_expand_more);
                        }
                    }
                }
                return false;
            }
        });

    }

    /*
    * RETRIEVING TABLE VALUE FROM DATABASE.
    * WHEN WE GOT DATA THEN GROUP DATA ACCORDING TO GROUP NAME.
    */

    public void getGroupData() {
        try {
            DatabaseController dbCotroller = SingleTon.getInstance(getApplicationContext(), SingleTon.dbName, SingleTon.version);
            Cursor cursor = null;
            strQuery = "select * from tbl_electronics";

            cursor = dbCotroller.getValue(strQuery);

            if (cursor.moveToFirst()) {
                do {


                    String subChildName = "";
                    subChildName = cursor.getColumnName(1).toString().trim();
                    subChildName = subChildName.substring(0, 1).toUpperCase() + subChildName.substring(1);

                    arrayListGroupData.add(subChildName);

                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGroup() {
        listItem.add("Apple");
        listItem.add("Samsung");
        listItem.add("Apple");
        listItem.add("Others");
    }

    private void createCollection() {
        ArrayList<String> listChild = new ArrayList<String>();

        /*
            SEPARATING EACH DATA ACCORDING TO GROUP NAME
         */

        for (int j = 0; j < arrayListGroupData.size(); j++) {
            if (j < 6) {
                listChild.add(arrayListGroupData.get(j));
            }

        }

        /*
            PUTTING EACH CHILD DATA WITH ITS PARENT as column name.
        */

        for (String brand : listItem) {
            childItem = new ArrayList<String>();
            childItem.addAll(listChild);


            dataCollection.put(brand, childItem);
        }

    }

}
