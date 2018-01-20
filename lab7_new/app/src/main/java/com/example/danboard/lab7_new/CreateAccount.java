package com.example.danboard.lab7_new;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {
    private EditText user;
    private EditText newPW;
    private EditText confirmPW;
    private String str1;
    private String str2;
    private String str3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        user = (EditText)findViewById(R.id.user2);
        newPW = (EditText)findViewById(R.id.newPW);
        confirmPW = (EditText)findViewById(R.id.confirmPW);

        /*
        `*点击ok创建账号
         */
        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1 = user.getText().toString();
                str2 = newPW.getText().toString();
                str3 = confirmPW.getText().toString();

                if (str1.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (str2.isEmpty() || str3.isEmpty()){
                    Toast.makeText(CreateAccount.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else if(str3.equals(str2)) {
                    SharedPreferences sp = getSharedPreferences(str1, MODE_PRIVATE);
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putString("Password", str2);
                    spe.apply();

                    Intent intent = new Intent();
                    intent.setClass(CreateAccount.this, FileEditor.class);
                    intent.putExtra("Username", str1);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateAccount.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
        `*点击clear按钮清空信息
         */
        Button cls2 = (Button) findViewById(R.id.cls2);
        cls2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setText("");
                newPW.setText("");
                confirmPW.setText("");
            }
        });
    }
}
