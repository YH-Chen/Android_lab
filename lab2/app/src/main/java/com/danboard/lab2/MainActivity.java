package com.danboard.lab2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] items = {"拍摄","从相册选择"};
        final AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setTitle("上传图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(),"您选择了["+items[which]+"]", Toast.LENGTH_SHORT).show();}
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplication(),"您选择了[取消]",Toast.LENGTH_SHORT).show();}
                }).create();
        ImageView img=(ImageView)findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.show();
            }
        });

        final RadioGroup RGroup = (RadioGroup)findViewById(R.id.RadioGroup);
        RGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                 choice = checkedId;
                if(checkedId==R.id.RB1)
                {
                    Snackbar.make(group,"您选择了[学生]",Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else
                {
                    Snackbar.make(group,"您选择了[教职工]",Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
            }
        });

        final TextInputLayout NumText = (TextInputLayout)findViewById(R.id.editNum);
        final TextInputLayout PWText = (TextInputLayout)findViewById(R.id.editPW);
        EditText NunEdit = NumText.getEditText();
        EditText PWEdit = PWText.getEditText();
        NunEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int cnt, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int cnt) {
                if(s.length()!=0)
                {
                    NumText.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        PWEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int cnt, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int cnt) {
                if(s.length()!=0)
                {
                    PWText.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String msg1 = "";
                String getNum = NumText.getEditText().getText().toString();
                String getPW = PWText.getEditText().getText().toString();
                if (getNum.equals("123456") && getPW.equals("6666")) {
                    msg1 = "登录成功";
                }
                else {
                    msg1 = "学号或密码错误";
                }
                Snackbar.make(view,msg1,Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();
                if(getNum.length()==0)
                {
                    NumText.setErrorEnabled(true);
                    NumText.setError("学号不能为空");
                }
                else if(getPW.length()==0)
                {
                    PWText.setErrorEnabled(true);
                    PWText.setError("密码不能为空");
                }
            }
        });
        Button btn2 = (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String msg2 = "";
                choice = RGroup.getCheckedRadioButtonId();
                if (choice==R.id.RB1) {
                   msg2 = "学生注册尚未开启";
                }
                else {
                    msg2 = "教职工注册尚未开启";
                }
                Snackbar.make(view,msg2,Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();
            }
        });
    }
}