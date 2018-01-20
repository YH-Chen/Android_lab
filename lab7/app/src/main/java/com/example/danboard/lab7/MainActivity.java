package com.example.danboard.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText newPW;
    private EditText confirmPW;
    private String str1;
    private String str2;

    private Button ok;
    private Button cls;

    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private String storage;

    private boolean is_registed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newPW = (EditText)findViewById(R.id.newPW);
        confirmPW = (EditText)findViewById(R.id.confirmPW);

        sp = getSharedPreferences("Password", MODE_PRIVATE);
        spe = sp.edit();
        storage = sp.getString("Password", null);
        if(storage != null) {
            is_registed = true;
            newPW.setVisibility(View.INVISIBLE);
            confirmPW.setHint("Password");
        }
        /*
        `*点击确认按钮保存密码/进入文件编辑
         */
        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str1 = newPW.getText().toString();
                str2 = confirmPW.getText().toString();
                if(is_registed) {
                    if(str2.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        if(str2.equals(storage)) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, FileEditor.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if(str1.isEmpty() || str2.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    }else if(str2.equals(str1)){
                        spe.putString("Password", str2);
//                        spe.commit();
                        spe.apply();

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, FileEditor.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, "Password Mismatch", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*
        `*点击确认clear按钮清空密码
         */
        cls = (Button)findViewById(R.id.cls);
        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPW.setText("");
                confirmPW.setText("");
            }
        });
    }
}
