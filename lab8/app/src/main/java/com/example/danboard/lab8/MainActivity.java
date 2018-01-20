package com.example.danboard.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<String, Object>> list = new ArrayList<>();
    private MyDB db = new MyDB(this);
    private SimpleAdapter sa;
    private ListView listView;
    private String name;
    private String birth;
    private String gift;
    private TextView getName;
    private TextView getPhone;
    private EditText getBirth;
    private EditText getGift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAG","OnCreate");
        /*
         *添加新信息
         */
        Button add = (Button) findViewById(R.id.addItem);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","AddItem");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddItem.class);
                startActivity(intent);
            }
        });

        /*
         *初始化信息列表
         */
        ShowList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("TAG","AlterDialog");
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog, null);
                getName = (TextView)dialogView.findViewById(R.id.getName);
                getBirth = (EditText)dialogView.findViewById(R.id.getBirth);
                getGift = (EditText)dialogView.findViewById(R.id.getGift);
                getPhone = (TextView)dialogView.findViewById(R.id.getPhone);

                name = list.get(position).get("name").toString();
                birth = list.get(position).get("birth").toString();
                gift = list.get(position).get("gift").toString();

                getName.setText(" " + name);
                getBirth.setText(birth);
                getGift.setText(gift);
//                Log.d("TAG", "获取点击的联系人信息");

                AlertDialog.Builder b1 = new AlertDialog.Builder(MainActivity.this)
                        .setView(dialogView);
                String selection = "display_name = ?";
                String[] selectionArgs = { name };
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        selection, selectionArgs, null);
                if(cursor!=null && cursor.getCount()>=0) {
                    while (cursor.moveToNext()) {
                        int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.
                                HAS_PHONE_NUMBER)));
                        Log.d("TAG", "isHas = " + cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        if (isHas > 0) {
                            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            getPhone.setText(phoneNumber);
                        }
                    }
                    cursor.close();
                }else {
                    getPhone.setText("无");
                }

                b1.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "修改已放弃", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        birth = getBirth.getText().toString();
                        gift = getGift.getText().toString();
                        Member item = new Member(name, birth, gift);
                        db.update(item);
                        list.get(position).put("birth", birth);
                        list.get(position).put("gift", gift);
                        sa.notifyDataSetChanged();
//                        list.clear();
//                        ShowList();
//                        listView.setAdapter(sa);
                    }
                }).create().show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder b2 = new AlertDialog.Builder(MainActivity.this);
                b2.setMessage("是否删除？");
                b2.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "不删除联系人", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(list.get(position).get("name").toString());
                        list.remove(position);
                        sa.notifyDataSetChanged();
                    }
                }).create().show();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShowList();
        Log.d("TAG","OnStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TAG","OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG","OnDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG","OnPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG","OnReSume");
    }

    public void ShowList() {
        list.clear();
        Cursor c = db.query(null, null);
        while (c.moveToNext()) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("name", c.getString(c.getColumnIndex("name")));
            temp.put("birth", c.getString(c.getColumnIndex("birth")));
            temp.put("gift", c.getString(c.getColumnIndex("gift")));
            list.add(temp);
        }
        listView = (ListView)findViewById(R.id.list);
        sa = new SimpleAdapter(MainActivity.this, list,
                R.layout.item, new String[]{"name", "birth", "gift"}, new int[]{R.id.name, R.id.birth, R.id.gift});
        listView.setAdapter(sa);
        c.close();
        Log.d("TAG", "ShowList Successfully");
    }
}
