package com.example.danboard.lab7_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText user;
    private EditText pw;
    private String str1;
    private String str2;

    private boolean is_registed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText)findViewById(R.id.user1);
        pw = (EditText)findViewById(R.id.pw);

        /*
        `*点击login登录
         */
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1 = user.getText().toString();
                str2 = pw.getText().toString();

                SharedPreferences sp = getSharedPreferences(str1, MODE_PRIVATE);
                String storage = sp.getString("Password", null);

                if(str1.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                } else if(str2.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if(str2.equals(storage)){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, FileEditor.class);
                    intent.putExtra("Username", str1);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "User does not exist or Password error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
        `*点击register创建新用户
         */
        Button register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
        /*
        `*点击clear按钮清空信息
         */
        Button cls1 = (Button) findViewById(R.id.cls1);
        cls1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setText("");
                pw.setText("");
            }
        });
    }
}
