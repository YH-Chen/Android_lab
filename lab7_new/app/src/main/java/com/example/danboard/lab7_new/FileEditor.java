package com.example.danboard.lab7_new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEditor extends AppCompatActivity {
    private EditText fileName;
    private EditText fileContent;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_editor);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        username = bundle.getString("Username");

        TextView user = (TextView) findViewById(R.id.username);
        user.setText(username);

        fileName = (EditText)findViewById(R.id.fileName);
        fileContent = (EditText)findViewById(R.id.fileContent);

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveFile(fileName.getText().toString(), fileContent.getText().toString());
            }
        });

        Button load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileContent.setText(LoadFile(fileName.getText().toString()));
            }
        });

        Button clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileContent.setText("");

            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFile(fileName.getText().toString());
            }
        });
    }

    public void SaveFile(String name, String content)
    {
        try {
            FileOutputStream output = openFileOutput(username+":"+name, 0);
            output.write(content.getBytes());
            output.flush();
            output.close();
            Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "Save Successfully");
        } catch (IOException ex) {
            Toast.makeText(this, "Fail to save file", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Fail to save file");
        }
    }

    public String LoadFile(String name) {

        try {
            FileInputStream input = openFileInput(username+":"+name);
            byte[] contents = new byte[input.available()];
            StringBuilder sb = new StringBuilder("");
            while(input.read(contents)!=-1){
                sb.append(new String(contents));
            }
            input.close();
            Toast.makeText(this, "Load Successfully", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "Load Successfully");
            return sb.toString();
        } catch (IOException ex) {
            Toast.makeText(this, "Fail to load file", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Fail to read file.");
        }
        return "";
    }

    public void DeleteFile(String name) {
        deleteFile(name);
        Toast.makeText(this, "Delete succesfully", Toast.LENGTH_SHORT).show();
    }

}
