package com.example.danboard.lab8_new;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItem extends AppCompatActivity {

    private EditText name;
    private  EditText birth;
    private EditText gift;
    private MyDB db = new MyDB(AddItem.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        name = (EditText) findViewById(R.id.newName);
        birth = (EditText) findViewById(R.id.newBirth);
        gift = (EditText) findViewById(R.id.newGift);

        Button ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = name.getText().toString();
                String str2 = birth.getText().toString();
                String str3 = gift.getText().toString();
                Member item = new Member(str1, str2, str3);

                if(str1.isEmpty()) {
                    Toast.makeText(AddItem.this, "名字为空，请完善", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c = db.query("name", str1);
                if(c.getCount() > 0) {
                    Toast.makeText(AddItem.this.getApplicationContext(), "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
                }else {
                    db.insert(item);
                    finish();
                }
            }
        });
    }
}
