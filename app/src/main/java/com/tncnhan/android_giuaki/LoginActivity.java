package com.tncnhan.android_giuaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DBHelper DBhelper;
    // Dùng để lưu dữ liệu toàn cục
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
//    sharedPreferences.getString(USENAME_KEY,"")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Init_DB();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginbtn= findViewById(R.id.btnLogin);

        EditText editusername= findViewById(R.id.edtLogin);
        EditText editpassword= findViewById(R.id.edtPassword);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodowndiagonal);
        editusername.startAnimation(animation);
        editpassword.startAnimation(animation);
        loginbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Kiểm tra đã nhập đủ thông tin chưa
                if(editusername.getText().toString().trim().isEmpty()){
                    editusername.setError("Nhập tài khoản ^^");
                }
                if(editpassword.getText().toString().trim().isEmpty()) {
                    editpassword.setError("Nhập mật khẩu ^^");
                }
                //  Kiểm tra đúng tài khoản mật khẩu chưa
                Cursor dt= DBhelper.GetData("select userrole,fullname from users where teacherid='"+editusername.getText().toString().trim()+"' and userpass='"+editpassword.getText().toString().trim()+"'");
                if (dt.getCount()<1){
                    // Sai tên đăng nhập hoặc tài khoản
                    Toast.makeText(getApplicationContext(),"Sai tên đăng nhập/ mật khẩu", Toast.LENGTH_SHORT).show();
                }else if(dt.getCount()==1){
                    dt.moveToNext();
                    int role= dt.getInt(0);
                    String fullname=dt.getString(1);
                    sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("username", editusername.getText().toString().trim());
                    editor.putString("password", editpassword.getText().toString().trim());
                    editor.putInt("role", role);
                    editor.putString("fullname",String.valueOf(fullname));
                    editor.commit();
                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công nha :))" , Toast.LENGTH_SHORT).show();
                    // Chuyển giao diện
                    Intent intent = new Intent(LoginActivity.this, AdminMenuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    // admin 1, user 0
    public  void Init_DB(){
        DBhelper= new DBHelper(this,"qlcd.sqlite",null,1);
        DBhelper.QueryData("create table if not exists users(teacherid varchar(30) primary key,userpass varchar(30),userrole int,fullname nvarchar(100),phone nvarchar(20))");
        DBhelper.QueryData("create table if not exists reportcard( reportid INTEGER PRIMARY KEY AUTOINCREMENT, teacherid varchar(30), delidate date , FOREIGN KEY (teacherid) REFERENCES users(teacherid) )");
        DBhelper.QueryData("create table if not exists class( classid varchar(20) primary key, classname varchar(30), price int )");
        DBhelper.QueryData("create table if not exists reportinfo( reportid int, classid varchar(20), primary key(reportid, classid), FOREIGN KEY (reportid) REFERENCES reportcard(reportid), FOREIGN KEY (classid) REFERENCES class(classid) )");
        DBhelper.QueryData("create table if not exists student( studentid varchar(30) primary key, studentname varchar(50) )");
        DBhelper.QueryData("create table if not exists score( studentid varchar(30), classid varchar(20), score float check(score >= 0 and score <= 10), primary key(studentid,classid), FOREIGN KEY (studentid) REFERENCES student(studentid), FOREIGN KEY (classid) REFERENCES class(classid) )");

        DBhelper.QueryData(" insert or replace into users values('admin','123',1,'Admin','0123456789')");
        DBhelper.QueryData(" insert or replace into users values('admin1','123',1,'Admin 1','0123456789')");
        DBhelper.QueryData(" insert or replace into users values('admin2','123',1,'Admin 2','0123456789')");
        DBhelper.QueryData(" insert or replace into users values('admin3','123',1,'Admin 3','0123456789')");
        DBhelper.QueryData("insert or replace into users values('user','123',0,'User','0123456789')");
        DBhelper.QueryData("insert or replace into users values('user1','123',0,'User 1','0123456789')");
        DBhelper.QueryData("insert or replace into users values('user2','123',0,'User 2','0123456789')");
        DBhelper.QueryData("insert or replace into users values('user3','123',0,'User 3','0123456789')");

//        DBhelper.QueryData("insert into users values('user','123',0,'Pham Nhat Quan','0123456789') WHERE not exists(select * from users where teacherid='user')");
    }
}