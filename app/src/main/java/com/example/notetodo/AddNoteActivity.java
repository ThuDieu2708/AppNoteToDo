package com.example.notetodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    ImageButton back, save;
    TextView dataTime;
    EditText editTitle, editContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        back = (ImageButton) findViewById(R.id.backNote);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        dataTime = (TextView) findViewById(R.id.txtDatatime);
        dataTime.setText(
                //"EEEE, dd MMMM yyyy HH:mm a" == Wednesday, 16 December 2021 22:04 PM
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );
        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);
        save = (ImageButton) findViewById(R.id.saveNote);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(editTitle.getText().toString()) && !TextUtils.isEmpty(editContent.getText().toString())){

                    DatabaseNote db = new DatabaseNote(AddNoteActivity.this);

                    db.addNote(editTitle.getText().toString(),editContent.getText().toString());
                    Intent i1 = new Intent(AddNoteActivity.this, MainActivity.class);
//                    System.out.println(editTitle.getText().toString() + editContent.getText().toString());
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i1);
                    finish();

                }else {
                    Toast.makeText(AddNoteActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}