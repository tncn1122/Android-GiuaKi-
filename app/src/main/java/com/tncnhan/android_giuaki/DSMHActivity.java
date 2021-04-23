package com.tncnhan.android_giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class DSMHActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DBHelper DBhelper;
    ArrayList<MonHoc> ArrMH  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsmh_layout);
        // GET THONG TIN USER
        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
        int role= sharedPreferences.getInt("role",0);
        ImageButton imgbtn_addmh= findViewById(R.id.btnThem);
        if(role==0){
            imgbtn_addmh.setVisibility(View.INVISIBLE);
        }else if(role ==1 ){
            imgbtn_addmh.setVisibility(View.VISIBLE);
            imgbtn_addmh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DSMHActivity.this, AddMHActivity.class);
                    startActivity(intent);
                }
            });

            DBhelper = new DBHelper(this,"qlcd.sqlite",null,1);
            Cursor dt= DBhelper.GetData("select * from class");
            while(dt.moveToNext())
            {
                Log.d("MHH", dt.getString(0) + " " + dt.getString(2));
                MonHoc MH = new MonHoc(dt.getString(0),dt.getString(1),Integer.parseInt(dt.getString(2)),"/");
                ArrMH.add(MH);
            }

        }

        CustomListAdapter_DSMH adapter = new CustomListAdapter_DSMH(ArrMH);

        ListView listView= findViewById(R.id.lvDSMH);
        listView.setAdapter(adapter);

    }


}